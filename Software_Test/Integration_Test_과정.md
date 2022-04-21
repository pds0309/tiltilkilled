



## 통합 테스트 수행 과정 예시


- 단위 테스트 같이 기능 검증의 목적을 둔 게 아니라 전반적인 플로우의 동작 여부를 검증하는 것

<br>


### 1. 통합 테스트를 위한 설정 추가하기

`@SpringBootTest`

- `properties` 커스터마이징도 가능 , 설정 없으면 default 설정으로 테스트 수행
	
- `@SpringBootTest( properties = {"spring.config.location=classpath:application-test.properties"} )`
		- `@TestPropertySource` 로도 가능

`@ActiveProfile`
	- 프로파일 전략 사용 시 테스트용으로 설정가능 `EX) dev , prod, test`

`@TestInstance`
	- 단위 테스트 라이프 사이클에 대한 설정이다.
	- `LifeCycle.PER_CLASS` 와 `LifeCycle.PER_METHOD` 가 있고 기본은 `Method`
	- `PER_CLASS` 는 static 이 필요한 `BeforeAll` 같은 사전 공통 작업을 클래스 레벨에 전역적으로 그냥 할 수 있게 해준다,

<br>

### 2. 웹 환경 설정하기

- 실제 요청 응답 환경 처럼 구성이 필요하겠죠?

**Mock**

- `@AutoConfigureMockMvc` 어노테이션으로 별다른 설정 없이 `MockMvc` 사용하는 테스트 진행 가능

- 실제 컨테이너를 실행시키진 않고 로직상으로의 테스트를 수행함


**Port 명시**

- `EmbeddedWebApplicationContext` 를 로드해 실제 서블릿 환경을 구성

- `RANDOM` or `DEFINE`

- 포트 명시했을 때 `@Transactional` 동작 안함(롤백 안됨)

**TestRestTemplate**


- `@SpringBootTest` 에서 `Web Environment` 설정하면 알아서 빈 생성되서 `@Autowired` 해서 사용 가능 

- http 요청 후 데이터를 응답받을 수 있는 템플릿 객체이며 Rest 방식의 API 테스트에 최적화 되어있다.
	- `Header`, `Body`, `Content-Type` 활용한 호출 가능
	- `ResponseEntity` 와 사용

- 서블릿 컨테이너를 직접 실행 시키는 방식임

<br>

**MockMvc vs TestRestTemplate 테스트 관점**

- `MockMvc` 는 서버 입장에서 API 에 대한 비즈니스 로직 검증 테스트를 하는 것

- `TestRestTemplate` 는 클라이언트 입장에서 실제 사용에 문제가 없는지 테스트를 하는 것
	- 좀 더 통합테스트의 관점을 지님


<br>

### 3. 테스트 예시

