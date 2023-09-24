package sample.cafekiosk.spring.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.controller.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
    List<String> productNumbers = orderCreateRequest.getProductNumbers();

    List<Product> products = findProducts(productNumbers);
    Order savedOrder = orderRepository.save(Order.create(products, registeredDateTime));

    return OrderCreateResponse.of(savedOrder);
  }

  private List<Product> findProducts(List<String> productNumbers) {
    List<Product> products = productRepository.findAllProductByProductNumberIn(productNumbers);
    Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, p -> p));

    return productNumbers.stream()
            .map(productMap::get)
            .collect(Collectors.toList());
  }


}
