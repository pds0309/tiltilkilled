

## Chaos Monkey for spring boot

<br>


### 카오스 엔지니어링이란?


- 대규모 분산 시스템에서 각 개별서비스가 올바르게 작동해도 서비스들간 상호작용 시 예측할 수 없는 장애 발생으로 혼란을 가져올 수 있는데 이를 사전에 식별하고 실험하고 해결하고자 하는 것임

> 카오스 엔지니어링은 시스템이 격동의 예측치 못한 상황을 견딜 수 있도록 신뢰성을 쌓기 위해 운영 중인 소프트웨어 시스템에 실험을 하는 규율이다

- 프로덕션 환경, 분산시스템 환경에서의 불확실성을 파악하고 해결 방안 찾기를 도와주는 넷플릭스에서 만든 도구



**운영 환경에서의 불확실성?**

- 네트워크 지연
- 디스크 오작동
- 메모리 누수
- 서버 장애

<br>


**실험단계**

(1) 정상상태 정의
(2) 정상상태가 실험군과 대조군 모두에서 정상일 것이라 가정
(3) 서버 장애 같은 실 문제를 정의
(4) 대조군과 실험군 사이의 정상상태 차이 조사

> 정상상태 방해가 어려울 수록 신뢰할 수 있는 시스템이라고 할 수 있다.


### CM4SB?

- 스프링부트 애플리케이션에 카오스 몽키 적용해주는 도구
- 소스코드를 건드리지 않고 여러유형의 공격을 재현해 상태를 보고 어떻게 대처할 것인지 판단하는게 가능함 

**공격대상**

- @RestController, @Controller, @Service, @Repository, @Component

**공격유형**

- 응답지연, 예외, 앱 종료, 메모리 누수

**뭘하려는지 쉽게알아보기**

- 예를 들어 UserController 에서 어떤 외부 서비스를 주입해 사용한다고 할 때 외부서비스 부분에 응답 지연을 준다던가 예외를 발생시킨다던가 했을 때 어떻게 되며 어떻게 대처할 것인지를 말함

<br>


### CM4SB 시작하기

**의존성 설치**

```xml
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>chaos-monkey-spring-boot</artifactId>
            <version>2.6.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
            <version>2.7.3</version>
        </dependency>
```

> actuator 는 스프링부트 운영용 툴로 런타임 중에 카오스 멍키 설정을 변경가능하며 헬스체킹, 로그레벨변경 등 다양한 운영용 설정 가능

**카오스몽키 활성화**

```
spring.profiles.active=chaos-monkey
```

**Actuator end point 활성화**

```
management.endpoint.chaosmonkey.enabled=true
management.endpoints.web.exposure.include=health,info.chaosmonkey
```

![image](https://user-images.githubusercontent.com/76927397/190947417-4e56a29e-2e9a-4d98-b566-521818da2c5e.png)

이거나오면됨

<br>

**활성화/ 활성화 체크**

활성화 여부 체크

```
http://localhost:8080/actuator/chaosmonkey/status
```

<br>

활성화: POST 

```
http://localhost:8080/actuator/chaosmonkey/enable
```

<br>

설정된 Watcher 들 확인

```
http://localhost:8080/actuator/chaosmonkey/watchers
```

```json
{
"controller": false,
"restController": false,
"service": false,
"repository": false,
"component": false,
"restTemplate": false,
"webClient": false,
"actuatorHealth": false,
"beans": [],
"excludeClasses": [],
}
```

<br>

watcher 활성화 (properties)

```
chaos.monkey.watcher.repository=true
```

해당 빈의 모든 public 동작들에 대해 공격가능

<br>

공격 목록 보기

```
http://localhost:8080/actuator/chaosmonkey/assaults
```

```json
{
"level": 1, // 빈도
"deterministic": false,
"latencyRangeStart": 1000,
"latencyRangeEnd": 3000,
"latencyActive": false,
"exceptionsActive": false,
"exception": {
"type": "java.lang.RuntimeException",
"method": "<init>",
"arguments": [
  {
"type": "java.lang.String",
"value": "Chaos Monkey - RuntimeException"
}
],
},
"killApplicationActive": false,
"memoryActive": false,
"memoryMillisecondsHoldFilledMemory": 90000,
"memoryMillisecondsWaitNextIncrease": 1000,
"memoryFillIncrementFraction": 0.15,
"memoryFillTargetFraction": 0.25,
"cpuActive": false,
"cpuMillisecondsHoldLoad": 90000,
"cpuLoadTargetFraction": 0.9,
"runtimeAssaultCronExpression": "OFF"
}
```

`POST` 요청으로 적절한 지연, 예외 등의 공격들을 활성화하고 수치를 변경해주면 됨




<br>

### Ref

[카오스 엔지니어링의 원칙](https://channy.creation.net/blog/1173)