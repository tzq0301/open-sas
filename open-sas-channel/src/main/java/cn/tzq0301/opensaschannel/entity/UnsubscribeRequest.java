package cn.tzq0301.opensaschannel.entity;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

public record UnsubscribeRequest(@NonNull Group group, @NonNull Version version, @NonNull Priority priority) {
}
