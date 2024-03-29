package cn.tzq0301.opensasopenmind.controller;

import cn.tzq0301.opensascore.channel.meta.ChannelMetaInfo;
import cn.tzq0301.opensasopenmind.config.OpenSasProperties;
import cn.tzq0301.opensasopenmind.entity.token.CreateTokenRequest;
import cn.tzq0301.opensasopenmind.exception.token.InvalidTokenException;
import cn.tzq0301.opensasopenmind.exception.user.AccountNotExistException;
import cn.tzq0301.opensasopenmind.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    private final String serverAddr;

    public ChannelController(ChannelService channelService, OpenSasProperties openSasProperties) {
        this.channelService = channelService;
        this.serverAddr = openSasProperties.getServerAddr();
    }

    @PostMapping("/token")
    public String createToken(@RequestBody CreateTokenRequest request) throws AccountNotExistException {
        return channelService.createToken(request.userId(), LocalDateTime.now().plus(3, ChronoUnit.MONTHS));
    }

    @GetMapping("/serverAddr")
    public String getServerAddr(@RequestParam String token) throws InvalidTokenException {
        if (!channelService.isTokenValid(token)) {
            throw new InvalidTokenException();
        }
        return serverAddr;
    }

    @GetMapping("/meta")
    public ChannelMetaInfo meta() {
        return channelService.meta();
    }
}
