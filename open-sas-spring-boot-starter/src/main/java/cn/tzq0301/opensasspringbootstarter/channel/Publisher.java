package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Publisher {
    void publish(@NonNull final Message message);
}
