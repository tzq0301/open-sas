package cn.tzq0301.opensascore.group;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.util.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

public record Group(@NonNull String group) {
    public Group {
        checkArgument(StringUtils.hasText(group));
    }
}
