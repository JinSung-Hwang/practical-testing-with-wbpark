# practical-testing-with-wbpark

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