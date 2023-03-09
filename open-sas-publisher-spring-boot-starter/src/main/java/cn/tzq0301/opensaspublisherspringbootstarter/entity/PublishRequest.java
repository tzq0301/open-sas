package cn.tzq0301.opensaspublisherspringbootstarter.entity;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

public record PublishRequest(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                             @NonNull Topic topic, @NonNull Message message) {
}
