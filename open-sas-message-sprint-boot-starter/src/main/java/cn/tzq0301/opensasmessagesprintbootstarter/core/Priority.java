package cn.tzq0301.opensasmessagesprintbootstarter.core;

import com.google.common.collect.ComparisonChain;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record Priority(int priority) implements Comparable<Priority> {
    public static final Priority MAX = Priority.of(Integer.MAX_VALUE);

    public static final Priority MIN = Priority.of(Integer.MIN_VALUE);

    public static Priority of(final int priority) {
        return new Priority(priority);
    }

    @Override
    public int compareTo(@NonNull final Priority o) {
        checkNotNull(o);
        return ComparisonChain.start()
                .compare(this.priority, o.priority)
                .result();
    }
}
