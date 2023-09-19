package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beaverage.Beaverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {
  private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
  private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

  private List<Beaverage> beaverages = new ArrayList<>();

  public void add(Beaverage beaverage) {
    beaverages.add(beaverage);
  }

  // note: 새로운 요구사항 여러개의 음료를 추가할 수 있어야한다. ex)
  public void add(Beaverage beaverage, int count) {
    if (count < 1) {
      throw new IllegalArgumentException("음료는 1잔 이상 주문 하실 수 있습니다.");
    }
    for (int i = 0; i < count; i++) {
      beaverages.add(beaverage);
    }
  }


  public void remove(Beaverage beaverage) {
    beaverages.remove(beaverage);
  }

  public void removeAll() {
    beaverages.clear();
  }

  public int calculateTotalPrice() {
    int totalAmount = 0;
    for(Beaverage beaverage: beaverages) {
      totalAmount += beaverage.getPrice();
    }
    return totalAmount;
  }

  public Order createOrder() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalTime currentTime = currentDateTime.toLocalTime();
    if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
      throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하십시오.");
    }

    return new Order(LocalDateTime.now(), beaverages);
  }

  public Order createOrder(LocalDateTime currentDateTime) {
    LocalTime currentTime = currentDateTime.toLocalTime();
    if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
      throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하십시오.");
    }

    return new Order(LocalDateTime.now(), beaverages);
  }


}
