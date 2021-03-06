


## item69. 예외는 진짜 예외 상황에만 사용하라

### 선요약

> 예외는 예외 상황에서 사용될 의도로 설계되었기 때문에 정상적인 제어 흐름에서 사용해서는 안되며 이를 강요하는 API 를 만들어서도 안된다.



**예외는 예외상황 처리에 쓸 용도로 설계되었기 때문에 JVM 구현자 입장에서 성능 최적화에 신경쓰지 않았을 가능성이 크다**

**코드를 try-catch 블록에 넣으면 JVM이 적용할 수 있는 최적화가 제한된다**


### 오직 예외 상황으로만 쓸 것

```java

try {
    int i = 0;
    while(true) {
        arr[i].get();
    }
} catch (ArrayIndexOutOfBoundsException e) {
	// ???
}
```

- 위 예제는 배열을 돌다가 배열의 범위가 초과할 때 나오는 예외를 활용해 순회를 종료하는 형식으로 일반적인 제어 흐름용으로 예외를 사용하고 있다.
	- 표준적인 관용구대로 배열을 순회했을 때 보다 성능이 안 좋을 수 있음
	- 내부에 버그가 있어도 예외가 정보를 숨겨버릴 수 있어 디버깅하기 어려워 유지보수에 문제가 생길 수 있다.

- 표준적이고 쉽게 이해되는 관용구를 사용하고 성능 개선을 목적으로 과하게 머리를 쓴 기법을 자제하라.

```java
for(Obj o : ObjArray) {
	o.get();	
}
```

- 이 표준적인 반복문은 범위 초과 예외를 활용하여 수행을 종료하는 것이 아니고 배열 경계에 도달했을 때 수행을 종료한다.
	- 반복문 내부에 버그가 있다고 한다면 버그에 대한 예외를 잡지 않고 StackTrace 정보를 남기며 스레드를 즉시 종료시키기에 버그를 식별하는 것이 가능하다.




<br>

### 잘 설계된 API 라면 정상적인 제어 흐름에서 예외를 사용할 일이 없게 해야 한다.

- 특정한 상태에서만 호출할 수 있는 `상태 의존적` 메소드를 제공하는 클래스에서는 `상태 검사` 메소드를 함께 제공해야 한다.

```java
for (Iterator<Foo> i = collection.iterator(); i.hasNext(); ) {
	Foo foo = i.next();
}
```

- `Iterator` 의 `next()` 가 상태 의존 메소드이며 `hasNext()` 가 상태 검사 메소드이다.

- 상태 검사 메소드 덕분에 표준 관용구 (for 문) 를 사용해 작업을 수행할 수 있다.

- 상태 검사 메소드 대신 `Optional` 을 사용하는 방법도 있다.

**언제 Optional?**

- 외부 동기화 없이 다중 스레드 접근이 가능하면서도 **외부 요인으로 상태가 변할 수 있다면** `Optional` 을 사용한다.
	- 상태 검사 메소드와 상태 의존 메소드 호출 사이에 객체의 상태가 변할 수 있기 때문임

- 성능이 중요할 때 상태 검사 메소드가 의존 메소드의 작업의 일부를 중복하여 수행할 때



<br>

**상태 검사기가 없었다면 클라이언트(사용자)가 대신 다음과 같은 형태로 처리를 했어야 한다.**

```java

try {
    Iterator<Foo> i = collections.iterator();
    while(true) {
        Foo foo = i.next();
    }
} catch(NoSuchElementException e) {
    // 순회 종료!
}

```

- 의도하지 않은 곳에서 버그가 생겨 발생되는 예외를 숨길 가능성이 있고 일반적인 순회에 비해 성능이 안좋아질 가능성이 높다.

 