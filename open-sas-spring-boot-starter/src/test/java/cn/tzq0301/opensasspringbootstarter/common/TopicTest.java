package cn.tzq0301.opensasspringbootstarter.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicTest {
    @Test
    void test() {
        assertThrows(IllegalArgumentException.class, () -> new Topic(""));
        assertThrows(IllegalArgumentException.class, () -> new Topic(" "));
        assertThrows(IllegalArgumentException.class, () -> new Topic("\t"));
        assertThrows(IllegalArgumentException.class, () -> new Topic("\n"));
    }
}