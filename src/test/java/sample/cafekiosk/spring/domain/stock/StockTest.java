package sample.cafekiosk.spring.domain.stock;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {
  
  @DisplayName("재고의 수량이 주어진 수량보다 작은지 확인한다.")
  @Test
  void isQuantityLessThan() {
    // Given
    Stock stock = Stock.create("001", 1);
    int quantity = 2;

    // When
    boolean result = stock.isQuantityLessThan(quantity);

    // Then
    assertThat(result).isTrue();
  }

  @DisplayName("재고를 주어진 재고 만큼 차감 할 수 있다.")
  @Test
  void deductQuantity() {
    // Given
    Stock stock = Stock.create("001", 1);
    int quantity = 1;

    // When
    stock.deductQuantity(quantity);

    // Then
    assertThat(stock.getQuantity()).isZero();
  }

  @DisplayName("재고보다 많은 수량으로 차감 시도하는 경우 예외가 발생한다.")
  @Test
  void deductQuantity2() {
    // Given
    Stock stock = Stock.create("001", 1);
    int quantity = 2;

    // When Then
    assertThatThrownBy(() -> stock.deductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("차감할 재고 수량이 없습니다.");
  }

   // note: 테스트가 시나리오적으로 단계 단계 검증하면서 테스트하고 싶을때 DynamicTest를 진행할 수 있다.
  @DisplayName("")
  @TestFactory
  Collection<DynamicTest> dynamicTest() {
    return List.of( // note: 이터러블한 객체를 리턴하면 된다. list, stream 등등
        DynamicTest.dynamicTest("", () -> {

        }),
        DynamicTest.dynamicTest("", () -> {

        })
    );
  }

  //note: 다이나믹 테스트 예시
  @DisplayName("재고 차감 시나리오")
  @TestFactory
  Collection<DynamicTest> stockDeductionDynamicTest() {
    // given
    Stock stock = Stock.create("001", 1);

    return List.of(
        DynamicTest.dynamicTest("재고를 주어진 만큼 차감할 수 있다.", () -> {
          // given
          int quantity = 1;

          // when
          stock.deductQuantity(quantity);

          // then
          assertThat(stock.getQuantity()).isZero();
        }),
        DynamicTest.dynamicTest("재고보다 많은 재고를 차감하려고하면 예외가 발생한다.", () -> {
          // given
          int quantity = 1;

          // then
          assertThatThrownBy(() -> stock.deductQuantity(quantity))
              .isInstanceOf(IllegalArgumentException.class)
              .hasMessage("차감할 재고 수량이 없습니다.");
        })
    );
  }




}