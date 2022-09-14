package com.pds.javatest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaggingTest {

    @Test
    @DisplayName("태깅 테스트")
    @Tag("local")
    void test() {
        assertEquals(1, 1);
    }

    @Test
    @DisplayName("태깅 테스트2")
    @Tag("ci")
    void test2() {
        assertEquals(1, 1);
    }

}