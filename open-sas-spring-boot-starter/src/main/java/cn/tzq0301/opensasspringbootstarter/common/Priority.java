package cn.tzq0301.opensasspringbootstarter.common;

import com.google.common.collect.ComparisonChain;
import com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas", name = "priority")
public record Priority(int priority) implements Comparable<Priority> {
    public static final Priority MAX = Priority.of(Integer.MAX_VALUE);

    public static final Priority MIN = Priority.of(Integer.MIN_VALUE);

    public Priority(@Value("${open-sas.priority}") int priority) {
        this.priority = priority;
    }

    public static Priority of(final int priority) {
        return new Priority(priority);
    }

    public @NonNull Priority cloneWithBias(final int bias) {
        return Priority.of(priority + bias);
    }

    public @NonNull Priority cloneByDownGrade(final int grade) {
        return cloneWithBias(-grade);
    }

    public @NonNull Priority cloneByDownGrade() {
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
