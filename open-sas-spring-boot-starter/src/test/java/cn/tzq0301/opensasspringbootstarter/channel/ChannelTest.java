package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.channel.impl.ChannelImpl;
import cn.tzq0301.opensasspringbootstarter.channel.impl.PublisherImpl;
import cn.tzq0301.opensasspringbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.jupiter.api.Assertions.*;

class ChannelTest {
    static class MiddlewareImpl implements Middleware {
        Publisher publisher;

        Subscriber subscriber;

        MiddlewareImpl(Channel channel,
                       Group group,
                       Version version,
                       Priority priority,
                       Map<Topic, MiddlewareCallback> topicToMiddlewareCallbackMap) {
            this.publisher = (topic, message) -> channel.publish(group, version, Priorities.cloneByDownGrade(priority), topic, message);
            this.subscriber = new SubscriberImpl(group, version, priority, new HashMap<>() {{
                topicToMiddlewareCallbackMap.forEach(((topic, middlewareCallback) -> {
                    put(topic, (t, message) -> middlewareCallback.onMessage(t, message, publisher));
                }));
            }});
        }

        MiddlewareImpl(Channel channel,
                       Group group,
                       Version version,
                       Priority priority,
                       Topic topic,
                       MiddlewareCallback callback) {
            this(channel, group, version, priority, new HashMap<>() {{
                put(topic, callback);
            }});
        }

        @Override
        public void publish(@NonNull final Topic topic, @NonNull final Message message) {
            publisher.publish(topic, message);
        }

        @Override
        public void onMessage(@NonNull final Topic topic, @NonNull Message message) {
            subscriber.onMessage(topic, message);
        }

        @Override
        public void subscribe(@NonNull Channel channel) {
            checkNotNull(channel);
            subscriber.subscribe(channel);
        }

        @Override
        public void unsubscribe(@NonNull Channel channel) {
            checkNotNull(channel);
            subscriber.unsubscribe(channel);
        }
    }

    @Test
//    @Disabled
    void test() {
        Channel channel = new ChannelImpl();

        Group groupA = Group.of("A");

        Version v1 = Version.of(1, 0, 0);

        Topic topicA = Topic.of("A");
        Topic topicB = Topic.of("B");

        Publisher publisherA1 = new PublisherImpl(channel, groupA, v1, Priority.MAX);
        publisherA1.publish(topicA, new Message("Nothing"));
        // Nothing ~

        Subscriber subscriberA1 = new SubscriberImpl(groupA, v1, Priority.MIN, new HashMap<>() {{
            put(topicA, (t, o) -> System.out.println("groupA & v1 & topic=A -> " + o));
            put(topicB, (t, o) -> System.out.println("groupA & v1 & topic=B -> " + o));
        }});
        subscriberA1.subscribe(channel);
        publisherA1.publish(topicA, new Message("1st"));

        Middleware middlewareA1_3 = new MiddlewareImpl(channel, groupA, v1, Priority.of(3), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 333"));
        });
        middlewareA1_3.subscribe(channel);
        publisherA1.publish(topicA, new Message("2nd"));

        Middleware middlewareA1_1 = new MiddlewareImpl(channel, groupA, v1, Priority.of(1), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 1"));
        });
        middlewareA1_1.subscribe(channel);
        publisherA1.publish(topicA, new Message("3rd"));

        Middleware middlewareA1_4 = new MiddlewareImpl(channel, groupA, v1, Priority.of(4), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 4444"));
        });
        middlewareA1_4.subscribe(channel);
        publisherA1.publish(topicA, new Message("4th"));

        Middleware middlewareA1_2 = new MiddlewareImpl(channel, groupA, v1, Priority.of(2), topicA, (topic, message, publisher) -> {
        });
        middlewareA1_2.subscribe(channel);
        publisherA1.publish(topicA, new Message("5th"));
        middlewareA1_2.unsubscribe(channel);
        publisherA1.publish(topicA, new Message("6th"));

        Version v2 = Version.of(2, 0, 0);

        Publisher publisherA2 = new PublisherImpl(channel, groupA, v2, Priority.MAX);
        publisherA2.publish(topicA, new Message("Nothing"));

        Subscriber subscriberA2 = new SubscriberImpl(groupA, v2, Priority.MIN, new HashMap<>() {{
            put(topicA, (t, o) -> System.out.println("groupA & v2 -> " + o));
        }});
        subscriberA2.subscribe(channel);
        assertThrows(IllegalArgumentException.class, () -> subscriberA2.subscribe(channel));
        publisherA1.publish(topicA, new Message("7th"));
        publisherA2.publish(topicA, new Message("8th"));

        Middleware middlewareA2_3 = new MiddlewareImpl(channel, groupA, v2, Priority.of(3), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 333"));
        });
        middlewareA2_3.subscribe(channel);
        publisherA1.publish(topicA, new Message("9th"));
        publisherA2.publish(topicA, new Message("10th"));

        Middleware middlewareA2_6 = new MiddlewareImpl(channel, groupA, v2, Priority.of(6), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 666666"));
        });
        middlewareA2_6.subscribe(channel);
        publisherA1.publish(topicA, new Message("11th"));
        publisherA2.publish(topicA, new Message("12th"));

        middlewareA2_3.unsubscribe(channel);
        publisherA1.publish(topicA, new Message("13th"));
        publisherA2.publish(topicA, new Message("14th"));

        Publisher publisherA2_3 = new PublisherImpl(channel, groupA, v2, Priority.of(3));
        publisherA2_3.publish(topicA, new Message("15th"));

        Group groupB = Group.of("B");

        Publisher publisherB1 = new PublisherImpl(channel, groupB, v1, Priority.MAX);
        publisherB1.publish(topicA, new Message("Nothing"));
        // Nothing ~

        Subscriber subscriberB1 = new SubscriberImpl(groupB, v1, Priority.MIN, new HashMap<>() {{
            put(topicA, (t, o) -> System.out.println("groupB & v1 -> " + o));
        }});
        subscriberB1.subscribe(channel);
        publisherB1.publish(topicA, new Message("16th"));

        Middleware middlewareB1_3 = new MiddlewareImpl(channel, groupB, v1, Priority.of(3), topicA, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 333"));
        });
        middlewareB1_3.subscribe(channel);
        publisherA1.publish(topicA, new Message("17th"));
        publisherB1.publish(topicA, new Message("18th"));

        middlewareA1_3.unsubscribe(channel);
        publisherA1.publish(topicA, new Message("19th"));
        publisherB1.publish(topicA, new Message("20th"));

        publisherA1.publish(topicA, new Message("21st"));
        publisherA1.publish(topicB, new Message("22nd"));
        publisherB1.publish(topicB, new Message("23th")); // Nothing ~

        Middleware middlewareA1_4_B = new MiddlewareImpl(channel, groupA, v1, Priority.of(4), topicB, (topic, message, publisher) -> {
            publisher.publish(topic, new Message(message.message() + " 4444"));
        });
        middlewareA1_4_B.subscribe(channel);

        publisherA1.publish(topicB, new Message("24th"));
        publisherB1.publish(topicB, new Message("25th")); // Nothing ~

    }
}