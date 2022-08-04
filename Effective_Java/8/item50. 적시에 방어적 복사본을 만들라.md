


## item50. 적시에 방어적 복사본을 만들라

<br>




### 클라이언트가 불변식을 깨뜨리기 위해 노력한다고 가정하고 방어적으로 프로그래밍하라

<br>

자바로 작성한 클래스는 시스템 다른 부분에서 사용할 때 불변식이 지켜지나 반드시 뚫을 수 없는 건 아님

어떤 경우든 적절하지 않은 클라이언트로부터 클래스를 보호하는데 충분한 시간을 투자하자

<br>


### 어떤 객체든 객체 허락 없이 외부에서 내부를 수정하는 일은 불가능해야 한다.

<br>

당연히 그렇게 될 것만 같지만 주의를 기울이지 않으면 자기도 모르게 내부를 수정하도록 할 수도 있다.

<br>

```java

public final class Period {
	private final Date start;
	private final Date end;

	public Period(Date start, Date end) {
		if(start.compareTo(end) > 0) {
			throw new IllegalArgumentException("시작 시간이 더 늦다");
		}
		this.start = start;
		this.end = end;
	}
	public Date end() {
		return end;
	}
}
```

불변처럼 보이며 시작 시각이 종료시각보다 늦을 수 없다는 불변식이 무리없이 지켜질 것 같지만 `Date`가 `가변`이라는 사실을 이용해 불변식을 깨뜨릴 수 있다.

<br>

```java

Date start = new Date();
Date end = new End();
Period p = new Period(start, end);
end.setYear(77);
```
<br>


`Date` 는 낡은 API로 사용을 안해야 하고 자바 8부터는 `Instant` 나 `LocalDateTime`, `ZonedDateTime` 을 사용해도 된다.

위의 낡은 코드들을 외부 공격으로부터 보호하려면 생성자에서 받은 가변 매개변수를 모두 방어적 복사해야 한다.

<br>


```java
	public Period(Date start, Date end) {
		this.start = new Date(start.getTime());
		this.end = new Date(end.getTime());
		if(start.compareTo(end) > 0) {
			throw new IllegalArgumentException("시작 시간이 더 늦다");
		}
	}
```

<br>

매개변수의 유효성 검사를 하기 전 방어적 복사본을 만들어 이 복사본으로 유효성 검사를 하였다.

따라서 클라이언트에서 가변하는 Date 객체를 조작해도 Period 객체 사용이 무결해지게 된다.

<br>

**매개변수가 제 3자에 의해 확장될 수 있는 타입이면 복사본 생성시 clone 을 쓰지 말라**

- 위에서 Date는 final 클래스가 아니라 clone이 Date가 정의한 게 아닐 수 있다. => clone이 악의를 가진 하위 클래스의 인스턴스를 반환할 수 있다.


<br>

또한 Period 객체는 생성자 뿐 아니라 접근자 메소드의 잘못된 점 때문에 내부 변경이 가능한 상태이다.

```java
public Date end() {
	return end;
}

// 이렇게
Period p = new Period(start, end);
p.end().setYear(77);
```

단순히 조회의 역할만 해야하는 메소드이지만 `Date` 객체 자체가 가변하기 때문에 변경할 수 있게 된다. 

이 접근자가 가변 필드의 방어적 복사본을 반환하게 만들면 된다.

```java
public Date end() {
	return new Date(start.getTime());
}
```

이렇게 할 경우 Period 객체 자신 외에는 가변 필드에 접근할 방법이 없어 모든 필드가 객체안에 완벽하게 캡슐화되어 불변으로 거듭나게 된다.

<br>


### 불변 객체만을 위해 방어적 복사를 하는 건 아님

<br>

메소드던 생성자던 클라이언트가 제공한 객체의 참조를 내부의 자료구조에 보관해야 할 때면 항상 그 객체가 잠재적으로 변경될 수 있는지를 생각해야한다.

변경될 수 있는 객체라고 한다면 그 객체가 클래스에 넘겨진 뒤 임의로 변경되었을 때 문제가 발생할지를 판단해보아야 한다.

만약 클라이언트가 건넨 객체를 내부 `Set` 이나 `Map` 의 키로 사용한다면 추후 그 객체가 변경되었을 때 해당 컬렉션들의 불변성이 깨질 것이다.


<br>


### 안심할 수 없다면 방어적 복사를 해야하지만 방어적 복사에는 성능저하가 따르고 항상 쓸 수 있는 것도 아니다

<br>

- 되도록 불변 객체들을 조합해 객체를 구성해야 방어적 복사할 일이 줄어든다.

- 복사 비용이 크거나 클라이언트가 신뢰된다면 방어적 복사 대신 수정의 책임이 클라이언트에게 있음을 명시한다.

<br>

