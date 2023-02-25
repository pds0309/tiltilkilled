

### Q.  react-testing-library를 통해 컴포넌트에 대한 테스트를 수행할 때 사용자 이벤트로 발생할 수 있을만한 모든 예측가능한 상황을 찾아서 테스트를 수행하는 것이 좋은가요?

리액트에서 react-testing-library를 사용하여 컴포넌트에 대한 테스트를 작성할 때, 모든 예측 가능한 사용자 상황을 테스트하는 것이 중요합니다. 그러나 가능한 모든 상황을 커버하기에는 시간과 노력이 너무 많이 들기 때문에 일부 핵심적인 상황을 우선적으로 고려해야 합니다.

또한 테스트 케이스를 작성할 때는 테스트 대상이 되는 컴포넌트의 동작과 기능에 초점을 맞추어야 합니다. 예를 들어, 로그인 폼 컴포넌트를 테스트하는 경우, 로그인 버튼이 클릭되었을 때 정상적으로 로그인이 되는지, 로그인에 실패했을 때 적절한 오류 메시지가 나타나는지 등에 대한 상황을 테스트할 수 있습니다.

마지막으로, 테스트는 가능한 한 실제 사용자의 상황을 재현해야 합니다. 따라서 테스트 시나리오를 만들 때는 사용자가 실제로 대화하는 방식과 유사한 방식으로 테스트를 작성하는 것이 좋습니다. 예를 들어, 사용자가 키보드로 입력할 때 테스트 케이스에서도 키보드 이벤트를 시뮬레이션하여 입력을 모방하는 것이 좋습니다.

<br>

### Q. e2e테스트는 코드 작성에 오랜 시간이 걸리고 비용이 큰 것으로 알고 있습니다. edge case에 대한 상세한 테스트는 unit 또는 functional test로 검사하고 e2e 테스트에서는 happy path 에 대한 testing만 해도 적절한 것일까요? 또는 일반적으로 보통 e2e테스트를 어느정도 수준으로 작성하는지 아시나요?

e2e 테스트는 전체 시스템의 동작을 검증하므로, happy path에 대한 테스트 뿐만 아니라 edge case 및 예외 상황에 대한 검증도 수행하는 것이 좋습니다. 그러나 e2e 테스트가 코드 작성에 오랜 시간과 비용이 드는 것은 사실이기 때문에, 모든 edge case에 대한 상세한 검증을 수행하기는 어려울 수 있습니다.

이에 따라, 보통은 e2e 테스트에서는 주요한 기능에 대한 happy path 검증을 우선적으로 수행하고, 그 이후에 edge case 및 예외 상황에 대한 검증을 추가적으로 수행하는 것이 일반적입니다. 이러한 방식으로도 전체 시스템의 동작을 검증할 수 있지만, 시스템의 크기와 복잡도에 따라 테스트의 깊이와 범위를 조정해야 할 수 있습니다.

또한, e2e 테스트를 어느 정도 수준으로 작성할 것인지는 프로젝트의 성격과 요구사항, 리소스 등에 따라 다를 수 있습니다. 예를 들어, 금융 분야와 같이 실수가 큰 영역에서는 더욱 철저한 검증이 필요할 것이며, 반면에 작은 규모의 프로젝트에서는 단순한 happy path 검증만으로도 충분할 수 있습니다.

따라서 e2e 테스트를 작성할 때는 프로젝트의 요구사항과 상황에 맞게 테스트의 범위와 깊이를 조정하고, 테스트의 효율성과 유지보수성을 고려하여 검증할 기능을 선별하는 것이 중요합니다.


<br>

### Q. storybook, react-testing-library, cypress를 nextjs application에서 모두 사용해 어플리케이션을 테스트할 생각입니다. 되도록이면 각 테스트 도구들을 가장 적합한 상황에서만 사용하고 싶습니다. "이런 상황에서는 이런 테스트를 한다" 라는 정책을 세우고자 할 때 어떤 기준으로 세우면 좋을지 조언해주세요. 또는 일반적인 사례가 있다면 알려주세요 

