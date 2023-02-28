package cn.tzq0301.messagemiddleware5example;

import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Topic;
import cn.tzq0301.opensasspringbootstarter.sdk.common.Listener;
import cn.tzq0301.opensasspringbootstarter.sdk.middleware.ListenableMiddleware;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageMiddleware5ExampleApplication {
    @Listener(topic = "A")
    public static class A implements ListenableMiddleware {
        @Override
        public void onMessage(@NonNull Topic topic, @NonNull Message message, @NonNull Publisher publisher) {
            System.out.printf("%s %s\n", topic, message);
            publisher.publish(topic, new Message("55555 " + message.message()));
        }
    }

    @Listener(topic = "B")
    public static class B implements ListenableMiddleware {
        @Override
        public void onMessage(@NonNull final Topic topic, @NonNull Message message, @NonNull Publisher publisher) {
            System.out.printf("%s %s\n", topic, message);
//            publisher.publish(topic, new Message("55555 " + message.message()));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageMiddleware5ExampleApplication.class, args);
    }

}
