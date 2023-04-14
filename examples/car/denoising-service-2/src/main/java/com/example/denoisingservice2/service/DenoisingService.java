package com.example.denoisingservice2.service;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.middleware.MiddlewareCallback;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.example.denoisingservice2.context.RuntimeContextDistance;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedList;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@Listener(topic = "sensor")
@Slf4j
public class DenoisingService implements MiddlewareCallback {
    private static final int THRESHOLD = 5;

    private final ObjectMapper objectMapper;

    private final DistanceHistory leftHistory;

    private final DistanceHistory rightHistory;

    public DenoisingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.leftHistory = new DistanceHistory();
        this.rightHistory = new DistanceHistory();
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

        RuntimeContextDistance runtimeContextDistance = Objects.requireNonNull(
                objectMapper.convertValue(message.message(), RuntimeContextDistance.class));

        synchronized (this) {
            RuntimeContextDistance result = new RuntimeContextDistance(
                    leftHistory.offer(runtimeContextDistance.leftDistance()),
                    rightHistory.offer(runtimeContextDistance.rightDistance()));
            Message toBePublished = new Message(result);
            publisher.publish(topic, toBePublished);
            log.error(toBePublished.toString());
        }
    }

    private static class DistanceHistory {
        private final LinkedList<Integer> list;

        private DistanceHistory() {
            this.list = new LinkedList<>();
        }

        private int offer(int distance) {
            int size = list.size();

            if (size < THRESHOLD) {
                list.offer(distance);
                return distance;
            }

            double average = list.stream().mapToInt(Integer::intValue).average().orElseThrow();
            boolean isAbnormal = (distance > (average + 30)) || (distance < (average - 30));

            if (isAbnormal) {
                distance = list.get(size - 1);
            }

            list.poll();

            list.offer(distance);

            return distance;
        }
    }
}
