package sample.cafekiosk.spring.controller.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.controller.product.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCreateResponse {
  private Long id;
  private int totalPrice;
  private LocalDateTime registeredDateTime;
  private List<ProductResponse> products;

  @Builder
  private OrderCreateResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
    this.id = id;
    this.totalPrice = totalPrice;
    this.registeredDateTime = registeredDateTime;
    this.products = products;
  }

  public static OrderCreateResponse of(Order savedOrder) {
    return OrderCreateResponse.builder()
             .id(savedOrder.getId())
             .totalPrice(savedOrder.getTotalPrice())
             .registeredDateTime(savedOrder.getRegisteredDateTime())
             .products(
                     savedOrder.getOrderProducts()
                             .stream()
                             .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                             .collect(Collectors.toList())

             )
             .build();
  }
}
