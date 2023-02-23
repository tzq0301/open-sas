package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface OnMessageCallback {
    void onMessage(@NonNull Message message);
}
