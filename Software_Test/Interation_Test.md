


## 통합 테스트

- 통합 테스는 애플리케이션 개발 사이클에서 `종단-종단` 시스템의 동작을 검증하는데 매우 유용하다.


**In Spring MVC**

- 단위 테스트로 `RequestMapping`, `Data Binding`, `Type Conversion` , `Validation` 등을 커버할 수 없음

- 테스트 커버 정도를 높여 더 견고한 프로그램임을 보증하고자 한다면 통합테스트를 실시


- 모든 빈을 컨테이너에 올리고 테스트하여 운영환경과 유사한 환경에서 테스트 가능

- 오래걸리고 특정 계층에서의 오류에 대해 디버깅이 어려움 => 단위 테스트가 결국 중요

**주요 Annotation**

- `@SpringBootTest`
	- 테스트를 위한 `Application Context` 를 로딩하여 여러가지 속성을 제공


- `@AutoConfigureMockMvc`
	- 결국 통합테스트는 `E-2-E` 간의 동작을 검증하기에 `MockMvc` 를 이용하게 되는데 `@WebMvcTest` 어노테이션을 사용하지 않을 경우에 `MockMvc`를 사용하기 위해 필요한 어노테이션임
	- `@WebMvcTest` 와 같이 사용하면 충돌남


- `@WebMvcTest`
	- 웹 계층(Controller) 만을 테스트할 때 사용
	- 스프링 컨테이너에 `@Controller` , `@JsonComponent` , Converter, Filter , HandlerInterceptor, WebMvcConfigurer 와 같은 특정 빈만 등록함
	- **다른 계층의 빈들을 테스트 컨테이너에 올리지 않기 때문에** `@MockBean` 과 함께 사용함
	- `Spring Security` 사용 시 관련 설정 또한 자동으로 진행됨 => 전반적인 네트워킹 스펙

<br>

**MockMvc**

- `MockMvc` 는 애플리케이션의 엔드포인트를 쉽게 호출하게 해주고 적절한 검증을 할 수 있게 해주나 몇 가지 한계가 있다

- `DispatcherServlet` 의 서브클래스를 사용하여 테스트 요청을 핸들링한다. 내부적으로 `TestDispatcherServlet` 을 감싼 형태로 우리가 `perform()` 으로 컨트롤러를 요청할 때 마다 이를 직접 사용한다.
	- 실제 네트워크 연결이 구성된게 아니고 그런 척 한거임. => 실제 전체적인 네트워크 스펙을 가지고 동작할 때 처럼 테스트 하는건 불가능

- Spring 애플리케이션의 완전한 모든 기능을 지원하지 않을 수도 있다.
	- 가짜 Web Application Context 를 올려서 요청, 응답 흉내를 내기 때문임
		- `EX)  Http Redirection 을 지원하지 않음` 

 - 좀 더 리얼한 환경에서의 테스트에 대한 대안
	- restassured, testresttemplate
	- ```java @SpringBootTest(webEnvironment = DEFINED_PORT) ```
	- 다음 설정으로 내장 톰캣을 사용하며 `MockMvc` 대신 `RestTemplate` 사용 가능


<br>


### (통합에 국한된 건 아니고 전반적인) 테스트 팁

- 테스트 코드는 문서로써의 역할도 한다
	- 어떤의도로 작성된 테스트며 뭘 하는지 의도를 명확히 나타내라
	
- 모든 응답에 대한 테스트를 진행하라
	- api 가 수정될 경우 테스트 코드가 실패한다. 당연하다. 변경에 대해 항상 적절한 테스트 코드를 유지할 수 있게 한다

- 테스트 작성의 목적을 생각하자
	- 테스트 코드의 커버리지는 높을 수록 좋지만 정상적으로 작동하는 부분만을 테스트하는 건 잘못된 테스트다
	- 실수나 오류를 발견하고 이를 줄이고 수정하기 위해 작성하는 것이다.

- (통합) 새로운 메소드 생성을 고려
	- 통합 테스트 시 특정한 외부 자원(DB 등) 에 의존하는 테스트가 있는데 핵심적으로 수행하고자 하는 테스트에 집중하기 위해 테스트 전용 메소드를 별도로 만드는 것도 괜찮다.
