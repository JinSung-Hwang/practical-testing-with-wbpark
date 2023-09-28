package sample.cafekiosk.spring.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

  public boolean sendMail(String senderMail, String receiverMail, String title, String contents) {
    throw new IllegalArgumentException("메일 전송에 실패했습니다.");
  }

}
