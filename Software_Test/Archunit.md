


## Archunit

[archunit](https://www.archunit.org/)

<br>


- 애플리케이션 아키텍쳐를 테스트할 수 있는 오픈소스 라이브러리로 패키지,클래스,레이어 간 의존성 확인 가능

- 애플리케이션이 주어진 아키텍쳐 규칙을 잘 만족하고 있는지 확인할 수 있다.



**Use case example**

- 특정 패키지가 특정 패키지에서만 사용되고 있는가?

- Service라는 역할(이름을 가진) 클래스가 Controller에서만 참조되고 있는가?

- Service라는 역할 클래스가 service 패키지에 존재하는가?

- 특정 스타일의 아키텍쳐를 따르는가

- 순환참조가 없는가


<br>


### 시작하기

**의존성 설치**

```xml
		<dependency>
			<groupId>com.tngtech.archunit</groupId>
			<artifactId>archunit-junit5-engine</artifactId>
			<version>1.0.0-rc1</version>
		</dependency>
```

<br>



**사용하기**

- 확인할 규칙을 정의하고 특정 패키지에 해당하는 클래스를 읽어들인다.

- 읽어온 클래스들이 규칙을 잘 따르는지 확인한다.

**패키지 구조 검증 사용 예시**

```java
class ArchTests {

    // 패키지 경로의 클래스들 가져옴
    private static final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.prac.softwaretest");

    @Test
    @DisplayName("domain 패키지 클래스들은 domain, member, post, dto 에서만 참조 가능하다.")
    void domainPackageDependencyTest() {
        //import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition
        classes().that()
                .resideInAPackage("..domain..")  // domain키워드가 있는 패키지 안에 있는 클래스들은
                .should()
                .onlyBeAccessed()
                .byClassesThat()
                .resideInAnyPackage("..domain..", "..member..", "..post..", "..dto..")// 에서만 참조할 수 있다.
                .check(classes);
        // 테스트 패키지도 포함되니 주의할 것
    }
}
```


**Junit5 확장**

- `@AnalyzeClasses` : 클래스를 읽어들여 확인할 패키지 설정

- `@ArchTest`: 확인할 규칙 정의



<br>

### Ref

[archunit](https://recordsoflife.tistory.com/696)

[inflearn](https://www.inflearn.com/course/the-java-application-test)