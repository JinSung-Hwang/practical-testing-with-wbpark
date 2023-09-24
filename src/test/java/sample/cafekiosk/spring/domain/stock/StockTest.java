package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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


}