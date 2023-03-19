package cn.tzq0301.opensaspublisherspringbootstarter.handler;

import cn.tzq0301.http.rest.result.Result;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensaspublisherspringbootstarter.config.OpenSasProperties;
import cn.tzq0301.opensaspublisherspringbootstarter.entity.PublishRequest;
import cn.tzq0301.spring.websocket.StompSessionHandlerAdaptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;

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

    private String serverAddr;

    private final Group group;

    private final Version version;

    private final Priority priority;

    private final HttpClient httpClient;

    private StompSession session;

    public WebSocketHandler(OpenSasProperties openSasProperties,
                            ObjectMapper objectMapper,
                            Group group,
                            Version version,
                            Priority priority,
                            HttpClient httpClient) {
        this.openMindAddr = String.format(
                "%s:%s",
                openSasProperties.getOpenMind().getHost(),
                openSasProperties.getOpenMind().getPort());
        this.openMindToken = openSasProperties.getOpenMind().getToken();
        this.objectMapper = objectMapper;
        this.group = group;
        this.version = version;
        this.priority = priority;
        this.httpClient = httpClient;
    }

    @Override
    public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
        checkNotNull(session);
        checkNotNull(connectedHeaders);
        this.session = session;
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

    @Override
    public void publish(@NonNull Topic topic, @NonNull Message message) {
        checkNotNull(topic);
        checkNotNull(message);
        checkNotNull(session);
        session.send("/topic/publish", new PublishRequest(group, version, priority, topic, message));
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
