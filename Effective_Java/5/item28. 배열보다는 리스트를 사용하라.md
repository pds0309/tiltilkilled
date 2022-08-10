

## item 28. 배열보다는 리스트를 사용하라

<br>




### 배열과 제네릭 타입의 차이

**배열은 공변한다**

- Sub가 Super 의 하위타입이면 배열 `Sub[]` 는 `Super[]` 의 하위타입으로 같이 변한다.

- 컬렉션의 제네릭 타입은 서로 다른 타입이어도 관계가 없다.

<br>


```java
Object[] obj = new Long[1];
obj[0] = "Hello";
```

> 런타임 시점에 java.lang.ArrayStoreException: java.lang.String 발생

<br>

```java
List<Object> list = new ArrayList<Long>();
```

> 선언과 초기화 부분의 호환이 안맞아 컴파일 시점에 에러가 난다.

<br>

어느쪽이던 Long 저장소에 String을 넣을 수 없지만 컬렉션을 사용하면 `컴파일 시점` 에 이 오류를 알 수 있기 때문에 효과적이다.

<br>

**배열은 실체화 된다**

- `배열은 런타임에도 자신이 담기로 한 원소의 타입을 인지하고 확인한다.`

- `제네릭은 컴파일타임에 원소타입을 검사하고 런타임 시점에는 모두 소거되어 알 수 없다`

> 소거는 제네릭이 지원되기 전의 레거시와 제네릭을 함께 사용할 수 있게 해주는  메커니즘이다.

<br>

이런 차이들로 인해 배열과 제네릭은 잘 어울리지 못하고 제네릭타입,매개변수화타입, 타입 매개변수로 사용할 수 없다.

타입에 안전하지 않기 때문에 비허용되어있다.

> 불가능! :  new List<E>[], new List<String>[], new E[]


<br>

**배열로 형변환 시 제네릭 배열 생성오류나 비검사 형변환 경고가 뜨는 경우 대부분은 컬렉션은 사용하면 해결된다.**

- 코드가 복잡해지고 성능이 저하될 수는 있지만 타입 안정성과 상호 운용성이 좋아진다.


```java
public class Hello {
    public static void main(String[] args) {
        Chooser chooser = new Chooser(Arrays.asList(new int[]{1,2,3}));
        int[] array = (int[]) chooser.choose();
        System.out.println(Arrays.toString(array));
    }

    public static class Chooser {
        private final Object[] choiceArray;

        public Chooser(Collection choices) {
            choiceArray = choices.toArray();
        }

        public Object choose() {
            Random random = ThreadLocalRandom.current();
            return choiceArray[random.nextInt(choiceArray.length)];
        }
    }
}
```

생성자에서 컬렉션을 받고 하나를 반환하는 메소드를 제공하는 클래스다.

위처럼 이 클래스를 사용하려면 `choose` 메소드를 호출할 때 마다 반환된 `Object` 객체를 원하는 타입으로 형변환해야하고 혹시 타입이 다른 원소가 들어있으면 런타임에 형변환 오류가 났을 것이다.

<br>

```java
    public static class Chooser2<T> {
        private final T[] choiceArray;

        public Chooser2(Collection<T> choices) {
            choiceArray = (T[]) choices.toArray();
        }
        public Object choose() {
            Random random = ThreadLocalRandom.current();
            return choiceArray[random.nextInt(choiceArray.length)];
        }
    }
```

![image](https://user-images.githubusercontent.com/76927397/183882553-44e12a96-3b04-4ae2-a046-7a96aa932d87.png)

클래스를 제네릭으로 변경했는데 T가 무슨타입인지 알 수 없어 컴파일러는 이 형변환이 런타임에도 안전한지 보장할 수 없다는 메시지가 출력된다.

동작은 하지만 컴파일러가 안전을 보장해주지 못하는데 런 타임시점에서는 무슨 타입인지  제네릭에서 알 수 없기 때문이다.

이런 비검사 형변환 경고를 제거하려면 배열 대신 리스트를 사용하면 된다.

<br>


```java
    public static class Chooser3<T> {
        private final List<T> choiceList;

        public Chooser3(Collection<T> choices) {
            choiceList = new ArrayList<>(choices);
        }
        public Object choose() {
            Random random = ThreadLocalRandom.current();
            return choiceList.get(random.nextInt(choiceList.size()));
        }
    }
```

리스트로 변경해 런타임에서 클래스 형변환 예외를 만날일은 더 없어진다.

<br>

**요약**

- 배열은 공변이며 실체화되나 제네릭은 불공변이며 동작할 때 타입정보가 소거된다.

- 제네릭과 배열을 섞어쓰다 컴파일오류나 경고를 만나면 가장 먼저 배열을 리스트로 대체하는 방법을 생각해보자

