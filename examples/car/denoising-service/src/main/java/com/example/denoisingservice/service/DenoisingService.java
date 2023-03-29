package com.example.denoisingservice.service;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.middleware.MiddlewareCallback;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import com.example.denoisingservice.context.RuntimeContextDistance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Listener(topic = "sensor")
public class DenoisingService implements MiddlewareCallback {
    private static final int THRESHOLD = 10;

    private static final int WINDOW_SIZE = 20;

    private static final int N_SIGMA = 1;

    private final Topic topic;

    private final ObjectMapper objectMapper;

    private final Queue<RuntimeContextDistance> queue;

    public DenoisingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.topic = new Topic("sensor");
        this.queue = new LinkedList<>();
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
            queue.add(runtimeContextDistance);

            if (queue.size() < THRESHOLD) {
                return;
            }

            System.out.println(queue.size());

            List<RuntimeContextDistance> window = queue.stream().limit(WINDOW_SIZE).toList();

            for (int i = 0; i < WINDOW_SIZE; i++) {
                queue.poll();
            }

            publisher.publish(this.topic, new Message(denoisingRuntimeContextDistance(window)));
        }
    }

    private RuntimeContextDistance denoisingRuntimeContextDistance(final List<RuntimeContextDistance> window) {
        checkNotNull(window);

        double meanLeftDistance = denoisingDistanceAndReturnMean(window.stream().map(RuntimeContextDistance::leftDistance).toList());
        double meanRightDistance = denoisingDistanceAndReturnMean(window.stream().map(RuntimeContextDistance::rightDistance).toList());

        return new RuntimeContextDistance(Double.valueOf(meanLeftDistance).intValue(), Double.valueOf(meanRightDistance).intValue());
    }

    private double denoisingDistanceAndReturnMean(final List<Integer> window) {
        int windowSize = window.size();
        checkArgument(windowSize != 1); // avoid divide by zero exception

        double mean = window.stream().mapToInt(Integer::intValue).average().orElseThrow();
        double sampleStandardDeviation = Math.sqrt(
                window.stream()
                        .map(num -> Math.pow(num - mean, 2))
                        .mapToDouble(Double::doubleValue)
                        .sum() / (windowSize - 1));

        return window.stream()
                .limit(THRESHOLD)
                .filter(value -> value >= (mean - N_SIGMA * sampleStandardDeviation) || value <= (mean + N_SIGMA * sampleStandardDeviation))
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0f);
    }
}
