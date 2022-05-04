

## item 72. 표준 예외를 사용하라.

상황이 된다면 자바 라이브러리는 대부분의 API 에서 쓰기 충분한 수의 예외를 제공하기 때문에 재사용하는 것이 좋음

- 익숙한 규약을 따르기 때문에 사용이 쉽다.

- 예외 클래스 수가 적을 수록 메모리 사용량도 줄고 클래스 적재 시간도 적게 걸린다.

<br>


### 표준 예외 예시

**IllegalArgumentExeption**

- 호출자가 인수로 부적절한 값을 넘길 때 던지는 예외

**IllegalStateException**

- 대상 객체의 상태가 호출된 메소드를 수행하기 적합하지 않을 때 (초기화 되지 않았을 때)

**NullPointerException**

- `null` 을 허용하지 않는 메소드에 `null`을 줬을 때

**UnsupportedOperationException**

- 클라이언트가 요청한 동작을 대상 객체가 지원하지 않을 때 던짐

**ConcurrentModificationException**

- 단일 스레드에서 사용하려고 설계한 객체를 여러 스레드가 동시에 수정하려 할 때

<br>


### Exception, RuntimeException, Throwable, Error 는 직접 재사용하지 말자

- 이 클래스들은 추상 클래스격으로 다른 예외들의 상위 클래스(여러 성격을 포함하는 클래스)라 안정적으로 테스트 할 수 없음



