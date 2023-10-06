package sample.cafekiosk.spring.domain.product;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;


class ProductTypeTest {
  
  @DisplayName("HANDMADE 상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType() {
    // Given
    ProductType givenType = ProductType.HANDMADE;

    // When
    boolean result = ProductType.containsStockType(givenType);

    // Then
    assertThat(result).isFalse();
  }

  @DisplayName("BAKERY 상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType2() {
    // Given
    ProductType givenType = ProductType.BAKERY;

    // When
    boolean result = ProductType.containsStockType(givenType);

    // Then
    assertThat(result).isTrue();
  }

  // note: 아래는 안좋은 테스트 예시
  // note:   하나의 테스트에 여러개를 테스트하고 읽는 사람에게 생각을 필요하게 만든다.
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockType3() {
    // Given
    ProductType givenType1 = ProductType.HANDMADE;
    ProductType givenType2 = ProductType.BOTTLE;
    ProductType givenType3 = ProductType.BAKERY;

    // When
    boolean result1 = ProductType.containsStockType(givenType1);
    boolean result2 = ProductType.containsStockType(givenType2);
    boolean result3 = ProductType.containsStockType(givenType3);

    // Then
    assertThat(result1).isFalse();
    assertThat(result2).isTrue();
    assertThat(result3).isTrue();
  }

  // note: 참고 사이트: https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
  // note: 아래 처럼 @ParameterizedTest가 아니라 spock이라는 것을 통해 더 가독성을 높일 수도 있다. https://spockframework.org/spock/docs/1.0/spock_primer.html
  // note: containsStockType3을 보기 좋게 바꾼 예 1
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @CsvSource({"HANDMADE,false", "BOTTLE,true", "BAKERY,true"})
  @ParameterizedTest
  void containsStockType4(ProductType productType, boolean expected) {
    // When
    boolean result = ProductType.containsStockType(productType);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  // note: containsStockType3을 보기 좋게 바꾼 예 2
  private static Stream<Arguments> provideProductTypesForCheckingStockType() {
    return Stream.of(
        Arguments.of(ProductType.HANDMADE, false),
        Arguments.of(ProductType.BOTTLE, true),
        Arguments.of(ProductType.BAKERY, true)
    );
  }
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @MethodSource("provideProductTypesForCheckingStockType")
  @ParameterizedTest
  void containsStockType5(ProductType productType, boolean expected) {
    // When
    boolean result = ProductType.containsStockType(productType);

    // Then
    assertThat(result).isEqualTo(expected);
  }
}