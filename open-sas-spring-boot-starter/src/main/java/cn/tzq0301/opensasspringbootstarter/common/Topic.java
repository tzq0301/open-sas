package cn.tzq0301.opensasspringbootstarter.common;

import com.google.errorprone.annotations.CheckReturnValue;
import org.apache.logging.log4j.util.Strings;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public record Topic(@NonNull String topic) {
    public Topic {
        checkArgument(!Strings.isBlank(topic));
    }

    @CheckReturnValue
    @NonNull
    public static Topic of(@NonNull final String topic) {
        checkArgument(!Strings.isBlank(topic));
        return new Topic(topic);
    }
}
