package com.pds.javatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest {

    int a = 1;

    @Order(2)
    @Test
    void test2() {
        System.out.println("내가 뒤에");
        assertEquals(3, ++a);
    }

    @Order(1)
    @Test
    void test1() {
        System.out.println("내가먼저");
        assertEquals(2, ++a);
    }

}