

## React Query

- 서버 상태 관리 라이브러리

<br>


### Client State vs Server State

**Client State**

- 브라우저 세션과 관련된 정보 (사용자에 의한 상태)

**Server State**

- 클라이언트에 표시하는데 필요한 서버로부터의 데이터에 대한 상태
- 다른사람과 함께 공유되며 데이터를 가져오기 위해 비동기 API가 필요하고 앱에서 사용하는 데이터가 유효기간이 지난 상태가 될 수 있다.

**서버 상태 관리를 위해 다음과 같은 작업이 필요할 수 있음**

- 캐싱

- 서버 데이터 중복 호출 제거

- 만료데이터 제거/업데이트

- 쿼리결과 메모이제이션

- 페이지네이션, 지연 로딩 데이터 성능 최적화



<br>

### 왜씀

- 클라이언트를 위한 서버데이터 캐시 유지, 관리를 위해

> 필요 시 서버로부터 데이터를 패치해와 사용하는게 아닌 리액트 쿼리 캐시를 이용하는 것

- redux 등 상태 관리 데이터만 사용하면 클라이언트 상태와 혼재되기 때문에 관리가 힘들어진다. 서버 데이터만 효율적으로 관리할 수 있게 해준다.

<br>

**효율적인 서버 상태 관리 도구 제공**

- `loading, error state` 같은 서버에 대한 모든 쿼리의 상태를 유지해줘 따로 리듀서 같은걸로 커스텀하지 않고 쉽게 재사용하고 쉽게 구성할 수 있다.

- 서버 데이터에 대한 `Pagination` 등을 제공해줘 데이터를 조각으로 가져올 수 있음

- 키 기반으로 서버로부터 데이터를 패치해와 캐싱하기 때문에 동일한 데이터에 대해 중복으로 요청하지 않을 수 있다.

**Prefetching**

- 데이터를 미리 가져와 넣고 사용자에게 데이터가 필요할 때 캐시에서 가져오게끔 하여 서버 통신을 기다릴필요 없게 한다.

**Mutation**

- 데이터 변경이나 업데이트를 쉽게 관리할 수 있음




<br>


### 시작하기

**설치**

```
npm install react-query
```


> 22/07/20 에 react query 4버전이 나왔고 TanStack Query 의 react-query 사용이 권장됨

> React Query 버전 4를 사용하시는 경우, 다음과 같은 특이사항이 있습니다.

> 1. 버전 4에서는 설치와 임포트(import)에 react-query가 아닌 @tanstack/react-query를 사용하시는 것을 권장합니다.

> 2. 쿼리 키는 반드시 배열이어야 하며, 만약 강의에서 문자열을 쿼리 키로 사용하는 경우, 해당 문자열을 반드시 배열 안에 넣어야 합니다.

<br>

3버전 설치

```
npm install react-query@^3
```

4버전 설치

```
npm install @tanstack/react-query
```


<br>

**Query Client 생성**

- 쿼리와 서버의 데이터 캐시를 관리하는 클라이언트

<br>

 **QueryProvider 제공**

- 자식 컴포넌트에 캐시와 클라이언트 구성을 제공할 `QueryProvider` 를 적용

```javascript
import { QueryClient, QueryClientProvider } from "react-query";

import React from "react";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <div className="App"></div>
    </QueryClientProvider>
  );
}

export default App;
```


**useQuery Hook 실행**

- 서버에서 데이터를 가져오기 위해 `useQuery` hook을 실행한다.

```javascript
import { Post } from "../Models/type";
import { useQuery } from "react-query";

const fetchPosts = async () => {
  const response = await fetch(
    "https://jsonplaceholder.typicode.com/posts?_limit=10&_page=0"
  );
  return response.json();
};

const Posts = (): JSX.Element => {
  const { data } = useQuery<Post[]>("posts", fetchPosts);
  return (
    <div>
      <ul>
        {data?.map((post: Post) => (
          <li key={post.id} className="post-title">
            {post.title}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Posts;
```

<br>


### Stale, Cache

**Stale(만료)**

- `staleTime` 은 데이터를 허용하는 `최대 생존 시간` 이라고 할 수 있다.

