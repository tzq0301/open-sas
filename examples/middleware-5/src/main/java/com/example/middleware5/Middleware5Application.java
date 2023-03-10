package com.example.middleware5;

import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.middleware.MiddlewareCallback;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Middleware5Application {

    @Listener(topic = "A")
    public static class A implements MiddlewareCallback {
        @Override
        public void onMessage(@NonNull Topic topic, @NonNull Message message, @NonNull Publisher publisher) {
            System.out.printf("%s %s\n", topic, message);
            publisher.publish(topic, new Message("55555 " + message.message()));
        }
    }

    @Listener(topic = "B")
    public static class B implements MiddlewareCallback {
        @Override
        public void onMessage(@NonNull final Topic topic, @NonNull Message message, @NonNull Publisher publisher) {
            System.out.printf("%s %s\n", topic, message);
//            publisher.publish(topic, new Message("55555 " + message.message()));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Middleware5Application.class, args);
    }

}
