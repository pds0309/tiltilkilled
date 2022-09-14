package com.pds.javatest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class HelloTest {

    @Test
    @DisplayName("특정 조건에 따라 테스트 실행하기")
    void test() {
        assumingThat(1 == 1, () -> {
            System.out.println("HI");
            assertEquals(1, 1);
        });
        assumeTrue(1 == 0);
        Hello hello = new Hello();
        assertNotNull(hello);
    }

    @Test
    @DisplayName("특정 조건에 따라 테스트 실행하기2")
    @DisabledOnOs(OS.WINDOWS)
    @EnabledOnJre({JRE.JAVA_17})
    void test2() {
        assertEquals(1, 1);
    }

    @Test
    @Disabled
    void test3() {
        assertEquals(1, 1);
    }
}