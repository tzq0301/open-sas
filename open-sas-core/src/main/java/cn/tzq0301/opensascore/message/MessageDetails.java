package cn.tzq0301.opensascore.message;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public record MessageDetails(@NonNull Group group, @NonNull Version version, @NonNull Priority priority,
                             @NonNull Topic topic, @NonNull Message message) {
    public MessageDetails {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topic);
        checkNotNull(message);
    }
}
