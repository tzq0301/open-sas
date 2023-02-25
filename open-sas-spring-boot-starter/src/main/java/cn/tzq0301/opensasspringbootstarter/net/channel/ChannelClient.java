package cn.tzq0301.opensasspringbootstarter.net.channel;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

// TODO
public class ChannelClient implements Channel {

    @Override
    public void registerSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull SubscriberCallback subscriber) {

    }

    @Override
    public void unregisterSubscriber(@NonNull Group group, @NonNull Version version, @NonNull Priority priority) {

    }

    @Override
    public void publish(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull Message message) {

    }
}
