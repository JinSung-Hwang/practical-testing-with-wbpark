package sample.cafekiosk.spring.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.controller.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.service.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final StockRepository stockRepository;


  /**
   * 재고 감소 -> 동시성 고민
   * optimistic lock // pessimistic lock // ...
   */
  public OrderCreateResponse createOrder(OrderCreateServiceRequest orderCreateRequest, LocalDateTime registeredDateTime) {
    List<String> productNumbers = orderCreateRequest.getProductNumbers();

    List<Product> products = findProducts(productNumbers);

    deductStockQuantities(products);

    Order order = Order.create(products, registeredDateTime);
    Order savedOrder = orderRepository.save(order);
    return OrderCreateResponse.of(savedOrder);
  }

  private void deductStockQuantities(List<Product> products) {
    // 재고 상품 번호 조회
    List<String> stockProductNumbers = extractStockProductNumbers(products);
    // 상품 번호별 재고맵 생성
    Map<String, Stock> stockMap = makeStockMapBy(stockProductNumbers);
    // 상품 번호별 카운팅맵 생성
    Map<String, Long> productCountMap = makeProductCountMapBy(stockProductNumbers);

    // 재고 차감 로직 수행
    for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
      Stock stock = stockMap.get(stockProductNumber);
      int quantity = productCountMap.get(stockProductNumber).intValue();
      if (stock.isQuantityLessThan(quantity)) {
        throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
        // note: stock.deductQuantity에서 예외처리를 하지만 여기서 예외처리하는 이유는 예외 전환을 위해서 사용했다.
      }
      stock.deductQuantity(quantity);
    }
  }

  private List<String> extractStockProductNumbers(List<Product> products) {
    return products.stream()
            .filter(product -> ProductType.containsStockType(product.getType()))
            .map(Product::getProductNumber)
            .collect(Collectors.toList());
  }

  private Map<String, Stock> makeStockMapBy(List<String> stockProductNumbers) {
    List<Stock> stocks = stockRepository.findByProductNumberIn(stockProductNumbers);
    Map<String, Stock> stockMap = stocks.stream()
            .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    return stockMap;
  }

  private Map<String, Long> makeProductCountMapBy(List<String> stockProductNumbers) {
    Map<String, Long> productCountMap = stockProductNumbers.stream()
            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    return productCountMap;
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
