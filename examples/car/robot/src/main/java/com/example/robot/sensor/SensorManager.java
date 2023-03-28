package com.example.robot.sensor;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import com.example.robot.context.RuntimeContext;
import com.example.robot.context.RuntimeContextDistance;
import com.example.robot.context.RuntimeContextInformation;
import org.springframework.stereotype.Component;

@Component
public class SensorManager {
    private final LeftSensor leftSensor;

    private final RightSensor rightSensor;

    private final RuntimeContext runtimeContext;

    private final Publisher publisher;

    private final Topic topic;

    public SensorManager(LeftSensor leftSensor,
                         RightSensor rightSensor,
                         RuntimeContext runtimeContext,
                         Publisher publisher) {
        this.leftSensor = leftSensor;
        this.rightSensor = rightSensor;
        this.runtimeContext = runtimeContext;
        this.publisher = publisher;
        this.topic = new Topic("sensor");
    }

    public void sendRuntimeContextInformation(int coordinateOfApp) {
        RuntimeContextInformation runtimeContextInformation = runtimeContext.getRuntimeContextInformation();

        report(runtimeContextInformation, coordinateOfApp);

        if (hasCrashed(runtimeContextInformation, coordinateOfApp)) {
            throw new RuntimeException("Crash!");
        }

        int leftSensorDistance = leftSensor.getDistance(runtimeContextInformation, coordinateOfApp);
        int rightSensorDistance = rightSensor.getDistance(runtimeContextInformation, coordinateOfApp);
        publisher.publish(topic, new Message(new RuntimeContextDistance(leftSensorDistance, rightSensorDistance)));
    }

    private void report(RuntimeContextInformation runtimeContextInformation, int coordinateOfApp) {
        System.out.printf("%s %s\n", runtimeContextInformation, coordinateOfApp);
    }

    private boolean hasCrashed(RuntimeContextInformation runtimeContextInformation, int coordinateOfApp) {
        int leftCoordinate = runtimeContextInformation.leftCoordinate();
        int rightCoordinate = runtimeContextInformation.rightCoordinate();
        return leftCoordinate < coordinateOfApp || coordinateOfApp < rightCoordinate;
    }
}