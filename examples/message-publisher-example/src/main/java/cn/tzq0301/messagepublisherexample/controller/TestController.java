package cn.tzq0301.messagepublisherexample.controller;

import cn.tzq0301.opensasspringbootstarter.common.MessageContent;
import cn.tzq0301.opensasspringbootstarter.sdk.publisher.MessagePublisher;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Resource
    MessagePublisher publisher;

    @GetMapping("/publish/{message}")
    public String publish(@PathVariable String message) {
        publisher.publish(new MessageContent(message));
        return "<h1>OK</h1>";
    }
}
