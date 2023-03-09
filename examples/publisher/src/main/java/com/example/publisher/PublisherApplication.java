package com.example.publisher;

import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.publisher.Publisher;
import cn.tzq0301.opensascore.topic.Topic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PublisherApplication {

    @RestController
    public static class PublishController {
        private final Publisher publisher;

        public PublishController(Publisher publisher) {
            this.publisher = publisher;
        }

        @GetMapping("/publish/{topic}/{message}")
        public String publish(@PathVariable String topic, @PathVariable String message) {
            publisher.publish(new Topic(topic), new Message(message));
            return "<h1>OK</h1>";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }

}
