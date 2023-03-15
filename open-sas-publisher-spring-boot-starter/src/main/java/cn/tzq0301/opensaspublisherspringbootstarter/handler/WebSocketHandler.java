package cn.tzq0301.opensaspublisherspringbootstarter.handler;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import cn.tzq0301.opensaspublisherspringbootstarter.config.OpenSasProperties;
import cn.tzq0301.opensaspublisherspringbootstarter.entity.PublishRequest;
import cn.tzq0301.spring.websocket.StompSessionHandlerAdaptor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
public class WebSocketHandler implements Publisher, StompSessionHandlerAdaptor {
    private final String serverAddr;

    private final Group group;

    private final Version version;

    private final Priority priority;

    private StompSession session;

    public WebSocketHandler(OpenSasProperties openSasProperties, Group group, Version version, Priority priority) {
        this.serverAddr = String.format(
                "ws://%s:%s",
                openSasProperties.getServerAddr().getHost(),
                openSasProperties.getServerAddr().getPort());
        this.group = group;
        this.version = version;
        this.priority = priority;
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

    @EventListener(ApplicationReadyEvent.class)
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
}
