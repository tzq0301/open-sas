package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface MiddlewareCallback extends Function<MessageContent, Optional<MessageContent>> {
}
