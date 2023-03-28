package cn.tzq0301.opensassubscriberspringbootstarter.handler;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.SubscriberListenerRegistry;
import cn.tzq0301.opensascore.message.MessageDetails;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensassubscriberspringbootstarter.config.OpenSasConfig;
import cn.tzq0301.opensassubscriberspringbootstarter.config.OpenSasProperties;
import cn.tzq0301.opensassubscriberspringbootstarter.entity.SubscribeRequest;
import cn.tzq0301.opensassubscriberspringbootstarter.entity.UnsubscribeRequest;
import cn.tzq0301.spring.websocket.StompSessionHandlerAdaptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
public class WebSocketHandler implements StompSessionHandlerAdaptor, ApplicationListener<ApplicationReadyEvent> {
    private final String openMindAddr;

    private final String openMindToken;

    private final ObjectMapper objectMapper;

    private String serverAddr;

    private final HttpClient httpClient;

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final SubscriberListenerRegistry subscriberListenerRegistry;

    public WebSocketHandler(ObjectMapper objectMapper, OpenSasProperties openSasProperties,
                            HttpClient httpClient, OpenSasConfig openSasConfig,
                            SubscriberListenerRegistry subscriberListenerRegistry) {
        this.openMindAddr = String.format(
                "%s:%s",
                openSasProperties.getOpenMind().getHost(),
                openSasProperties.getOpenMind().getPort());
        this.openMindToken = openSasProperties.getOpenMind().getToken();
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.group = openSasConfig.getGroup();
        this.version = openSasConfig.getVersion();
        this.priority = openSasConfig.getPriority();
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

    public void connectOnStartUp() {
        if (!tryConnect(serverAddr)) {
            throw new RuntimeException("WebSocket session connect fail");
        }
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        URI uri = new URI(String.format("http://%s/channel/serverAddr?token=%s", openMindAddr, openMindToken));
        HttpRequest request = HttpRequest
                .newBuilder(uri)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Result<String> result = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        serverAddr = result.data();
        serverAddr = String.format("ws://%s", serverAddr);
        connectOnStartUp();
    }
}