각각의 테스트 도구들은 각각의 특징과 장단점이 있으므로, 어떤 상황에서 어떤 도구를 사용하는 것이 적합한지 결정하기 위해서는 각 도구의 목적과 특징을 잘 이해해야 합니다. 다음은 각 도구의 특징과 적합한 상황에 대한 일반적인 가이드라인입니다.

**Storybook**

**목적**: UI 컴포넌트의 개발과 문서화

**적합한 상황**: 개발 초기 단계에서 UI 컴포넌트를 개발하고 문서화하는데 사용합니다. 개발자들은 Storybook을 통해 개발한 UI 컴포넌트들을 쉽게 확인하고, 재사용할 수 있습니다.

**React Testing Library**

**목적**: 단위 테스트, 컴포넌트 동작 검증

적합한 상황: 단위 테스트를 위해 사용합니다. 컴포넌트의 동작을 검증하고, 프로젝트의 로직이 예상대로 동작하는지 확인합니다.

**Cypress**

**목적**: E2E (End-to-End) 테스트, 사용자 시나리오 검증

**적합한 상황**: 시스템 전체적인 동작을 검증하기 위해 사용합니다. 사용자 시나리오에 맞게 웹 애플리케이션의 동작을 검증합니다.
일반적으로는 이러한 가이드라인에 따라, 개발 초기 단계에서는 Storybook을 사용해 UI 컴포넌트를 개발하고 문서화하며, 개발 중단계에서는 React Testing Library를 사용해 컴포넌트의 동작을 검증하고, 마지막으로 Cypress를 사용해 E2E 테스트를 수행하는 것이 일반적입니다. 하지만 이는 단순한 일반적인 가이드일 뿐이므로, 실제 프로젝트에서는 테스트 도구를 사용하는 목적과 상황에 맞게 적절히 선택하고 사용하는 것이 중요합니다.

<br>

### Q. e2e 테스트를 진행할 때 서버의 data를 fetching해야하는 상황이 필수적일 것입니다. 이 때 production server의 api를 사용해서 테스트하는 것이 맞는지, mocking 된 api를 사용하는 것이 맞는지, 아니면 develop server를 추가로 구성해 해당 서버의 api를 사용하는 것이 맞는지 궁금합니다. production server의 api를 사용하면 실제 서비스 환경에 영향을 줄 것 같고 mock api를 사용하면 실제 사용자 환경과 차이가 있을 수 있다고 생각하는데 어떤 방법이 좋을까요?

e2e 테스트는 실제로 사용자가 앱을 사용하는 것처럼 테스트를 수행하기 때문에, 앱에서 실제로 사용되는 API와 동일한 환경에서 테스트하는 것이 가장 좋습니다. 따라서, develop server를 추가로 구성하여 해당 서버의 API를 사용하는 것이 가장 좋은 방법 중 하나입니다.

Mock API를 사용하면 테스트에 사용되는 데이터가 실제 서비스와 다를 수 있기 때문에, 잠재적인 문제를 발견할 수 없을 수 있습니다. 실제 API를 사용하면 테스트의 신뢰성을 높일 수 있지만, 테스트가 실제 서비스에 영향을 줄 수 있기 때문에 이 점을 고려해야 합니다.

실제 API를 사용할 경우, 테스트를 수행할 때마다 데이터가 변경될 수 있으므로, 테스트 데이터에 대한 관리도 중요합니다. 테스트 데이터베이스를 별도로 구성하거나 테스트 데이터를 초기화하는 스크립트를 작성하여 테스트 데이터에 대한 일관성을 유지하는 것이 좋습니다.

따라서, 가능하다면 develop server를 구성하여 해당 서버의 API를 사용하고, 테스트 데이터에 대한 관리를 철저히 하여 테스트를 수행하는 것이 좋습니다.