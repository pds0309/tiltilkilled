


## Spring Security WebSecurityConfigurerAdapter deprecated

<br>

> Spring Security 5.7.0-M2부터 (boot 2.7 이상으로 추정) deprecated 된 부분에 대해 간단하게 알아보았다.

<br>


**개요**

Spring Security 5.7.0-M2 에서 `WebSecurityConfigurerAdapter` 가 Deprecated 되었다.

스프링에서는 이를 사용하지 않고 컴포넌트 기반 security configuration 설정을 하도록 권장한다.

따라서 `WebSecurityConfigurerAdapter` 를 상속해 재정의하는 대신 

`SecurityFilterChain` 및 `WebSecurityCustomizer` 유형의 빈을 선언해 사용하는 방식으로 변경되었다.

보안 설정 관련 부분들을 빈으로 등록해 스프링 컨테이너에서 관리되게끔 하는 방식이라고 판단된다.

<br>

**HttpSecurity 설정하기**

시큐리티 5.4에서 `SecurityFilterChain` bean 을 통해 HttpSecurity 설정 하는 방법이 소개되었었다.

> 기존 어댑터 방식

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
    }

}
```

> 5.7.0-M2 이후로 권장되는 FilterChain 방식이다.

```java

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
```

반환 형태가 `void` 에서 설정유형 타입으로 변경되었다.


<br>

**WebSecurity 설정하기**

`WebSecurityCustomizer` 를 사용하는 방식 역시 역시 5.4 버전에서 소개되었다.


> 기존 방식

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
}
```


<br>

> 권장 방식

```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
}
```
 

