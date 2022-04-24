

## Stub vs Mock


### Test Double

테스트를 목적으로 하는 실제 객체를 흉내내는 위장용 객체

`Dummy`, `Fake Object`, `Stub` , `Mock`

- 테스트 더블 객체들의 역할이 분리되거나 독립적인 개념이 아니라 일반적으로 `Stub` 와 `Mock` 만 구분한다.


### Stub?

- 호출되면 미리 준비된 답변으로 응답하는 것을 말함

- 협력객체의 특정 부분이 테스트하기 힘들 경우 `stub` 를 사용하면 수월하게 테스트 가능

- **External Service** 를 사용하는 메서드를 테스트할 때 사용
	- `EX) Database`
	- DB커넥션 자체를 테스트하고 싶진 않을 것이다. 이런 부분들을 `Stub` 로 대체하고 비즈니스 로직 테스트에 집중함

- `Mock` 과 혼동하여 사용하는 경우 많음

- `Stub` 하고자 하는 부분을 최소한으로 구현하고 테스트에서는 호출된 요청에 대해 미리 준비해둔 결과를 반환한다.

<br>

**Stubbing with Mockito**

> 다음과 같은 Customer 엔티티 객체와 데이터베이스로부터 Customer 를 조회하는 CustomerService의 findName 메소드가 있다.

```Java
@Entity
@Getter
public class Customer {
	@Id
	private Long id;
	private String name;
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final EntityManager em;

	public String findName(Long id) {
		Customer customer = em.find(Customer.class, id);
		return customer.getName();
	}
}
```

우리가 이 서비스 빈을 테스트한다고 생각해봅시다.

단순하게 생각하면 실제 DB와 커넥션하도록 의존성을 주입해서 사용가능하게 만들고 테스트할 것이다.

하지만 이 경우에는 많은 문제가 있습니다.

- 이 테스트는 DB 연결/동작에 대해 너무 깊은 의존성을 가지게 되고 로직 이외의 많은 단계의 테스트를 구성해야 할 확률이 높아집니다.

- 가장 좋은 해결방법은 단위테스트에서 DB 의존성 자체를 완전히 제거하는 것입니다.
	- DB 커넥션 관련 된 기능 자체를 `Stubbing` 하는 것입니다.
	- 이 예시에서는 `EntityManager` 를 Mockito Stubbing 하는 것입니다.

> Stub 테스트 예시

```java

public class CustomerServiceTest {

	CustomerService service;	

	@Test
	public void findNameTest() {
		Customer sampleCustomer = new Customoer("김갑환");
		
		// 가짜 EntityManager 객체를 만든다
		EntityManager em = mock(EntityManager.class);
		// 엔티티매니저에서 1L 이라는 아이디로 Customer 를 find 할 경우 sampleCustomer 를 반환하겠다고 'stubbing' 합니다
		when(em.find(Customer.class, 1L)).then(sampleCustomer);
		
		// 엔티티매니저를 주입하며 서비스 초기화
		service = new CustomerService(em);
		
		// 실제 테스트 해야할 메소드
		String name = service.findName(1L);
		// 테스트할 로직을 수행하고 정말 김갑환이라는 상태가 나왔는지를 검증하게 됩니다
		assertEquals("김갑환", name);		
	}
}

```

위의 예제에서 보면 테스트를 수행해야되는 부분(service)의 관점에서 바라보면 `EntityManager` 가 어디 최씨인지 알 수 없고 알 바도 아니다.

실제로 `mockito` 인지 모르고 단지 자신의 메소드를 수행해 원하는 결과가 나오는지를 검증하고 있다.

외부의 어떤 자원에 대한 의존성 없이 순수하게 자바 코드로 원하는 로직을 테스트할 수 있어 빠르고 간결해진다.


> 위의 서비스 메소드에서 파라미터로 아이디를 받아 이름을 조회하게 되는데 추가적으로 파라미터로 받은 아이디가 null 일경우에 대해서 처리해야 한다고 생각해보자

> null 이 주어졌을 때에 대해 우리가 원하는 결과(Exception 이라던가) 를 서비스 메소드에서 잘 반환하는지에 관심이 있지 DB에서 어떤 아이디로 조회했을 때 진짜 없는 객체인지 여부는 알 바가 아니다.

```java
@Test
public voic shouldThrowExceptionfindNameWithNullParam() {

	when(entityManager.find(Customer.class,1L)).thenReturn(null);
	
	// 아이디 파라미터가 null 일 때 우리가 YourException 을 반환하는 결과를 기대한다고 생각하자
	assertThrows(YourException.class, ()-> service.findName(1L))	
}
```

**로직 외적인 것에 대해 Stubbing 을 통해 단지 어떤 결과를 리턴시키도록 미리 구성을 해둘 뿐이고 이를 활용해 실제 원하는 방향으로의 로직만을 테스트 하는 것이다.**
 


