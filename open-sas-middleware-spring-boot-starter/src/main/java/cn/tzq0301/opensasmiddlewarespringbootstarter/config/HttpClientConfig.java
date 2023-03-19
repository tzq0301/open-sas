package cn.tzq0301.opensasmiddlewarespringbootstarter.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootConfiguration
public class HttpClientConfig {
    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
