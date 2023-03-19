package cn.tzq0301.opensascore.channel.meta;

import java.util.Set;

public record TopicMetaInfo(String topic, Set<PriorityMetaInfo> priorities) {
}
