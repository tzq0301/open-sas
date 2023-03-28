package com.example.robot.robot;

import com.example.robot.sensor.SensorManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimulatedRuntimeContextAwareRobot {
    private final SensorManager sensorManager;

    private AtomicInteger coordinate;

    public SimulatedRuntimeContextAwareRobot(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.coordinate = new AtomicInteger(0);
    }

    public void moveTo(final int newCoordinate) {
        this.coordinate.set(newCoordinate);
    }

    @Async
    @Scheduled(fixedRate = 50)
    public void sendMessageBySensor() {
        try {
            sensorManager.sendRuntimeContextInformation(coordinate.get());
        } catch (NullPointerException ignored) {
        }
    }
}
