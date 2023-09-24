package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;


@ActiveProfiles("test")
class OrderTest {

  @DisplayName("주문 생성시 주문의 총 가격을 계산한다.")
  @Test
  void calculateTotalPrice() {
    // Given
    List<Product> products = List.of(
            createProduct("001", 1000),
            createProduct("002", 3000)
    );

    // When
    Order order = Order.create(products, LocalDateTime.now());

    // Then
    assertThat(order).isNotNull();
    assertThat(order.getTotalPrice()).isEqualTo(4000);
  }

  @DisplayName("주문 생성시 주문의 상태는 INIT이다.")
  @Test
  void OrderStatusIsInit() {
    // Given
    List<Product> products = List.of(
            createProduct("001", 1000),
            createProduct("002", 3000)
    );

    // When
    Order order = Order.create(products, LocalDateTime.now());

    // Then
    assertThat(order.getStatus()).isEqualByComparingTo(OrderStatus.INIT);
    // note: isEqualByComparingTo이거는 이넘값 자체를 비교해준다. isEqualTo를 써도 무방하긴하다. 하지만 풍부한 표현력을 위해서 이렇게도 사용한다.
  }

  @DisplayName("주문 생성시 등록 시간을 기록한다.")
  @Test
  void registeredDateTime() {
    // Given
    List<Product> products = List.of(
            createProduct("001", 1000),
            createProduct("002", 3000)
    );

    LocalDateTime registeredDateTime = LocalDateTime.now();

    // When
    Order order = Order.create(products, registeredDateTime);

    // Then
    assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    // note: isEqualByComparingTo이거는 이넘값 자체를 비교해준다. isEqualTo를 써도 무방하긴하다. 하지만 풍부한 표현력을 위해서 이렇게도 사용한다.
  }


  private Product createProduct(String productNumber, int price) { // 테스트 코드를 문서처럼 읽기 쉽도록 리펙토링을 진행했다.
    return Product.builder()
            .type(HANDMADE)
            .productNumber(productNumber)
            .sellingStatus(SELLING)
            .price(price)
            .name("음료")
            .build();
  }

}