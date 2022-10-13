

## testing-library: React Hook

- [react hooks testing library](https://github.com/testing-library/react-hooks-testing-library)

- 리액트 훅 테스팅 라이브러리

**사용 주의점**

- react와 react-test-renderer 와 같은 버전이어야만 한다.
- 사용하는 react 등의 버전들과 맞춰서 의존성 설치를 할 것

<br>

```
npm install @testing-library/react-hooks
```

### 왜 있을까?

> 커스텀 훅을 많이 만들텐데 hook은 컴포넌트 루트 body 안에서만 호출할 수 있다.

> 커스텀 훅이 잘 작동하나 테스트하기 위해 컴포넌트 전체를 테스트를 위해 사용해야하고 그렇게 되면 hook 동작에 대한 다양한 이벤트를 컴포넌트로 트리거해야 하고 복잡도가 증가하며 테스트 의도가 맞지 않게 된다.

- React Hook에 대한 간단한 테스트 도구를 만들 수 있을 뿐만 아니라 입력을 업데이트하고 출력을 검색하기 위한 다양한 유용한 유틸리티 기능을 제공할 수 있다.

- 실제 훅이 사용되는 것 처럼 테스트 되는 것을 할 수 있다.

- 훅 테스트를 위해서 리액트 컴포넌트와의 상호작용이나 렌더링에 대해 걱정할 필요 없고 훅을 직접 연결해 테스트하고 assertion 할 수 있다.



<br>

### 언제쓸까?

- 여러 컴포넌트에서 사용되는 커스텀 훅이 있을 때

- 컴포넌트와의 상호작용만으로는 훅 내부의 동작을 검증하기 어려운 복잡한 로직을 가진 훅일 경우

<br>

### 언제 안써도 될까?

- 컴포넌트와의 상호작용만으로 충분히 테스트가 가능한 정도의 훅일 경우


<br>


### 사용해보기

- [자세한 api reference](https://react-hooks-testing-library.com/reference/api#initialprops)

**renderHook**

```javascript
function renderHook(callback: (props?: any) => any, options?: RenderHookOptions): RenderHookResult
```

- 콜백형태로 원하는 hook 을 렌더링한다.


**options**

> initialProps: 프로퍼티로 넘길 기본 프로퍼티 값이다.

> wrapper: hook 렌더링 시 감싸서 같이 렌더링 시킬 컴포넌트이다.


**결과**

```json
{
  all: Array<any>
  current: any,
  error: Error
}
```

> current: hook이 현재 가진 결과물

<br>

**act**

- hook을 변경시킨다.



### 사용 예시

```javascript
import { useState, useCallback } from 'react'

function useCounter() {
  const [count, setCount] = useState(0)

  const increment = useCallback(() => setCount((x) => x + 1), [])

  return { count, increment }
}

export default useCounter
```


```javascript
import { renderHook, act } from '@testing-library/react-hooks'
import useCounter from './useCounter'

test('should increment counter', () => {
  const { result } = renderHook(() => useCounter())

  act(() => {
    result.current.increment()
  })

  expect(result.current.count).toBe(1)
})
```
