package sample.cafekiosk.spring.service.product.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductResponse {
  private Long id;
  private String productNumber;
  private ProductType type;
  private ProductSellingStatus sellingStatus;
  private String name;
  private int price;

  @Builder // note: 이렇게 클래스가 아닌 method에 @Builder를 붙이면 @Builder패턴으로 생성할 수 있는 필드를 제한 할 수 있다.
  private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    // note: builder를 외부에서 사용하지 못하도록 private를 선언했다.
    this.id = id;
    this.productNumber = productNumber;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public static ProductResponse of(Product product) {
    return ProductResponse.builder()
            .id(product.getId())
            .productNumber(product.getProductNumber())
            .type(product.getType())
            .sellingStatus(product.getSellingStatus())
            .name(product.getName())
            .price(product.getPrice())
            .build()
            ;
  }
}