```java

// 실제 Servlet 환경에서 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// test 전용 프로필을 활성화 하여 로컬(개발) 환경 또는 운영환경과 테스트 환경을 분리
// 이 예제에서는 테스트 환경에서만 In-Memory H2 데이터베이스를 사용하도록 하는 설정을 하였다
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberIntegrationTest {

    //webEnvironment 활성화로 bean 이 생성되어 있어 주입 가능하다
    @Autowired
    private TestRestTemplate testRestTemplate;

    // 회원 조회 로직의 Flow 체크를 위해 사전 작업이 필요해 repository 를 주입하여 사용했음
    @Autowired
    private MemberRepository memberRepository;

    private Member member = null;

    @BeforeAll
    void setMember() {
        member = memberRepository.save(SampleMember.of());
    }

    @Nested
    @DisplayName("단일 회원 조회 API")
    class FindMemberByIdTest {

        // exchange 요청 방식
        @Test
        @DisplayName("정상적으로 조회 결과를 리턴한다.")
        void shouldReturnMemberInfoResponse() {
            ResponseEntity<MemberInfoResponse> memberInfoResponseResponseEntity = testRestTemplate.exchange(
                    "/api/v1/members/" + member.getId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<MemberInfoResponse>() {
                    });

            assertThat(memberInfoResponseResponseEntity).isNotNull();
            assertThat(memberInfoResponseResponseEntity.getStatusCode())
                    .isEqualTo(HttpStatus.OK);
            assertThat(memberInfoResponseResponseEntity.getBody())
                    .isInstanceOfSatisfying(MemberInfoResponse.class, m -> {
                        assertThat(m.getId()).isEqualTo(member.getId());
                        assertThat(m.getName()).isEqualTo(member.getName());
                    });
        }

        // getForEntity 요청 방식
        @Test
        @DisplayName("정상적으로 조회 결과를 리턴한다 버전2 - getForEntity")
        void shouldReturnMemberInfoResponse2() {
            ResponseEntity<MemberInfoResponse> memberInfoResponseResponseEntity = testRestTemplate.getForEntity(
                    "/api/v1/members/" + member.getId(),
                    MemberInfoResponse.class
            );
            assertThat(memberInfoResponseResponseEntity).isNotNull();
            assertThat(memberInfoResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(memberInfoResponseResponseEntity.getBody()).isNotNull();
        }

    }

    @Nested
    @DisplayName("회원 가입 API")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RegisterMemberTest {

        private HttpHeaders headers = new HttpHeaders();

        @BeforeAll
        void setMember() {
            headers.add("Content-Type", String.valueOf(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("새로운 회원으로 정상적으로 가입합니다")
        void shouldResponseRegisterSuccess() {

            // existed real member : 김갑환
            SignUpRequest signUpRequest =
                    SignUpRequest.builder()
                            .name("최번개")
                            .age(24)
                            .build();
            HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest, headers);
            assertThat(
                    testRestTemplate.exchange(
                            "/api/v1/members",
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<Void>() {
                            })
            ).isNotNull()
                    .isInstanceOfSatisfying(ResponseEntity.class, re -> {
                        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                        assertThat(re.getHeaders().containsKey("Location")).isTrue();

                        Long newMemberId = Long.parseLong(
                                re.getHeaders()
                                        .get("Location")
                                        .get(0)
                                        .replace("/members/", ""));
                        assertThat(memberRepository.findById(newMemberId))
                                .isPresent();
                    });
        }

        @Test
        @DisplayName("이미 존재하는 이름으로 가입을 시도해 가입에 실패합니다")
        void shouldThrowsRuntimeExceptionDueToDuplicatedNameRequest() {
            // existed real member : 김갑환
            SignUpRequest signUpRequest =
                    SignUpRequest.builder()
                            .name("김갑환")
                            .age(24)
                            .build();
            assertThat(
                    testRestTemplate.exchange(
                            "/api/v1/members",
                            HttpMethod.POST,
                            new HttpEntity<>(signUpRequest, headers),
                            new ParameterizedTypeReference<Void>() {
                            })
            ).isInstanceOfSatisfying(
                    ResponseEntity.class, re ->
                            // 이 테스트에서 500 status 를 발견하고 핸들러에 추가함
                            assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT)
            );
        }

        @Test
        @DisplayName("잘못된 나이의 입력으로 가입을 시도해 가입에 실패합니다")
        void shouldThrowsMethodArgumentNotValidExceptionDueToInvalidAgeRequest() {
            SignUpRequest signUpRequest =
                    SignUpRequest.builder()
                            .name("장거한")
                            .age(-999)
                            .build();
            assertThat(
                    testRestTemplate.exchange(
                            "/api/v1/members",
                            HttpMethod.POST,
                            new HttpEntity<>(signUpRequest, headers),
                            new ParameterizedTypeReference<Void>() {
                            })
            ).isInstanceOfSatisfying(
                    ResponseEntity.class, re ->
                            assertThat(re.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }
}
```

- 사실 `MockMvc` 를 사용하여 컨트롤러를 테스트할 때와 방법에서는 큰 차이가 없는 것 같다. 

- `TestRestTemplate` 를 사용하여 테스트 하는 방식은 컨트롤러에서 사용자의 요청(가정)으로 부터 시작해서 존재하는 계층들을 모두 갔다온 뒤 응답 여부를 체크하는 형태로
한 번에 사용 그 자체를 테스트할 수 있는 느낌이 강한 것 같다.

- `동작 여부` 에 대해 통합적인 테스트를 수행하고 있는 것 같다.

<br>

### 느낀점

- `TestRestTemplate` 을 사용한 통합 테스트를 수행하게 될 경우 `RestTemplate` 의 메소드 사용법에 더 익숙해져야 할 것 같다.

- `AssertJ` 의 `Assertions.assertThat` 메소드 사용법이 익숙하지 않아서 힘들었다.

- 테스트 프로필을 분리해 테스트하는건 매우 좋은 생각 같다. (특히 DB관련 하여 개발 또는 운영환경에 영향을 안 주고 편하게 테스트할 수 있다.)



<br>

### RestTemplate Method


| METHOD | HTTP | DETAILS |
|-----------|--------|------------------------|
|getForObject|GET|GET 메소드로 객체를 반환받음|
|getForEntity|GET|GET 메소드 결과로 `ResponseEntity` 를 반환받음|
|postForLocation|POST|POST 결과로 헤더에 저장된 URI 를 결과로 반환받음|
|postForEntity|POST|POST 결과로 `ResponseEntity` 를 반환받음|
|delete|DELETE|삭제|
|put|PUT|PUT 실행|
|optionsForAllow|OPTIONS|해당 URL 이 지원하는 HTTP 메소드 조회|
|`exchange`|any|Http 헤더를 새로 만들 수 있고 어떤 HTTP 메소드도 사용 가능|


<br>
