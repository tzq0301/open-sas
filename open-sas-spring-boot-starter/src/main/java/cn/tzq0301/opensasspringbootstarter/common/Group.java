package cn.tzq0301.opensasspringbootstarter.common;

import org.apache.logging.log4j.util.Strings;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Component
@ConditionalOnProperty(prefix = "open-sas", name = "group")
public record Group(@NonNull String group) {
    public Group(@NonNull @Value("${open-sas.group}") String group) {
        checkArgument(!Strings.isBlank(group));
        this.group = group;
    }

    public static Group of(@NonNull final String group) {
        checkArgument(!Strings.isBlank(group));
        return new Group(group);
    }
}
