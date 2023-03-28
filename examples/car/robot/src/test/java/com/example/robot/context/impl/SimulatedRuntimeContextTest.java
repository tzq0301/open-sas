package com.example.robot.context.impl;

import com.example.robot.context.RuntimeContext;
import com.example.robot.context.RuntimeContextInformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatedRuntimeContextTest {

    @Test
    void getRuntimeContextInformation() {
        RuntimeContext context = new SimulatedRuntimeContext();

        for (int i = 0; i < 249; i++) {
            context.getRuntimeContextInformation();
        }

        // 250-th
        RuntimeContextInformation information = context.getRuntimeContextInformation();
        assertEquals(0, information.leftCoordinate());
        assertEquals(-100, information.rightCoordinate());

        for (int i = 0; i < 49; i++) {
            context.getRuntimeContextInformation();
        }

        // 300-th
        information = context.getRuntimeContextInformation();
        assertEquals(-50, information.leftCoordinate());
        assertEquals(-150, information.rightCoordinate());

        for (int i = 0; i < 49; i++) {
            context.getRuntimeContextInformation();
        }

        // 350-th
        information = context.getRuntimeContextInformation();
        assertEquals(0, information.leftCoordinate());
        assertEquals(-100, information.rightCoordinate());

        for (int i = 0; i < 49; i++) {
            context.getRuntimeContextInformation();
        }

        // 400-th
        information = context.getRuntimeContextInformation();
        assertEquals(50, information.leftCoordinate());
        assertEquals(-50, information.rightCoordinate());

        for (int i = 0; i < 49; i++) {
            context.getRuntimeContextInformation();
        }

        // 450-th
        information = context.getRuntimeContextInformation();
        assertEquals(50, information.leftCoordinate());
        assertEquals(-50, information.rightCoordinate());
    }
}