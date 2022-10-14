

## Cypress

- [공식문서](https://docs.cypress.io/guides/overview/why-cypress)

-E2E 테스트를 위한 오픈소스 도구

- 브라우저를 조작할 수 있는 별도의 드라이버를 만들어 테스트하며 GUI도구를 지원한다.


**설치**

```
npm install cypress
```

**package.json 에 추가**

```json
"scripts": {
    "cypress:open": "cypress open",
    "cypress:run": "cypress run --browser chrome"
  },
```

**실행**

```
npm run cypress:open 
```

테스트 창이 뜨면 E2E를 선택하고 테스트 파일을 만들자

![image](https://user-images.githubusercontent.com/76927397/193007893-ac2c306c-8dc9-4b5f-99ce-cd9509f39a1d.png)


<br>


**테스트파일만들기**

자기 앱 경로 접속 테스트

```javascript
describe("My First Test", () => {
  it("passes", () => {
    cy.visit("http://localhost:3000");
  });
});
```

![image](https://user-images.githubusercontent.com/76927397/193012318-7b636c40-1fa4-4018-b088-4008d1133cb6.png)

접속이 잘 되면 E2E 테스트를 작성할 준비가 된 것



**사용예시**

```javascript
    cy.get("button").contains("Order Sundae").should("be.disabled");

```

`.get` 으로 element를 가져오고 should로 검증할 수 있음


```javascript
cy.get("div")
      .contains("label", "Vanilla scoop")
      .parent()
      .find("input[type='number']")
      .type("2")
      .should("not.have.class", "is-invalid");
```

input을 가져와서 조작할 수 있음


**E2E 테스트 예시**

```javascript
describe("통합 Happy Path Testing", () => {
  const vanillaValue = 2;
  const invalidChocolateValue = 2.5;
  it("Load Main Page", () => {
    cy.visit("http://localhost:3000");
  });

  it("사용자가 Vanilla Scoops 개수를 조작 테스팅: 버튼활성화/소계반영", () => {
    cy.get("button").contains("Order Sundae").should("be.disabled");

    // valid input
    cy.get("div")
      .contains("label", "Vanilla scoop")
      .parent()
      .find("input[type='number']")
      .type(vanillaValue + "")
      .should("not.have.class", "is-invalid");

    cy.get("button").contains("Order Sundae").should("be.enabled");

    cy.get("h2").contains("Grand total: $4.00");

    // invalid input
    cy.get("div")
      .contains("label", "Chocolate scoop")
      .parent()
      .find("input[type='number']")
      .type(invalidChocolateValue + "")
      .should("have.class", "is-invalid");

    cy.get("h2").contains("Grand total: $4.00");
  });

  it("사용자가 Toppings 를 조작하고 이는 소계에 반영된다.", () => {
    cy.get("div")
      .contains("label", "Chocolate topping")
      .parent()
      .find("input")
      .check();

    cy.get("h2").contains("Grand total: $5.50");
  });

  it("사용자가 주문 요청 버튼을 클릭하여 OrderSummary Page 로 이동된다", () => {
    cy.get("button").contains("Order Sundae").click();
    cy.get("h2").contains("Order Summary");
    cy.get("li").contains(vanillaValue + ".Vanilla");
    cy.get("li").contains(0 + ".Chocolate");
  });

  it("OrderSummary Page에서는 기본으로 이용정보 동의 체크가 해제되어있으며 체크 시 주문확인 버튼이 활성화 된다.", () => {
    const confirmOrderButton = cy.get("button").contains("Confirm order");
    confirmOrderButton.should("not.be.enabled");
    const confirmCheckbox = cy
      .get("div")
      .contains("label", "I agree toTerms and Conditions")
      .parent()
      .find("input[type='checkbox']")
      .should("not.be.checked");

    confirmCheckbox.check();
    confirmOrderButton.should("be.enabled");
  });

  it("OrderSummary Page의 agree 하이라이트 텍스트에 마우스 오버 시 부가 정보를 보여준다. ", () => {
    const confirmHighlightText = cy
      .get("span")
      .contains("Terms and Conditions")
      .should("have.css", "color", "rgb(0, 0, 255)");

    confirmHighlightText.trigger("mouseover");
    const additionalText = cy.get(".popover").contains("실제로는 안준다");
    additionalText.should("be.visible");
    confirmHighlightText.trigger("mouseout");
    cy.get(".popover").should("not.exist");
  });

  it("OrderSummary Page에서 Confirm Order 버튼 클릭 시 주문 결정 페이지로 이동한다.", () => {
    const confirmOrderButton = cy
      .get("button")
      .contains("Confirm order")
      .should("be.enabled");

    confirmOrderButton.click();
    cy.get("h1").contains("Thank you");
    // 올바른 주문 정보 id가 있다.
    cy.get("p")
      .contains("Your Order number is")
      .then((text) => {
        expect(
          text
            .text()
            .replace("Your Order number is", "")
            .trim()
            .match(/[0-9]/gi)
        ).to.not.be.null;
      });
  });

  it("Create new Order 버튼 클릭 시 초기 페이지로 이동하며 모든 주문 사용자 정보가 초기화 된다.", () => {
    const createNewOrderButton = cy
      .get("button")
      .contains("Create new order")
      .should("be.enabled");
    createNewOrderButton.click();

    cy.get("button").contains("Order Sundae").should("be.disabled");
    cy.get("h2").contains("Scoops Total: $0.00");
    cy.get("h2").contains("Toppings Total: $0.00");
  });
});
```

![image](https://user-images.githubusercontent.com/76927397/193029745-43605310-875a-47fd-9fa0-5965a2a1ce85.png)

<br>


### API Mocking 하기

- 통합 테스트 시 기본적으로는 실제 서버의 api를 사용하게끔 되어있을 것인데 실 서버 api가 아직 없거나 사용량 제한이 있어 자원을 낭비하면 안되거나 서버 데이터 상태가 유동적이라 테스트가 힘들 수 있다.

- 따라서 msw를 활용해 mocking 하여 함수적 테스트를 진행했던 것 처럼 cypress에서도 api를 mocking하여 e2e 테스팅을 진행할 수 있다.


**방법**

- `cypress-msw-interceptor` 라이브러리 또는 cypress interceptor 활용

- 기존 msw 설정 그대로 사용하기

> msw 활용 시 node가 아닌 browser 로 mock 서버를 구성해야만 사용가능


~~mock browser server 를 공식문서대로 만들고 다음과 같이 e2e.js 파일에 설정을 추가해 cypress e2e 테스트 시 mock api 사용가능~~

```javascript
import "./commands";

import { worker } from "../../src/mock/browser";

Cypress.on("test:before:run:async", async () => {
  await worker.start();
});
```


