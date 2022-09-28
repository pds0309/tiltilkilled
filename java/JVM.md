

## JRE

- Java Runtime Environment는 최소한의 자바 프로그램 배포단위로 애플리케이션을 실행할 수 있도록 구성된 배포판임

- JVM과 라이브러리 및 자바 런타임 환경에 사용하는 프로퍼티 세팅이나 리소스 파일을 가지고 있음

- 컴파일시 사용하는 javac 같은 개발 툴은 없고 실행가능한 환경만이 구성됨

<br>

## JDK

- Java Development Kit 은 JRE + 개발 도구가 포함됨

- 소스코드 작성 시 사용되는 자바 언어 자체는 플랫폼에 독립적이다.

- `오라클은 자바 11부터는 JDK만 제공한다.(JRE없음)`


## JVM

- Java Virtual Machine 은 자바 바이트 코드를 OS에 특화된 코드로 변환하여 실행해줌

- 표준 스펙 하의 다양한 구현체 벤더가 있다.(oracle, Azul, amazon)

- 특정 플랫폼(OS)에 종속적임

- 자바 언어자체와 연관관계가 있다기 보단 `.class` 파일만 있으면 실행가능 하기 때문에 여러 언어를 동작시킬 수 있다.(kotlin, scala, groovy)


### JVM 구조

![image](https://user-images.githubusercontent.com/76927397/192672061-829e2bd7-8f20-4856-bd5c-14f344eb66e1.png)


**Class Loader**

- Loading -> Link -> Initialization 의 과정을 거침

![image](https://user-images.githubusercontent.com/76927397/192674255-f49c911b-8582-4c3b-a7d5-11fbfa84d36f.png)

<br>

Loading

- `.class` 파일을 읽고 적절한 바이너리 데이터를 만들어 `메소드영역`에 저장한다.
- Full Qualified Class Name, Class, Interface, Enum, M ethod, Variable 을 메소드 영역에 저장함
- 로딩이 끝나면 해당 클래스 타입의 `Class` 객체를 만들어 `힙 영역`에 저장한다.
- Bootstrap -> Platform -> Application 계층 구조로 되어있음

> Bootstrap Class Loader: JAVA_HOME/lib 의 코어 자바 api 제공
> Platform Class Loader: JAVA_HOME/lib/ext 폴더의 클래스를 읽음
> Application Class Loader: 애플리케이션 classpath에서 클래스들을 읽음

> 애플리케이션에 클래스 하나 만들고 어느 클래스로더에서 읽어들였나 확인해볼 것
 


<br>


Link

- Verify: `.class` 파일의 형식 유효성을 검사함

- Prepare: 클래스 변수와 기본값에 필요한 메모리를 준비하는 과정

- Resolve(Optional): 심볼릭 메모리 레퍼런스(클래스 파일에 존재하는 특정 인스턴스를 지금 시점까지는 실제 존재하는 애를 가리키게 되어있지 않음)를 메소드 영역에 있는 실제 레퍼런스로 교체 


<br>


Initialization 

- `static` 값들 초기화 및 변수에 할당


 




**Memory(JVM Runtime Data Area)**

- Method Area: 클래스 수준의 정보를 저장해두고 공유하게끔 해주는 영역으로 GC대상임

- Heap Area: 인스턴스를 저장하고 공유하여 사용

- Stack Area: `쓰레드마다` 런타임 스택을 만들고 안에 메소드 호출을 스택 프레임으로 쌓는다. 쓰레드 종료 시 런타임 스택도 사라짐

- PC Register: `쓰레드마다` 쓰레드 내 현재 실행할 스택 프레임을 가리키는 포인터가 생성됨


**Execution Engine**

- 애플리케이션 로드 시 JVM에 바이트 코드가 적재되는데 이를 바이트 단위로 읽어 실행함(Interpreter).

- 반복적으로 읽어 해석해 실행해야 되는 코드는 모두 네이티브 코드로 변경해두고 바로 사용함(JIT Compiler)


### 하는일 요약

- 클래스 파일을 읽어들이고 메모리에 올려둔다.

- 인터프리터나 jit 컴파일러로 실행한다.



<br>


#### Ref

- [jvm](https://coding-factory.tistory.com/828)

- [inflearn 더자바](https://www.inflearn.com/course/the-java-code-manipulation)