package sample.cafekiosk.spring.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.controller.order.response.CreateOrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test") // note: 테스트할때는 application.yml의 test profile로 실행된다.
@SpringBootTest
class OrderServiceTest {
  @Autowired
  private OrderService orderService;

  @Autowired
  private ProductRepository productRepository;


  @DisplayName("주문 번호를 입력하면 주문이 생성됩니다.")
  @Test
  void createOrder() {
    // Given
    Product product1 = createProduct("001", SELLING, 1000);
    Product product2 = createProduct("002", HOLD, 3000);
    Product product3 = createProduct("003", STOP_SELLING, 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();

    LocalDateTime registeredDateTime = LocalDateTime.now();
    // When
    CreateOrderResponse createOrderResponse = orderService.createOrder(createOrderRequest, registeredDateTime);

    // Then
    assertThat(createOrderResponse.getId()).isNotNull();
    assertThat(createOrderResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 4000);
    assertThat(createOrderResponse.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                    tuple("001", 1000),
                    tuple("002", 3000)
            );
  }

  private Product createProduct(String productNumber, ProductSellingStatus sellingStatus, int price) { // 테스트 코드를 문서처럼 읽기 쉽도록 리펙토링을 진행했다.
    return Product.builder()
            .type(HANDMADE)
            .productNumber(productNumber)
            .sellingStatus(sellingStatus)
            .price(price)
            .name("음료")
            .build();
  }

}