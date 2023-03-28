package com.example.robot.sensor;

import com.example.robot.context.RuntimeContextInformation;
import org.springframework.stereotype.Component;

@Component
public class LeftSensor {
    public int getDistance(RuntimeContextInformation runtimeContextInformation, int coordinateOfApp) {
        return coordinateOfApp - runtimeContextInformation.leftCoordinate();
    }
}
