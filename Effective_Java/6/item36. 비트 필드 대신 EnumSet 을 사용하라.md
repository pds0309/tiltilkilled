


## item 36. 비트 필드 대신 EnumSet 을 사용하라

<br>

열거한 값들이 주로 집합으로 사용될 때 예전에는 각 상수에 서로 다른 2의 거듭제곱 값을 할당한 정수 열거 패턴을 사용했다.


```java

public class Text {
	public static final int STYLE_BOLD = 1 << 0;
	public static final int STYLE_ITALIC = 1 << 1;

	public void applyStyles(int styles) {}
}

```


다음과 같은 식으로 비트별 OR 을 사용해 여러 상수를 하나의 집합으로 모을 수 있으며 이를 `비트 필드` 라고 한다.

```java
	text.applyStyles(STYLE_BOLD | STYLE_ITALIC);
```

<br>


이런 비트 필드 사용 시 집합 연산을 수행할 수 있지만 다음과 같은 단점이 있다.


- 비트 필드 값이 그대로 출력될 때 단순 정수 열거 상수를 출력할 때 보다 해석하기가 까다롭다.

- 몇 비트가 필요한지 예측하여 적절한 타입을 선택해야 한다.


<br>


**EnumSet 을 사용하여 비트필드를 구현하자**

- `EnumSet` 클래스는 열거타입의 상수 값으로 구성된 집합을 효과적으로 표현해준다.

- Set 인터페이스를 완벽하게 구현하고 타입에 안전하며 어떤  Set 구현체와도 함께 사용할 수 있다.

- 내부가 비트 벡터로 구현되어있어 원소가 64개 이하라면 EnumSet 전체를 long 변수 하나로 표현하여 비트 필드에 비견되는 성능을 보여준다.


<br>


```java

public class Text {
	public enum Style { BOLD, ITALIC }
	// EnumSet 을 받겠지만 파라미터를 인터페이스 타입으로 설정하여 어떤 Set 구현체에 대해서도 열려있다.	
	public void applyStyles(Set<Style> styles) {... }
}
```

클라이언트 부분에서는 다음과 같이 `EnumSet`  인스턴스를 건네도록 하면 된다.

```java
text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
```

비트필드 수준의 성능을 제공하면서도 명료하여 `EnumSet` 사용을 권하지만

저자의 말을 옮겨 적어보자면 자바11까지도 EnumSet을 불변으로 만들 수 없는 단점이 있다고 한다.

구아바 라이브러리를 통해 불변으로 만들 수 있으나 내부적으로는 EnumSet 을 사용했기에 성능적 손해가 어느정도 있고

명확성과 성능을 조금 희생해서 `Collections.unmodifiableSet` 으로 감싸 사용하는 방법이 있다고 한다.

