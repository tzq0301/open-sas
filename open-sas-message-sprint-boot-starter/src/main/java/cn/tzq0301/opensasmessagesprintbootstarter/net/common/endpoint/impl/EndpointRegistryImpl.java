package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl;

import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.Endpoint;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.EndpointRegistry;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.request.Request;
import cn.tzq0301.opensasmessagesprintbootstarter.net.common.response.Response;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class EndpointRegistryImpl implements EndpointRegistry {
    private final Map<String, Endpoint> registry;

    public EndpointRegistryImpl() {
        this(Maps.newHashMap());
    }

    public EndpointRegistryImpl(final @NonNull Map<String, Endpoint> registry) {
        checkNotNull(registry);
        this.registry = Maps.newHashMap(registry);
    }

    @Override
    public Response<?> call(@NonNull Request<?> request, @NonNull WebSocketSession session) {
        checkNotNull(request);
        checkArgument(registry.containsKey(request.payload().method()));
        return registry.get(request.payload().method()).call(request, session);
    }

    @Override
    public void register(@NonNull String name, @Nullable Endpoint endpoint) {
        checkNotNull(name);
        checkNotNull(endpoint);
        checkArgument(!registry.containsKey(name));
        registry.put(name, endpoint);
    }
}
