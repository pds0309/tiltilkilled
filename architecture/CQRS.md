


## CQRS란

<br>

```
시스템에서 명령을 처리하는 책임과 조회를 처리하는 책임을 분리하는 것
```

<br>

### Command, Query ,Responsibility, Segregation

**요약: Select 와 DML을 분리하자**

<br>

### 전통적인 데이터베이스 작업 처리

- DB하나에서 CRUD를 모두 다 했다.

- DML의 경우 빈도가 많지 않지만 트랜잭션 관련 처리가 필요해 성능적으로 저하가 있음(Lock, Waiting)

- 이런 DML의 성능을 낮추는 작업이 가장 빈번하게 일어나는 조회(SELECT) 에도 영향을 줌


### MSA 관점

- 서비스 형태에 따라 DML용, 조회용 DB로 나누고 동기화시킴





