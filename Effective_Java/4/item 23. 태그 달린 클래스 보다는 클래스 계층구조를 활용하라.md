

## item 23. 태그 달린 클래스 보다는 클래스 계층구조를 활용하라

<br>

두 가지 이상의 의미를 표현할 수 있으며 표현하는 의미를 태그 값으로 알려주는 클래스를 사용하지 말자


```java

class Figure {
    enum Shape { RECT, CIRCLE };

    // 태그 필드 : 현재 모양을 나타냄
    final Shape shape;

    double length;
    double width;
    double radius;

    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    Figure(double length, double width) {
        shape = Shape.RECT;
        this.length = length;
        this.width = width;
    }
}
```

위 클래스는 원과 사각형 두가지를 나타낼 수 있다.

원 또는 사각형 둘 중 하나에 대해 초기화 할 수 있도록 생성자가 있고 필요한 어떤 동작을 위해서 두 가지 상황을 모두 고려해 구현해야 한다.



**태그 달린 클래스의 단점**


- 열거 타입, 태그 필드, 여러 처리를 위한 분기문 등 쓸데없는 코드가 많아진다.

- 여러 구현이 혼합되어있어 가독성이 나쁘다.

- 필요한 것만 사용하려 해도 함께 있ㄲ는 형태이니 메모리도 많이 사용하며 final 선언하여 필드 사용 시 사용되지 않는 필드들도 초기화해야한다.

- 의도에 맞지 않는 필드들을 초기화 해도 런타임시에 문제가 발생된다.

- 새로운 Figure 가 추가된다고 생각해보자. 그에 맞는 필드를 추가하고 분기문들을 찾아 동작을 모두 추가해줘야 해서 오류의 위험성이 있다.


<br>

**클래스 계층구조로 구현하자**

(1) 루트가 될 `추상 클래스`를 정의한다.

(2) 태그값에 따라 동작이 달라질 메소드들을 추상 메소드로 정의한다.

(3) 태그값과 상관없이 일정한 동작을 하는 (공통)메소드들은 일반 메소드로 정의한다.

(4) 부모 추상클래스를 상속받아 확장시킬 Circle, Rect 등의 구현 클래스를 만들고 추상메소드를 동작에 맞게 재정의한다.

<br>


```java

abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    final double radius;
    Circle(double radius) {
        this.radius = radius;
    }
    @Override double area() {
         // 적절한 동작
    }
}

```

쓸데없는 코드가 간소화 되었으며 한 상황에 맞지 않는 의미없는 필드들이 모두 제거되었다.

루트 추상 클래스를 건드리지 않고 필요한 도형에 대해 개발자가 상속하여 재정의 하고 사용할 수 있어 확장성이 좋아졌다.


<br>

**요약**

- 여러 기능을 혼합해둔 태그 달린 클래스를 사용하지 말자

- 해당 상황이 발생할 것 같다면 계층구조로 대체하자

- 기존 클래스가 그런 상황이라면 계층구조로 리팩터링 하는 것을 고려하자

