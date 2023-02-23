package cn.tzq0301.opensasmessagesprintbootstarter.callback;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SubscriberCallback {
    void onMessage(@NonNull final MessageContent content);
}
