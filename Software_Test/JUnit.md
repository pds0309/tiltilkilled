

## Junit5

- 자바 테스팅 프레임워크
- 자바8 이상
- boot 2.2.1 이후로는 junit5 를 자동으로 사용


<br>

### 사용해보기

**@Test**

- 테스트 메소드

**@BeforeAll, @AfterAll**

- 테스트 클래스의 테스트 메소드 동작 전/후 한 번만 실행됨
- 리턴타입이 없어야 하고 기본 라이프사이클에서는 `static` 키워드를 붙여야함

<br>

**@BeforeEach, @AfterEach**

- 테스트 클래스의 테스트 메소드 동작 전/후 마다 실행됨

<br>

**@Disabled**

- 테스트 실행하지 않고자 할 때 붙임


<br>

**특정 조건에 따라 테스트하기**

```
	org.junit.jupiter.api.Assumption
```

- `assume[True|False]` , `assumingThat` 메소드 활용 가능

- `@EnabledOnOs`, `@EnabledOnJre`, '@EnabledIfEnvironmentVariable` 등의 어노테이션 활용해 특정 조건에서만 테스트 가능


<br>


**테스트 태깅**
 
- `@Tag` 어노테이션을 통해 테스트 그룹을 만들고 원하는 테스트 그룹만 테스트 실행 가능

- ide 및 maven 프로파일 설정 등으로 적절한 상황에 적절한 테스트만 수행 가능


예시

```
    <profiles>
        <profile>
            <id>local</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <build>
                <plugins>
                    <plugin>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <groups>local</groups>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```

<br>


### 반복 테스트

**'@RepeatedTest`**

```java
    @RepeatedTest(value = 5, name = "반복이닷")
    @DisplayName("반복 테스트1")
    void test(RepetitionInfo repetitionInfo) {
        System.out.println("안녕?" + repetitionInfo.getCurrentRepetition());
    }
```

<br>

**`@ParameterizedTest`**

```java
    @ParameterizedTest(name = "{index} {displayName} of {0}")
    @DisplayName("반복 테스트3")
    @ValueSource(strings = {"김치", "찌개"})
    void test3(String value) {
        System.out.println(value);
    }
```

<br>

**소스**

- @ValueSource
- @NullSource, @EmptySource, @NullAndEmptySource
- @EnumSource, @MethodSource, @CsvSource

**인자 타입 변환**

- [암묵적 변환](http://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)

- @ConvertWith 로 `SimpleArgumentConverter` 구현체 제공

**인자 조합 변환**

- ArgumentAccessor
- @AggregateWith 로 `ArgumentsAggregator` 구현체 제공

<br>

### 테스트 라이프사이클

- 테스트를 위해서는 테스트 클래스의 인스턴스가 생성되어야 하는데 기본전략은 테스트 메소드마다 생성이다.(` 모든 테스트간 독립적이다`)

- `@TestInstance(Lifecycle.PER_CLASS)` 를 클래스에 선언해 생명주기를 클래스단위로 변경하여 클래스당 인스턴스를 한번만 만들 수 있다.

> @BeforeAll, @AfterAll 메소드를 기본으로 단 한번만 실행하기 때문에 non-static 으로 사용할 수 있음


<br>


### 테스트 순서

- 기본적으로 테스트 순서는 알 수 없고 순서에 의존해서는 안되지만 단위테스트가 아닌 통합테스트나 시나리오 테스트일 경우 순서가 필요할 수 있다.
- 이전의 상태나 데이터를 참조해야 할 경우 매 번 새로운 인스턴스를 만들기 보다는 순서적용이 효율적일 수 있다.


<br>

### junit-platform.properties

- 테스트용 프로퍼티 파일로 `src/test/resources` 에 넣어 적용함


<br>


### Junit 확장 모델

- Junit4 에서 여러개였던 것과 달리 Junit5은 `Extension` 뿐임


**등록방법**

- `@ExtendWith` 로 선언
- `@RegisterExtension`



