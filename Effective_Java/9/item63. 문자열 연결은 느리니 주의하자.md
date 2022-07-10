

## item 64. 문자열 연결은 느리니 주의하자

<br>

문자열 연결 연산자 `+`  는 편하지만 잦은 사용은 성능을 저하시킨다.

한 줄 짜리 출력 혹은 작고 크기가 고정된 객체의 문자열 표현을 만들때는 괜찮지만 

문자열 연결 연산자로 문자열 n개를 잇는 시간은 n제곱에 비례한다.

**문자열은 불변이라 두 문자열을 연결할 때 양쪽 내용 모두를 복사해야 하기 때문에 성능 저하를 피할 수 없다**

<br>

```java

String str = "hi";
String result = "";
for(int i = 0; i < 50000; i ++) {
    result += str;   
}
```

> 문자열 연결 연산이 많아질 경우 메소드가 심각하게 느려질 수 있다.


<br>

### StringBuilder 사용하기


`StringBuilder` 의 append 메소드로 문자열 연결하자

문자 배열을 사용하거나 문자열을 연결하지 않고 하나씩 처리하는 방법도 있다.

[참고하기](https://github.com/pds0309/java_basic_practice/tree/main/string_stringbuilder_stringbuffer)

