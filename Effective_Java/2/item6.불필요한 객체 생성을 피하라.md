


### Item6: 불필요한 객체 생성을 피해라

- 생성자 대신 `정적 팩토리 메소드`를 제공하는 불변 클래스에서는 정적 팩터리 메소드를 사용해 불필요한 객체 생성을 피할 수 있다.

> 예: Boolean(String) 생성자 대신 Boolean.valueOf(String) 팩터리 메소드 사용

- 생성자는 호출마다 새로운 객체를 만들지만 팩토리 메소드는 아니다.

- 불변객체가 아니어도 사용중에 변경되지 않을 것임을 안다면 재사용할 수 있다.


<br>

- 객체가 불변이라면 재사용해도 안전함이 명백하지만 반대되는 상황도 있다. (어댑터)
	- 어댑터: 실제 작업은 뒷 객체에 위임하고 자신은 제2의 인터페이스 역할을 하는 객체
- 어댑터는 뒷 객체만 관리하면 되고 그 외에는 관리할 상태가 없어서 객체 하나당 어댑터 하나씩만 만들어지면 충분하다.

> Map 인터페이스의 keySet 메소드는 호출마다 새로운 Set 인스턴스가 만들어진다고 생각할 수 있으나 사실 매번 같은 Set 인스턴스를 반환한다.

<br>

- 불필요한 객체를 만들어내는 예로 `오토박싱` 을 들 수 있다.

오토박싱
	- 기본타입과 그에 대응하는 박싱된 기본 타입을 같이 쓸 때 자동으로 상호변환해주는 것
	- 의미상으로는 차이가 없지만 `성능` 에는 차이가 있다.


> 모든 양의 정수 총합을 구하는 다음 메소드를 보자

```java
private static long sum() {
	Long sum = 0L;
	for (long i = 0 ; i < Integer.MAX_VALUE; i ++) {
		sum += i;
	}
	return sum;
}
```

- `sum` 변수를 기본타입이 아닌 래퍼 타입 `Long` 으로 선언해서 불필요한 인스턴스가 약 2^31 개 만들어진다.(long 타입인 i 가 Long 타입인 sum에 더해질 때마다)

- **박싱된 타입보다는 기본 타입을 사용하고 의도치 않은 오토박싱이 숨어들지 않도록 주의하자**


