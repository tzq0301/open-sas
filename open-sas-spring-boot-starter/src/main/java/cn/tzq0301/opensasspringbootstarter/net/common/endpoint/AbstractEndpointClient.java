package cn.tzq0301.opensasspringbootstarter.net.common.endpoint;

import cn.tzq0301.opensasspringbootstarter.net.common.payload.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractEndpointClient implements Endpoint {
    protected static final ObjectMapper mapper = new ObjectMapper(); // no need for each instance & concurrent safety

    @Override
    public void call(@Nullable Payload payload, @NonNull WebSocketSession session) {
        checkNotNull(payload);
        checkNotNull(session);

        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
