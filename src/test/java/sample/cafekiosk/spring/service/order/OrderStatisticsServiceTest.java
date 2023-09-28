package sample.cafekiosk.spring.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.service.mail.MailService;

@ActiveProfiles("test")
@SpringBootTest
class OrderStatisticsServiceTest {
  // note: SpringBootTest를 하면 Spring Bean을 객체를 모두 등록해서 테스트 하기떄문에 테스트 코드에서 각 객체들을 주입할 필요는 없다.
  // note: 다만 밑에서 @Autowired를 하는것은 아래 테스트 코드에서 사용할 객체들을 주입해서 사용한것이다.
  // note:   예를 들어 MailService는 테스트 코드에서 사용하지 않기때문에 @Autowired로 주입하지 않았다.
  // note:   nestjs에서는 테스트할때 beforeEach를 만들고 createTestingModule 모듈에 테스트용 mock객체들을 넣어줘야 했는데 그럴필요가 없다.!!

  @Autowired
  private OrderStatisticsService orderStatisticsService;

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private MailSendHistoryRepository mailSendHistoryRepository;


  @MockBean // note: mailSendClient를 mock 객체로 만들고 mailSendClient대신 주입한다.
  private MailSendClient mailSendClient;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @DisplayName("결제 완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
  @Test
  void sendOrderStatisticsMail() {
    // Given
    LocalDateTime now = LocalDateTime.of(2023, 2, 2, 0, 0);
    String receiverMail = "receiver@receive.com";

    Product product1 = createProduct("001", HANDMADE, 1000, "아메리카노", SELLING);
    Product product2 = createProduct("002", HANDMADE, 2000, "카페라떼", SELLING);
    Product product3 = createProduct("003", HANDMADE, 3000, "팥빙수", SELLING);
    productRepository.saveAll(List.of(product1, product2, product3));

    Order order1 = createOrder(List.of(product1, product2, product3), OrderStatus.PAYMENT_COMPLETED, LocalDateTime.of(2023, 2, 1, 23, 59, 59));
    Order order2 = createOrder(List.of(product1, product2, product3), OrderStatus.PAYMENT_COMPLETED, now);
    Order order3 = createOrder(List.of(product1, product2, product3), OrderStatus.PAYMENT_COMPLETED, LocalDateTime.of(2023, 2, 2, 23, 59, 59));
    Order order4 = createOrder(List.of(product1, product2, product3), OrderStatus.PAYMENT_COMPLETED, LocalDateTime.of(2023, 2, 3, 0, 0, 0));
    orderRepository.saveAll(List.of(order1, order2, order3, order4));

    Mockito.when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class))) // note: 메일 클라이언트를 stubbing을 진행했다.
        .thenReturn(true);

    // When
    boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 2, 2), receiverMail);

    List<MailSendHistory> mailSendHistories = mailSendHistoryRepository.findAll();

    // Then
    assertThat(result).isTrue();
    assertThat(mailSendHistories).hasSize(1)
        .extracting("contents")
        .contains("매출 정산 총 금액: 12000");
  }

  private Order createOrder(List<Product> products, OrderStatus status, LocalDateTime now) {
    return Order.builder()
        .products(products)
        .status(status)
        .registeredDateTime(now)
        .build();
  }

  private Product createProduct(String productNumber, ProductType type, int price, String name, ProductSellingStatus sellingStatus) {
    return Product.builder()
        .type(type)
        .productNumber(productNumber)
        .sellingStatus(sellingStatus)
        .price(price)
        .name(name)
        .build();
  }
}