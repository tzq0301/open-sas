package cn.tzq0301.opensasspringbootstarter.channel;

import cn.tzq0301.opensasspringbootstarter.channel.impl.ChannelImpl;
import cn.tzq0301.opensasspringbootstarter.channel.impl.PublisherImpl;
import cn.tzq0301.opensasspringbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasspringbootstarter.common.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
                       MiddlewareCallback callback) {
            this.publisher = message -> channel.publish(group, version, Priorities.cloneByDownGrade(priority), message);
            this.subscriber = new SubscriberImpl(group, version, priority, message -> callback.onMessage(message, publisher));
        }

        @Override
        public void publish(@NonNull Message message) {
            publisher.publish(message);
        }

        @Override
        public void onMessage(@NonNull Message message) {
            subscriber.onMessage(message);
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
    @Disabled
    void test() {
        Channel channel = new ChannelImpl();

        Group groupA = Group.of("A");

        Version v1 = Version.of(1, 0, 0);

        Publisher publisherA1 = new PublisherImpl(channel, groupA, v1, Priority.MAX);
        publisherA1.publish(new Message("Nothing"));
        // Nothing ~

        Subscriber subscriberA1 = new SubscriberImpl(groupA, v1, Priority.MIN, o -> System.out.println("groupA & v1 -> " + o));
        subscriberA1.subscribe(channel);
        publisherA1.publish(new Message("1st"));

        Middleware middlewareA1_3 = new MiddlewareImpl(channel, groupA, v1, Priority.of(3), (message, publisher) -> {
            publisher.publish(new Message(message + " 333"));
        });
        middlewareA1_3.subscribe(channel);
        publisherA1.publish(new Message("2nd"));

        Middleware middlewareA1_1 = new MiddlewareImpl(channel, groupA, v1, Priority.of(1), (message, publisher) -> {
            publisher.publish(new Message(message + " 1"));
        });
        middlewareA1_1.subscribe(channel);
        publisherA1.publish(new Message("3rd"));

        Middleware middlewareA1_4 = new MiddlewareImpl(channel, groupA, v1, Priority.of(4), (message, publisher) -> {
            publisher.publish(new Message(message + " 4444"));
        });
        middlewareA1_4.subscribe(channel);
        publisherA1.publish(new Message("4th"));

        Middleware middlewareA1_2 = new MiddlewareImpl(channel, groupA, v1, Priority.of(2), (message, publisher) -> {});
        middlewareA1_2.subscribe(channel);
        publisherA1.publish(new Message("5th"));
        middlewareA1_2.unsubscribe(channel);
        publisherA1.publish(new Message("6th"));

        Version v2 = Version.of(2, 0, 0);

        Publisher publisherA2 = new PublisherImpl(channel, groupA, v2, Priority.MAX);
        publisherA2.publish(new Message("Nothing"));

        Subscriber subscriberA2 = new SubscriberImpl(groupA, v2, Priority.MIN, o -> System.out.println("groupA & v2 -> " + o));
        subscriberA2.subscribe(channel);
        assertThrows(IllegalArgumentException.class, () -> subscriberA2.subscribe(channel));
        publisherA1.publish(new Message("7th"));
        publisherA2.publish(new Message("8th"));

        Middleware middlewareA2_3 = new MiddlewareImpl(channel, groupA, v2, Priority.of(3), (message, publisher) -> {
            publisher.publish(new Message(message + " 333"));
        });
        middlewareA2_3.subscribe(channel);
        publisherA1.publish(new Message("9th"));
        publisherA2.publish(new Message("10th"));

        Middleware middlewareA2_6 = new MiddlewareImpl(channel, groupA, v2, Priority.of(6), (message, publisher) -> {
            publisher.publish(new Message(message + " 666666"));
        });
        middlewareA2_6.subscribe(channel);
        publisherA1.publish(new Message("11th"));
        publisherA2.publish(new Message("12th"));

        middlewareA2_3.unsubscribe(channel);
        publisherA1.publish(new Message("13th"));
        publisherA2.publish(new Message("14th"));

        Publisher publisherA2_3 = new PublisherImpl(channel, groupA, v2, Priority.of(3));
        publisherA2_3.publish(new Message("15th"));

        Group groupB = Group.of("B");

        Publisher publisherB1 = new PublisherImpl(channel, groupB, v1, Priority.MAX);
        publisherB1.publish(new Message("Nothing"));
        // Nothing ~

        Subscriber subscriberB1 = new SubscriberImpl(groupB, v1, Priority.MIN, o -> System.out.println("groupB & v1 -> " + o));
        subscriberB1.subscribe(channel);
        publisherB1.publish(new Message("16th"));

        Middleware middlewareB1_3 = new MiddlewareImpl(channel, groupB, v1, Priority.of(3), (message, publisher) -> {
            publisher.publish(new Message(message + " 333"));
        });
        middlewareB1_3.subscribe(channel);
        publisherA1.publish(new Message("17th"));
        publisherB1.publish(new Message("18th"));

        middlewareA1_3.unsubscribe(channel);
        publisherA1.publish(new Message("19th"));
        publisherB1.publish(new Message("20th"));
    }
}