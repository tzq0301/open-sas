package cn.tzq0301.messagesubscriberexample;

import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.common.Topic;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.ListenableSubscriber;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageSubscriberExampleApplication {
    @Listener(topic = "A")
    public static class A implements ListenableSubscriber {
        @Override
        public void onMessage(@NonNull final Topic topic, @NonNull Message message) {
            System.out.printf("%s %s\n", topic, message);
        }
    }

    @Listener(topic = "B")
    public static class B implements ListenableSubscriber {
        @Override
        public void onMessage(@NonNull final Topic topic, @NonNull Message message) {
            System.out.printf("%s %s\n", topic, message);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageSubscriberExampleApplication.class, args);
    }

}
