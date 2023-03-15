package cn.tzq0301.opensassubscriberspringbootstarter.handler;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.SubscriberListenerRegistry;
import cn.tzq0301.opensascore.message.MessageDetails;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensassubscriberspringbootstarter.config.OpenSasProperties;
import cn.tzq0301.opensassubscriberspringbootstarter.entity.SubscribeRequest;
import cn.tzq0301.opensassubscriberspringbootstarter.entity.UnsubscribeRequest;
import cn.tzq0301.spring.websocket.StompSessionHandlerAdaptor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
public class WebSocketHandler implements StompSessionHandlerAdaptor {
    private final String serverAddr;

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final SubscriberListenerRegistry subscriberListenerRegistry;

    public WebSocketHandler(OpenSasProperties openSasProperties,
                            Group group, Version version, Priority priority,
                            SubscriberListenerRegistry subscriberListenerRegistry) {
        this.serverAddr = String.format(
                "ws://%s:%s",
                openSasProperties.getServerAddr().getHost(),
                openSasProperties.getServerAddr().getPort());
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.subscriberListenerRegistry = subscriberListenerRegistry;
    }

    @Override
    public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
        checkNotNull(session);
        checkNotNull(connectedHeaders);
        subscribe(session);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> unsubscribe(session)));
        session.subscribe("/user/topic/message", new StompFrameHandler() {
            @Override
            @NonNull
            public Type getPayloadType(@NonNull StompHeaders headers) {
                checkNotNull(headers);
                return MessageDetails.class;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                checkNotNull(headers);
                MessageDetails messageDetails = (MessageDetails) payload;
                subscriberListenerRegistry.onMessage(messageDetails.group(), messageDetails.version(),
                        messageDetails.priority(), messageDetails.topic(), messageDetails.message());
            }
        });
    }

    private void subscribe(@NonNull StompSession session) {
        checkNotNull(session).send("/topic/subscribe",
                new SubscribeRequest(group, version, priority, subscriberListenerRegistry.getTopics()));
    }

    private void unsubscribe(@NonNull StompSession session) {
        checkNotNull(session).send("/topic/unsubscribe", new UnsubscribeRequest(group, version, priority));
    }

    @Override
    public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
        checkNotNull(session);
        checkNotNull(exception);
        if (exception instanceof ConnectionLostException) {
            retryConnect(serverAddr);
        } else {
            exception.printStackTrace();
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connectOnStartUp() {
        if (!tryConnect(serverAddr)) {
            throw new RuntimeException("WebSocket session connect fail");
        }
    }
}
