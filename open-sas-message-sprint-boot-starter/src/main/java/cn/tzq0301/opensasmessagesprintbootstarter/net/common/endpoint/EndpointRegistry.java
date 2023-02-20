package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface EndpointRegistry extends Endpoint {
    void register(@NonNull String name, @Nullable Endpoint endpoint);
}
