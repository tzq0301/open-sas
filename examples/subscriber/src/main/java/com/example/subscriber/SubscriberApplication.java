package com.example.subscriber;

import cn.tzq0301.opensascore.group.Group;
import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.message.Message;
import cn.tzq0301.opensascore.priority.Priority;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import cn.tzq0301.opensascore.version.Version;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubscriberApplication {

	@Listener(topic = "A")
	public static class A implements SubscriberCallback {
		@Override
		public void onMessage(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull Topic topic, @NonNull Message message) {
			System.out.printf("%s %s\n", topic, message);
		}
	}

	@Listener(topic = "B")
	public static class B implements SubscriberCallback {
		@Override
		public void onMessage(@NonNull Group group, @NonNull Version version, @NonNull Priority priority, @NonNull Topic topic, @NonNull Message message) {
			System.out.printf("%s %s\n", topic, message);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}

}
