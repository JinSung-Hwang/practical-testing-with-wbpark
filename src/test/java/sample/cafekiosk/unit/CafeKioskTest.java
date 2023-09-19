package sample.cafekiosk.unit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beaverage.Americano;
import sample.cafekiosk.unit.beaverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

  @Test
  void add_manual() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());

    System.out.println("담긴 음료 수:" + cafeKiosk.getBeaverages().size()); // note: 이렇게 테스트하면 사람이 매번 확인해야하기때문에 자동화 테스트가 아니다. 테스트가 100, 1000개 넘어가면 사람이 일일이 확인 못한다.
    System.out.println("담긴 음료 이름:" + cafeKiosk.getBeaverages().get(0).getName()); // note: 당연히 단언을 사용해서 테스트해야한다.
  }

//  @DisplayName("음료 1개 추가 테스트")
  @DisplayName("음료 1개 추가하여 주문 목록에 담겨있는지 확인한다.") // note: 위 테스트 보다는 이 테스트가 보기 더 편하다
  @Test
  void add() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();

    // When
    cafeKiosk.add(new Americano());

    // Then
    assertThat(cafeKiosk.getBeaverages()).hasSize(1);
    assertThat(cafeKiosk.getBeaverages().get(0).getName()).isEqualTo("Americano");
  }

  @Test
  void addSeveralBeverages() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    // When
    cafeKiosk.add(americano, 2);

    // Then
    assertThat(cafeKiosk.getBeaverages().get(0)).isEqualTo(americano);
    assertThat(cafeKiosk.getBeaverages().get(1)).isEqualTo(americano);
  }

  @Test
  void addZeroBeverages() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    // When // Then
    assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("음료는 1잔 이상 주문 하실 수 있습니다.");
  }

  @Test
  void remove() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    cafeKiosk.add(americano);

    // When
    cafeKiosk.remove(americano);

    // Then
    assertThat(cafeKiosk.getBeaverages()).isEmpty();
  }
  
  @Test
  void clear() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());

    // When
    cafeKiosk.removeAll();

    // Then
    assertThat(cafeKiosk.getBeaverages()).isEmpty();
  }

  @Test
  void calculateTotalPrice() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    Latte latte = new Latte();

    cafeKiosk.add(americano);
    cafeKiosk.add(latte);

    assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(3500);
  }

  @Disabled
  @Test
  void createOrder() {
    // note: 이 테스트 코드는 테스트하는 시간에 따라서 성공할수도 실패할수도 있다.
    // note: 이 함수 안에서 현재시간을 계산해서 로직을 수행하기 때문이다.
    // note: 그래서 테스트하기 쉽도록 CafeKiosk클래스의 createOrder(LocalDateTime currentDateTime) 메소드를 만들었다.
    // note: 테스트하기 어려운 영역을 외부로(파라미터로) 뺴서 테스트하기 쉽도록 만들었다.
    // note: 이것은 실제 코드와 테스트코드가 조금은 달라 보일 순 있지만 사실 테스트하고 싶은것은 시간에 따라서 주문을 받느냐 안받느냐를 테스트하고 싶은것이다.
    // note: 그래서 현재시간을 파라미터로 받아도 테스트 코드를 작성하는데 무리가 없다.
    // note: 아래의 createOrderWithCurrentTime와 createOrderWithOutsideOpenTime의 테스트 코드를 살펴보자.

    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);
    // When
    Order order = cafeKiosk.createOrder();

    // Then
    assertThat(order.getBeaverages()).hasSize(1);
    assertThat(order.getBeaverages().get(0).getName()).isEqualTo("Americano");
  }

  @Test
  void createOrderWithCurrentTime() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);
    // When
    Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 9, 19, 10, 0));

    // Then
    assertThat(order.getBeaverages()).hasSize(1);
    assertThat(order.getBeaverages().get(0).getName()).isEqualTo("Americano");
  }

  @Test
  void createOrderWithOutsideOpenTime() {
    // Given
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);
    // When // Then
    assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 9, 19, 9, 59)))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하십시오.");
  }


}