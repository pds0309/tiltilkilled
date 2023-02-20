

## What?

> 이벤트 버블링(Event Bubbling)과 이벤트 캡쳐링(Event Capturing)은 웹에서 이벤트가 DOM 구조에서 전파되는 방식을 말한다.

<br>

## Event Capturing

이벤트가 발생한 엘리먼트보다 상위 엘리먼트에서 하위 엘리먼트로 이벤트가 전파되는 방식을 말한다.

즉, 가장 상위의 부모 엘리먼트에서 시작하여 하위 엘리먼트로 이벤트가 전달되는 방식이다.

기본적으로는 `false`로 설정되어있고 `eventListener` 의 3번째 인자를 true로 설정하거나 `{ capture: true }` 설정 시 캡쳐링을 설정할 수 있다.

```html
<div id="parent" style='width:300px; height:100px; background-color:red'>
<div id="child" style='width:200px; height:50px; background-color:blue'>
```

```javascript
const parent = document.getElementById('parent');
const child = document.getElementById('child');

parent.addEventListener('click', () => {
  console.log('Parent element clicked');
}, true);

child.addEventListener('click', (e) => {
  console.log('Child element clicked');
});
```

![cap1](https://user-images.githubusercontent.com/76927397/220111478-c2ba18cf-20d0-4528-bfc4-25b082f0c1a5.gif)


<br>

## Event Bubbling

이벤트 버블링은 이벤트가 발생한 엘리먼트에서 상위 엘리먼트로 이벤트가 전파되는 방식을 말한다.

즉, 가장 하위의 자식 엘리먼트에서 시작하여 상위 엘리먼트로 이벤트가 전달되는 방식이다.

자바스크립트에서는 기본적으로 이벤트 버블링이 발생하며

위의 이벤트 캡쳐링 예시에서 true설정을 뺀다면 이벤트 버블링이 동작한다.

해당 타깃에서 document 객체를 만날 때까지 핸들러가 모두 호출된다.

![cap3](https://user-images.githubusercontent.com/76927397/220113136-450db2b8-33c4-41a1-8181-05fe83699837.gif)

<br>

## 이벤트 전파 방지

`event.stopPropagation()`과 `event.preventDefault()`는 이벤트 전파를 제어하고 기본 동작을 방지하는 데 사용되는 메서드다

<br>

### event.stopPropagation()

이벤트의 전파를 중지시킨다.

이벤트가 발생한 엘리먼트에서 더 이상 다음 엘리먼트로 이벤트가 전파되지 않는다.

![cap2](https://user-images.githubusercontent.com/76927397/220112862-3547f0f2-edbd-4425-9d36-453f3c9c4bfe.gif)

<br>

### event.preventDefault()

해당 엘리먼트가 가지는 이벤트의 기본 동작을 취소시킨다.

예를 들어 링크를 클릭하면 브라우저는 해당 링크의 주소로 이동해야 하지만 이 메소드를 사용해 그런 기본 이벤트 동작을 막을 수 있다.

