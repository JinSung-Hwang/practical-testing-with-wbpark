# Practical-Testing-With-WBPark

인프런 강의 박우빈님의 "실용적인 테스트 가이드" 강의를 수강하고 작성한 코드 저장소입니다.

## 테스트를 하는 이유 

1. **개발중에 빠른 에러에 대한 피드백을 받을 수 있다.(제일 중요)** </br>
   수동으로 테스트하면 웹사이트를 열어서 로그인하고 버튼을 클릭하며 테스트해야한다. </br> 여기에 서버 재시작하면 리로딩도 해야한다. </br>
   하지만 테스트 코드는 한번 작성해두면 반복적으로 즉각적인 피드백을 전달해주기때문에 빠른 피드백을 받을 수 있다. </br>
   또 모든 레이어, 모든 기능을 만들고 테스트를 시작하면 버그 찾기가 너무나도 어려워진다. </br>
   간단한 에러는 스택 트레이스에서 확인하여 바로 찾을 수 도 있겠지만 계층간에 얽혀있는 문제는 풀려면 에러의 원인 파악이 쉽지 않아서 삽질하면서 시간을 낭비 할 수도 있다.
2. 안전한 리펙토링을 진행할 수 있다. </br>
   리펙토링을 할때 코드를 변경하는 순간부터 이 코드가 예전의 요구사항을 모두 만족한다는 보장이 없어진다. </br>
   하지만 잘 만들어진 테스트 케이스가 있으면 언제든지 코드를 변경해도 테스트 케이스를 통과하는지 테스트 할 수 있어서 안전한 리펙토링이 가능하다. </br>
3. 사이드 이펙트가 나는것을 미리 알 수 있다. </br>
   개발을 하다보면 내가 만든 기능 혹은 수정한 부분 이외에 다른 부분이 내 코드로 인해서 버그가 생길 수 있다. </br>
   이런 부분은 정말 알아차리기 힘든 부분인데 이것을 테스트 코드로 전체 테스트를 진행하면 사이드 이펙트가 생기는 것을 미리 알 수 있다. </br>
4. 코드에 자신감과 안정감을 준다. 

## 1. 단위 테스트 

작은 코드의 단위를 독립적으로 검증하는 테스트이다. </br>
검증 속도가 빠르고, 안정적인것이 장점이다.

## 2. 경계값 테스트

해피 케이스 </br>
예외 케이스 

--> 경계값 테스트 (범위(미만, 초과), 구간, 날짜, 요일 등)

**테스트 코드 작성할때는 경계값 테스트를 진행해야하고 경계값을 기준으로 해피케이스, 예외케이스 2가지를 짜야한다.**

## 3. 테스트 하기 어려운 영역을 구분하고 분리하기

테스트 하기 어려운 코드는 외부로 분리할수록 테스트 가능한 코드가 많아진다. </br>
그렇다고 외부, 상위 계층으로 만 뺴는것이 능사는 아니다.

### 3-1. 테스트할때마다 테스트 하기 어려운 영역

1.  관측 할때마다 다른 값에 의존하는 코드 </br>
ex) 현재 날짜, 시간, 랜덤 값, 전역 변수, 함수, 사용자 입력

1.  외부 세계에 영향을 주는 코드 </br>
ex) 표준 출력, 메시지 발송, 데이터베이스에 기록하기

**즉, 순수함수가 테스트하기 쉬운 코드이다.**

## 4. Test Driven Development 

Red: 실패하는 테스트 코드 -> Green: 테스트를 통과하는 코드 -> Refactor: Green을 유지하면서 개선시키는 테스트 코드 (동치성 일반화)

### 선 테스트 후 기능 구현 

1. 복잡도가 낮은 테스트 가능한 코드로 구현할 수 있게 한다. 
1. 쉽게 발견하기 어려운 엣지 케이스를 놓치지 않게 해준다. 
1. 구현에 빠른 피드백을 받을 수 있다. 

### TTD를 사용하면 테스트 코드의 관점의 변화가 생긴다. 

TTD는 "테스트 코드는 구현부를 검증하지만 작성하기 귀찮은 코드"에서 "구현부와 상호작용하며 발전하는 테스트 코드"로 테스트 코드의 관점이 전환된다. 

## 5. 테스트는 문서이다.

### 테스트는 문서로서 가치가 있다. 

구현부의 코드의 사용 방법, 요구 사항 등을 설명해준다. 
때문에 테스트 코드는 잘 짜야하고 읽기 쉽도록 만들어야한다.

### DisplayName으로 이쁘게 만들자.

테스트 이름을 한글을 적어서 가독성을 높이자. </br>
또 명사의 나열보다는 자세한 문장으로 테스트에 설명을 쓰도록 하자.  </br>

Ex) </br>
--> 음료 1개 추가 테스트 (X) </br>
--> 음료 1개 추가하여 주문 목록에 담긴다. (O) </br>
첫번쨰 보다는 두번쨰가 문장으로 되어있으며 행위 뿐만 아니라 결과까지 기술하여 테스트 코드를 더 읽기 쉽게 만든다. 

Ex2) </br>
--> 특정 시간 이전에 주문을 생성하면 실패한다. (X) </br>
--> 영업 시작 시간 이전에는 주문을 생성할 수 없다. (O) </br>
메서드 보다는 도메인 용어를 사용해서 한층 추상화 된 내용을 담으면 좋다.  </br>
또한 '실패했다~' 라는 테스트 현상보다는 추상적으로 도메인적으로 무엇을 의도했는지 적어야 한다. </br>

### BDD 스타일로 테스트 코드를 작성하자

Given: 시나리오에 진행에 필요한 모든 준비 과정 (객체, 값, 상태 등등)
When: 시나리오 행동 진행
Then: 시나리오 진행에 대한 결과 명시, 검증

--------------


### 요구사항이 들어왔을때 반응

질문하기: 암묵적이거나 아직 드러나지 않는 요구사항이 있는가?


### 사용 라이브러리

- Junit5: https://junit.org/junit5/
- AssertJ: https://joel-costigliola.github.io/assertj/index.html

### 요구사항이 들어왔을때 반응

질문하기: 암묵적이거나 아직 드러나지 않는 요구사항이 있는가?

