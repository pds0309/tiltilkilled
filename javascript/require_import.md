


## Require vs Import

모두 `Javascript`에서 모듈을 가져오는데 사용되는 키워드다.

`require`는 `CommonJS` 스펙에 따라 사용되고 `import`는 `ECMAScript` 모듈 스펙에 따라 사용되는 차이가 있다.


**CommonJS**

`Node.js`에서 사용되는 모듈 시스템의 표준 스펙이다.

`JavaScript`언어의 모듈화 문제를 해결하기 위해 만들어졌다고 한다.

**어떤 문제가 있었을까?**

js 사용 초기에 웹 페이지에서 동적으로 코드를 실행하는 목적으로 사용되었는데

하나의 파일에 모든 코드를 넣기에는 복잡하고 유지보수가 어려웠다.

따라서 파일별로 모듈화를 하는데 이 때 각 파일은 각자의 스코프를 가지고 있기 때문에 한 파일에서 선언한 변수나 함수를 다른 파일에서 

사용할 수 없는 문제가 있었다.

이런 문제를 해결하기 위해 여러 파일로 코드를 분리하면서도 파일 간 변수와 함수를 공유할 수 있는 방법이 필요했고 `CommonJS`가 등장했다.

```javascript
let num = 0;

function increase() {
  num = num + 1;
}

function printNum() {
  console.log("require " + a);
}

module.exports = { increase, printNum };
```

```javascript
const express = require("express");
const a = require("./a");
const app = express();
const port = 4000;

app.get("/", (req, res) => {
  a.increase(); // increase는 선언되었던 환경을 기억하여 본인 파일의 변수를 사용할 수 있다.
  a.printNum(); // 1.. 2.. 3.. 4..
});

app.listen(port, () => {
  console.log("start");
});
```

> 위의 `a`모듈의 환경은 런타임동안 기억되며 모든 파일에서 이를 가져와 사용해도 상태가 유지된다.

> `import`의 경우 모듈을 `import`로 로드할 때마다 해당 모듈의 내용을 새로운 스코프로 가져오기 때문에 독립적이다.


**어디서 쓸까?**

> serverside application

> CLI tool

브라우저 언어에서의 모듈 사용(import)에서의 한계를 극복하여 사용된다.

모듈이 export되는 시점에 한 번 로드되며 이를 사용하는 곳에서 동적으로 가져와 파일 내부 상태가 유지된다.






### require

`exports` 객체로 모듈을 정의하고 `require('모듈이름')`과 같은 형식으로 불러와 사용된다.

```
const fs = require("fs");

fs.readFileSync('input.txt');
```

- `require`는 `동기`로 모듈을 로드하고 반환한다.

- `require`는 런타임 때 `동적`으로 모듈을 로딩한다.

<br>

### import

`import 모듈명 from '경로'` 또는 `import { 변수명 } from '경로'` 형태로 사용

```
import mysql from "mysql"
```


- `import`는 비동기로 모듈을 로드한다. (`Promise`를 반환)

- `import`는 컴파일 타임에 `정적`으로 모듈을 로딩한다.

> import 문이 모듈을 로드할 때마다 해당 모듈의 내용을 새로운 스코프로 가져온다.

