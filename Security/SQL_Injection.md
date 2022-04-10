

## What is SQL Injection


### SQL 인젝션


- 클라이언트의 입력값을 조작해 데이터베이스를 공격하는 방법

- 사용자 입력에 대해 이스케이핑이나 필터링을 제대로 하지 않았을 때 발생

- 공격이 쉽지만 치명적이라 조심해야 됨

<br>

### 방식

- 블라인드 인젝션 등으로 SQL 을 주입해보며 SQL Injection 취약점이 있는지 확인

- 일부러 에러를 내며 서버의 DB 정보, 종류를 파악하고 공격함
 

<br>

### SQL 인젝션 예시

Retrieving hidden data (숨겨진 데이터 검색)

- SQL문 수정으로 숨겨진 데이터를 조회합니다.

Subverting application logic(애플리케이션 로직 파괴)

- SQL문 수정으로 애플리케이션 실행 로직을 방해합니다.

Blind SQL injection

- 오류 메시지가 보이지 않을 때 쿼리의 True/False 동작으로 데이터베이스 구조를 파악하는 방법입니다.

Union SQL injection

- 기존 쿼리에 악성쿼리를 합집합하여 정보를 획득하는 방법입니다.

Examining the database

- 데이터베이스 버전, 구조 같은 정보를 얻을 수 있습니다.



**체험하기**

[체험 사이트](http://testphp.vulnweb.com/)



<br>

### 방어 방법

- 사용자에게 받은 값을 직접 SQL 쿼리로 넘기면 안된다.

- Escape 함수, 언어나 프레임워크에서 제공해주는 `prepared statement` 사용하기

<br>

### Mybatis

`#{parameter}` 방식으로 파라미터를 받으면 내부적으로 파라미터 바인딩을 사용해 Sql Injection 에 안전한다.

- 값을 바인딩 하는 시점에서 전달된 쿼리들을 필터링한다.


**동적 파라미터를 사용하고 싶다면?**

- 쿼리의 파라미터로 컬럼명을 받고 싶을 경우 `${parameter}` 를 사용하게 된다.

- 이 방식은 직접 SQL 쿼리문에 입력을 넣기 때문에 SQL Injection 에 취약하다.

[mybatis ${} 사용시 sql injection 방어하기](https://www.hanumoka.net/2019/09/21/spring-20190921-spring-mybatis-sqlinjectionsafe/)
[샘플](https://github.com/rkpunjal/sql-safe-annotation-example)


<br>

### JPA

- 기본적으로 JPA 에서는 크게 SQL Injection 에 대한 걱정이없다.

- 기본적으로 제공을 해주는 쿼리 메소드들을 사용하면 파라미터 바인딩이 적용 된다.

<br>

![126594788-5600a986-hhh4c74-8dc4-936254210ef7](https://user-images.githubusercontent.com/76927397/162607264-3f25c2f1-4366-4636-8a65-fd880fdafd73.png)

- 인젝션을 시도했지만 파라미터 바인딩에 의해 PLAYER_NAME 자체에 `and 1=1` 이 같이 들어가는 것을 알 수 있다.


**직접 쿼리스트링을 만들어 EntityManager 로 전달하는 것은 위험할 수 있다.**

```java

@RequiredArgsConstructor
@Repository
public class MyRepository {
    private final EntityManager em;
    public List<Player> findPlayer(String name){
        String sql = "SELECT * FROM PLAYER WHERE PLAYER_NAME='"+name+"';";
        List<Player> playerList = em.createNativeQuery(sql)
                .getResultList();
        return playerList;
    }
}

```

![geaeaheaheaa](https://user-images.githubusercontent.com/76927397/162607630-85de6c18-8e6e-46f4-85f0-27bc77509752.JPG)









 