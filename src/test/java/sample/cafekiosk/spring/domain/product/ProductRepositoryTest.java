package sample.cafekiosk.spring.domain.product;


import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import sample.cafekiosk.spring.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@Transactional
//@ActiveProfiles("test") // note: 테스트할때는 application.yml의 test profile로 실행된다.
//@SpringBootTest
//@DataJpaTest // note: SpringBootTest와 마찬가지로 SpringBoot를 띄워서 테스트를 진행하지만 JPA관련된 것만 띄워서 테스트를 진행한다.
               // note: 또한 DataJpaTest는 내부에 @Transactional이 있다. 따라서 하나의 테스트가 끝나고 rollback을 수행한다.

               // note: 내용 추가 - 테스트 수행을 줄이기 위해서 @DataJpaTest를 주석처리하고 IntegrationTestSupport를 상속받았다. IntegrationTestSupport가 없어서 @Transactional을 붙였다.
class ProductRepositoryTest extends IntegrationTestSupport {
  // note: QueryMethod를 테스트
  // note:   사실 QueryMethod는 JPA에서 지원해주는 기능인데 왜 테스트 해야하는지 의문이 들 수 있다.
  // note:   하지만 아래 2가지 이유로 테스트를 진행한다.
  // note:     1. 간단하지만 인터페이스를 선언한것도 내가 만든 코드이며 만든 queryMethod가 잘 동작하는지 테스트가 필요하다.
  // note:     2. 만든 쿼리 메소드가 변형이 일어날수 있고 메소드명은 그대로지만 queryDsl, nativeQuery, mybatis로 기술이 변경되어 테스트가 필요할 수도 있다.
  
  @Autowired
  private ProductRepository productRepository;

  @DisplayName("원하는 판매상태를 가진 상품을 조회한다.")
  @Test
  void findAllBySellingStatusIn() {
    // Given
    Product product1 = createProduct("001", HANDMADE, 4000, "아메리카노", SELLING);
    Product product2 = createProduct("002", HANDMADE, 4500, "카페라떼", HOLD);
    Product product3 = createProduct("003", HANDMADE, 7000, "팥빙수", STOP_SELLING);
    productRepository.saveAll(List.of(product1, product2, product3));

    // When
    List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

    // Then
    assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
              tuple("001", "아메리카노", SELLING),
              tuple("002", "카페라떼", HOLD)
            );
  }

  @DisplayName("상품 번호로 상품들을 조회한다.")
  @Test
  void findAllProductByProductNumberIn() {
    // Given
    Product product1 = createProduct("001", HANDMADE, 4000, "아메리카노", SELLING);
    Product product2 = createProduct("002", HANDMADE, 4500, "카페라떼", HOLD);
    Product product3 = createProduct("003", HANDMADE, 7000, "팥빙수", STOP_SELLING);
    productRepository.saveAll(List.of(product1, product2, product3));

    // When
    List<Product> products = productRepository.findAllProductByProductNumberIn(List.of("001", "002"));

    // Then
    assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                    tuple("001", "아메리카노", SELLING),
                    tuple("002", "카페라떼", HOLD)
            );
  }

  @DisplayName("가장 마지막에 저장된 상품의 상품 번호를 조회한다.")
  @Test
  void findLatestProductNumber() {
    // Given
    Product product1 = createProduct("001", HANDMADE, 4000, "아메리카노", SELLING);
    Product product2 = createProduct("002", HANDMADE, 4500, "카페라떼", HOLD);
    Product product3 = createProduct("003", HANDMADE, 7000, "팥빙수", STOP_SELLING);
    productRepository.saveAll(List.of(product1, product2, product3));

    // When
    String productNumber = productRepository.findLatestProductNumber();

    // Then
    assertThat(productNumber).isEqualTo("003");
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


  @DisplayName("가장 마지막에 저장된 상품의 상품 번호를 조회할때, 상품이 하나도 없는 경우에는 null을 반환한다.")
  @Test
  void findLatestProductNumberWhenProductIsEmpty() {
    // When
    String productNumber = productRepository.findLatestProductNumber();

    // Then
    assertThat(productNumber).isNull();
  }


}