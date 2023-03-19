package cn.tzq0301.opensasopenmind.manager.impl;

import cn.tzq0301.opensascore.channel.ChannelMetaInfo;
import cn.tzq0301.opensasopenmind.config.OpenSasProperties;
import cn.tzq0301.opensasopenmind.manager.ChannelManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelManagerImpl implements ChannelManager {
    private final RestTemplate restTemplate;

    private final String serverAddr;

    public ChannelManagerImpl(RestTemplate restTemplate,
                              OpenSasProperties openSasProperties) {
        this.restTemplate = restTemplate;
        this.serverAddr = openSasProperties.getServerAddr();
    }

    @Override
    public ChannelMetaInfo meta() {
        return restTemplate
                .getForEntity(String.format("http://%s/channel/meta", serverAddr), ChannelMetaInfo.class)
                .getBody();
    }
}
