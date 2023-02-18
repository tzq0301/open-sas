package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.core.MessageContent;

import java.util.function.Consumer;

@FunctionalInterface
public interface SubscriberCallback extends Consumer<MessageContent> {
}
