package cn.tzq0301.opensasspringbootstarter.sdk.middleware;

import cn.tzq0301.opensasspringbootstarter.channel.MiddlewareCallback;
import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Topic;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas.middleware", name = "enable", havingValue = "true")
public final class MiddlewareCallbackRegistry implements MiddlewareCallback {
    private final Map<Topic, MiddlewareCallback> topicToCallbackMap;

    public MiddlewareCallbackRegistry() {
        this.topicToCallbackMap = Maps.newHashMap();
    }

    public void add(@NonNull final Topic topic, @NonNull final MiddlewareCallback callback) {
        checkNotNull(topic);
        checkNotNull(callback);
        checkArgument(topicToCallbackMap.get(topic) == null, "duplicate listener for same topic (%s)", topic);
        topicToCallbackMap.put(topic, callback);
    }

    public Map<Topic, MiddlewareCallback> getCallbacks() {
        return ImmutableMap.copyOf(topicToCallbackMap);
    }

    @Override
    public void onMessage(@NonNull Topic topic, @NonNull Message message, @NonNull Publisher publisher) {
        checkNotNull(topic);
        checkNotNull(message);
        checkNotNull(publisher);
        checkNotNull(topicToCallbackMap.get(topic)).onMessage(topic, message, publisher);
    }
}
