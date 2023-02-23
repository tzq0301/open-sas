package cn.tzq0301.opensasmessagesprintbootstarter.net.config;

import cn.tzq0301.opensasmessagesprintbootstarter.net.handler.client.MessageSubscriberHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@SpringBootConfiguration
@Import({MessageSubscriberHandler.class})
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public class SubscriberConfig implements ApplicationListener<ApplicationReadyEvent> {
    private final String serverAddr;

    private final MessageSubscriberHandler webSocketHandler;

    public SubscriberConfig(@Value("${open-sas.subscriber.server-addr}") String serverAddr,
                            MessageSubscriberHandler webSocketHandler) {
        this.serverAddr = serverAddr;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        checkNotNull(event);
        var client = new StandardWebSocketClient();
        var manager = new WebSocketConnectionManager(client, webSocketHandler, serverAddr);
        manager.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                webSocketHandler.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // send `unregister` before WebSocket close
                manager.stop();
            }
        }));
    }
}
