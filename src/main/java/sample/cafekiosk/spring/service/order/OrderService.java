package sample.cafekiosk.spring.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.controller.order.response.CreateOrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.service.product.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest, LocalDateTime registeredDateTime) {
    List<String> productNumbers = createOrderRequest.getProductNumbers();

    List<Product> products = productRepository.findAllProductByProductNumberIn(productNumbers);

    Order savedOrder = orderRepository.save(Order.create(products, registeredDateTime));

    return CreateOrderResponse.of(savedOrder);
  }



}
