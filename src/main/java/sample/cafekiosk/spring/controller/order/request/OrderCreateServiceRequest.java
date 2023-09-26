package sample.cafekiosk.spring.controller.order.request;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {
  @NotEmpty(message = "상품 번호는 필수입니다.")
  private List<String> productNumbers;

  @Builder
  private OrderCreateServiceRequest(List<String> productNumbers) {
    this.productNumbers = productNumbers;
  }

  public OrderCreateServiceRequest toServiceRequest() {
    return OrderCreateServiceRequest.builder()
        .productNumbers(productNumbers)
        .build();
  }
}
