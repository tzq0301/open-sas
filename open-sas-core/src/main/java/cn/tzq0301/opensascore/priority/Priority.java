package cn.tzq0301.opensascore.priority;

import com.google.common.collect.ComparisonChain;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record Priority(int priority) implements Comparable<Priority> {
    public static final Priority MAX_PRIORITY = new Priority(Integer.MAX_VALUE);

    public static final Priority MIN_PRIORITY = new Priority(Integer.MIN_VALUE);

    public Priority(int priority) {
        this.priority = priority;
    }

    @NonNull
    public Priority cloneWithBias(final int bias) {
        return new Priority(priority + bias);
    }

    @NonNull
    public Priority cloneByDownGrade(final int grade) {
        return cloneWithBias(-grade);
    }

    @NonNull
    public Priority cloneByDownGrade() {
        return cloneByDownGrade(1);
    }

    @Override
    public int compareTo(@NonNull final Priority o) {
        checkNotNull(o);
        return ComparisonChain.start()
                .compare(this.priority, o.priority)
                .result();
    }
}
