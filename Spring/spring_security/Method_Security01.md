

## 메소드 보안 인가 방식


**기존 url 방식의 인가처리**

- 웹 계층의 인가 방식
	- 화면, 메뉴 단위로의 인가처리

- 필터 기반의 방식으로 인가처리한다.


**메소드 방식의 인가처리**

- 서비스 계층의 인가처리 방식
	- 기능 단위로의 인가처리

- 메소드 처리 전,후로 보안 검사가 수행되어 인가처리를 한다.

- AOP 기반으로 동작한다.

<br>

### AOP 기반으로 동작한다.

- `proxy` 와 `advice` 로 메소드 인가 처리 수행


<br>


### 인가 과정

**초기화 과정**

- 초기화 시 빈 전체를 검사하며 보안이 설정된 메소드를 탐색한다.

- 빈의 프록시 객체를 생성한다.
	- `EX) MemberService => MemberServiceProxy`

- 보안 메소드에 권한심사를 하는 `Advice`(부가기능) 를 등록

- 빈 참조시 실제 빈이 아닌 프록시 빈 객체를 참조




<br>

**진행과정**

- 메소드 호출 시 프록시 객체를 통해 메소드 호출

- `Advice` 가 등록되어있다면 작동하게 하여 인가 처리를 한다.

- `MethodSecurityInterceptor` 에 의한 권한 검사에 통과하면 **실제 빈**의 메소드를 호출 한다.


<br>





### 보안 설정 방식


**어노테이션 권한 설정 방식**

- 보안이 필요한 메소드에 설정한다.

- `@EnableGlobalMathodSecurity(prePostEnabled = true, securedEnabled = true)` 로 활성화 해야 사용 가능

- `@PreAuthorize`
	- 미리 권한을 검사한다.

- `@PostAuthorize`

- `MethodSecurityMetadataSource` 를 구현하는 `PrePostAnnotationSecurityMetadataSource` 가 담당한다.

<br>


```java

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)

```

- securedEnabled : @Secure 
- prePostEnabled: @PreAuthorize, @PostAuthorize


**@Secured**

- 사용자 권한 정보에 따라 메소드 접근 제한 가능
- 표현식을 사용할 수 없다.


**@PreAuthorize**

- 동작 수행전에 권한 or 역할을 검사
- SpringEL 의 expression 도 사용 가능하며, Custom Expression(메소드) 도 호출할 수 있다.

```java

@GetMapping
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
public List<Student> getAllStudents() {
	return STUDENTS;
}

@PostMapping
@PreAuthorized("hasAuthority('student:write')")
public void register(@RequestBody Student student) {
	System.out.println(student);
}

```

```java

// Component

@Component("loginService")
public class LoginService {
	public boolean pageReadRoleCheck(String id) {
		return true;
	}
}

// Controller

@GetMapping("/api/v1/projects")
@PreAuthorize("@loginService.pageReadRoleCheck(#pj.id)")
public List<ProjectResponseDto> getProjectList(Project pj) {
	return projectService.findAll();
}

```


**@PostAuthorize**

- 동작 수행 후 권한 or 역할을 검사한다.

- `returnObject` 예약어를 통해 해당되는 메소드의 수행 결과 값도 전달할 수 있다.


```java

@PostAuthorize("@loginService.pageReadRoleCheck(returnObject.name)")
@RequestMapping( value = "/{id}", method = RequestMethod.GET )
public Project getProject( @PathVariable("id") long id ){
    return service.findOne(id);
}

```


**표현식 종류**

**hasRole([role])**

- 현재 사용자의 권한이 파라미터의 권한과 동일한 경우 `true`

**hasAnyRole([role1,role2])**

- 현재 사용자의 권한이 파라미터들 중 포함되어 있다면 `true`

**principal**

- 사용자 증명 주요 객체에 직접 접근

**authentication**

- `SecurityContext` 에 있는 `authentication` 객체에 접근

**denyAll**

- 모든 접근 금지

**permitAll**

- 모든 접근 허용

**isAnonymous()**

- 익명사용자인 경우 `true`

**isRememberMe()**

- 현재 사용자가 rememberMe 면 `true`

**isAuthenticated()**

- 현재 사용자가 로그인 상태면 `true`




<br>



**맵 기반 권한 설정 방식**

- `MapBasedMethodSecurityMetadataSource` 사용

- 기본적인 구현이 완성되어 있고 DB로부터 자원과 권한정보를 매핑한 데이터를 전달하면 메소드 방식의 인가처리가 이루어짐


> 맵 기반 메소드 보안 설정하기

```java

@Configuration
@EnableGlobalMethodSecurity
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Override
	protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
		return new MapBasedMethodSecurityMetadataSource();
	}
}
```
