


## Item3: private 생성자나 열거 타입으로 싱글턴임을 보증하라


### 싱글턴

- 인스턴스를 한 번만 만들 수 있게끔 하여 사용하는 클래스이다.

- 상태가 없는 객체, 설계상 유일해야 하는 시스템 컴포넌트를 싱글턴으로 만들 수 있다.


**단점**

- 싱글턴으로 만든 클래스는 이를 사용하는 클라이언트를 테스트하기가 어렵다.

- 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해 만든 싱글턴이 아니라면 싱글턴 인스턴스를 `Mock` 구현으로 대체할 수 없기 때문이다.


### 만드는 방법

**1. `public` 필드 방식**

```java

public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){}
    
    public void test1(){
	//
    }
}

```

- `private` 생성자는 `INSTANCE` 초기화 시 단 한번만 수행된다.

- 권한이 있는 클라이언트가 리플랙션 API 인 `AccessibleObject.setAccessible` 을 사용하지 않는 이상 단 한번 생성이 보장된다.

<br>

**2. 정적 팩터리 메소드 방식**

```java

public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() {
	return INSTANCE;
    }
}
```


- API 변경 없이 싱글턴이 아니게 변경할 수 있다.

- 원한다면 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다.(`아이템 30`)

- 정적 팩터리의 메서드 참조를 `supplier` 로 사용할 수 있다. (`아이템 43, 44`)

<br>

**직렬화**

싱글톤 클래스를 직렬화 가능해지기 위해 `Serializable` 인터페이스를 구현하는 순간 싱글톤임을 보장할 수 없다.

직렬화를 통해 초기화해둔 인스턴스가 아닌 다른 인스턴스가 반환된다.

- 모든 인스턴스 필드를 `transient` 로 선언하고 (직렬화 대상에서 제외하고) `readResolve` 메소드를 제공해야 한다.

- 이 메소드 정의로 역직렬화 과정에서 만들어진 인스턴스 대신에 기존에 생성된 인스턴스를 반환하도록 해주면 된다.

```java

public class Single {

    private static final Single INSTANCE = new Single();
    private Single() {}

    public static getInstance() {
	return INSTANCE;
    }

    private Object readResolve() {
	// 진짜 객체를 반환하고 가짜는 가비지 컬렉터에 넣는다.
	return INSTANCE;
    }

}


```

<br>


**3. enum 으로 만들기**

```java
public enum Elvis {
    INSTANCE;

    public void hi() {
	//
    }
}
```

- public 필드 방식과 비슷하지만 더 간결하고 직렬화도 할 수 있고 리플렉션 공격에서도 하나의 인스턴스만 생기는 것이 보장된다.

- 대부분 상황에서는 원소가 하나뿐인 `enum` 이 싱글턴을 만드는 가장 좋은 방법이다.

