package cn.tzq0301.opensasspringbootstarter.net.config;

import cn.tzq0301.opensasspringbootstarter.common.Version;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class VersionConfig {
    @Bean
    @ConditionalOnMissingBean(Version.class)
    public Version version() {
        return Version.DEFAULT_VERSION;
    }
}
