


## item9.try-finally 보다는 try-with-resources를 사용하라


- 자바 라이브러리에는 `close` 메소드를 통해 직접 닫아줘야 하는 자원들이 있다.
	- `inputStream`, `java.sql.Connection`

- 닫기를 놓치면 예측할 수 없는 성능 저하로 이어질 수 있음

- `finalizer` 를 많이 사용하지만 그리 믿을만 하지 못하다.


> 일반적인 try-finally 구조

```java

String hi(String path) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(path));
	try {
	    return br.readLine();
	}
	finllay {
	    br.close();
	}
}
```


> 자원 둘 이상 사용 시 try-finally 방식은 지저분해진다.

```java
String hi(String path) throws IOException {
	InputStream in = new FileInputStream(path);
	try {
	    OutputStream out = new FileOutputStream(path);
	    //	...
	}
	finllay {
	    out.close();
	}
	finally {
	    in.close();
	}
}
```


- 첫번째 예시에서 기기에 물리적인 문제가 생겼을 때 `readLine` 메소드가 예외를 던지고 같은 이유로 `close` 메소드도 실패한다.
	- 이런 상황에서 두번째 예외가 첫번째 예외를 집어삼키게 된다. => 스택 추적 내역에 첫 번째 예외정보가 남지 않게 되어 디버깅이 힘들어진다.


<br>

**try-with-resources로 해결**

- 해당 자원이 `AutoCloseable` 을 구현해야 한다.
	- 자바 라이브러리들과 3rd 라이브러리들의 수많은 클래스와 인터페이스들이 이미 `AutoCloseable` 을 구현하거나 확장해두었다.

> BufferedReader 클래스를 통해 `AutoClosable` 이 구현되었나 확인해보자!

```java
public class BufferedReader extends Reader {
    private Reader in;
    // ..
    public void close() throws IOException {
        synchronized (lock) {
            if (in == null)
                return;
            try {
                in.close();
            } finally {
                in = null;
                cb = null;
            }
        }
    }
    // ..    
}
```

> BufferedReader 는 Reader 를 상속받고 있다. Reader 클래스를 확인해보자

```java
public abstract class Reader implements Readable, Closeable {
	// ...
	abstract public void close() throws IOException;
}
```

> Closable 인터페이스가 구현되어있는 것을 알 수 있다.


> 첫 번째 예시를 try-with-resources 로 변경

```java
String hi(String path) throws IOException {
	try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		return br.readLine();
	}
}
```

- `try-with-resources` 방식이 짧고 읽기 수월하며 문제 진단에도 좋음

- 위 예시의 경우 `readLine` 과 `close` 양쪽에서 예외가 발생하면 `close` 예외는 숨겨지고 `readLine` 예외가 기록된다.
	- 개발자에게 보여줄 예외 하나만 보존되고 다른 예외는 숨겨져 출력된다.
	- `Throwable` 의 `getSuppressed` 메소드를 이용해 프로그램 코드에서 가져올 수도 있다.

- `catch` 절을 사용할 수 있다.
	- `try` 문을 더 중첩하지 않고도 다양한 예외를 처리할 수 있다.

<br>

**결론: try-with-resources를 사용해야 하는 이유**

- 코드가 짧고 분명해짐
- 만들어지는 예외정보가 훨씬 유용해짐
- 정확하고 쉽게 자원을 회수 (`close`) 할 수 있다.