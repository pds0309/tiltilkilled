

## item. 73 추상화 수준에 맞는 예외를 던져라

<br>

**상위 계층에서는 저수준의 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꿔서 던져야 한다**

```java
try {
   //...
} catch(LowLevelException e) {
    throw new HighLevelException( ...);
} 
```

메소드 사용 시 저수준 예외를 따로 처리하지 않고 그대로 전파하면 수행하려는 일과 관련없는 정보가 튀어나올 것이다.

이는 프로그래머를 당황시킬 뿐 아니라 내부 구현방식을 드러내어 윗 레벨 API 를 오염시킬 수 있다.

<br>

**저수준 예외가 디버깅에 도움이 된다면 예외 연쇄를 사용하자**

`예외 연쇄` 란 저수준에서 발생한 근본 원인(cause)에 대한 정보를 고수준 예외에 실어 보내는 방식을 말한다.

접근자 메서드 `getCuase` 를 통해 언제든지 저수준 예외를 꺼내서 볼 수 있기 때문에 오류를 분석하기 좋아진다.

```java

try {

} catch(LowLevelException e) {
    throw new HighLevelException(e);
}

```

고수준 예외의 생성자는 상위 클래스의 생성자에 원인을 건네주고 `Throwable` 까지 건네지게 한다.


```java
public abstract class AuthenticationException extends RuntimeException {
	/**
	 * Constructs an {@code AuthenticationException} with the specified message and root
	 * cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public AuthenticationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs an {@code AuthenticationException} with the specified message and no
	 * root cause.
	 * @param msg the detail message
	 */
	public AuthenticationException(String msg) {
		super(msg);
	}
}

```


대부분 표준예외는 예외 연쇄용 생성자가 위처럼 있으나 예외 연쇄용 생성자가 없다면 `Throwable` 의 `initCuase` 메소드를 이용해 원인을 직접 지정한다.

<br>


**요약**

아래 계층의 예외를 예방하거나 스스로 처리할 수 없고 상위계층에 그대로 노출시키기는 곤란하다면 `예외 번역` 을 하자.

이 때 필요시 `예외 연쇄` 를 적절히 사용하자

