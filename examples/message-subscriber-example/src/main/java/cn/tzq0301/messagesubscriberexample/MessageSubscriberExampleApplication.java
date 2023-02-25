package cn.tzq0301.messagesubscriberexample;

import cn.tzq0301.opensasspringbootstarter.channel.MiddlewareCallback;
import cn.tzq0301.opensasspringbootstarter.channel.Publisher;
import cn.tzq0301.opensasspringbootstarter.common.Message;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.Listener;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.ListenableSubscriber;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageSubscriberExampleApplication {
//    @Component
//    public static class Callback {
//        @Listener
//        @SuppressWarnings("unused")
//        public SubscriberOnMessageCallback onMessageCallback() {
//            return System.out::println;
//        }
//    }

    @Listener
    public static class CallbackListenable implements ListenableSubscriber {
        @Override
        public void onMessage(@NonNull Message message) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageSubscriberExampleApplication.class, args);
    }

}
