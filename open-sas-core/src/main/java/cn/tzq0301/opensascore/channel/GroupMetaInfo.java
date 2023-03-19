package cn.tzq0301.opensascore.channel;

import java.util.Set;

public record GroupMetaInfo(String group, Set<TopicMetaInfo> topics) {
}
