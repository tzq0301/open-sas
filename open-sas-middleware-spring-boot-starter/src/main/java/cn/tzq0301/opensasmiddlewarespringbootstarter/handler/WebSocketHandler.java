package cn.tzq0301.opensasmiddlewarespringbootstarter.handler;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.MiddlewareListenerRegistry;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.message.MessageDetails;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensasmiddlewarespringbootstarter.config.OpenSasConfig;
import cn.tzq0301.opensasmiddlewarespringbootstarter.config.OpenSasProperties;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.PublishRequest;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.SubscribeRequest;
import cn.tzq0301.opensasmiddlewarespringbootstarter.entity.UnsubscribeRequest;
import cn.tzq0301.spring.websocket.StompSessionHandlerAdaptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
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
public class WebSocketHandler implements Publisher, StompSessionHandlerAdaptor, ApplicationListener<ApplicationReadyEvent> {
    private final String openMindAddr;

    private final String openMindToken;

    private final ObjectMapper objectMapper;

    @MonotonicNonNull
    private String serverAddr;

    private final HttpClient httpClient;

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final MiddlewareListenerRegistry middlewareListenerRegistry;

    @MonotonicNonNull
    private StompSession session;

    public WebSocketHandler(ObjectMapper objectMapper, OpenSasProperties openSasProperties,
                            HttpClient httpClient, OpenSasConfig openSasConfig,
                            MiddlewareListenerRegistry middlewareListenerRegistry) {
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

                Group group = messageDetails.group();
                Version version = messageDetails.version();
                Priority priority = messageDetails.priority();
                Topic topic = messageDetails.topic();
                Message message = messageDetails.message();

                Publisher publisher = (t, m) -> {
                    PublishRequest request = new PublishRequest(group, version, priority.cloneByDownGrade(), t, m);
                    session.send("/topic/publish", request);
                };

                middlewareListenerRegistry.onMessage(group, version, priority, topic, message, publisher);
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
