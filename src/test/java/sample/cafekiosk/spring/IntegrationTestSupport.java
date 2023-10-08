package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.MailSendClient;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
  // note: 테스트 클래스에서 @SpringBootTest 가 있으면 매번 springBoot가 띄워진다.
  // note: 이렇게 SpringBoot가 매번 띄워지면 테스트 수행시간이 길어져서 비용(테스트 수행 비용)이 늘어난다.
  // note: 하여 IntegrationTestSupport와 같이 테스트 통합 추상 클래스를 만들고 각 테스트 클래스에서 IntegrationTestSupport 클래스를 상속받으면 SpringBoot가 여러번 띄워지지 않는다.


  // note: 11 ~ 13 라인과 다르게 @MockBean이 있으면 SpringBoot가 새로 띄워진다.
  // note: 그 이유는 @MockBean으로 되면 객체가 Mock으로 대신 주입되어야하는데 환경이 달라지니 SpringBoot를 새로 띄워서 테스트하는 것이다.
  // note: 하여 해결하는 방법은 각 테스트 클래스에있는 @MockBean을 아래 코드 처럼 IntegrationTestSupport 로 올려서 공통적으로 MockBean처리하는 것이다.
  // note: 하지만 이렇게 하면 어떤 테스트에서는 @MockBean을 사용하고 싶지 않을 수 있다.
  // note: 그래서 IntegrationTestSupport  클래스를 2가지 타입으로 나누어서 @MockBean이 있는것과 없는것으로 나누는 방법이 있다.
  @MockBean
  protected MailSendClient mailSendClient;  // note: 하위 클래스에서 사용하려면 protected로 선언해야한다. 꼼꼼히 확인하자
}
