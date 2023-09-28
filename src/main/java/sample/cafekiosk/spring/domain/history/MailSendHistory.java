package sample.cafekiosk.spring.domain.history;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MailSendHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String senderMail;
  private String receiverMail;
  private String title;
  private String contents;

  @Builder
  public MailSendHistory(String senderMail, String receiverMail, String title, String contents) {
    this.senderMail = senderMail;
    this.receiverMail = receiverMail;
    this.title = title;
    this.contents = contents;
  }
}
