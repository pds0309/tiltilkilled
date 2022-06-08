

## item 65. 리플렉션보다는 인터페이스를 사용하라

<br>

### 선요약

리플렉션은 복잡한 특수 시스템 개발 시(컴파일 타임에는 알 수 없는 클래스를 사용하는 프로그램 등) 필요한 강력한 기능이나 단점이 많아 사용하게 되면

되도록 객체 생성에만 사용하고 생성한 객체를 이용할 때는 적절한 인터페이스나 컴파일 시점에 알 수 있는 상위 클래스로 형변환하여 사용해야 한다.

<br>

### 리플렉션?

- 구체적인 클래스 타입을 몰라도 메소드, 타입, 변수들에 접근할 수 있게 해주는 자바 api

- 컴파일 타임이 아닌 런타임 시에 동적으로 특정 클래스의 정보를 추출할 수 있는 기법임

<br>

리플렉션을 이용하면 임의의 클래스에 접근할 수 있음.

`Class` 객체가 주어지면 그 클래스의 생성자, 메소드, 필드에 해당되는 인스턴스를 가져올 수 있고 이어서 인스턴스들로 그 클래스의 멤버이름, 필드타입, 메소드 시그니쳐 등을 가져올 수 있다.

<br>

> 클래스 작성 시점에는 어떤 클래스를 쓸지 모르겠으나 런타임 시점에서 사용해야할 때, 스프링 같이 프로그램 실행 도중 동적으로 클래스 정보를 가져와야 할 때 등 사용되나 리플렉션의 단점을 알아보자


<br>

### 리플렉션의 단점

- 컴파일 타입 검사가 주는 이점을 누릴 수 없음

- 코드가 지저분해짐

- 성능이 안좋음


<br>


> 코드분석 도구나 의존관계 주입 프레임워크 등에서 리플렉션이 사용되나 이마저도 사용을 줄여나가는 편임


<br>


### 리플렉션을 제한된 형태로만 사용하여 이점만 취하자

컴파일타임에 이용할 수 없는 클래스를 사용해야만 하는 프로그램은 컴파일타임이라도 적절한 인터페이스나 상위 클래스를 이용할 수 있을 것이다.

이런 경우 **리플렉션은 인스턴스 생성에만 사용하고 이 인스턴스는 인터페이스나 상위 클래스로 참조해서 사용한다**


<br>


```java
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        
        // 클래스 이름을 Class 객체로 변환
        Class<? extends Set<String>> cl = null;
        try {
            cl = (Class<? extends Set<String>>)
            Class.forName(args[0]) ;       
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 생성자 얻기
        Constructor<? extends Set<String>> cons = null;
        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 집합의 인스턴스를 만든다.
        Set<String> s = null;
        try{
            s = cons.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 생성한 집합 사용
        s.add("hi");
        System.out.println(s);
    }
}
``` 

<br>

리플렉션으로 생성하고 인터페이스로 참조해 활용한다.

위 코드는 `Set<String>` 인터페이스의 인스턴스를 생성하는데 정확한 클래스는 명령줄의 첫 번째 인수로 확정된다. (TreeSet or HashSet)

이 코드에서는 인스턴스 생성을 위해 리플렉션이 없었더라면 컴파일 시점에 잡아낼 수 있었던 것들을 런타임 시점 예외로 던지고 있고

클래스 이름만으로 인스턴스를 생성하기 위해 긴 코드를 작성하고 있다.

객체 생성 부분에서만이고 객체가 만들어지고 나면 이후에는 `Set` 을 사용할 때와 똑같지만 

생성하는 부분도 리플렉션을 사용하지 않았으면 생성자 호출 한 줄로 끝내면서 런타임 예외에 대한 걱정도 할 필요 없었을 것이다.

<br>

리플렉션은 런타임에 존재하지 않을 수도 있는 클래스 등의 의존성을 관리할 때 적합하다.

버전이 여러 개인 외부 패키지를 다룰 때 가동만 가능한 수준으로 컴파일시키고 리플렉션을 통해 가능한 최신 버전의 클래스와 메소드들에 접근할 수 있는 것이다.


