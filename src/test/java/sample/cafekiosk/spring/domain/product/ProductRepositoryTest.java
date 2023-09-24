package sample.cafekiosk.spring.domain.product;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test") // note: 테스트할때는 application.yml의 test profile로 실행된다.
//@SpringBootTest
@DataJpaTest // note: SpringBootTest와 마찬가지로 SpringBoot를 띄워서 테스트를 진행하지만 JPA관련된 것만 띄워서 테스트를 진행한다.
             // note: 또한 DataJpaTest는 내부에 @Transactional이 있다. 따라서 하나의 테스트가 끝나고 rollback을 수행한다.
class ProductRepositoryTest {
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
    Product product1 = Product.builder()
            .productNumber("001")
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
    Product product2 = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("카페라뗴")
            .price(4500)
            .build();
    Product product3 = Product.builder()
            .productNumber("003")
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("밭핑수")
            .price(7000)
            .build();
    productRepository.saveAll(List.of(product1, product2, product3));

    // When
    List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

    // Then
    assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
              tuple("001", "아메리카노", SELLING),
              tuple("002", "카페라뗴", HOLD)
            );
  }

  @DisplayName("상품 번호로 상품들을 조회한다.")
  @Test
  void findAllProductByProductNumberIn() {
    // Given
    Product product1 = Product.builder()
            .productNumber("001")
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
    Product product2 = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("카페라뗴")
            .price(4500)
            .build();
    Product product3 = Product.builder()
            .productNumber("003")
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("밭핑수")
            .price(7000)
            .build();
    productRepository.saveAll(List.of(product1, product2, product3));

    // When
    List<Product> products = productRepository.findAllProductByProductNumberIn(List.of("001", "002"));

    // Then
    assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                    tuple("001", "아메리카노", SELLING),
                    tuple("002", "카페라뗴", HOLD)
            );
  }
}