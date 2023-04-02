package com.example.robot.context.impl;

import com.example.robot.context.RuntimeContext;
import com.example.robot.context.RuntimeContextInformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import java.util.Random;

import static io.vavr.API.*;

@Component
public class SimulatedRuntimeContext implements RuntimeContext {
    private static final double noisePercentage = 0.13f;

    private static final int minNoise = 30;

    private static final int maxNoiseBias = 100;

    private final Random random;

    private int epoch;

    public SimulatedRuntimeContext() {
        this.epoch = 0;
        this.random = new Random();
    }

    @Override
    @NonNull
    public RuntimeContextInformation getRuntimeContextInformation() {
        epoch++;
        int leftCoordinate = getLeftCoordinate();
        int rightCoordinate = getRightCoordinate();
        return new RuntimeContextInformation(leftCoordinate, rightCoordinate);
    }

    private int getLeftCoordinate() {
        return Match(epoch).of(
                Case($(t -> t >= 200 && t < 300), 250 - epoch),
                Case($(t -> t >= 300 && t < 400), epoch - 350),
                Case($(), 50)
        ) + makeNoise();
    }

    private int getRightCoordinate() {
        return Match(epoch).of(
                Case($(t -> t >= 200 && t < 300), 150 - epoch),
                Case($(t -> t >= 300 && t < 400), epoch - 450),
                Case($(), -50)
        ) + makeNoise();
    }

    private int makeNoise() {
        if (epoch <= 5 || random.nextDouble() > noisePercentage) {
            return 0;
        }

        return random.nextInt(maxNoiseBias) + (random.nextBoolean() ? minNoise : -minNoise);
    }
}
