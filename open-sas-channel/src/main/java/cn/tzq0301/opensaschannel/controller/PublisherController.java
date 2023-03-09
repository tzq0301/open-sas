package cn.tzq0301.opensaschannel.controller;

import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.message.MessageDetails;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class PublisherController {
    private final Channel channel;

    public PublisherController(Channel channel) {
        this.channel = channel;
    }

    @MessageMapping("/topic/publish")
    public void publish(@Payload MessageDetails details) {
        System.out.println(details); // FIXME
        channel.publish(details);
    }
}
