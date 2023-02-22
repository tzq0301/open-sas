package cn.tzq0301.opensasmessagesprintbootstarter.channel;

import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.ChannelImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.MiddlewareImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.PublisherImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.channel.impl.SubscriberImpl;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Group;
import cn.tzq0301.opensasmessagesprintbootstarter.common.MessageContent;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Priority;
import cn.tzq0301.opensasmessagesprintbootstarter.common.Version;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ChannelTest {
    @Test
    void test() {
        Channel channel = new ChannelImpl();

        Group groupA = Group.of("A");

        Version v1 = Version.of(1, 0, 0);

        Publisher publisherA1 = new PublisherImpl(channel, groupA, v1, Priority.MAX);
        publisherA1.publish(new MessageContent("Nothing"));
        // Nothing ~

        Subscriber subscriberA1 = new SubscriberImpl(groupA, v1, Priority.MIN, o -> System.out.println("groupA & v1 -> " + o));
        subscriberA1.subscribe(channel);
        publisherA1.publish(new MessageContent("1st"));

        Middleware middlewareA1_3 = new MiddlewareImpl(channel, groupA, v1, Priority.of(3), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 333"));
        });
        middlewareA1_3.subscribe(channel);
        publisherA1.publish(new MessageContent("2nd"));

        Middleware middlewareA1_1 = new MiddlewareImpl(channel, groupA, v1, Priority.of(1), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 1"));
        });
        middlewareA1_1.subscribe(channel);
        publisherA1.publish(new MessageContent("3rd"));

        Middleware middlewareA1_4 = new MiddlewareImpl(channel, groupA, v1, Priority.of(4), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 4444"));
        });
        middlewareA1_4.subscribe(channel);
        publisherA1.publish(new MessageContent("4th"));

        Middleware middlewareA1_2 = new MiddlewareImpl(channel, groupA, v1, Priority.of(2), (content, publisher) -> {});
        middlewareA1_2.subscribe(channel);
        publisherA1.publish(new MessageContent("5th"));
        middlewareA1_2.unsubscribe(channel);
        publisherA1.publish(new MessageContent("6th"));

        Version v2 = Version.of(2, 0, 0);

        Publisher publisherA2 = new PublisherImpl(channel, groupA, v2, Priority.MAX);
        publisherA2.publish(new MessageContent("Nothing"));

        Subscriber subscriberA2 = new SubscriberImpl(groupA, v2, Priority.MIN, o -> System.out.println("groupA & v2 -> " + o));
        subscriberA2.subscribe(channel);
        assertThrows(IllegalArgumentException.class, () -> subscriberA2.subscribe(channel));
        publisherA1.publish(new MessageContent("7th"));
        publisherA2.publish(new MessageContent("8th"));

        Middleware middlewareA2_3 = new MiddlewareImpl(channel, groupA, v2, Priority.of(3), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 333"));
        });
        middlewareA2_3.subscribe(channel);
        publisherA1.publish(new MessageContent("9th"));
        publisherA2.publish(new MessageContent("10th"));

        Middleware middlewareA2_6 = new MiddlewareImpl(channel, groupA, v2, Priority.of(6), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 666666"));
        });
        middlewareA2_6.subscribe(channel);
        publisherA1.publish(new MessageContent("11th"));
        publisherA2.publish(new MessageContent("12th"));

        middlewareA2_3.unsubscribe(channel);
        publisherA1.publish(new MessageContent("13th"));
        publisherA2.publish(new MessageContent("14th"));

        Publisher publisherA2_3 = new PublisherImpl(channel, groupA, v2, Priority.of(3));
        publisherA2_3.publish(new MessageContent("15th"));

        Group groupB = Group.of("B");

        Publisher publisherB1 = new PublisherImpl(channel, groupB, v1, Priority.MAX);
        publisherB1.publish(new MessageContent("Nothing"));
        // Nothing ~

        Subscriber subscriberB1 = new SubscriberImpl(groupB, v1, Priority.MIN, o -> System.out.println("groupB & v1 -> " + o));
        subscriberB1.subscribe(channel);
        publisherB1.publish(new MessageContent("16th"));

        Middleware middlewareB1_3 = new MiddlewareImpl(channel, groupB, v1, Priority.of(3), (content, publisher) -> {
            publisher.publish(new MessageContent(content.content() + " 333"));
        });
        middlewareB1_3.subscribe(channel);
        publisherA1.publish(new MessageContent("17th"));
        publisherB1.publish(new MessageContent("18th"));

        middlewareA1_3.unsubscribe(channel);
        publisherA1.publish(new MessageContent("19th"));
        publisherB1.publish(new MessageContent("20th"));
    }
}