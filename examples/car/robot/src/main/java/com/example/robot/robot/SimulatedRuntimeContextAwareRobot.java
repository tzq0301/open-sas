package com.example.robot.robot;

import com.example.robot.sensor.SensorManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimulatedRuntimeContextAwareRobot {
    private final SensorManager sensorManager;

    private final AtomicInteger coordinate;

    public SimulatedRuntimeContextAwareRobot(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.coordinate = new AtomicInteger(0);
    }

    public void move(final int coordinateBias) {
        this.coordinate.addAndGet(coordinateBias);
    }

    @Async
    @Scheduled(fixedRate = 500)
    public void sendMessageBySensor() {
        try {
            sensorManager.sendRuntimeContextInformation(coordinate.get());
        } catch (NullPointerException ignored) {
        }
    }
}
