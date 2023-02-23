package cn.tzq0301.messagecenterexample;

import cn.tzq0301.opensasspringbootstarter.net.handler.server.MessageServerHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MessageCenterExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessageCenterExampleApplication.class, args);
        System.out.println(context.getBean(MessageServerHandler.class));
    }

}
