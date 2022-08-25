

## item18. 상속보다는 컴포지션을 고려하라

<br>

**(구현)상속은 코드 재사용의 수단이나 항상 최선은 아님**

- 잘못 사용하면 오류를 내기 쉬운 소프트웨어를 만들 수 있다.


**상속은 캡슐화를 깨트린다**

- 상위 클래스가 어떻게 구현되냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.

> 상위 클래스 업데이트에 하위 클래스는 의존된다.

> 상위 클래스 설계자가 확장을 적절히 고려하지 않고 설계하였고 문서화가 적절하지 않다면 상위 클래스의 수정에 하위클래스가 닫혀있음을 보장하기 어렵다

<br>


```java
/**
 * 첫 초기화 후 몇 개의 원소가 더해지는지 확인할 수 있는 HashSet이다.
 * @param <E>
 */
class InstrumentedHashSet<E> extends HashSet<E> {

    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
``` 

```java
        InstrumentedHashSet<String> set = new InstrumentedHashSet<>();
        set.addAll(List.of("너와","나의"));
        System.out.println(set.getAddCount()); // 4
```

<br>

위와 같이 초기화 후 몇개의 원소가 더해졌나 체크해줄 수 있는 `HashSet` 을 상속받은 클래스를 구성했다

컬렉션을 통해 `addAll()` 될 경우 그 길이만큼 카운트가 되어야 하기 때문에 다음과 같이 구성했지만 의도대로 카운트되지 않는다.


```java
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }
``` 

`HashSet` 의 `addAll` 메소드 내부인데 `add`를 사용하고 있기에 내부의 `add` 를 도는만큼 카운트를 더 하고 있기 때문이다.

`addAll` 을 재정의 하지 않으면 되지만 여기서의 문제는 **상위 클래스의 구현 정책에 의존** 하게 된다는 것이다.

`addAll` 에서 `add` 를 self-use 한 것이 업데이트 시에도 유지될 지 알 수 없다.

<br>

**메소드 재정의가 원인**

내가 만든 하위 클래스에서 추가되는 원소를 어떤 조건에 의해 검증하는 로직이 필요하다고 가정하면 모든 재정의된 원소 추가 메소드 전반에 조건검사기를 추가해야 한다.

괜찮아보이지만 상위 클래스 새 버전이 나와 새로운 원소 추가 메소드가 생기게 되고 정의한 하위 클래스에서 이를 반영(재정의)하지 못하면

그대로 의도하지 않은 원소를 추가하게 되어버릴 수 있다.

<br>

**재정의 말고 그냥 아예 새로 추가하는건 안전할까?**

안전할 확률이 높지만 결국 상속으로 인한 상-하 관계가 있기 때문에 상위 클래스의 변경 발생에 불안해질 수 있다.  

애초에 상위 클래스의 규약을 만족하지 못하는 동작일 수있을 뿐 아니라

`상위 클래스에 내가 하위클래스에 만든 메소드와 같은 시그니처를 가지면서도 반환 타입이 다른` 메소드가 출시되면 컴파일 단계에서부터 막히게 된다.

즉 상위 클래스의 추가/수정 발생에 하위클래스는 불안에 떨어야 될 확률이 조금이라도 있는 것이다.

<br>


**컴포지션을 활용해 관계 결합도를 낮추자**

(1) 기존 클래스를 확장하여 하위 클래스를 구성하는 대신 새로운 클래스를 만들라.

(2) 새로운 클래스를 만들어 기존 클래스 인스턴스를 참조하게 하라


`기존 클래스가 새로운 클래스 인스턴스의 구성요소로 활용`되어 **컴포지션** 이라고 한다.

새 클래스에서는 기존 클래스 인스턴스의 메소드를 그대로 `전달(forwarding)` 만 하여 기존 클래스를 덧씌운다.

이 결과로 새로운 클래스는 기존 클래스의 내부 구현 방식의 영향에서 벗어나며 영향을 받지 않는다.


```java
    InstrumentedHashSet<String> set = new InstrumentedHashSet<>(new HashSet<>());
```
  

<br>

**상속은 is-a 관계에서만 쓸 것**

- 하위클래스가 정말 부모클래스인가 생각해보고 확신할 때 사용할 것

<br>

**자바에서 위반된 예시: Stack**

```java
/**
 * The {@code Stack} class represents a last-in-first-out
 * (LIFO) stack of objects. It extends class {@code Vector} with five
 * operations that allow a vector to be treated as a stack. The usual
 * {@code push} and {@code pop} operations are provided, as well as a
 * method to {@code peek} at the top item on the stack, a method to test
 * for whether the stack is {@code empty}, and a method to {@code search}
 * the stack for an item and discover how far it is from the top.
 * <p>
 * When a stack is first created, it contains no items.
 *
 * <p>A more complete and consistent set of LIFO stack operations is
 * provided by the {@link Deque} interface and its implementations, which
 * should be used in preference to this class.  For example:
 * <pre>   {@code
 *   Deque<Integer> stack = new ArrayDeque<Integer>();}</pre>
 *
 * @author  Jonathan Payne
 * @since   1.0
 */
public class Stack<E> extends Vector<E> {
```

매우 예전에 나온 `Stack` 은 `Vector` 를 상속하는데

`LIFO` 구조여야 함에도 `Vector` 의 메소드를 이용해 중간에 삽입 삭제를 할 수 있게 되어있다.

상속을 사용하지 말아야 할 상황에 (`스택`은 `벡터`가 아니다) 상속을 사용해 **내부 구현을 불필요하게 노출**했다.


**확장하고자 하는 클래스에 결함이 있는지 체크한다**

- 상속을 사용할 때 확장하려는 상위 클래스의 API 를 검사하고 내가 만들 하위 클래스에 전파했을 때 결함이 없는지 체크하자. 그대로 전파되니까

<br>


**요약**

- 상속은 캡슐화를 해친다. 순수 `is-a` 관계일 때 사용하자
- `is-a` 관계이더라도 상위 클래스의 확장성이나 결함을 체크하고 사용하자
- 래퍼클래스로 구현할 적당한 인터페이스가 있다면 컴포지션을 활용하자