package cn.tzq0301.opensasmessagesprintbootstarter.core;

import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Priorities {
    private Priorities() {
    }

    @CheckReturnValue
    public static @NonNull Priority cloneWithBias(@NonNull final Priority priority, final int bias) {
        checkNotNull(priority);
        return Priority.of(priority.priority() + bias);
    }

    @CheckReturnValue
    public static @NonNull Priority cloneByDownGrade(@NonNull final Priority priority, final int grade) {
        checkNotNull(priority);
        return cloneWithBias(priority, -grade);
    }

    @CheckReturnValue
    public static @NonNull Priority cloneByDownGrade(@NonNull final Priority priority) {
        checkNotNull(priority);
        return cloneWithBias(priority, -1);
    }
}
