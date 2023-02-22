

# Reflow & Repaint


<br>

## Render Tree

DOM과 CSSOM 트리를 함께 결합하여 구성된 트리와 같은 구조

렌더트리를 사용하여 브라우저에 보이는 레이아웃을 계산하여 구성하고 페인팅한다.

최종적으로 화면에 보여질 항목에 대한 것들로 픽셀 매트릭스 영역에 포함되지 않는 노드는 포함되지 않는다. 

<br>

## Reflow

> 웹페이지의 레이아웃이 변경될 때 발생하는 과정입니다. 브라우저는 새로운 레이아웃을 계산하고, 해당 레이아웃에 따라 요소들의 크기와 위치를 결정합니다.

브라우저가 각 노드의 레이아웃 정보를 계산하게 되고

레이아웃은 픽셀 단위의 각 노드 크기와 화면에 인쇄될 위치로 구성된다.

사용자 이벤트 등으로 새로운 HTML요소가 변경되거나하여 렌더 트리가 다시 만들어지고

레이아웃 동작을 다시 수행할 때 이런 과정을 `reflow` 라고 하며 꽤 비용이 큰 작업이라 브라우저는 필요할 때만 `reflow`를 수행한다.

<br>

### 언제 발생할까?

- 페이지를 처음 로드할 때
- 브라우저 창의 크기가 변경될 때
- 요소의 크기, 위치, 내용이 변경될 때
- 폰트 크기나 스타일, 이미지 크기 등과 같은 요소의 스타일이 변경될 때

<br>

### Reflow를 최소화하는 방법

<br>

**CSS 선택자를 최소화하고 인라인스타일을 사용하지 않는다**

> CSS 선택자와 인라인 스타일이 Repaint에 영향을 주는 이유는, 이들이 요소의 스타일을 동적으로 변경할 수 있는 요소이기 때문

- css선택자에 대한 엘리먼트를 브라우저가 찾고 스타일규칙을 적용해 계산하고 적용하는 것은 꽤 큰 비용이며 css선택자의 `specificity`가 높을수록 적용되는 스타일규칙이 세부적이어서 많이 발생함

- 인라인스타일은 엘리먼트에 직접 적용되는데 브라우저는 해당 요소의 스타일을 즉시변경하며 `reflow`를 발생시킴

<br>

**요소의 크기와 위치를 변경하기 전에, 해당 요소를 변수에 저장해둔 후 변경한다.**

- 요소의 크기와 위치를 변경할 때마다, 브라우저는 해당 요소의 스타일 계산과 레이아웃을 다시 계산하고, Reflow와 Repaint를 발생시킴

- 변수에 엘리먼트를 저장하면 브라우저가 해당 요소를 계산하고 변수에 저장하게되고 변수를 이용해 크기와 위치를 변경하고 중복되는 스타일은 그대로 사용해 요청 수를 줄일 수 있다.

<br>

**`Flex`나 `Grid` 등의 레이아웃 기술을 사용한다..**

- Flexbox와 Grid는 레이아웃을 계산할 때 캐싱을 이용하고, 유연한 크기 조절 기능을 제공하여, Reflow와 Repaint를 최소화할 수 있다.

<br>

**레이아웃 위치나 크기 등 css속성을 변경할 때 한 번에 변경하자**

```css
.my-element {
  width: 100px;
  height: 100px;
  padding: 10px;
  border: 1px solid #000;
  background-color: #fff;
}
```
```javascript
const element = document.querySelector('.my-element');
// bad
element.style.width = '200px';
element.style.height = '200px';
element.style.padding = '20px';
element.style.border = '2px solid #f00';
element.style.backgroundColor = '#ccc';

// good
element.style.cssText = 'width: 200px; height: 200px; padding: 20px; border: 2px solid #f00; background-color: #ccc';

```


<br>


### Reflow를 유발하는 스타일 속성

> width, height, margin, padding, border, position, display, float, clear, font-size, font-weight, text-align, white-space, line-height



<br>

<br>

## Repaint

> 화면에 가시성이 변하지만 레이아웃에 영향을 미치지 않는 요소의 외관을 변경할 때 발생한다. 

요소들의 위치와 크기, 스타일 계산이 완료된 Render Tree 를 이용해, 픽셀 값을 채워넣게 된다.

요소 스타일이 변경될 때 브라우저가 해당 요소의 새로운 스타일을 계산하고, 해당 요소를 다시 그리는 것을 `repaint`라고 하며 `reflow`에 비해 비용이 작다.

<br>

### 언제 발생할까?

- 요소의 스타일이 변경될 때
- 요소 위에 다른 요소가 나타나고 사라질 때, 요소가 숨겨졌다가 나타날 때

<br>

### Repaint를 최소화하는 방법

<br>

**가능하다면 `css animation`을 쓸 것**

- 일반적으로 css 애니메이션은 `transform`, `opacity`를 사용하는데 이들은 `repaint`가 아니라 브라우저 GPU에서 처리되서 빠름
- css애니메이션은 하드웨어 가속을 사용할 수 있다.
- javascript애니메이션은 처리를 위해 CPU를 사용하며 병렬처리가가능한 GPU에 비해 느릴 수 있어 많은 `repaint` `reflow`를 유발할 수 있다.

> 처리 속도가 너무 느리면 브라우저의 렌더링 엔진이 요청을 처리하지 못하고, 일시적으로 렌더링이 중단될 수 있습니다. 이러한 상황에서는, 처리 속도가 느려지고, 처리할 수 있는 요소의 양이 줄어들기 때문에, 렌더링 엔진은 reflow 및 repaint를 더 많이 수행할 가능성이 높아지며, 이로 인해 전체적인 성능이 저하될 수 있습니다.

<br>

**transform, opacity를 활용할 것**

- GPU가속에 의해 처리되는 속성들로 해당 속성이 변경되어도 레이아웃 엔진에서 계산하지 않고 GPU에서 직접처리 되기 때문에 빠르다.

<br>

### Reflow를 유발하는 속성들

> color들, visibility, shadow

<br>


## 리액트에서 reflow, repaint 최소화하기

<br>

**css변경 최소화하기**

- css클래스를 추가하거나 지우는 것이 인라인 스타일 조작 보다 비용이 적다

**React.Memo, shouldComponentUpdate**

- 메모이제이션으로 리렌더링을 막아 최적화시킴

> styled-component등이 컴포넌트가 리렌더링되어 리렌더링이 된다고 repaint, reflow가 되는 것은 아니며 기존 스타일과 비교해 같으면 유발하지 않지만 리렌더링 시 모든 스타일을 다시 계산하고 적용하기 때문에 repaint, reflow를 유발할 수도 있다.

> 특히 함수나 객체를 props으로 받아 스타일을 구성하게 되면 repaint, reflow를 발생시킬 수 있다. 되도록이면 기본타입을 사용하던가 memo, callback등을 활용하자

