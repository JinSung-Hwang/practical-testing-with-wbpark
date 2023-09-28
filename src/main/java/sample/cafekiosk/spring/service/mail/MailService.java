package sample.cafekiosk.spring.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;

@RequiredArgsConstructor
@Service
public class MailService { // note: 메일 전송의 전처리, 후처리 하는 책임이 있는 service이다.
  private final MailSendClient mailClient;
  private final MailSendHistoryRepository mailSendHistoryRepository;

  public boolean sendMail(String senderMail, String receiverMail, String title, String contents) {
    boolean isSend = mailClient.sendMail(senderMail, receiverMail, title, contents);
    if (isSend) {
      mailSendHistoryRepository.save(
          MailSendHistory
              .builder()
              .senderMail(senderMail)
              .receiverMail(receiverMail)
              .title(title)
              .contents(contents)
              .build()
      );
      return true;
    }
    return false;
  }
}
