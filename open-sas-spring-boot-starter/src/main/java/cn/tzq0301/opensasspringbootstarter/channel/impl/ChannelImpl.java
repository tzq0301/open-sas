package cn.tzq0301.opensasspringbootstarter.channel.impl;

import cn.tzq0301.opensasspringbootstarter.channel.Channel;
import cn.tzq0301.opensasspringbootstarter.channel.SubscriberCallback;
import cn.tzq0301.opensasspringbootstarter.common.Group;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Priority;
import cn.tzq0301.opensasspringbootstarter.common.Version;
import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
@ConditionalOnProperty(prefix = "open-sas", name = "server", havingValue = "true")
public final class ChannelImpl implements Channel {
    private final Map<Group, Map<Version, NavigableMap<Priority, SubscriberCallback>>> groupMap;

    public ChannelImpl() {
        this.groupMap = Maps.newHashMap();
    }

    @Override
    public synchronized void registerSubscriber(@NonNull final Group group,
                                                @NonNull final Version version,
                                                @NonNull final Priority priority,
                                                @NonNull final SubscriberCallback subscriber) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(subscriber);

        if (!groupMap.containsKey(group)) {
            groupMap.put(group, Maps.newHashMap());
        }

        var versionMap = checkNotNull(groupMap.get(group));
        if (!versionMap.containsKey(version)) {
            versionMap.put(version, Maps.newTreeMap());
        }

        var priorityMap = checkNotNull(versionMap.get(version));
        checkArgument(!priorityMap.containsKey(priority), "The subscriber with same group (%s), same priority (%) and same version (%s)", group, priority, version);
        priorityMap.put(priority, subscriber);
    }

    @Override
    public synchronized void unregisterSubscriber(@NonNull final Group group,
                                                  @NonNull final Version version,
                                                  @NonNull final Priority priority) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);

        checkArgument(groupMap.containsKey(group), "Group (%s) is not exists", group);

        var versionMap = checkNotNull(groupMap.get(group));
        checkArgument(versionMap.containsKey(version), "Version (%s) is not exists", version);

        var priorityMap = checkNotNull(versionMap.get(version));
        checkArgument(priorityMap.containsKey(priority), "Priority (%s) is not exists", priority);

        // remove subscribe
        priorityMap.remove(priority);

        // dynamically remove priorityMap if empty
        if (priorityMap.isEmpty()) {
            versionMap.remove(version);
        }

        // dynamically remove versionMap if empty
        if (versionMap.isEmpty()) {
            groupMap.remove(group);
        }
    }

    @Override
    public synchronized void publish(@NonNull final Group group,
                                     @NonNull final Version version,
                                     @NonNull final Priority priority,
                                     @NonNull final Message message) {
        checkNotNull(message);

        if (!groupMap.containsKey(group)) {
            return;
        }

        var versionMap = checkNotNull(groupMap.get(group));
        if (!versionMap.containsKey(version)) {
            return;
        }

        var priorityMap = checkNotNull(versionMap.get(version));
        Optional.ofNullable(priorityMap.floorEntry(priority))
                .map(Map.Entry::getValue)
                .ifPresent(subscriber -> subscriber.onMessage(message));
    }
}
