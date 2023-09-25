package sample.cafekiosk.spring.controller.product.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.sql.Select;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;


@Getter
public class ProductCreateRequest {
  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private String name;
  private int price;


  @Builder
  private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public Product toEntity(String productNumber) {
    return Product
        .builder()
        .type(type)
        .productNumber(productNumber)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}
