package sample.cafekiosk.spring.controller.order.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {
  private List<String> productNumbers;

  @Builder
  private CreateOrderRequest(List<String> productNumbers) {
    this.productNumbers = productNumbers;
  }
}
