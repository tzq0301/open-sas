package cn.tzq0301.opensasspringbootstarter.callback;

import cn.tzq0301.opensasspringbootstarter.common.MessageContent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SubscriberCallback {
    void onMessage(@NonNull final MessageContent content);
}
