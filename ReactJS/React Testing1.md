

## RTL

> 리액트 UI 컴포넌트 테스트를 위한 도구로 CRA 에 내장되어있음

- `구현`보다는 `동작`을 테스트하기
- 사용자 측면의 앱과 상호작용 테스트하기
- 코드에 가능한 한 접근할 수 있도록 접근성 핸들로 요소 찾기


**역할**

- 테스트를 위한 가상 DOM을 제공하며 이 곳에 컴포넌트를 렌더링 해준다.
- 가상 DOM을 검색해준다.
- 가상 DOM과 상호작용하여 요소를 클릭하거나 텍스트를 입력할 수 있다.

<br>

**테스트 러너가 따로 필요하다.**

- 테스트를 찾고 실행하여 검증할 무언가가 있어야 함. (jest, Jasmine 등)
- Jest가 프레임워크로 권장되며 CRA에 기본으로 내장됨


**테스트에서 Element 접근하는 방법**

- [가이드](https://testing-library.com/docs/guide-which-query/)

- [역할찾기](https://www.w3.org/TR/wai-aria/#role_definitions)

- [Jest-dom Testing-Library 깃헙](https://github.com/testing-library/jest-dom)


## Jest

- 테스트 케이스를 만드는 테스팅 프레임워크
- 리액트에서 테스트를 진행하기 위해 RTL에서 사용할 수 있는 테스트 러너
	
> Jest를 통해 기능 테스트를 진행할 수 있고 RTL은 React 컴포넌트에 대한 테스트를 위해 제공되는 환경이라고 할 수 있다.


### Jest Dom

- cra와 함께 제공되며 설치됨
- `setupTests.js` 파일을 통해 각 테스트 전에 jest-dom 가져오기를 한다.

> 즉 모든 테스트에서 jest-dom Matcher를 사용할 수 있다.

- 일반적인 Matcher 와 DOM 기반 Matcher 가 존재한다.


<br>

### Jest Watch Mode

- Jest를 실행하는 방법으로 마지막 커밋 이후 파일의 모든 변경사항을 확인해 마지막 커밋 이후 변경된 파일과 연관된 테스트만 실행한다.

- 변경이 없으면 테스트를 진행하지 않음


### Jest 동작

2개 인수를 가진 전역 테스트 메소드가 있음

- string description: 무슨 테스트인지에 대한 설명
- test function: 테스트 함수

테스트 함수를 돌려 실패 / 성공을 검증함


<br>


### 테스트 코드 문법 알아보기

```javascript
import { render, screen } from "@testing-library/react";

test("renders learn react link", () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
```

**render**

- render 메소드를 처음으로 테스트함수에서 실행한다.
- JSX에 관한 가상 DOM을 생성한다.
 

**랜더링된 가상 DOM에 어떻게 액세스?**

- `RTL` 의 `screen` global 객체 활용


```javascript
const linkElement = screen.getByText(/learn react/i);
```

- 표시되는 모든 텍스트를 DOM에서 찾는다.


```javascript
expect(linkElement).toBeInTheDocument();
```

- `Assertion` 으로 해당 텍스트가 실제 컴포넌트에 있는지 검증하는 부분

<br>

`RTL` 을 이용해 컴포넌트를 렌더링하고 기능 `Assertion`을 `Jest`를 통해 진행


<br>

**Assertions**

- 기본적으로 `expect([arguments).Matcher()` 구조


> 예: expect(element.textContent).toBe('hello');


<br>


### TDD

**Red -> Green**

- 테스트 코드를 먼저 작성하고 테스트에 통과되도록 실제 코드를 작성


**왜?**

- 코드 작성 전에 테스트를 작성하면 변경 후에 자동으로 다시 실행할 수 있어 모든 테스트 작성해두면 변경 사항 생길 때마다 모든 테스트를 다시 실행해 자동 회귀 테스트를 할 수 있다.

- 변경 사항 확인을 위해 애플리케이션을 구동해 수동으로 확인할 필요가 없다.


<br>

### 테스트 종류

**Unit Test**

- 별개의 함수나 별개의 리액트 컴포넌트로 코드의 한 단위를 테스트하는 것

- 하나의 유닛이 다른 코드의 유닛과 상호작용하는 부분을 테스트하지 않음


**Integration Test**

- 유닛간의 상호작용을 테스트한다.


**Functional Test**

- 소프트웨어의 특정 기능(동작)을 테스트하는 것

- 코드가 아닌 동작을 ㅌ스트하는 것

**E2E Test**

- 종단간 테스트로 실제 브라우저와 애플리케이션용 서버가 필요함
- Cypress 등
- RTL 스펙은 아님

<br>

### 단위 테스트와 기능 테스트

**단위테스트**

- `단위 테스트`는 테스트를 최대한으로 격리시킨다.
- 함수나 컴포넌트 테스트할 때 의존하는 다른 함수를 모킹하여 사용ㅎ마
- 이를 통해 테스트 실패 시 다른 의존성 부분이 원인이 아니라는 것을 보장하여 실패를 쉽게 찾는다.


> 하지만 사용자가 소프트웨어와 상호작용하는 방식과는 거리가 있다.
> 사용자 상호방식과 관계 없이 성공/실패할 수 있음


**기능 테스트**

- 테스트하는 특정 동작이나 유저 플로우와 연관된 모든 단위를 포함한 테스트임
- 소프트웨어와 상호작용하는 방식과 아주 밀접한 방식으로 좀 더 견고할 수 있음

> 테스트성공-> 사용자 사용 문제X 확률이 높다.
> 단위 테스트 처럼 실제 부분과만 밀접하지 않아 디버깅이 어려움

<br>


**기능 -> 단위테스트 분리가 권장되는 경우?**

- 여러 컴포넌트에서 재사용될 기능일 경우
- 기능 테스트 하기에는 로직이 복잡한 경우
- 엣지 케이스가 많을 경우



