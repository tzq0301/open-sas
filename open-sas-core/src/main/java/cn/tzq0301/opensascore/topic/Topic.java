package cn.tzq0301.opensascore.topic;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.util.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

public record Topic(@NonNull String topic) {
    public Topic {
        checkArgument(StringUtils.hasText(topic));
    }
}
