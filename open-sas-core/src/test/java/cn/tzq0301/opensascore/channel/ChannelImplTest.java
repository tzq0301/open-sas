//package cn.tzq0301.opensascore.channel;
//
//import cn.tzq0301.opensascore.group.Group;
//import cn.tzq0301.opensascore.message.Message;
//import cn.tzq0301.opensascore.middleware.Middleware;
//import cn.tzq0301.opensascore.middleware.MiddlewareImpl;
//import cn.tzq0301.opensascore.priority.Priority;
//import cn.tzq0301.opensascore.publisher.Publisher;
//import cn.tzq0301.opensascore.publisher.PublisherImpl;
//import cn.tzq0301.opensascore.subscriber.Subscriber;
//import cn.tzq0301.opensascore.subscriber.SubscriberImpl;
//import cn.tzq0301.opensascore.topic.Topic;
//import cn.tzq0301.opensascore.version.Version;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//
//import static cn.tzq0301.opensascore.priority.Priority.MAX_PRIORITY;
//import static cn.tzq0301.opensascore.priority.Priority.MIN_PRIORITY;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class ChannelImplTest {
//    @Test
//    void test() {
//        Channel channel = new ChannelImpl();
//
//        Group groupA = new Group("A");
//
//        Version v1 = new Version(1, 0, 0);
//
//        Topic topicA = new Topic("A");
//        Topic topicB = new Topic("B");
//
//        Publisher publisherA1 = new PublisherImpl(channel, groupA, v1, MAX_PRIORITY);
//        publisherA1.publish(topicA, new Message("Nothing"));
//        // Nothing ~
//
//        Subscriber subscriberA1 = new SubscriberImpl(groupA, v1, MIN_PRIORITY, new HashMap<>() {{
//            put(topicA, (t, o) -> System.out.println("groupA & v1 & topic=A -> " + o));
//            put(topicB, (t, o) -> System.out.println("groupA & v1 & topic=B -> " + o));
//        }});
//        subscriberA1.subscribe(channel);
//        publisherA1.publish(topicA, new Message("1st"));
//
//        Middleware middlewareA1_3 = new MiddlewareImpl(channel, groupA, v1, new Priority(3), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 333"));
//            });
//        }});
//        middlewareA1_3.subscribe(channel);
//        publisherA1.publish(topicA, new Message("2nd"));
//
//        Middleware middlewareA1_1 = new MiddlewareImpl(channel, groupA, v1, new Priority(1), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 1"));
//            });
//        }});
//        middlewareA1_1.subscribe(channel);
//        publisherA1.publish(topicA, new Message("3rd"));
//
//        Middleware middlewareA1_4 = new MiddlewareImpl(channel, groupA, v1, new Priority(4), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 4444"));
//            });
//        }});
//        middlewareA1_4.subscribe(channel);
//        publisherA1.publish(topicA, new Message("4th"));
//
//        Middleware middlewareA1_2 = new MiddlewareImpl(channel, groupA, v1, new Priority(2), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {});
//        }});
//        middlewareA1_2.subscribe(channel);
//        publisherA1.publish(topicA, new Message("5th"));
//        middlewareA1_2.unsubscribe(channel);
//        publisherA1.publish(topicA, new Message("6th"));
//
//        Version v2 = new Version(2, 0, 0);
//
//        Publisher publisherA2 = new PublisherImpl(channel, groupA, v2, MAX_PRIORITY);
//        publisherA2.publish(topicA, new Message("Nothing"));
//
//        Subscriber subscriberA2 = new SubscriberImpl(groupA, v2, MIN_PRIORITY, new HashMap<>() {{
//            put(topicA, (t, o) -> System.out.println("groupA & v2 -> " + o));
//        }});
//        subscriberA2.subscribe(channel);
//        assertThrows(IllegalArgumentException.class, () -> subscriberA2.subscribe(channel));
//        publisherA1.publish(topicA, new Message("7th"));
//        publisherA2.publish(topicA, new Message("8th"));
//
//        Middleware middlewareA2_3 = new MiddlewareImpl(channel, groupA, v2, new Priority(3), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 333"));
//            });
//        }});
//        middlewareA2_3.subscribe(channel);
//        publisherA1.publish(topicA, new Message("9th"));
//        publisherA2.publish(topicA, new Message("10th"));
//
//        Middleware middlewareA2_6 = new MiddlewareImpl(channel, groupA, v2, new Priority(6), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 666666"));
//            });
//        }});
//        middlewareA2_6.subscribe(channel);
//        publisherA1.publish(topicA, new Message("11th"));
//        publisherA2.publish(topicA, new Message("12th"));
//
//        middlewareA2_3.unsubscribe(channel);
//        publisherA1.publish(topicA, new Message("13th"));
//        publisherA2.publish(topicA, new Message("14th"));
//
//        Publisher publisherA2_3 = new PublisherImpl(channel, groupA, v2, new Priority(3));
//        publisherA2_3.publish(topicA, new Message("15th"));
//
//        Group groupB = new Group("B");
//
//        Publisher publisherB1 = new PublisherImpl(channel, groupB, v1, MAX_PRIORITY);
//        publisherB1.publish(topicA, new Message("Nothing"));
//        // Nothing ~
//
//        Subscriber subscriberB1 = new SubscriberImpl(groupB, v1, MIN_PRIORITY, new HashMap<>() {{
//            put(topicA, (t, o) -> System.out.println("groupB & v1 -> " + o));
//        }});
//        subscriberB1.subscribe(channel);
//        publisherB1.publish(topicA, new Message("16th"));
//
//        Middleware middlewareB1_3 = new MiddlewareImpl(channel, groupB, v1, new Priority(3), new HashMap<>() {{
//            put(topicA, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 333"));
//            });
//        }});
//        middlewareB1_3.subscribe(channel);
//        publisherA1.publish(topicA, new Message("17th"));
//        publisherB1.publish(topicA, new Message("18th"));
//
//        middlewareA1_3.unsubscribe(channel);
//        publisherA1.publish(topicA, new Message("19th"));
//        publisherB1.publish(topicA, new Message("20th"));
//
//        publisherA1.publish(topicA, new Message("21st"));
//        publisherA1.publish(topicB, new Message("22nd"));
//        publisherB1.publish(topicB, new Message("23th")); // Nothing ~
//
//        Middleware middlewareA1_4_B = new MiddlewareImpl(channel, groupA, v1, new Priority(4), new HashMap<>() {{
//            put(topicB, (topic, message, publisher) -> {
//                publisher.publish(topic, new Message(message.message() + " 4444"));
//            });
//        }});
//        middlewareA1_4_B.subscribe(channel);
//
//        publisherA1.publish(topicB, new Message("24th"));
//        publisherB1.publish(topicB, new Message("25th")); // Nothing ~
//    }
//}