package com.example.robot.context.impl;

import com.example.robot.context.RuntimeContext;
import com.example.robot.context.RuntimeContextInformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;

@Component
public class SimulatedRuntimeContext implements RuntimeContext {
    private int epoch;

    public SimulatedRuntimeContext() {
        this.epoch = 0;
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
        );
    }

    private int getRightCoordinate() {
        return Match(epoch).of(
                Case($(t -> t >= 200 && t < 300), 150 - epoch),
                Case($(t -> t >= 300 && t < 400), epoch - 450),
                Case($(), -50)
        );
    }
}
