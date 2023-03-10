package cn.tzq0301.opensascore.channel;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ChannelImpl implements Channel {
    private final Map<Group, Map<Version, Map<Topic, NavigableMap<Priority, SubscriberCallback>>>> groupMap;

    public ChannelImpl() {
        this.groupMap = Maps.newHashMap();
    }

    @Override
    public void registerSubscriber(@NonNull final Group group,
                                   @NonNull final Version version,
                                   @NonNull final Priority priority,
                                   @NonNull final Map<Topic, SubscriberCallback> topicToCallbackMap) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topicToCallbackMap);

        synchronized (this) {
            if (!groupMap.containsKey(group)) {
                groupMap.put(group, Maps.newHashMap());
            }

            var versionMap = checkNotNull(groupMap.get(group));
            if (!versionMap.containsKey(version)) {
                versionMap.put(version, Maps.newHashMap());
            }

            var topicMap = checkNotNull(versionMap.get(version));
            topicToCallbackMap.forEach((topic, subscriberCallback) -> {
                if (!topicMap.containsKey(topic)) {
                    topicMap.put(topic, Maps.newTreeMap());
                }
                var priorityMap = checkNotNull(topicMap.get(topic));
                checkArgument(!priorityMap.containsKey(priority), "The subscriber with same group (%s), same version (%s), same priority (%), same topic(%s)", group, version, priority, topic);
                priorityMap.put(priority, subscriberCallback);
            });
        }
    }

    @Override
    public void unregisterSubscriber(@NonNull final Group group,
                                     @NonNull final Version version,
                                     @NonNull final Priority priority) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);

        synchronized (this) {
            checkArgument(groupMap.containsKey(group), "Group (%s) is not exists", group);

            var versionMap = checkNotNull(groupMap.get(group));
            checkArgument(versionMap.containsKey(version), "Version (%s) is not exists", version);

            var topicMap = checkNotNull(versionMap.get(version));
            topicMap.forEach((topic, prioritySubscriberCallbackNavigableMap) -> {
                var priorityMap = checkNotNull(topicMap.get(topic));
                // remove subscribe
                priorityMap.remove(priority);
            });

            topicMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

            // dynamically remove topicMap if empty
            if (topicMap.isEmpty()) {
                versionMap.remove(version);
            }

            // dynamically remove versionMap if empty
            if (versionMap.isEmpty()) {
                groupMap.remove(group);
            }
        }
    }

    @Override
    public void publish(@NonNull final Group group,
                        @NonNull final Version version,
                        @NonNull final Priority priority,
                        @NonNull final Topic topic,
                        @NonNull final Message message) {
        checkNotNull(message);

        synchronized (this) {
            if (!groupMap.containsKey(group)) {
                return;
            }

            var versionMap = checkNotNull(groupMap.get(group));
            if (!versionMap.containsKey(version)) {
                return;
            }

            var topicMap = checkNotNull(versionMap.get(version));
            if (!topicMap.containsKey(topic)) {
                return;
            }

            var priorityMap = checkNotNull(topicMap.get(topic));
            Optional.ofNullable(priorityMap.floorEntry(priority))
                    .map(Map.Entry::getValue)
                    .ifPresent(callback -> callback.onMessage(topic, message));
        }
    }
}
