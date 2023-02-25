package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public final class SubscriberCallbackRegistry {
    private final List<SubscriberCallback> list;

    public SubscriberCallbackRegistry() {
        this.list = Lists.newArrayList();
    }

    public void add(@NonNull final SubscriberCallback callback) {
        list.add(callback);
    }

    public void forEach(Consumer<SubscriberCallback> consumer) {
        list.forEach(consumer);
    }

    public List<SubscriberCallback> getCallbacks() {
        return ImmutableList.copyOf(list);
    }
}
