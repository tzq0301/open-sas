package cn.tzq0301.opensassubscriberspringbootstarter.entity;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

public record SubscribeRequest(@NonNull Group group, @NonNull Version version,
                               @NonNull Priority priority, @NonNull Set<Topic> topics) {
}
