package sample.cafekiosk.spring.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.service.product.response.ProductResponse;
import sample.cafekiosk.spring.service.product.request.ProductCreateServiceRequest;

@ActiveProfiles("test")
@SpringBootTest
// note: SpringBootTest를 하면 Spring Bean을 객체를 모두 등록해서 테스트 하기떄문에 테스트 코드에서 각 객체들을 주입할 필요는 없다.
// note: 다만 밑에서 @Autowired를 하는것은 아래 테스트 코드에서 사용할 객체들을 주입해서 사용한것이다.
// note:   예를 들어 MailService는 테스트 코드에서 사용하지 않기때문에 @Autowired로 주입하지 않았다.
// note:   nestjs에서는 테스트할때 beforeEach를 만들고 createTestingModule 모듈에 테스트용 mock객체들을 넣어줘야 했는데 그럴필요가 없다.!!
// note: 하지만 SpringBean을 모두 등록해야하기때문에 느리다는 단점이있다.
// note:   그래서 경우에 따라서 @@WebMvcTest, @DataJpaTest(repository용)를 사용한다.
class ProductServiceTest {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  @AfterEach()
  void tearDown() {
    productRepository.deleteAllInBatch();
  }

  @DisplayName("신규 상품을 추가한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
  @Test
  void createProduct() {
    // Given
    Product product = createProduct("001", HANDMADE, 7000, "팥빙수", SELLING);
    productRepository.saveAll(List.of(product));

    ProductCreateServiceRequest productCreateServiceRequest = ProductCreateServiceRequest
        .builder()
        .sellingStatus(SELLING)
        .type(HANDMADE)
        .name("자바 푸라푸치노")
        .price(10000)
        .build();

    // When
    ProductResponse productResponse = productService.createProduct(productCreateServiceRequest);

    // Then
    // note: 서비스 테스트는 응답과 DB에 데이터가 잘 들어갔는지도 검증했다. service를 통합 테스트형식으로 작성했기 때문이다.
    assertThat(productResponse).isNotNull()
        .extracting("productNumber", "sellingStatus", "type", "name", "price")
        .contains("002", SELLING, HANDMADE, "자바 푸라푸치노", 10000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(2)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "팥빙수", 7000),
            tuple("002", HANDMADE, SELLING, "자바 푸라푸치노", 10000)
        );
  }

  @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
  @Test
  void createProductWhenProductsIsEmpty() {
    // Given
    ProductCreateServiceRequest productCreateServiceRequest = ProductCreateServiceRequest
        .builder()
        .sellingStatus(SELLING)
        .type(HANDMADE)
        .name("자바 푸라푸치노")
        .price(10000)
        .build();

    // When
    ProductResponse productResponse = productService.createProduct(productCreateServiceRequest);

    // Then
    // note: 서비스 테스트는 응답과 DB에 데이터가 잘 들어갔는지도 검증했다. service를 통합 테스트형식으로 작성했기 때문이다.
    assertThat(productResponse).isNotNull()
        .extracting("productNumber", "sellingStatus", "type", "name", "price")
        .contains("001", SELLING, HANDMADE, "자바 푸라푸치노", 10000);

    List<Product> products = productRepository.findAll();
    assertThat(products).hasSize(1);
    assertThat(products.get(0))
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .contains("001", HANDMADE, SELLING, "자바 푸라푸치노", 10000);

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