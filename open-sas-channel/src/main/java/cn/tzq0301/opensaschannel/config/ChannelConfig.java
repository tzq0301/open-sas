package cn.tzq0301.opensaschannel.config;

import cn.tzq0301.opensascore.channel.Channel;
import cn.tzq0301.opensascore.channel.ChannelImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ChannelConfig {
    @Bean
    public Channel channel() {
        return new ChannelImpl();
    }
}
