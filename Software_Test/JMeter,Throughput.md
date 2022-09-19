
## Apache JMeter

- 성능 측정 및 부하 테스트 기능 제공하는 자바 애플리케이션

> 간단한 부하테스트에는 Apache Bench 를 사용해보자


<br>

**다음과 같은 형태의 애플리케이션 테스트 지원**

- 웹
- REST
- FTP
- DB(JDBC)
- SMTP


<br>

**CLI 지원됨**

- CI/CD 툴과 연동 시 편함
- UI보다 자원사용량 적음

<br>


**윈도우 실행 방법**

- [설치](https://jmeter.apache.org/download_jmeter.cgi)

- `/bin/jmeter` 파일 실행

<br>

### 주요 파라미터

**Thread Group**

- 한 쓰레드에 사용자 한명이라고 생각하자. 동시에 몇 명이 요청할 것인가?

> Number of Threads: 쓰레드 개수
> Ramp-up period: 쓰레드 개수를 만드는데 드는 시간
> Loop Count: 쓰레드 개수x 루프 개수만큼 요청 보냄

**Sampler**

- 각 유저가 어떤 행동을 취할 것인지를 나타낸다.(http 요청을 보낸다)
- 여러 Sampler를 순차적으로 등록도 가능

**Listener**

- 요청에 대한 응답을 받았을 때 무엇을 할 것인가(리포팅, 검증, 그래프 그리기)


**Configuration**

- Sampler 가 사용할 공통 설정값(http 헤더 정보 등)

**Assertion**

- 응답 코드, 본문 등 성공 확인

<br>


### 간단한 테스트 흐름

(1) 스레드 그룹을 생성한다.

![image](https://user-images.githubusercontent.com/76927397/190942421-c2fbe699-5586-4266-a229-62dcafdf727c.png)


<br>

(2) 샘플러를 생성한다.


![image](https://user-images.githubusercontent.com/76927397/190942548-90b08816-3cc5-4f40-b314-26935b0c61c8.png)

<br>


(3) 적절한 리스너를 추가하고 요청해 통계를 확인한다.

![image](https://user-images.githubusercontent.com/76927397/190943071-feb80c83-94ec-4688-b4e2-9f0cc38be965.png)

<br>

성능을 확인하는 지표는 주로 `Throughput` 과 `ResponseTime` 이다.

**처리량**

- 처리량(Throughput) 은 TPS를 주로 사용하는데 단위 시간당 시스템에서 처리되는 트랜잭션 건수를 의미하여 수치가 높을수록 좋은 것

**응답시간**

- 사용자 측면에서의 성능테스트 지표로 요청에서 응답까지 걸리는 시간을 의미하며 다음과 같은 리소스별 합으로 나타낼 수 있음

> Response Time = Client Time + Network Time + Server Processing/Sending Time

> 티어별로 구분시 클라이언트, 네트워크, 웹서버, 웹애플리케이션서버, DB 등의 레이어로 나눌 수 있음

- JMeter 에서 처리시간에 대한 평균,최대,최소,중앙값과 90~99% 백분율 값을 제공하는데 응답시간 오름차순에서의 백분율 순위를 의미함

![image](https://user-images.githubusercontent.com/76927397/190943513-5d4c5411-aaa4-4b7e-bcc2-2a99c78471a6.png)

<br>

- 일반적으로 90% 응답시간과 평균 응답시간을 유효한 지표로 사용함 (시스템 등 기타 특수 요인들 때문에 상위 10% 배제)

<br>

**처리량과 응답시간 상관관계**

- 서비스 시간이 일정하여 `부하` 가 증가할 수록 일정 수준까지는 `Throughput` 이 선형적으로 증가한다.

- 부하가 계속되면 응답시간 증가량에 비해 처리량이 더이상 증가하지 않는 변곡점이 존재하는데 사용자(부하량)이 증가해도 성능은 그대로인데다가 대기시간만 점점 길어지게 되기 때문

- 변곡점 부분이 `최대 허용 동시 사용자` 를 의미한다. (해당 시스템 최대 처리량을 의미) 

- 적절한 부하를 줘가며 변곡점을 찾아(최대 허용 가능한 수준을 찾아) 시스템의 한계를 확인하면 될 것이다.

![image](https://user-images.githubusercontent.com/76927397/190943735-ed5c8d88-7001-4375-8b3d-981873dcf1fc.png)

<br>


### Cli로 실행하기

- gui로 하는 것 보다 자원을 덜 먹어 원활한 테스트 가능
- 만든 성능 테스트 셋 (`jmx` 확장자) 설정 xml 파일을 gui 없이 커맨드로 실행

```shell
jmeter -n -t [your.jmx]
```

<br>


다양한 그래프를 제공해주는 Jmeter 플러그인이나 BlazeMeter 같은 시나리오 캡쳐도구로도 테스트 샘플러를 쉽게 적용할 수 있으니 해보자

 
 