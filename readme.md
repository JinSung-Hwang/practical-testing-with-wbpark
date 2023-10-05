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

## 6. 통합 테스트 

What: 여러 모듈이 협력하는 기능을 통합적으로 검증하는 테스트
Why: 일반적으로 작은 범위의 unit test만으로 기능 전체의 신뢰성을 보장할 순 없다. 
How: 풍부한 단위 테스트와 시나리오 기반의 큰 기능 단위를 검증하는 통합 테스트 구성으로 테스트 코드를 작성한다.

## 7. Presentaion Layer

1. 외부 세계의 요청을 가장 먼저 받는 계층이다.
2. 파라미터에 대한 최소한의 검증을 수행한다. 

### MockMvc

Mock(가짜)객체를 사용해 스프링 MVC 동작을 재현할 수 있는 테스트 프레임워크이다.

## 8. 좋은 테스트를 작성하기 위한 지키면 좋은 원칙들

1. 한 문단에는 하나의 테스트만 작성하는것이 가독성이 좋다. </br>
   (@displayName에 한 문장으로 작성할 수 있는지 확인하면 한 문단에 하나의 테스트만 작성했는지 알기 쉽다.)
1. 테스트 안에서 논리적 구조(if, for, 이미 존재하는 다른 API를 호출, 팩토리 메서드)가 있지 않고 생성자 또는 Builder 기반 코드로 테스트를 진행해야 코드 읽기가 쉽다.
1. 테스트는 완벽하게 제어가능해야한다. </br>
   (메서드에 제어할 수 없는 시간 변수는 파라미터로 받아 테스트 가능하도록, 외부 시스템 연동은 Mock을 통해 테스트)
1. 현재 시간과 같은 제어하기 힘든 값은 외부로(파라미터) 받는다. </br>
   (이 원칙을 팀 컨벤션두고 코딩해두면 요구사항이 변해 코드가 변할떄 테스트하는 코드로 만들기 쉽다. 테스트하기 어려운 코드들이 비지니스 로직에 이미 붙어있다면 리펙토링을 여러곳을 해야할지도 모른다.)
1. 테스트의 독립성을 보장해야한다. </br>(테스트끼리 영향을 주거나 받으면 안된다. 테스트간에 공유자원을 사용하면 안된다.)

## 9. TextFixture 잘 작성하기 위한 4가지 조언

Fixture: 고정물, 고정되어 있는 물체
TestFixture 란: 테스트를 위해 원하는 상태로 고정시킨 일련의 객체, 쉽게 이야기해서 BDD 스타일로 작성할때 Given절에 생성되는 객체들

1. BeforeAll, BeforeEach에서 공통적인 TestFixture를 생성하지 말자. </br>
      --> 이유: 테스트를 문서라고 생각했을때 BeforeAll에서 TestFixture를 생성하면 스크롤하면서 읽어야하기때문에 가독성도 떨어지고 객체를 바꾸려고하면 여러 객체에 영향이 미친다. </br>
   그러면 BeforeEach를 언제 사용하는가? 아래 질문을 스스로 해보기 </br>
      --> 확인 질문: 각 테스트 입장에서 봤을때 아예 몰라도 테스트 내용을 이해하는데 문제가 없는가? 라는 질문을 해보기 </br>
      --> 확인 질문: 수정해도 모든 테스트에 영향을 주지 않는가? 라는 질문을 해보기 </br>
1. data.sql을 통해서 초기화된 데이터 생성 스크립트를 사용하지말자
      --> 이유: 마찬가지로 Given이 파편화되어서 테스트 읽기가 어렵다. 테스트 관리 포인트가 늘어난다. 
1. Given절에 Builder를 함수로 뺄때는 변경이 일어나는 멤버 변수만 명확하게 파라미터로 받아서 어떤 값이 변경되는지 표현하자. 
1. Given절에 사용되는 Builder를 한 클래스로 모아두고 사용하지 말자. </br>
      --> 이유: 하나의 클래스로 Given을 모아두고 점점 커지다보면 파라미터마다 다른 형태의 객체가 생성되고 복잡해져서 관리하기가 힘들었다고한다. - wbpark

## 10. 데이터 클렌징 deleteAll과 deleteAllInBatch 차이점

#### deleteAll 작동 방식
- deleteAll은 findAll을 통해서 모든 레코드를 조회하여 1차캐시에 넣어두고 한 레코드씩 delete문을 호출하여 삭제하는 방식이다. 매우 비효율적이다.
- 다만 deleteAll은 연관관계가 맺어진 entity의 데이터도 같이 삭제 시켜준다. 단 연관관계 설정을 하지 않았다면 연관된 테이블의 데이터를 같이 지우지 않는다.

#### deleteAllInBatch 작동 방식
- deleteAllInBatch는 바로 delete문만 생성하고 바로 레코드를 삭제하는 방식이다. 효율적이다. 
- 연관된 entity데이터를 지우지 않는다. 따라서 데이터를 클렌징할때 데이터 클렌징 순서가 중요할 수 있다.
- 테스트 수행도 비용이니 효율적이고 빠른 deleteAllInBatch를 사용하길 추천한다. (그래도 상황에 따라서 잘 선택하는것이 좋다.)

--------------


### 요구사항이 들어왔을때 반응

질문하기: 암묵적이거나 아직 드러나지 않는 요구사항이 있는가?


### 사용 라이브러리

- Junit5: https://junit.org/junit5/
- AssertJ: https://joel-costigliola.github.io/assertj/index.html

### 세팅
<details><summary>상세 세팅 살펴보기</summary>

### Lombok 세팅

intellij build를 사용하려면 annotation processing를 활성화 시켜야 한다.

1. preference 열기
2. plugins 선택
3. market 탭 선택
4. lombok 설치
4. Apply 선택
5. OK 선택
1. 다시 preference 열기
2. annotation processors 검색
3. enable annotation processing 체크
4. Apply 선택
5. OK 선택

### Build 세팅
이것을 해야 build가 빨라지고 test에서 intellij에 @DisplayName이 표시된다.

1. preference
1. gradle 검색
1. build and running using: intellij idea 선택
1. run tests using: intellij idea 선택
1. OK 선택

### Encoding 세팅
1. preference 열기
2. file encodings 선택
   1. global encodings: utf-8 선택
   1. project encodings: utf-8 선택
   1. default encodings for properties files: utf-8 선택
   1. transparent native-to-ascii conversion 체크박스 체크표시
3. apply 클릭
4. ok 클릭

### Live Template 세팅
Edit창에 ttd 입력으로 템플릿 뜰 수 있도록 만들기

1. preference 열기
1. live template 검색
1. add 버튼 클릭
1. group template 선택
1. "custom" 입력
1. OK 클릭
1. add 버튼 클릭
1. live template 선택
1. "ttd" 입력
1. 아래 내용 입력
```java
@org.junit.jupiter.api.DisplayName("$displayName$")
@org.junit.jupiter.api.Test
void $methodName$() {
  // Given
  $end$
  // When

  // Then
}
```
11. 하단 부분에 change 클릭(어떤 확장자에서 live templates이 표시될것인지 설정해야함)
12. java 클릭
13. OK 버튼 클릭
</details>
