package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasspringbootstarter.common.Topic;
import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpointReference;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

@WebSocketEndpointReference(Register.class)
public record RegisterRequest(@NonNull Set<Topic> topics) {
}
