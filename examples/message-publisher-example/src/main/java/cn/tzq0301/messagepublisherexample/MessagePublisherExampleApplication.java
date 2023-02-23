package cn.tzq0301.messagepublisherexample;

import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import cn.tzq0301.opensasmessagesprintbootstarter.net.handler.client.MessagePublisherHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.socket.WebSocketHandler;

@SpringBootApplication
public class MessagePublisherExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(MessagePublisherExampleApplication.class, args);
        Thread.sleep(1_000);
        context.getBean(MessagePublisherHandler.class).publish(new MessageContent("111"));
    }

}
