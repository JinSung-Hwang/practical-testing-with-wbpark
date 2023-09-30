package sample.cafekiosk.spring.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
  // note: MailService는 통합 테스트가 아니라 단위 테스트로 진행한다.
  // note: 하여 의존성 주입은 Mock을 통해서 진행한다.

  @DisplayName("메일 전송 테스트1")
  @Test
  void sendMail1() {
    // Given
    MailSendClient mailSendClient = mock(MailSendClient.class);
    MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);

    MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

    Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);
//    Mockito.when(mailSendHistoryRepository.save(any(MailSendHistory.class)))
//        .thenReturn(null);

    // When
    boolean result = mailService.sendMail(
        "",
        "",
        "",
        ""
    );

    // Then
    // note: 위에 31번 라인 주석으로 된 방식을 통해서 테스트 할 수도 있지만 아래 방식을 통해서 함수가 호출 되었는지 더 명시적으로 테스트 할 수 있다.
    assertThat(result).isTrue();
    Mockito.verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class));
  }

  @Mock
  MailSendClient mailSendClient;
//   @Spy
//   MailSendClient mailSendClient;
//   note: @Spy는 객체 전체를 Mock으로 만들지 않고 일부 함수만 Mock으로 만들때 사용한다.
//   note: 위 주석을 풀고 실행 시키면 MailSendClient에서 stubbing하지 않은 메서드가 실제 실행되면서 로그가 찍히는것을 확인할 수 있다.
  @Mock
  MailSendHistoryRepository mailSendHistoryRepository;
  @InjectMocks
  MailService mailService;
  @DisplayName("메일 전송 테스트2")
  @Test
  void sendMail2() {
    // note: 위에 sendMail을 테스트 코드를 @Mock과 @InjectMocks로 리펙토링한 버전이다.
    // note: 그리고 테스트 클래스 제일 위에 @ExtendWith(MockitoExtension.class)를 작성해서 Mockito를 사용해서 mock을 만들것이라고 명시해야 @Mock, @InjectMocks가 제대로 동작한다.

    // Given
//    Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);
    // note: BDD format에 테스트 코드를 작성하는데 Given 단락에 Mockito.when이라는 문법이 어색하다. 그래서 Mockito를 상속받은 BDDMockito라는 클래스는 BDD format으로 테스트 코드를 작성할 수 있게 해준다.
    BDDMockito.given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
        .willReturn(true);

    // note: @Spy를 사용하면 위에 Mockito.when 방식으로 stubbing할순 없고 doReturn형식으로 stubbing을 진행해야 된다.
//    doReturn(true)
//        .when(mailSendClient)
//        .sendMail(anyString(), anyString(), anyString(), anyString());

    // When
    boolean result = mailService.sendMail(
        "",
        "",
        "",
        ""
    );

    // Then
    assertThat(result).isTrue();
    Mockito.verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class));
  }
}