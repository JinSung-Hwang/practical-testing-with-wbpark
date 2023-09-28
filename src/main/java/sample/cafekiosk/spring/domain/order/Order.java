package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderProduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Orders")
@Entity
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private int totalPrice;

  private LocalDateTime registeredDateTime;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @Builder
  public Order(OrderStatus status, LocalDateTime registeredDateTime, List<Product> products) {
    this.status = status;
    this.totalPrice = calculateTotalPrice(products);
    this.registeredDateTime = registeredDateTime;
    this.orderProducts = products.stream()
        .map(product -> new OrderProduct(this, product))
        .collect(Collectors.toList());
  }

  public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
    return Order.builder()
        .status(OrderStatus.INIT)
        .registeredDateTime(registeredDateTime)
        .products(products)
        .build();
  }

  private int calculateTotalPrice(List<Product> products) {
    return products
         .stream()
         .mapToInt(Product::getPrice)
         .sum();
  }

}
