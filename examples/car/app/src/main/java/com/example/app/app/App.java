package com.example.app.app;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.middleware.MiddlewareCallback;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.example.app.context.RuntimeContextDistance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Listener(topic = "sensor")
public class App implements MiddlewareCallback {
    private final Topic topic;

    private final ObjectMapper objectMapper;

    public App(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.topic = new Topic("actor");
    }

    @Override
    public void onMessage(@NonNull final Group group, @NonNull final Version version, @NonNull final Priority priority,
                          @NonNull final Topic topic, @NonNull final Message message, @NonNull final Publisher publisher) {
        checkNotNull(group);
        checkNotNull(version);
        checkNotNull(priority);
        checkNotNull(topic);
        checkNotNull(message);
        checkNotNull(publisher);

        RuntimeContextDistance runtimeContextDistance = objectMapper.convertValue(message.message(), RuntimeContextDistance.class);

        int leftDistance = runtimeContextDistance.leftDistance();
        int rightDistance = runtimeContextDistance.rightDistance();
        int coordinateBias = (rightDistance - leftDistance) / 2;
        publisher.publish(this.topic, new Message(coordinateBias));
    }
}
