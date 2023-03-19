package cn.tzq0301.opensascore.channel;

import java.util.Set;

public record TopicMetaInfo(String topic, Set<PriorityMetaInfo> priorities) {
}
