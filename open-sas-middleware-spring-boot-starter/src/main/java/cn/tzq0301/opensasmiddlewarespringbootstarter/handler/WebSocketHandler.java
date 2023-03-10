package cn.tzq0301.opensasmiddlewarespringbootstarter.handler;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.MiddlewareListenerRegistry;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.message.MessageDetails;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.PublishRequest;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.SubscribeRequest;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.UnsubscribeRequest;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
public class WebSocketHandler extends StompSessionHandlerAdapter implements Publisher {
    private final Group group;

    private final Version version;

    private final Priority priority;

    private final MiddlewareListenerRegistry middlewareListenerRegistry;

    private StompSession session;

    public WebSocketHandler(Group group, Version version, Priority priority,
                            MiddlewareListenerRegistry middlewareListenerRegistry) {
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.middlewareListenerRegistry = middlewareListenerRegistry;
    }

    @Override
    public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
        checkNotNull(session);
        checkNotNull(connectedHeaders);

        this.session = session;

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
                middlewareListenerRegistry.onMessage(messageDetails.topic(), messageDetails.message(), WebSocketHandler.this);
            }
        });
    }

    private void subscribe(@NonNull StompSession session) {
        checkNotNull(session).send("/topic/subscribe",
                new SubscribeRequest(group, version, priority, middlewareListenerRegistry.getTopics()));
    }

    private void unsubscribe(@NonNull StompSession session) {
        checkNotNull(session).send("/topic/unsubscribe", new UnsubscribeRequest(group, version, priority));
    }

    @Override
    public void publish(@NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        checkNotNull(session);
        session.send("/topic/publish", new PublishRequest(group, version, priority.cloneByDownGrade(), topic, message));
    }
}
