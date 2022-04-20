


## 단위 테스트

단위 테스트는 응용 프로그램에서 테스트 가능한 가장 작은 소프트웨어를 실행하여 예상대로 동작하는지 확인하는 것

- 일반적으로 클래스 또는 메소드 수준으로 단위를 나누며 단위가 작을 수록 복잡성이 낮아짐

- 단위 테스트 단위를 작게 설정하여 테스트를 최대한 간단하고 디버깅하기 쉽게 작성해야 함

<br>


### Mock

**테스트를 위해 실제 객체를 만들어 사용하기에 비용이 많이 고려되거나 객체 서로간의 의존성이 강해 구현이 힘들 경우 사용되는 가짜 객체**

- 단위 테스트를 위해 한 번에 하나의 메소드를 실행해보는데 이 메소드가 다른 네트워크적인 것이나 데이터베이스 연결과 같은 제어하기 어려운 것에 의존하고 있다면 단위 테스트가 어려움

- 이럴 때 현재 메소드에 해당하지 않는 Flow 에 대해서 가짜 객체를 만들어 테스트를 하게 된다.


> 예를 들어 Service 를 테스트할 때 Repository 의존성이 있겠지만 이 Repository 는 자기의 단위 테스트에서 테스트가 되어있는 상태라 동작이 보장되어있는 상태이다. 굳이 Service 에서 또 주입받아 사용할 필요 없다.

<br>


- Test Double
	- 테스트를 진행하기 어려운 경우 이를 대신해 테스트를 진행해줄 수 있도록 해주는 객체를 말함

- Dummy Object
	- 단순히 인스턴스화 될 수 있는 수준으로만 객체를 구성
	- 객체 자체가 필요하지 객체의 기능은 필요없는 경우

- Test Stub
	- 더미보단 구현도가 높은 객체로 실제로 동작하는 것처럼 보이게 만들어 놓는 객체
	- 객체의 특정 `상태를 가정`해서 특정 값을 리턴시킴

- Mock Object
	- `행위`를 검증하기 위해 사용되는 객체를 지칭

- Mock vs Stub
	- Mock 은 행위 검증에, Stub 는 상태 검증에 사용
	- Stub
		- 테스트의 입력에 집중하는가?
		- 그 입력에 따른 결과값을 비교하는가?
		- 그 입력에 따라 Exception 이 발생할 수 있나?
	- Mock
		- 테스트의 출력 또는 결과에 집중하고 정상적으로 호출되는지가 중요한가?



**원리 요약**

> Service 클래스에 다음과 같은 메소드가 있다고 치자

```java

private final ARepository aRepository;

public void registerA(RequestDTO dto) {
	Optional<A> maybeA = aRepository.findByName(dto.getName());
	
	if(maybeA.isPresent()) {
		throw new RuntimeException("이미 있는 이름"); 
	}
	aRepository.save(dto.toEntity());
}
```

`ARepository` 의 `findByName` 같은 메소드는 해당되는 단위 테스트에서 모두 검증되었거나 검증되어야 한다.

이 서비스 클래스를 테스트 할 때는 `Arepository` 의 메소드들은 모두 동작의 성공이 보장되어있다고 할 수 있다.

DB에 어떻게 저장되고 잘 저장되는지는 Service 를 테스트할 때는 알 바가 아니다

따라서 이 서비스에 대한 단위 테스트를 진행할 때 가짜객체에게 이야기해주는 것이다.

> 내가 aRepository 의 findByName(something) 메소드를 실행하면 maybeA 를 리턴해줘!


<br>


**자바에서 Mock 객체 선언하기**


1. `mock()`

```java

@extendwith(mockitoextension.class) // mock 객체를 테스트를 실행하는 동안 사용할 수 있게 해줌
class Test {

private ARepository aRepository = mock(ARepository.class);

}

```


2. @Mock

```java

class Test {

@Mock
private ARepository aRepository;

private AService aservice;

@BeforeEach
void setUp() {
	MockitoAnnotations.initMocks(this); // @extendwith(mockitoextension.class)
	aService = new AService(aRepository);	
	}

}

```


<br>


**@MockBean**

- @Mock 과는 다르게 Spring 영역의 어노테이션임

- Mock 객체를 Spring Application Context 에 등록하고 스프링 컨텍스트에 의해 @Autowired 가 동작할 떄 등록된 Mock 객체를 사용할 수 있게 해준다.

- @MockBean 은 통합 테스트에서 외부 서비스 등과 같은 특정한 빈이 필요할 때 가짜화 시킬 때 유용하다.


**@InjectMock**

- 이 어노테이션을 붙인 클래스가 필요한 의존성과 맞는 Mock 객체들을 감지하여 해당 클래스의 객체가 만들어질 때 사용한다.

<br>


**@Mock vs @MockBean**

- @Mock 은 @InjectMocks 에 대해서만 해당 클래스 안에서 정의된 객체를 찾아서 의존성을 해결한다.

- @Mockbean 은 mock 객체를 스프링 컨텍스트에 등록하는 것이기 때문에 @SpringBootTest 를 통해서 @Autowired 에 의존성이 주입되게 된다.


<br>


**Mock 객체 사용시 유의할 점**

- 정말 필요한가?

- Mock 을 사용하면 테스트 케이스 유지 복잡성이 증가하기 때문에 되도록이면 의존성이 적은 구조로 프로그래밍한다.

- 실제 객체로 작동했을 때 작동하지 않을 수 있다.


