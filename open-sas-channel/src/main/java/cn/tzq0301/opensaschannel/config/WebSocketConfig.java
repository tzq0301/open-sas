package cn.tzq0301.opensaschannel.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.google.common.base.Preconditions.checkNotNull;

@SpringBootConfiguration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    private final int port;

    public WebSocketConfig(@Value("${open-sas.port}") int port) {
        this.port = port;
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        checkNotNull(registry);
        registry
                .addEndpoint("/")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setUserDestinationPrefix("/client");
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(port);
    }
}
