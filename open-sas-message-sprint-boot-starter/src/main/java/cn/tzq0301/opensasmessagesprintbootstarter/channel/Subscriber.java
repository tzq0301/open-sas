package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Subscriber {
    void onMessage(@NonNull final Channel channel, @NonNull final MessageContent content);

    void subscribe(@NonNull final Channel channel);

    void unsubscribe(@NonNull final Channel channel);

    @CheckReturnValue
    @NonNull
    Group group();

    @CheckReturnValue
    @NonNull
    Version version();

    @CheckReturnValue
    @NonNull
    Priority priority();
}
