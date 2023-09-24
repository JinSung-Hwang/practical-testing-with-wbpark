package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String productNumber;

  private int quantity;

  @Builder
  public Stock(String productNumber, int quantity) {
    this.productNumber = productNumber;
    this.quantity = quantity;
  }

  public static Stock create(String productNumber, int quantity) {
    return Stock
            .builder()
            .productNumber(productNumber)
            .quantity(quantity)
            .build();
  }

  public boolean isQuantityLessThan(int quantity) {
    return this.quantity < quantity;
  }

  public void deductQuantity(int quantity) {
    if (isQuantityLessThan(quantity)) {
      throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
    }
    // note: 서비스와 관련없이 이 도메인 자체로만으로 무결성이 보장되어야하기때문에 서비스에서 예외처리를 하더라도 이 메소드에서 예외처리를 해줘야한다.
    this.quantity -= quantity;
  }
}
