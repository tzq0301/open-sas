package cn.tzq0301.messagesubscriberexample;

import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.Listener;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.OnMessageCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MessageSubscriberExampleApplication {
    @Component
    public static class Callback {
        @Listener
        @SuppressWarnings("unused")
        public OnMessageCallback onMessageCallback() {
            return System.out::println;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageSubscriberExampleApplication.class, args);
    }

}
