package cn.tzq0301.opensasmiddlewarespringbootstarter.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

@SpringBootConfiguration
public class WebSocketConfig {
    private final String serverAddr;

    private final StompSessionHandler stompSessionHandler;

    public WebSocketConfig(OpenSasProperties openSasProperties,
                           StompSessionHandler stompSessionHandler) {
        this.serverAddr = String.format(
                "ws://%s:%s",
                openSasProperties.getServerAddr().getHost(),
                openSasProperties.getServerAddr().getPort());
        this.stompSessionHandler = stompSessionHandler;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connect() throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession stompSession = stompClient.connectAsync(serverAddr, stompSessionHandler).get();
        if (!stompSession.isConnected()) {
            throw new RuntimeException("WebSocket session connect fail");
        }
    }
}
