

## item16. public 클래스에서는 public 필드가 아닌 접근자 메소드를 사용해라

<br>


**public 인스턴스 사용 단점**

```java
public class A {
	public double x;
	public double y;
}
```

- 클래스의 데이터 필드에 직접 접근할 수 있어 캡슐화의 이점 제공 불가능

- API 수정 없이는 내부 표현을 바꿀 수 없고 불변성 보장이 안되며 외부에서 필드 접근 시 부수작업을 수행할 수도 없다.


<br>

**getter 사용**

```java

public class A {
	private double x;
	public double getX() {
		return x;
	}
}

```

- 패키지 바깥에서 접근할 수 있는 클래스라면 위 처럼 접근자를 제공한다.

- 클래스 내부 표현 방식을 언제든지 바꿀 수 있는 유연성 제공


<br>

**`package-private` 또는 `private` 중첩 클래스라면 데이터 필드 노출 시에도 문제가 없음**


- 클래스가 표현하려는 추상 개념만 올바르게 표현하라

- 선언이나 사용 부분에서 접근자 방식보다 깔끔함

- 클라이언트 코드가 이 클래스 내부 표현에 묶이긴 하나 클라이언트도 어차피 이 클래스 포함하는 패키지 안에서만 동작하는 코드임

- 패키지 바깥 코드 건드리지 않고 데이터 표현방식을 바꿀 수 있다.

- private 중첩 클래스일 경우 수정 범위가 더 좁아져 이 클래스를 포함하는 외부 클래스까지로 제한된다.


```java
public class OuterClass {

    private final InnerClass innerClass;

    public OuterClass() {
        this.innerClass = new InnerClass();
    }

    public void changeValue(int value) {
        innerClass.value = value;
    }

    public int value() {
        return innerClass.value;
    }

    private class InnerClass {
        public int value;
    }
}
```

<br>


**요약**

`public` 클래스는 절대 가변 필드를 직접 노출해서는 안된다.

불변필드라면 덜 위험하나 완전히 안심할 수는 없다.

