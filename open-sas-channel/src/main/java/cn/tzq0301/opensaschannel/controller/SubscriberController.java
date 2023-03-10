package cn.tzq0301.opensaschannel.controller;

import cn.tzq0301.opensaschannel.entity.SubscribeRequest;
import cn.tzq0301.opensaschannel.entity.UnsubscribeRequest;
import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.message.MessageDetails;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class SubscriberController {
    private final Channel channel;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public SubscriberController(Channel channel,
                                SimpMessagingTemplate simpMessagingTemplate) {
        this.channel = channel;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/topic/subscribe")
    public void subscribe(@Payload SubscribeRequest request, @Header("simpSessionId") String sessionId, MessageHeaders messageHeaders) {
        channel.registerSubscriber(request.group(), request.version(), request.priority(), new HashMap<>() {{
            request.topics().forEach(topic -> put(topic, (t, message) ->
                    simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/message",
                            new MessageDetails(request.group(), request.version(), request.priority(), t, message), messageHeaders)));
        }});
    }

    @MessageMapping("/topic/unsubscribe")
    public void unsubscribe(@Payload UnsubscribeRequest request) {
        channel.unregisterSubscriber(request.group(), request.version(), request.priority());
    }
}
