package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.WebSocketEndpointReference;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpointReference(Register.class)
public final class RegisterClient implements Endpoint {
    private static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    @Override
    public void call(@Nullable final Payload payload, @NonNull final WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
