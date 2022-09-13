


## Mock Service Worker

- 서비스 워커를 사용해 네트워크 호출을 가로채 API 모킹을 하는 라이브러리


**서비스워커**

> 서비스 워커는 연관된 웹 페이지/사이트를 통제하여 탐색과 리소스 요청을 가로채 수정하고, 리소스를 굉장히 세부적으로 캐싱할 수 있다. 이를 통해 웹 앱이 어떤 상황에서 어떻게 동작해야 하는지 완벽하게 바꿀 수 있다.

<br>

- 백엔드 API 흉내를 내 프론트의 요청에 대해 가짜 응답 데이터를 제공해주는 것이라고 할 수 있음

- `rest` , `graphQL` 서버를 모킹할 수 있음

- 서비스 워커는 웹 표준이라 리액트 말고 다른 라이브러리/프레임워크랑도 잘 동작함


<br>

### 사용하기

**설치**

`npm install msw`


**핸들러 생성**

- 특정 URL과 라우트에 무엇을 반환할지 결정하는 함수


```javascript
import { rest } from 'msw';

export const handlers = [
	rest.post('/login', null),
	rest.get('user', null),
]
```

**테스트 서버만들기**

- 요청을 처리할 가짜 서버

- 테스트 도중 가짜서버가 잘 응답할지 확인


```javascript
import { handlers } from "./handler";
import { setUpServer } from "msw/node";

export const server = setUpServer(...handlers);

```

`setUpTests.js` 에 서버 추가

```javascript
import { server } from "./mock/server";

// 모든 테스트 시도 전에 API 모킹 서버를 동작 시킴
beforeAll(() => server.listen());

// 테스트 하나 종료 후 request Handler 를 초기화 해 다른 테스트에서 영향을 받지 않게 함
afterEach(() => server.resetHandlers());

// 테스트 종료 후 서버 종료
afterAll(() => server.close());

```



<br>


### 특징

- mocking이 네트워크단에서 일어나기 때문에 실제 백엔드 API 통신과 크게 다르지 않음. (실제 환경에서도 잘 동작할 것이다.)





