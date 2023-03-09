package cn.tzq0301.opensaschannel.controller;

import cn.tzq0301.opensaschannel.entity.PublishRequest;
import cn.tzq0301.opensascore.channel.Channel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class PublisherController {
    private final Channel channel;

    public PublisherController(Channel channel) {
        this.channel = channel;
    }

    @MessageMapping("/publish")
    public void publish(@Payload PublishRequest request) {
        channel.publish(request.group(), request.version(), request.priority(), request.topic(), request.message());
    }
}
