package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.publish;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpointReference;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@WebSocketEndpointReference(Publish.class)
public record PublishRequest(@NonNull Message message) {
    public PublishRequest {
        checkNotNull(message);
    }
}
