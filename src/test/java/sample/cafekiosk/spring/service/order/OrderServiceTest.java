package sample.cafekiosk.spring.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.controller.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test") // note: 테스트할때는 application.yml의 test profile로 실행된다.
@Transactional
@SpringBootTest
class OrderServiceTest {
  @Autowired
  private OrderService orderService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderProductRepository orderProductRepository;

//  @AfterEach
//  void tearDown() {
//    orderProductRepository.deleteAll();
//    orderRepository.deleteAll();
//    productRepository.deleteAll();
    // note: deleteAll보다는 deleteAllInBatch를 사용한 이유
    // note:   deleteAll은 레코드를 조회하고 1차캐시에 넣어두고 한 레코드씩 삭제하는 방식이다. 매우 비효율적이다.
    // note:   반면에 deleteAllInBatch는 바로 delete문만 생성하고 바로 레코드를 삭제하는 방식이다.

//    orderProductRepository.deleteAllInBatch();
//    orderRepository.deleteAllInBatch();
//    productRepository.deleteAllInBatch();
//  }

  @DisplayName("주문 번호를 입력하면 주문이 생성됩니다.")
  @Test
  void createOrder() {
    // Given
    Product product1 = createProduct("001", HANDMADE, 1000);
    Product product2 = createProduct("002", HANDMADE, 3000);
    Product product3 = createProduct("003", HANDMADE, 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();

    LocalDateTime registeredDateTime = LocalDateTime.now();
    // When
    OrderCreateResponse orderCreateResponse = orderService.createOrder(orderCreateRequest, registeredDateTime);

    // Then
    assertThat(orderCreateResponse.getId()).isNotNull();
    assertThat(orderCreateResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 4000);
    assertThat(orderCreateResponse.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                    tuple("001", 1000),
                    tuple("002", 3000)
            );
  }

  @DisplayName("중복된 상품 번호로 주문하면 주문 리스트가 잘 만들어집니다.")
  @Test
  void createOrderWithDuplicatedProductNumbers() {
    // Given
    Product product1 = createProduct("001", HANDMADE, 1000);
    Product product2 = createProduct("002", HANDMADE, 3000);
    Product product3 = createProduct("003", HANDMADE, 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001"))
            .build();

    LocalDateTime registeredDateTime = LocalDateTime.now();
    // When
    OrderCreateResponse orderCreateResponse = orderService.createOrder(orderCreateRequest, registeredDateTime);

    // Then
    assertThat(orderCreateResponse.getId()).isNotNull();
    assertThat(orderCreateResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 2000);
    assertThat(orderCreateResponse.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                    tuple("001", 1000),
                    tuple("001", 1000)
            );
  }

  private Product createProduct(String productNumber, ProductType type, int price) { // 테스트 코드를 문서처럼 읽기 쉽도록 리펙토링을 진행했다.
    return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .sellingStatus(SELLING)
            .price(price)
            .name("음료")
            .build();
  }

}