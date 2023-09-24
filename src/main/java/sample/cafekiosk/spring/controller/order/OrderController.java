package sample.cafekiosk.spring.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.controller.order.response.CreateOrderResponse;
import sample.cafekiosk.spring.service.order.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/api/v1/orders/new")
  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
    LocalDateTime registeredDateTime = LocalDateTime.now();
    return orderService.createOrder(createOrderRequest, registeredDateTime);
  }
}
