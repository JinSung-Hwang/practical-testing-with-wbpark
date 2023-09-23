package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA를 통해 데이터베이스와 상호작용하는 동안 엔터티의 생성 및 수정 시간을 자동으로 기록 할 수 있음, 이것은 @EntityListeners이거와 같이 사용함
@SpringBootApplication
public class CafekioskApplication {

  public static void main(String[] args) {
    SpringApplication.run(CafekioskApplication.class, args);
  }

}
