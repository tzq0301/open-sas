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
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ChannelImpl implements Channel {
//    private final Map<Group, NavigableMap<Version, Map<Topic, NavigableMap<Priority, SubscriberCallback>>>> groupMap;

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

//        synchronized (this) {
//            if (!groupMap.containsKey(group)) {
//                groupMap.put(group, Maps.newTreeMap());
//            }
//
//            var versionMap = checkNotNull(groupMap.get(group));
//            if (!versionMap.containsKey(version)) {
//                versionMap.put(version, Maps.newHashMap());
//            }
//
//            var topicMap = checkNotNull(versionMap.get(version));
//            topicToCallbackMap.forEach((topic, subscriberCallback) -> {
//                if (!topicMap.containsKey(topic)) {
//                    topicMap.put(topic, Maps.newTreeMap());
//                }
//                var priorityMap = checkNotNull(topicMap.get(topic));
//                checkArgument(!priorityMap.containsKey(priority), "The subscriber with same group (%s), same version (%s), same priority (%), same topic(%s)", group, version, priority, topic);
//                priorityMap.put(priority, subscriberCallback);
//            });
//        }
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

//        synchronized (this) {
//            checkArgument(groupMap.containsKey(group), "Group (%s) is not exists", group);
//
//            var versionMap = checkNotNull(groupMap.get(group));
//            checkArgument(versionMap.containsKey(version), "Version (%s) is not exists", version);
//
//            var topicMap = checkNotNull(versionMap.get(version));
//            topicMap.forEach((topic, prioritySubscriberCallbackNavigableMap) -> {
//                var priorityMap = checkNotNull(topicMap.get(topic));
//                // remove subscriber
//                priorityMap.remove(priority);
//            });
//
//            topicMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
//
//            // dynamically remove topicMap if empty
//            if (topicMap.isEmpty()) {
//                versionMap.remove(version);
//            }
//
//            // dynamically remove versionMap if empty
//            if (versionMap.isEmpty()) {
//                groupMap.remove(group);
//            }
//        }
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
            // to find the first version, which is either equals with the `version` or is compatible with the `version`
            for (var entry : priorityMap.entrySet()) {  // entry
                var currentPriority = entry.getKey();     // key of entry
                var currentVersionMap = entry.getValue(); // value of entry

                var versionEntry = currentVersionMap.floorEntry(version); // versionEntry
                if (versionEntry == null) {
                    continue;
                }
                Version currentVersion = versionEntry.getKey();             // key of versionEntry
                SubscriberCallback callback = versionEntry.getValue();      // value of versionEntry

                if (version.isNotStable() || currentVersion.isNotStable()) {
                    continue;
                }

                if (version.compatibleWith(currentVersion)) {
                    callback.onMessage(group, version, currentPriority, topic, message);
                    return;
                }
            }


//            Priority finalPriority = priorityMap.lastKey();
//
//            var versionMap = priorityMap.get(priority);
//            Optional.ofNullable(versionMap.floorEntry(version))
//                    .ifPresent(entry -> entry.getValue().onMessage(group, entry.getKey(), finalPriority, topic, message));
        }

//        synchronized (this) {
//            if (!groupMap.containsKey(group)) {
//                return;
//            }
//
//            // FIXME if 1.0.2 & 1.0.1 & 1.0.0, then 1.0.0 will never be access by the compatible rule
//            var versionMap = checkNotNull(groupMap.get(group));
//            Version maybeCompatibleVersion = version;
//            if (!versionMap.containsKey(version)) {
//                // 兼容性
//                maybeCompatibleVersion = versionMap.floorKey(version);
//                if (maybeCompatibleVersion == null || !version.compatibleWith(maybeCompatibleVersion)) {
//                    return;
//                }
//            }
//            checkNotNull(maybeCompatibleVersion);
//
//            var topicMap = checkNotNull(versionMap.get(maybeCompatibleVersion));
//            if (!topicMap.containsKey(topic)) {
//                return;
//            }
//
//            var priorityMap = checkNotNull(topicMap.get(topic));
//            Optional.ofNullable(priorityMap.floorEntry(priority))
//                    .ifPresent(entry -> entry.getValue().onMessage(group, version, entry.getKey(), topic, message));
//
////                    .map(Map.Entry::getValue)
////                    .ifPresent(callback -> callback.onMessage(group, version, priority, topic, message));
//        }
    }
}
