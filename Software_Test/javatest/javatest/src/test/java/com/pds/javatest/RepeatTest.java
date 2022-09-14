package com.pds.javatest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class RepeatTest {

    @RepeatedTest(value = 5, name = "반복이닷")
    @DisplayName("반복 테스트1")
    void test(RepetitionInfo repetitionInfo) {
        System.out.println("안녕?" + repetitionInfo.getCurrentRepetition());
    }

    @RepeatedTest(value = 5, name = "{displayName}, {currentRepetition} / {totalRepetitions}")
    @DisplayName("반복 테스트2")
    void test2() {
        System.out.println("안녕?");
    }

    @ParameterizedTest(name = "{index} {displayName} of {0}")
    @DisplayName("반복 테스트3")
    @ValueSource(strings = {"김치", "찌개"})
    void test3(String value) {
        System.out.println(value);
    }

    @ParameterizedTest(name = "{index} {displayName} of {0}")
    @DisplayName("반복 테스트4")
    @CsvSource(value = {"java", "10", "a"})
    void test4(String value) {
        System.out.println(value);
    }

    @ParameterizedTest(name = "{index} {displayName} of {0}")
    @DisplayName("반복 테스트5")
    @CsvSource(value = {"java, 10", "hi, 55"})
    void test5(@AggregateWith(RepeatAggregator.class) Repeat repeat) {
        System.out.println(repeat.getName());
    }

    static class RepeatAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext)
                throws ArgumentsAggregationException {
            return new Repeat(argumentsAccessor.getString(0),
                    argumentsAccessor.getInteger(1));
        }
    }
}