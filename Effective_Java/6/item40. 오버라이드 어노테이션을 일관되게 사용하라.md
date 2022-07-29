


## item40. @Override 어노테이션을 일관되게 사용하라.

<br>

`@Override` 어노테이션은 상위 타입의 메소드를 재정의 했음을 명시해주는 메소드용 어노테이션이다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Override {
}
```


일관하여 이 어노테이션을 사용하면 버그 예방에 좋다.

<br>


```java
public class Hi {
	private char a;
	private char b;
	public Hi(char a, char b) {
		this.a = a;
		this.b = b;
	}
	public boolean equals(Hi h) {
		return this.a == h.a && this.b == h.b;
	}
	public int hashCode() {
		return 31 * a + b;
	}
}
```

<br>

각각 같은 a와 b 멤버를 가지는 다른 두 객체를 비교하면 과연 같을까?

같지 않다.

`Object` 의 `equals` 메소드는 `Object` 타입의 파라미터를 가지는데 이를재정의 하지 않고 메소드 오버로딩으로 `equals`를 구현해버렸기 때문이다.

따라서 객체 비교 시 `Object` 의 `equals`를 그대로 사용하여 `==` 처럼 객체 동일성만 식별하기 때문에 동등한 객체처럼 취급하지 않는다.

<br>

**상위 클래스의 메소드를 재정의 할 경우 반드시 @Override 어노테이션을 붙여주자.**

여기서 `equals` 의 목적은 `Object` 객체의 것을 재정의하는 것이였을 것이기 때문에 `@Override` 어노테이션을 붙여줘야 한다.

그렇다면 메소드 형식이 달라 컴파일 오류가 발생하여 잘못되었음을 미리 식별할 수 있었을 것이다.

<br>

**인터페이스의 메소드 재정의 시에도 잘 붙여주자**

`default` 메소드가 나오면서 인터페이스의 구현이 필요한 메소드를 구현한 경우에 붙여주면 시그니처가 올바른지 확신할 수 있다.


<br>

**추상 클래스나 인터페이스에서는 상위 클래스,인터페이스의 메소드를 재정의 하는 모든 메소드에 @Override 를 붙이자**

`@Override`의 존재로 하여금 무엇을 부모로부터 얻어와 재정의 하였고 무엇이 이 클래스만의 것인지 명확하게 알 수 있다.

> Set 인터페이스는 Collection 인터페이스를 확장한 것이나 본인이 새로 추가한 메소드가 없는데 이를 모든 메소드 선언에 있는 @Override 를 통해 알 수 있다.


