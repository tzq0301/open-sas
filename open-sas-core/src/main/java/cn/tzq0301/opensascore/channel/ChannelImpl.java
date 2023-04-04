package cn.tzq0301.opensascore.channel;

import cn.tzq0301.opensascore.channel.meta.ChannelMetaInfo;
import cn.tzq0301.opensascore.channel.meta.GroupMetaInfo;
import cn.tzq0301.opensascore.channel.meta.PriorityMetaInfo;
import cn.tzq0301.opensascore.channel.meta.TopicMetaInfo;
import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ChannelImpl implements Channel {
    private final Map<Group, Map<Topic, NavigableMap<Priority, NavigableMap<Version, SubscriberCallback>>>> groupMap;

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

            var topicMap = checkNotNull(groupMap.get(group));
            topicToCallbackMap.forEach((topic, subscriberCallback) -> {
                if (!topicMap.containsKey(topic)) {
                    topicMap.put(topic, new TreeMap<>());
                }

                var priorityMap = topicMap.get(topic);
                if (!priorityMap.containsKey(priority)) {
                    priorityMap.put(priority, new TreeMap<>());
                }

                var versionMap = priorityMap.get(priority);
                checkArgument(!versionMap.containsKey(version), "The subscriber with same group (%s), same version (%s), same priority (%), same topic(%s)", group, version, priority, topic);
                versionMap.put(version, subscriberCallback);
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

            var topicMap = groupMap.get(group);

            var iter = topicMap.entrySet().iterator();
            while (iter.hasNext()) {
                var entry = iter.next();
                var priorityMap = entry.getValue();

                checkArgument(priorityMap.containsKey(priority), "Priority (%s) is not exists", priority);

                var versionMap = priorityMap.get(priority);
                checkArgument(versionMap.containsKey(version), "Version (%s) is not exists", version);

                versionMap.remove(version);

                if (versionMap.isEmpty()) {
                    priorityMap.remove(priority);
                }

                if (priorityMap.isEmpty()) {
                    iter.remove();
                }
            }

            if (topicMap.isEmpty()) {
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
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topic);
        checkNotNull(message);

        synchronized (this) {
            if (!groupMap.containsKey(group)) {
                return;
            }

            var topicMap = groupMap.get(group);
            if (!topicMap.containsKey(topic)) {
                return;
            }

            var priorityMap = topicMap.get(topic).descendingMap().tailMap(priority);
            if (priorityMap.isEmpty()) {
                return;
            }

            // loop the entries that key (priority) is equals with or lower than `priority`
            // to find the first version, which is consistent with the given `version`
            for (var entry : priorityMap.entrySet()) {  // entry
                var currentPriority = entry.getKey();     // key of entry
                var currentVersionMap = entry.getValue(); // value of entry

                var versionEntry = currentVersionMap.lowerEntry(
                        new Version(version.major() + 1, version.minor(), version.patch())); // versionEntry
                if (versionEntry == null) {
                    continue;
                }
                Version currentVersion = versionEntry.getKey();             // key of versionEntry
                SubscriberCallback callback = versionEntry.getValue();      // value of versionEntry

                if (version.isNotStable() || currentVersion.isNotStable()) {
                    continue;
                }

                if (version.consistentWith(currentVersion)) {
                    callback.onMessage(group, version, currentPriority, topic, message);
                    return;
                }
            }
        }
    }

    @Override
    public ChannelMetaInfo meta() {
        synchronized (this) {
            ChannelMetaInfo channelMetaInfo = new ChannelMetaInfo(Sets.newHashSet());

            groupMap.forEach(((group, topicMap) -> {
                GroupMetaInfo groupMetaInfo = new GroupMetaInfo(group.group(), Sets.newHashSet());
                topicMap.forEach((topic, priorityMap) -> {
                    TopicMetaInfo topicMetaInfo = new TopicMetaInfo(topic.topic(), Sets.newHashSet());
                    priorityMap.forEach((priority, versionMap) -> {
                        PriorityMetaInfo priorityMetaInfo = new PriorityMetaInfo(priority.priority(),
                                versionMap.keySet().stream().map(Version::format).collect(Collectors.toSet()));
                        topicMetaInfo.priorities().add(priorityMetaInfo);
                    });
                    groupMetaInfo.topics().add(topicMetaInfo);
                });
                channelMetaInfo.groups().add(groupMetaInfo);
            }));

            return channelMetaInfo;
        }
    }
}
