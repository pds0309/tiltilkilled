

## TestContainers

- Junit Test를 지원하는 Java Library로, Docker 컨테이너를 사용할 수 있도록 일회용 인스턴스를 제공한다.

- 테스트 실행 시 DB설정이나 프로그램, 스크립트를 실행할 필요 없고 프로덕션 환경과 보다 유사한 테스트를 진행할 수 있음

- 일회용 `Docker Containers`를 이용하여 독립적인 테스트 환경을 구축할 수 있다.

<br>


**왜씀?**

> DB를 예로 들면 개발/배포/테스트 각 환경에 맞는 DB를 구성할 것이고 스프링부트를 예로 들면 테스트 환경에서는 h2 메모리 DB를 사용할 것이다. JPA를 사용한다고 해도 기본적으로 트랜잭션 격리단계나 전파옵션이 DBMS마다 전략이 다를 수 있어 운영환경과 테스트환경 간 격차가 있을 수 있다.

> 로컬,CI환경에서 멀쩡하던게 프로덕션환경에서 안돌아갈 수 있는 상황이 발생할 수 있음

> 도커 컨테이너를 활용해 프로덕션환경과 같은 데이터베이스를 구축하고 테스트 함으로써 환경간의 격차를 줄여 안정성을 추구할 수 있다!

그렇다고 테스트할때마다 도커 컨테이너를 띄우고 내리고 하기는 귀찮은데 그 행동을 해줌



<br>


### 사용해보기


**모듈 설치**

Junit5 지원 모듈 설치하기

```
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>1.17.3</version>
            <scope>test</scope>
        </dependency>
```

**DBMS 모듈 설치**

```
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.17.3</version>
    <scope>test</scope>
</dependency>
```

**테스트 프로파일 설정**

```yml
spring.datasource.url=jdbc:tc:postgresql:///test
spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver
```


**테스트 예시**

```java
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest").withDatabaseName("test");

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    void insertTest() {
        Member member = Member.builder().name("김갑환").build();
        Member savedMember = memberRepository.save(member);
        assertEquals(member, savedMember);
    }
}
```

`@Container`

- 테스트 클래스 내부에서 컨테이너를 시작한다. `static` 붙이면 클래스 라이프사이클을 따른다.

`@Testcontainers`

- Junit5 extension으로 테스트 클래스의 `@Container` 필드를 찾아 라이프 사이클에 따른 동작을 수행(열고 닫기) 

<br>

`docker ps` 명령어 쳐보면 테스트동안 postgres 컨테이너가 띄워졌다가 테스트 종료되면 프로세스가 사라짐을 알 수 있음


<br>

**GenericContainer**

- 이미지만 있다면 제공해주지 않는 컨테이너라 하더라도 띄울 수 있다.

```java
new GenericContainer("yourimage")
	.withEnv("KEY", "value")
	.withExposedPorts(1111); //host port will be random
```


<br>


### Docker Compose 환경에서 Test Container 사용하기

**Docker Compose?**

- 컨테이너 여러개 한번에 띄우고 각각의 컨테이너 관계,네트워크,볼륨설정,의존성 설정 등 일련의여러 컴포넌트들을 같이 관리하게 해주는 툴

<br>

- Testcontainer로 `docker-compose.yml` 에 정의된 맞춤형 서비스 세트를 실행하여 테스트할 수 있다.

- 개발또는 다른 환경에서 이미 docker compose 가 사용되고 있을 때 테스트에서 사용하기에 유용함 


**사용하기**

테스트에서 `docker-compose.yml` 이 정의된 파일 위치를 찾아 `DockerComposeContainer` 를 컨테이너로 설정해 초기화 해준다.

```java
 @Container
    static DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"));
```

사용해야할 컨테이너가 많을 경우 띄우는데 시간이 오래걸릴 수 있기 때문에 `wait` 를 사용해 컨테이너 사용여부를 기다려주면 테스트가 먼저 수행됨을 방지할 수 있다.

```java
.withExposedService("redis_1", REDIS_PORT, 
                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));
```