> 앱에서 특정 데이터를 패치해서 보여주는데 이게 30초간 그대로 보여줄 수 있다고 생각된다면 staleTime은 30초인 것

- react query 에서 `데이터 리패칭`은  `stale` 된 데이터에 대해서만 실행된다.

- 기본은 0초로 데이터가 늘 최신상태를 유지하는 것이 왜 업데이트가 안되는지에 대한 것보다 중요하다는 마인드

- staletime 설정으로 인해 데이터가 fresh상태로 유지되는 동안은 컴포넌트 언마운트 후 다시 마운트해도 fetch가 발생하지 않음

<br>

**Cache**

- 데이터가 `inactive` 상태일 때 기준으로 캐시데이터를 남기며 `cachetime` 은 이를 얼마나 오래 남길지를 말한다.

- `stale` 이 리패칭에 대한 고려사항이라면  `cache` 는 나중에 다시 필요할 수 있는 데이터를 위함임

> 활성화된 useQuery가 없을 때 데이터는 cold storage로 이동하며 구성된 cachetime이 만료되면 캐시의 데이터가 만료되면 gc가 실행되어 더이상 사용할 수 없는 데이터가 됨

용도는?

- 컴포넌트가 언마운트 된 후 캐시타임만큼 캐싱되는데 그 동안 다시 마운트해 새로운 fetch가 발생했을 때 서버로부터 갱신된 새로운 값을 가져오는 동안 빈 공간(또는 loading state)을 보여주지 않고
 캐시 데이터를 보여준다.

<br>

**isLoading, isFetching**

- isFetching : 데이터가 fetch될 때 true, 캐싱 데이터가 있어서 백그라운드에서 fetch되더라도 true
- isLoading : 캐싱된 데이터가 없을때 fetch 중에 true


<br>
### React Query DevTools

- 앱에 추가할 수 있는 컴포넌트로 개발중인 모든 쿼리의 상태를 표시해준다.

- 기본적으로 프로덕션 환경에서는 동작하지 않도록 설정되어있음.

**알수있다**

- 쿼리들의 상태(active, inactive, stale)

- 마지막 갱신 timestamp

- data 탐색기, 쿼리 탐색기 제공




### useQuery