<br>

### Mock

- 다른 테스트 더블 객체와는 다르게 `행위 검증` 을 추구

- 내용에 따라 동작하도록 프로그래밍된 객체


**Mocking with Mockito**

- 어떤 상황에서 `mocking` 이 필요할까

```java

@Component
@RequiredArgsConstructor
public class LateInvoiceNotifier {
	private final EmailSender emailSender;
	private final InvoiceStorage invoiceStorage;
	
	public void notifyIfLate(Customer customer) {
		if(invoiceStorage.hasOutstandingInvoice(customer)) {
			emailSender.sendEmail(customer);
		}
	}
}
```

위 예제 빈은 두개의 외부 의존성을 가지고 있다.

두 외부자원이 테스트하기 어렵고 꺼려지는 것이라고 할 때 우리는 당연히 테스트에서 `mock` 객체로써 사용하게 될 것이다.

우리가 진짜 테스트하고 싶은 것은 `notifyLate` 메소드 

하지만 `notifyIfLate` 메소드를 보면 아무것도 리턴하지 않아 어떤 것을 실제로 `Assertion` 할 수가 없는 상황이다.


- 이 경우 우리는 코드의 `Side Effect` 에 중점을 두고 바라봐야 한다.

- 내부적으로 `invoiceStorage.hasOutstandingInvoice(customer)` 의 조건이 충족되면 `emailSender.sendEmail` 이 동작할 것이고 그렇지 않다면 아무것도 동작하지 않을 것이라는 것을 바라보자

- **mockito** 의 `verify` 를 활용해 사이드이펙트를 검증한다.

```java
	public class LateInvoiceNotifierTest {

		// 실제로 테스트 해야될 클래스이다.
		private LateInvoiceNotifier lateInvoiceNotifier;
	
		// 가짜 객체를 활용할 외부 의존성이다
		private EmailSender emailSender;
		private InvoiceStorage invoiceStorage;
	
		private Customer sampleCustomer;
	
		@Before
		public void setup(){
			// 외부 의존성1 을 Stubbing 한다.
			invoiceStorage = mock(InvoiceStorage.class);
			// 외부 의존성2 를 Stubbing 한다.
			emailSender = mock(EmailSender.class);
			
			// 가짜 의존성들 주입하며 테스트하에 있는 클래스 초기화
			lateInvoiceNotifier = new LateInvoiceNotifier(emailSender,invoiceStorage);
		
			sampleCustomer = new Customer("김갑환");
		}
	
		@Test
		@DisplayName("김갑환 SideEffect True Moment")
		public void lateInvoice(){
			// 해당 동작의 조건이 True 가 나오도록 미리 결과를 설정(Stubbing) 합니다.
			when(invoiceStorage.hasOutstandingInvoice(sampleCustomer)).thenReturn(true);
		
			// 실제 테스트하고자하는 메서드의 로직을 수행한다.
			lateInvoiceNotifier.notifyIfLate(sampleCustomer);
		
			// verify 를 통해 emailSender 의 sendEmail 메서드가 수행되었다면 테스트를 통과합니다.
			// 외부객체에 대한 가짜객체이고 실제 해당 객체에 있는 메소드 자체를 테스트하진 않지만 '동작을 진짜로 하는지' 에 대해 검증하는 것입니다.
			// 왜냐하면 우리는 검증하고자 하는 메서드의 조건부가 True 인 상황에서 이 외부객체의 메소드가 수행되기를 기대하고 있는 것이기 때문입니다.
			verify(emailSender).sendEmail(sampleCustomer);
		}
	
		@Test
		@DisplayName("김갑환 SideEffect False Moment")
		public void noLateInvoicePresent(){
			// 해당 동작의 조건이 False 가 나오도록 미리 결과를 설정(Stubbing) 합니다.
			when(invoiceStorage.hasOutstandingInvoice(sampleCustomer)).thenReturn(false);
		
			// 실제 테스트하고자하는 메서드의 로직을 수행한다.
			lateInvoiceNotifier.notifyIfLate(sampleCustomer);
		
			// 조건이 False 일 때 우리는 sendEmail 이 수행되지 않음을 기대합니다.
			// 따라서 수행 횟수가 0임을 검증하는 것이고 정말 수행되지 않았을 경우 테스트를 통과합니다.
			verify(emailSender, times(0)).sendEmail(sampleCustomer);
		}
	}  
```

- 필요한 경우 Mock 객체를 여전히 Stubbing 하여 사용하는 것을 알 수 있습니다.


<br>

### Stub vs Mock

- **Stub**와 **Mock** 의 차이는 **상태**와 **행위** 를 검증하는 것의 차이

- 상태검증
	- 메소드가 수행된 후 객체의 상태를 확인하여 올바르게 동작했는지를 확인

- 행위검증
	- 특정 동작을 수행하는지를 확인





 