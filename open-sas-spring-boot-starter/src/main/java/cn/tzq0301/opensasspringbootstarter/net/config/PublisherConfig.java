package cn.tzq0301.opensasspringbootstarter.net.config;

import cn.tzq0301.opensasspringbootstarter.net.handler.client.MessagePublisherHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import static com.google.common.base.Preconditions.checkNotNull;

@SpringBootConfiguration
@Import({MessagePublisherHandler.class})
@ConditionalOnProperty(prefix = "open-sas.publisher", name = "enable", havingValue = "true")
public class PublisherConfig implements ApplicationListener<ApplicationReadyEvent> {
    private final String serverAddr;

    private final MessagePublisherHandler webSocketHandler;

    public PublisherConfig(@Value("${open-sas.publisher.server-addr}") String serverAddr,
                           MessagePublisherHandler webSocketHandler) {
        this.serverAddr = serverAddr;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        checkNotNull(event);
        WebSocketClient client = new StandardWebSocketClient();
        var manager = new WebSocketConnectionManager(client, webSocketHandler, serverAddr);
        manager.start();

        Runtime.getRuntime().addShutdownHook(new Thread(manager::stop));
    }
}
