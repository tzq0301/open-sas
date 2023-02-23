package cn.tzq0301.opensasmessagesprintbootstarter.sdk;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import cn.tzq0301.opensasmessagesprintbootstarter.net.handler.client.MessagePublisherHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnBean(MessagePublisherHandler.class)
@Import({MessagePublisherHandler.class})
public final class MessagePublisher {
    private final MessagePublisherHandler handler;

    public MessagePublisher(@NonNull final MessagePublisherHandler handler) {
        checkNotNull(handler);
        this.handler = handler;
    }

    public void publish(@NonNull final MessageContent content) {
        checkNotNull(content);
        handler.publish(content);
    }
}
