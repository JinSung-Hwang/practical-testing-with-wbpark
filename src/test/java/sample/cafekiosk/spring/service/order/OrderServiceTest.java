package sample.cafekiosk.spring.service.order;

import org.assertj.core.api.Assertions;
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
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test") // note: 테스트할때는 application.yml의 test profile로 실행된다.
//@Transactional
// note: JPA의 dirtyChecking은 트랜잭션 경계선 설정이 되어있어야한다.
// note: 테스트케이스에서 @Transactional을 사용했는데 Service에서 @Transactional을 설정 안했다면 Test는 통과하지만 배포된 코드가 제대로 동작하지 않을 수 있다.
// note: 이점을 잘 인지하고 test code에서 @Transactional을 사용해야한다.
// note: 만약 이런 경우를 안만들기 위해서는 아래 코드 처럼 @AfterEach와 deleteAllInBatch를 이용해서 테스트 마다 데이터를 지워야한다.
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
  @Autowired
  private StockRepository stockRepository;


  @AfterEach
  void tearDown() {
//    orderProductRepository.deleteAll();
//    orderRepository.deleteAll();
//    productRepository.deleteAll();
    // note: deleteAll보다는 deleteAllInBatch를 사용한 이유
    // note:   deleteAll은 레코드를 조회하고 1차캐시에 넣어두고 한 레코드씩 삭제하는 방식이다. 매우 비효율적이다.
    // note:   반면에 deleteAllInBatch는 바로 delete문만 생성하고 바로 레코드를 삭제하는 방식이다.

    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    stockRepository.deleteAllInBatch();
  }

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

  @DisplayName("재고와 관련된 상품이 포함되어 있는 주문 번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrderWithStock() {
    // Given
    Product product1 = createProduct("001", BOTTLE, 1000);
    Product product2 = createProduct("002", BAKERY, 3000);
    Product product3 = createProduct("003", HANDMADE, 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    Stock stock1 = Stock.create("001", 2);
    Stock stock2 = Stock.create("002", 2);
    stockRepository.saveAll(List.of(stock1, stock2));

    OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

    LocalDateTime registeredDateTime = LocalDateTime.now();
    // When
    OrderCreateResponse orderCreateResponse = orderService.createOrder(orderCreateRequest, registeredDateTime);

    // Then
    assertThat(orderCreateResponse.getId()).isNotNull();
    assertThat(orderCreateResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 10000);
    assertThat(orderCreateResponse.getProducts()).hasSize(4)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                    tuple("001", 1000),
                    tuple("001", 1000),
                    tuple("002", 3000),
                    tuple("003", 5000)
            );

    List<Stock> stocks = stockRepository.findAll();
    assertThat(stocks).hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                    tuple("001", 0),
                    tuple("002", 1)
            );
  }

  @DisplayName("재고가 부족한 상품으로 주문을 생성하려고 하는 경우 예외가 발생한다.")
  @Test
  void createOrderWithNoStock() {
    // Given
    Product product1 = createProduct("001", BOTTLE, 1000);
    Product product2 = createProduct("002", BAKERY, 3000);
    Product product3 = createProduct("003", HANDMADE, 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    Stock stock1 = Stock.create("001", 1);
    Stock stock2 = Stock.create("002", 1);
    stockRepository.saveAll(List.of(stock1, stock2));

    OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

    LocalDateTime registeredDateTime = LocalDateTime.now();

    // When // Then
    assertThatThrownBy(() -> orderService.createOrder(orderCreateRequest, registeredDateTime))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족한 상품이 있습니다.");
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