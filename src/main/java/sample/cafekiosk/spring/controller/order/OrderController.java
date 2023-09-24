package sample.cafekiosk.spring.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.controller.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.service.order.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/api/v1/orders/new")
  public OrderCreateResponse createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
    LocalDateTime registeredDateTime = LocalDateTime.now();
    return orderService.createOrder(orderCreateRequest, registeredDateTime);
  }
}
