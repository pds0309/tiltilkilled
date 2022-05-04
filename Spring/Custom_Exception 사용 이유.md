


## Custom Exception 사용이유


### 무조건 써야될까?

- 기본적으로는 자바에서 제공하는 표준 예외를 사용할 수 있는 상황이라면 사용하는 것이 좋을 수 있다고 한다. (이펙티브 자바 item 72)

- 또 단순한 비즈니스에서 런타임 예외만 상속받아서 커스텀한 뒤 별다른 구현의 변경 없이 메시지 정도만 재정의 해서 사용할 경우에는 딱히 클래스 이름으로의 구분이라던가 이런게 큰 의미가 없을 것 같고 클래스만 더 추가되는 형태라 비효율적일 수 있을 것 같다.




<br>

### 커스텀 예외 사용 장점

**이름만으로 예외 정보 전달**

- 여러 도메인을 다루는 서비스에서 어떤 상황에서 발생한 예외인지 클래스명으로도 의미전달이 가능

> MemberNotFoundException 이면 Member 요청에 대한 예외임을 이름만으로 알 수 있다.

<br>

**상세 예외 메시지 전달**

- 상태 코드 등 예외 발생상황에서 필요하다고 생각되는 여러 메시지들을 전달할 수 있게끔 개발자가 커스텀할 수 있다.

<br>

**명확한 예외 발생 상황에 대한 처리**

- 기본적으로 제공되어 재사용되고 거의 대부분의 상황에서 사용된다는 장점을 가진 표준예외들의 특징이 단점이 될 수 있다.

- 개발자가 의도하지 않은 부분에서 발생할 가능성이 있음.


```java

@Service
public class MyService {

	private SomeDependency someDependency;

	public MyService(SomeDependency  someDependency) {
		this.someDependency = someDependency;
	}

	public void run(int hi) {
		if(condition()) {
			throw new IllegalArgumentException("condition 때문에 예외!");
		}
		someDependency.run2(hi);
		run3();
	}
}
``` 

- `IllegalArgumentException` 가 `condition()` 을 만족했을 때 발생하기도 하지만 `SomeDependency` 와 같은 외부 의존성 또는 라이브러리에서 예외가 발생하지 말라는 법은 없다.

- 기본적으로 스프링에서 아래와 같이 `ControllerAdvice` 를 활용해 예외를 전역적으로 처리하는데 개발자가 처리하려는 예외 상황과 어디선가 발생할법한 예외 상황에 대한 처리가 모호해질 수 있다.


```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(final IllegalArgumentException error) {
	return null;
    }
}
```

<br>

이런 모호한 상황에서 커스텀 예외를 만들어 개발자가 의도한 비즈니스적인 상황에 대한 예외와 프로그램상 발생될법한 런타임 예외 상황을 분리할 수 있을 것 같다.


```java

@Service
public class MyService {

	private SomeDependency someDependency;

	public MyService(SomeDependency  someDependency) {
		this.someDependency = someDependency;
	}

	public void run(int hi) {
		if(condition()) {
			throw new BusinessException("condition 때문에 예외!");
		}
		someDependency.run2(hi);
		run3();
	}
}

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleUnExpectedException(final RuntimeException error) {
	return null;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleExpectedException(final ExpectedException error) {
	return null;
    }

}


``` 

<br>


