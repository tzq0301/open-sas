package cn.tzq0301.opensasspringbootstarter.common;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas", name = "group")
public record Group(@NonNull String group) {
    public Group(@NonNull @Value("${open-sas.group}") String group) {
        checkNotNull(group);
        this.group = group;
    }

    public static Group of(@NonNull final String group) {
        checkNotNull(group);
        return new Group(group);
    }
}
