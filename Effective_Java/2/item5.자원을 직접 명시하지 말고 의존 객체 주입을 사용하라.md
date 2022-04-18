


## Item5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라


많은 클래스가 하나 이상의 자원에 의존한다.

> EX) 맞춤범 검사 클래스는 '사전' 에 의존할 것이다

<br>

다른 클래스에의 의존에 대해 다음처럼 정적 유틸리티 클래스나 싱글턴으로 구현하는 경우가 흔하다.


```java

public class SpellChecker {
	private static final Lexicon dictionary = ???;
	private SpellChecker() {}
	public static boolean isValid(String word) {
	...
	}
	public static List<String> suggetions(String typo) {
	...
	}
}
```

- 이런 방식은 모두 사전 단 하나만 사용한다는 가정하에의 것들이라 좋아보이지 않다.

- 실제로는 사전이 언어별로 따로 있거나 특수 어휘용 사전이 별도로 있을 수 있다.

- 사전 하나로 모든 상황에 대응할 수 있기를 바라고 있는 코드이다.

<br>

> 여러 자원 인스턴스를 지원해야 하며 클라이언트가 원하는 자원을 사용할 수 있어야 한단다. 그렇다면 어떤 패턴을 사용하지?


**1. 필드에서 `final` 을 제거하고 다른 사전으로 교체하는 메소드를 추가**

- 멀티스레드 환경에서 사용할 수 없다.

- 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 클래스나 싱글턴 방식이 적합하지 않다. 



**2.인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식**


```java
public class SpllChecker {
	private final Lexicon dictionary;
	public SpllChecker(Lexicon dictionary) {
		this.dictionary = Objects.requireNonNull(dictionary);
	}
	public static boolean isValid(String word) {
	...
	}
	public static List<String> suggetions(String typo) {
	...
	}
}
```

- 이 예시에서는 사전이라는 하나의 자원만 사용하지만 자원이 몇 개든 의존관계가 어떻게 되든 상관없다.

- `불변성` 이 보장되어 같은 자원을 사용하려는 여러 클라이언트가 의존객체들을 안심하고 공유할 수 있다.



<br>

**요약**

- 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다고 하면 `싱글턴` 이나 `static class` 는 사용하지 않는 것이 좋다.

- 이 자원들을 클래스가 직접 만들게 해서도 안된다.

- 대신 필요한 자원 또는 자원을 만들어주는 팩토리를 생성자에 넘겨줘라


**장점**

- 클래스의 유연성, 재사용성, 테스트 용이성을 개선해준다.

