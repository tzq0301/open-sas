package com.example.robot.actor;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.example.robot.robot.SimulatedRuntimeContextAwareRobot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Listener(topic = "actor")
public class Actor implements SubscriberCallback {
    private final SimulatedRuntimeContextAwareRobot robot;

    private final ObjectMapper objectMapper;

    public Actor(SimulatedRuntimeContextAwareRobot robot,
                 ObjectMapper objectMapper) {
        this.robot = robot;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(@NonNull final Group group, @NonNull final Version version, @NonNull final Priority priority,
                          @NonNull final Topic topic, @NonNull final Message message) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topic);
        checkNotNull(message);

        int coordinateBias = objectMapper.convertValue(message.message(), Integer.class);
        robot.move(coordinateBias);
    }
}