- [useQuery docs](https://react-query.tanstack.com/reference/useQuery)

<br>

```javascript
export declare function useQuery<TQueryFnData = unknown, TError = unknown, 
TData = TQueryFnData, TQueryKey extends QueryKey = QueryKey>(options: UseQueryOptions<TQueryFnData, TError, TData, TQueryKey>): UseQueryResult<TData, TError>;
```

> 첫번째 파라미터: 쿼리 키 (문자열 또는 배열)
> 두번째 파라미터: 쿼리 비동기 함수(이 쿼리에 대해 데이터를 가져오는 방법)
> 세번째 파라미터: ...options



**return value**

> data: 비동기 함수에 대한 결과 데이터

> isLoading: 데이터가 로딩중 상태인지에 대한 여부(데이터를 가져오는 중이고 캐시 데이터는 없는 상태)

> isFetching: 비동기 쿼리가 해결되지 않았음을 의미함(axios call 조차 안했음)

>isError: 데이터를 가져왔는데 에러 상태인지에 대한 여부로 default로 에러일 때 3번은 재시도한 후 결정한다.

> error: 에러상태일 때 리턴 결과 데이터

**option**

> cacheTime: inactive 데이터가 메모리에 유지될 시간으로 기본값은 5분임

> staleTime: 쿼리데이터가 refresh에서 stale로 전환되는데 걸리는 시간으로 기본 0초

> enabled: false일 시 쿼리가 비활성화되며 유효한 상황일 경우만 true를 전달하는 식으로 사용할 수 있음

> onSuccess: 쿼리 성공시 호출되는 함수(쿼리 실행 또는 setQueryData 성공 후)

> onError: 오류시 호출되는 함수

> onSettled: 쿼리 실행 시 호출되는 함수

> keepPreviousData: 쿼리키가 변경되어서 새로운 데이터를 가져오는 중에도 마지막 data 값을 유지한다.

> initalData: 캐시에 데이터가 없을 때 표시해줄 초기값

> refetchOnWindowFocus: 윈도우가 재 포커스되었을 때 데이터 호출할 것인지에 대한 여부





```javascript
const {data, isError, isLoading} = useQuery("yourkey", yourAsyncFunc);
```

<br>


### useMutation

- 데이터를 업데이트 할 수 있도록 서버에 네트워크 요청을 함

**데이터 갱신?**

> 서버에 요청하며 업데이트 사항을 미리 보여주며 문제 시 롤백하는 낙관적 업데이트

> 서버로부터 얻은 실제 갱신 데이터를 캐시를 업데이트 하여 반영

> 관련 쿼리를 무효화(invalidation) => 리패치하여 최신 상태로 수정 

**useMutation**


- 변경을 위한 mutate function을 리턴으로 반환하여 변경을 시도할 위치에서 이를 사용함

- 데이터를 저장하는 것이 아니기 때문에 쿼리키가 필요없으며 `'mutationFn` 자체가 파라미터를 가질 수 있음

- `isFetching` 이 없고 기본적으로 요청 재시도가 없다.


```javascript
UseMutateFunction<TData = unknown, TError = unknown, TVariables = void, TContext = unknown>
```

**params**

> TData: 리턴 타입: void
> TError: 에러 타입
> TVariables: 변이함수 인자 타입 (mutation 함수로 전달될 변수)
> TContext: onMutate 인자 타입

> 예시

```javascript
import { useMutation } from "react-query";

const deleteMutation = useMutation((postId: string) => deletePost(postId));

// ...
<button onClick={() => deleteMutation.mutate(post.id)}>Delete</button>
```

**낙관적 업데이트 - onMutate**

- mutation이 작동할 것이라 생각하고 캐시데이터를 서버와 응답하기 전에 미리 바꾸는 것

- 캐시 업데이트가 훨씬 빠르고 복수의 컴포넌트에서 쿼리를 사용할 때 응답속도가 빠름

- 서버 업데이트 실패시 코드가 복잡하다는 단점이 있음(업데이트 이전 데이터를 저장해두고 롤백할 수 있어야 한다)

> onError 핸들러에서 컨텍스트(이전 캐시 데이터)를 받아 롤백시킨다.







<br>

### useInfiniteQuery

- 파라미터 값만 변경하여 동일한 useQuery를 계속 호출할 때 사용(예: 무한스크롤)

```javascript
const res = useInfiniteQuery(queryKey, queryFn);
```


**vs useQuery**

- `useQuery`에서의 `data` 는 단순히 결과 `data` 를 말했으나 `useInfiniteQuery` 는 페이지 객체인 `pages` 와 각 페이지의 매개변수를 기록하는  `pageParams` 로 구성됨

> `useInfiniteQuery` 의 모든 쿼리는 page 배열에 고유한 요소를 가지고 그 요소는 해당 쿼리에 대한 데이터에 해당된다.

> pageParams 는 검색된 쿼리의 키를 추적한다.


<br>

### useInfiniteQuery Options

**pageParam**

- `useInfiniteQuery` 가 현재 어떤 페이지에 있는지 확인할 수 있는 파라미터 값임

```javascript
const res = useInfiniteQuery(
    ['infinitePerson'], 
//초기 파라미터 5
    ({ pageParam = 5 }) => axios.get('http://localhost:8080/person', {
    params: {
        id: pageParam
    }
}));
```

<br>

**getNextPageParam**

- 다음 페이지 데이터 조회 시 사용
- `lastPage` 파라미터는 쿼리로 호출한 가장 마지막에 있는 페이지 데이터를 의미
- `allPages`는 호출된 모든 페이지 데이터를 의미
- `return` 값이 다음 페이지 호출 시 `pageParam` 값으로 사용된다.

```javascript
const res = useInfiniteQuery(
    ['infinitePerson'], 
    ({ pageParam = 5 }) => axios.get('http://localhost:8080/person', {
    params: {
        id: pageParam
    }
}), {
    getNextPageParam: (lastPage, allPages) => {
        return lastPage.data.id + 1; 
    },
});
```

<br>

**fetchNextPage**

- 다음 페이지 데이터 호출 시 사용
- `useInfiniteQuery` 의 리턴값에 포함됨

```javascript
const res = useInfiniteQuery(queryKey, queryFn);

// ...
<button onClick={() => res.fetchNextPage()}>button</button>
```

- `useInfiniteQuery`를 이용해 호출되는 데이터는 page별로 배열에 담기는데 `nextPage` 로 호출하면 오른쪽에, `prevPage`로 호출하면 왼쪽에 담김

<br>

**hasNextPage, hasPreviousPage**

- 페이지 한계를 설정했을 때 다음 또는 이전 페이지가 존재하는지 여부를 알려줌

- `useInfiniteQuery` 리턴에 포함되며 boolean 타입을 가짐

- true인지 false인지를 결정해주는 곳은 `getNextPageParam`과 `getPreviousPageParam` 이며 사용하는 쪽에서 검사 후 패치할 수 있도록 설정하면 된다.

```javascript
<button onClick={() => res.hasNextPage && res.fetchNextPage()}>Next</button>
```

<br>

**isFetching**

- 페이징을 위한 패칭 중인지 여부를 알려줌 (일반적인 패칭중 상태와 구분됨)

- 로딩 스피너 보여주기 등에 사용함


**select**

- 리액트 쿼리는 불필요 연산을 줄이기 위해 메모이제이션 하는데 select는 데이터와 함수의 변경여부를 파악해 변경 시에만 수행된다.

- `useCallback` 같은 stable한 함수가 필요하다.

> 모든 목록을 보여주다가 필터링해서 보여준다던가 할 때 유용하다.


```javascript
  const selectFunc = useCallback(
    (data) => filterFunc(data, user),
    [user],
  );

  const { data: appointments = fallback } = useQuery(
    [queryKeys.appointments, monthYear.year, monthYear.month],
    () => getAppointments(monthYear.year, monthYear.month),
    { select: showAll ? undefined : selectFunc },
  );
```

- `select` 옵션 값이 `undefined` 면 반환해야될 모든 데이터 (data)를 반환하며 `showAll` 이라는 컴포넌트의 상태에 따라 필터된 데이터를 반환하여 보여준다.

- `selectFunc` 는 컴포넌트 내부에 있는 익명함수로 상태변경 발생시마다 다시 선언되기 때문에 `useCallback` 처리를 해주어야 정상적으로 메모이제이션이 되어 동작한다.

<br>


<br>

### Prefetching

```
useQueryClient().prefetchQuery([deps], async func);
```

- 데이터를 stale 상태이지만 캐시에 저장해두는 것으로 추후 사용자가 사용할법한 데이터들을 프리패칭한다.
- 데이터를 사용하고자 할 때 만료상태에서 캐시를 이용해 데이터를 다시 가져오는 것

사용자가 갈 것으로 기대되는 api 요청이 있는 컴포넌트가 렌더링 되기전에 미리 패치해 공백화면을 안보여주게 한다던가 응답속도를 향상시킨다.(예: Pagination 다음페이지)


<br>


### refetching

- refetch는 stale 데이터를 서버로부터 업데이트해 가져오는 것을 보장한다.

**언제 리패치되나**

> 윈도우 재포커스, 네트워크 재연결

> 새로운 쿼리 마운트

> useQuery를 call하는 리액트 컴포넌트가 마운트 될 때마다

> refetchInterval 적용 시 시간이 되었을 경우

**옵션**

> 전역 또는 쿼리에 적용가능

- refetchOnMount : default true

- refetchOnWindowFocus: default true

- refetchOnReconnect: default true

- refetchInterval: number


**리패칭을 제한하려면??**

- `staleTime` 을 늘리면 데이터 만료되는 시간이 길어져 리패칭이 일어나지 않음

- 특정 상황에서의 리패칭을 막으려면 위의 옵션을 설정해주면 된다.

**제한 주의점**

- 미세한 변동에도 큰 변화를 불러오는 데이터나 실시간에 가까운 데이터에 리패칭 제한은 위험하다.

- 하루에 한 번 바뀌는 등 큰 변화가 거의 없는 데이터에 매우 유용하다.(리패치할 필요가 없다)


**전략**

- 우선은 업데이트(리패칭)를 생각하고 잦은 업데이트가 불필요한 곳에 리패칭을 제한하자

<br>


### QueryClient

- 캐시데이터와 상호작용 할 수 있게 해줌



**InvalidateQueries**

- 캐시데이터를 무효화한다. => 사용자가 페이지를 새로고침하지 않아도 된다.

```javascript
queryClient.invalidateQueries([key접두사]);
```

- 쿼리를 `stale`시키고 현재 렌더링 중인 컴포넌트의 쿼리라면 `refetch`를 트리거한다.


<br>


**setQueryData**

- 쿼리의 캐시 데이터를 즉시 업데이트 해주는 동기 함수며 쿼리 키가 없는 경우에 대한 요청일 경우 새로 만든다.
- mutation 후 쿼리에 대한 데이터가 변경되었을 수 있기 때문에 명시적으로 사용하면 좋음



**removeQueries**

- `removeQueries` 메소드는 쿼리 키 또는 기타 기능적으로 액세스 가능한 쿼리 속성/상태를 기반으로 캐시에서 쿼리를 제거하는 데 사용한다.

**cancelQueries**

- `cancelQueries` 메소드는 쿼리 키 또는 기타 기능적으로 액세스 가능한 쿼리 속성/상태를 기반으로 나가는 쿼리를 취소하는 데 사용할 수 있다.

- 이는 쿼리 리패칭을 취소해야 하므로 낙관적 업데이트를 수행할 때 가장 유용합니다.


<br>

### React Query Test


**msw 설치하기**

- react query 는 서버와의 통신 데이터를 관리하는 것이기 때문에 테스팅에서 실제 서버 api를 사용하지 않게 하기 위한 mock api가 필요함

```
npm install msw
```

<br>

**QueryProvider wrapping 필요**

- 테스트 컴포넌트 렌더링 전에 `QueryProvider`를 감싸는 함수를 만든다.

- 테스트에 `queryClient`를 제공한다.

<br>

**단일 테스트에 QueryProvider, QueryClient 제공하기**

> 세팅

```javascript
import { render, RenderResult } from '@testing-library/react';
import { ReactElement } from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';

// make a function to generate unique queryClient for each test
const generateQueryClient = () => {
  return new QueryClient();
};

export function renderWithQueryClient(
  ui: ReactElement,
  client?: QueryClient,
): RenderResult {
  const queryClient = client ?? generateQueryClient();

  return render(
    <QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>,
  );
}
```

<br>

> 테스트 코드

```javascript
import { render, screen } from '@testing-library/react';
import { renderWithQueryClient } from 'test-utils';

import { Treatments } from '../Treatments';

test('renders response from query', () => {
  renderWithQueryClient(<Treatments />);
});

```

<br>

**useQuery 테스트하기**

- 해당 컴포넌트 렌더링 시 `reactQuery` 를 통해 api를 패치하고 보여준다고 할 때 진짜 그러나 테스트하기만 하면 된다.

```javascript
test('renders response from query', async () => {
  renderWithQueryClient(<Treatments />);
  const treatmentTitles = await screen.findAllByRole('heading', {
    name: /massage|facial|scrub/i,
  });
  expect(treatmentTitles).toHaveLength(3);
});
```

<br>

**useMutate 테스트하기**

- `msw` 는 `mutation` 으로 발생되는 변경을 모방하지 않음 (변경되지 않음)

- 실제 데이터로부터의 결과가 아닌 `mutation` 성공 응답 또는 성공 시 발생되는 이벤트나 컴포넌트의 동작 여부를 판단하면 된다.




<br>


**에러 테스팅을 위한 logger 설정**

- 기본적으로 리액트 쿼리는 에러 상황에 대해 모두 콘솔로 출력함
- 에러를 출력하지 않는다던가 에러 레벨을 설정할 수 있다.
- test 설정 파일에 다음과 같은 설정을 추가함

```javascript
import { setLogger } from 'react-query';

setLogger({
  // eslint-disable-next-line no-console
  log: console.log,
  // eslint-disable-next-line no-console
  warn: console.warn,
  error: () => {
    // do not print error log
  },
});
```



<br>


### Ref

- [udemy - react-query](https://www.udemy.com/course/react-query-react)

- [useInfiniteQuery](https://jforj.tistory.com/246)

- [React Query 사용하기](https://blog.rhostem.com/posts/2021-02-01T00:00:00.000Z)

