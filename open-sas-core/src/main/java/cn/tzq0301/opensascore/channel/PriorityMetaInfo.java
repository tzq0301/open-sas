package cn.tzq0301.opensascore.channel;

import cn.tzq0301.opensascore.version.Version;

import java.util.Set;

public record PriorityMetaInfo(Integer priority, Set<Version> data) {
}
