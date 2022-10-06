


## StoryBook 사용해보기

- [storybook](https://storybook.js.org/docs/react/writing-docs/docs-page)

<br>

### StoryBook?

- 컴포넌트 단위의 UI 개발 환경을 지원하는 도구임
- 비즈니스 로직과 컨텍스트로부터 분리된 UI컴포넌트를 만들 수 있도록 도와준다.
- 기능, 컴포넌트 등의 흐름은 함수적 테스트로 검증할 수 있으나 UI적인 면은 눈으로 확인할 순 없다. 이를 도와줌
- CDD 방식의 UI개발에 매우 적합함


<br>


### 시작하기

**설치**

```
npm install -g @storybook/cli
```

<br>

**스토리북 프로젝트에 인식하기**

- 스토리북 작성에 필요한 파일들을 생성해준다.

```
getstorybook init

```

![image](https://user-images.githubusercontent.com/76927397/194195563-e70095de-922e-46b6-992d-34979740c5c4.png)


이런 예제나 설정이 알아서 생김

<br>


**스토리 작성하기**

- 네이밍은 테스트 코드 처럼 `[컴포넌트].stories.tsx` 이런식으로 작성하고 생성된 stoires 에 넣으면 된다.

생성된 예제 버튼 스토리로 코드를 알아보자

```javascript
import React from 'react';
import { ComponentStory, ComponentMeta } from '@storybook/react';

import { Button } from './Button';

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'Example/Button',
  component: Button,
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    backgroundColor: { control: 'color' },
  },
} as ComponentMeta<typeof Button>;

// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
const Template: ComponentStory<typeof Button> = (args) => <Button {...args} />;

export const Primary = Template.bind({});
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Primary.args = {
  primary: true,
  label: 'Button',
};

export const Secondary = Template.bind({});
Secondary.args = {
  label: 'Button',
};

export const Large = Template.bind({});
Large.args = {
  size: 'large',
  label: 'Button',
};

export const Small = Template.bind({});
Small.args = {
  size: 'small',
  label: 'Button',
};

```

`Button` 이라는 컴포넌트도 같이 내장되어 있고 그걸 사용해서 스토리를 만들고 있다.


- `export default` 스코프 부분은 컴포넌트에 대한 스토리 설정들이다. 

> title: 스토리북에 명시될 컴포넌트명
> component: 해당 컴포넌트
> argTypes: 컴포넌트에 필요한 파라미터와 타입을 작성하는 것이며 control에 대응되는 조작으로 스토리북에서 유동적으로 변경가능하다.


- 아래의 `export const` 부분들이 각각 컴포넌트에 대한 스토리들이다.

- `Template` 을 통해 스토리 생성용 컴포넌트 반환 함수를 만들고 이를 `Template.bind({})` 로 각 스토리에 복사하여 사용한다.


- `action` 은 UI컴포넌트 독립적으로 만들 때 컴포넌트와 상호작용 확인에 도움이 된다.

[storybook action](https://storybook.js.org/docs/react/essentials/actions)

<br>

> .storybook/preview.js

```javascript
export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};
```

**스냅샷테스트**

- story는 앱 개발 시 외형 변경,수정 확인을 도와주는데 이를 자동화할 수 있다.

- 스냅샷 테스트는 스냅샷으로 컴포넌트 출력을 기록해두고 변경때마다 컴포넌트에 플래그를 지정하여 변경 발생을 알려준다.

```
npm add -D @storybook/addon-storyshots react-test-renderer
```

> storybook.test.ts

```javascript
import initStoryshots from "@storybook/addon-storyshots";
initStoryshots();
```

![image](https://user-images.githubusercontent.com/76927397/194207294-57912b68-5130-4f14-97fb-65d166f050c2.png)

이미지처럼 기존 스냅샷과 비교했을 때 컴포넌트에 변경사항이 있다면 테스트를 실패시키고 알려준다.

의도한 변경이었다면 업데이트 해주면 스냅샷이 교체된다.

<br>


테스트 시 프로퍼티 못 읽는 문제

```
Storyshots › Example/Header › Logged In

    TypeError: Cannot read property 'current' of undefined
```

> react, react-dom, react-test-renderer 버전이 같지 않아서 발생하는 문제임

> storybook/addon-storyshots 설치 시 test-renderer 버전을 체크하여 리액트 버전과 일치시켜주면 된다.


<br>

**컴포넌트 조합하기**

- 부모컴포넌트에 자식 스토리를 결합할 수 있다.

```javascript
import * as TodoItemStories from "../atoms/TodoItem.stories";

import { ComponentMeta, ComponentStory } from "@storybook/react";

import TodoListProvider from "../../organisms/TodoListProvider";

export default {
  component: TodoListProvider,
  title: "Organisms/TodoListProvider",
  actions: { argTypesRegex: "^on.*" },
  decorators: [(story) => <div style={{ padding: "3rem" }}>{story()}</div>],
} as ComponentMeta<typeof TodoListProvider>;

const Template: ComponentStory<typeof TodoListProvider> = (args) => (
  <TodoListProvider {...args} />
);
export const Empty = Template.bind({});

export const Listed = Template.bind({});
Listed.args = {
  todosValue: [
    {
      ...TodoItemStories.Default.args,
      id: new Date("3/3/2020"),
      text: "밥먹기",
    },
    {
      ...TodoItemStories.Default.args,
      id: new Date("3/3/2021"),
      text: "잠자기",
    },
  ],
};
```

<br>

**상태 데이터 연결하기**

- 부모로부터 props 를 얻어 데이터를 구성하는게 아니라 전역 상태 데이터를 컴포넌트에서 사용하는 경우에 데코레이터를 사용해 redux, recoil 같은 컨텍스트를 스토리에 제공할 수 있다.

- `recoilroot` 를 감싸게 한 뒤 설정한 mock 데이터를 recoil state에 반영시키면 된다.

```javascript
import * as TodoItemStories from "../atoms/TodoItem.stories";

import { RecoilRoot, useSetRecoilState } from "recoil";

import { ComponentStory } from "@storybook/react";
import { Todo } from "../../Models/types";
import TodoListProvider from "../../organisms/TodoListProvider";
import { todoState } from "../../store/todoStore";

// ------------------ Todo Mock Data------------------ //

type TodosProps = {
  todos: Todo[];
};
export const MockedState: TodosProps = {
  todos: [
    {
      ...TodoItemStories.Default.args,
      id: new Date("3/3/2021"),
      text: "잠자기",
    },
    {
      id: new Date("4/4/2022"),
      text: "밥먹기",
    },
    {
      id: new Date("4/7/2022"),
      text: "코풀기",
    },
  ],
};

// ------------------ Recoil Mock Provider------------------ //
const MockedTodoListProvider = ({ todos }: TodosProps): JSX.Element => {
  const setTodo = useSetRecoilState(todoState);
  setTodo(todos);
  return <TodoListProvider />;
};

// ------------------ Export(Default) Meta ------------------ //
export default {
  component: MockedTodoListProvider,
  title: "Organisms/TodoListProviderWithState",
  excludeStories: /.*MockedState$/,
};

// ------------------ Export Stories ------------------ //
const Template: ComponentStory<typeof MockedTodoListProvider> = (args) => (
  <MockedTodoListProvider {...args} />
);
export const Default = Template.bind({});
Default.args = {
  todos: MockedState.todos,
};

Default.decorators = [
  (story) => (
    <RecoilRoot>
      <div style={{ padding: "3rem" }}>{story()}</div>
    </RecoilRoot>
  ),
];

export const Empty = Template.bind({});
Empty.decorators = [
  (story) => (
    <RecoilRoot>
      <div style={{ padding: "3rem" }}>{story()}</div>
    </RecoilRoot>
  ),
];
```

<br>

**스토리 기반으로 컴포넌트 테스팅하기**

- 스토리 기반 시나리오 기반 테스팅을 하고자 할 때 스토리를 DOM에 렌더링 해 DOM 쿼리 코드를 수행해 동작이 적절한지 테스트할 수 있다.

> 애드온 추가

```
npm install @storybook/testing-react
```

<br>

> 예시

```javascript
import * as TodoListProviderWithStateStories from "../../stories/organisms/TodoListProviderWithState.stories";

import { fireEvent, render, screen } from "@testing-library/react";

import { MockedState } from "../../stories/organisms/TodoListProviderWithState.stories";
import { composeStories } from "@storybook/testing-react";

const { Default } = composeStories(TodoListProviderWithStateStories);

test("storybook 기반 TodoListProvider Default가 존재하며 삭제 기능이 잘 동작 함.", async () => {
  render(<Default />);

  // 가져와서 목록이 모두있나 확인
  const todoListElement: HTMLElement = screen.getByTestId("todolist");
  expect(todoListElement).toBeInTheDocument();

  MockedState.todos.forEach((todo) => {
    expect(screen.getByText(todo.text, { exact: false })).toBeInTheDocument();
  });

  // 한개 클릭했을 때 지워지나 확인
  const firstTodoElement = screen.getByText(MockedState.todos[0].text, {
    exact: false,
  });
  expect(firstTodoElement).toBeInTheDocument();
  fireEvent.click(firstTodoElement);
  expect(
    screen.queryByText(MockedState.todos[0].text, {
      exact: false,
    })
  ).not.toBeInTheDocument();
});

```

- 사전에 만든 스토리를 렌더링하여 그대로 컴포넌트 테스트를 진행할 수 있다.



<br>

### Ref

- [tutorial](https://storybook.js.org/tutorials/intro-to-storybook/react/ko/get-started/)