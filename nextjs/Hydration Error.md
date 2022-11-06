

## React Hydration Error


### Hydration

- render 트리를 만들지 않고 기존에 형성된 dom트리에 이벤트만 붙임

### 에러 왜 나옴

- 애플리케이션을 렌더링하는 동안 사전 렌더링된 React 트리(SSR/SSG)와 브라우저에서 첫 번째 렌더링 중에 렌더링된 React 트리 간에 차이가 있어서


- 일반적으로 ssr과 브라우저 간에 다를 수 있는 것에 의존하는 특정 라이브러리 또는 애플리케이션 코드를 사용하여 발생한다.

> 렌더링에서 `window` `을 사용하는 것이 문제가 될 수 있다.

> `div` 를 `p` 태그 안에 사용할 경우 문제가 될 수 있다


### Styled Components 사용 시 문제

- css-in-js 라이브러리가 사전 렌더링(SSR/SSG)을 위해 설정되지 않은 경우 종종 hydration 불일치가 발생한다.

- `emotion` 사용 시 해결 [with-emotion](https://github.com/vercel/next.js/tree/canary/examples/with-emotion)


<br>


### 본인은

- nextjs의 `Link` 안에 a 태그를 넣으니 발생했다.



### Ref

[nextjs-react-hydration-error](https://nextjs.org/docs/messages/react-hydration-error)