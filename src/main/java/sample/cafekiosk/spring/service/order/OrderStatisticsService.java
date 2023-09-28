package sample.cafekiosk.spring.service.order;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

//@Transactional 메일 서비스와 같은 외부 API 호출하는 서비스에는 transaction을 안쓰는것이 좋다.
@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

  private final OrderRepository orderRepository;
  private final MailService mailService;

  public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
    // 해당 일자에 결제 완료된 주문들을 가져와서
    List<Order> orders = orderRepository.findOrdersBy(
        orderDate.atStartOfDay(),
        orderDate.plusDays(1).atStartOfDay(),
        OrderStatus.PAYMENT_COMPLETED
    );

    // 총 매출 합계를 계산한다. // 주문들의 총 합들의 총합
    int totalPrice = orders.stream()
        .mapToInt(Order::getTotalPrice)
        .sum();

    // 매일을 전송한다.
    boolean result = mailService.sendMail(
        "devjinsung@cafekiosk.com",
        email,
        String.format("[매출 정산] %s", orderDate.atStartOfDay()),
        String.format("매출 정산 총 금액: %s", totalPrice)
    );

    if (!result) {
      throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
    }

    return true;
  }

}
