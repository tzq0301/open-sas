package cn.tzq0301.opensasspringbootstarter.net.config;


import cn.tzq0301.opensasspringbootstarter.net.handler.server.MessageServerHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.google.common.base.Preconditions.checkNotNull;

@SpringBootConfiguration
@EnableWebSocket
@Import({MessageServerHandler.class})
@ConditionalOnProperty(prefix = "open-sas", name = "server", havingValue = "true")
public class ServerConfig implements WebSocketConfigurer {
    private final MessageServerHandler messageServerHandler;

    public ServerConfig(@NonNull MessageServerHandler messageServerHandler) {
        checkNotNull(messageServerHandler);
        this.messageServerHandler = messageServerHandler;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        checkNotNull(registry);
        registry.addHandler(messageServerHandler, "/");
    }
}
