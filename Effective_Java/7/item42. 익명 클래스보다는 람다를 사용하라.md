

## item42. 익명 클래스보다는 람다를 사용하라

<br>


이전에는 자바에서 함수 타입 표현 시 추상 ㅁ소드 하나만 담은 인터페이스 또는 추상 클래스를 사용했다.

이런 인스턴스를 함수 객체라고 하며 특정 함수나 동작을 나타내는데 썼었고 그 이후 익명 클래스를 통해 함수 객체를 주로 만들었다.


> 익명 클래스 인스턴스를 통한 Collection 정렬

```java
Collection.sort(words, new Comparator<String>() {
	public int compare(String s1, String s2) {
		return Integer.compare(s1.length(), s2.length());
	}
})
```

과거 객체 지향 디자인 패턴에는 충분했으나 익명 클래스 방식으로 메소드를 정의하여 사용하는 방법은 너무 길어 함수형 프로그래밍에 적합하지 않다

<br>

자바8부터 이런 추상 메소드를 단 하나 가진 인터페이스의 인스턴스를 람다식을 사용해 만들 수 있다.

```java
Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length());
```

타입을 명시해야 코드가 더 명확한 경우를 제외하고는 람다의  모든 매개변수 타입을 생략하여 다음과 같이 사용할 수 있다.

<br>


> 다음과 같이 람다 자리에  비교 생성자 메소드를 사용하면 더 간결해지며

```java
Collections.sort(words, comparingInt(String::length));
```

<br>

> 자바 8 List 인터페이스에 추가된 sort 를 활용하면 더 간결해진다.

```java
words.sort(comparingInt(String::length));
```

<br>


람다를 언어 차원에서 지원해서 기존에 적합하지 않았던 곳에도 함수 객체를 활용해 실용적으로 사용할 수 있다.


```java
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double x, double y);
}
```

apply 메소드 동작이 상수마다 달라야 해서 각 상수에서 추상메소드를 재정의했다.

람다를 이용하면 열거 타입의 인스턴스 필드를 이용하는 방식으로 상수별로 다르게 동작하는 코드를 쉽게 구현할 수 있다.

열거 타입 상수의 동작을 람다로 구현해 생성자에 넘기고 이 람다를 인스턴스 필드로 저장해두면 된다.

<br>

```java
public enum Operation {
    PLUS("+", (x, y) -> x + y),
    MINUS("-", (x, y) -> x - y);

    private final String symbol;
    private final DoubleBinaryOperator operator;

    Operation(String symbol, DoubleBinaryOperator operator) {
        this.symbol = symbol;
        this.operator = operator;
    }

    public double apply(double x, double y) {
        return operator.applyAsDouble(x, y);
    }
}
```

<br>


### 람다 사용 주의점

**코드가 길어지거나 코드 자체로 동작이 설명되지 않으면 사용하지 말자**

- 이름이 없고 문서화도 할 수 없어 길면 가독성이 떨어진다.

<br>

**람다는 자신을 참조할 수 없다**

- 람다에서의 `this` 키워드는 바깥인스턴스를 가리킨다. 자신을 참조하려면 익명 클래스를 사용해야 한다.

<br>

**람다를 직렬화하는 일을 삼가자**

- 람다도 익명클래스 처럼 직렬화 형태가 구현별로 다를 수 있다.

- 직렬화 해야만하는 함수객체가 있다면 `private static inner class`의 인스턴스를 사용하자