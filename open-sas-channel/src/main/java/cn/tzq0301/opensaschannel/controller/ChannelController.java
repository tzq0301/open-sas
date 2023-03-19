package cn.tzq0301.opensaschannel.controller;

import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.channel.ChannelMetaInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final Channel channel;

    public ChannelController(Channel channel) {
        this.channel = channel;
    }

    @GetMapping("/meta")
    public ChannelMetaInfo meta() {
        return channel.meta();
    }
}
