package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public final class SubscriberOnMessageCallbackRegistry {
    private final List<SubscriberOnMessageCallback> list;

    public SubscriberOnMessageCallbackRegistry() {
        this.list = Lists.newArrayList();
    }

    public void add(@NonNull final SubscriberOnMessageCallback callback) {
        list.add(callback);
    }

    public void forEach(Consumer<SubscriberOnMessageCallback> consumer) {
        list.forEach(consumer);
    }
}
