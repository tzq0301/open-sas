package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint.impl.register;

import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record RegisterRequest(@NonNull Group group, @NonNull Version version, @NonNull Priority priority) {
    public RegisterRequest {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
    }
}