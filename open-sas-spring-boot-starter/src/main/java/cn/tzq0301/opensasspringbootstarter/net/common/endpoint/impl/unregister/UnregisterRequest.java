package cn.tzq0301.opensasspringbootstarter.net.common.endpoint.impl.unregister;

import cn.tzq0301.opensasspringbootstarter.net.common.endpoint.WebSocketEndpointReference;

@WebSocketEndpointReference(Unregister.class)
public record UnregisterRequest() {
}
