package sample.cafekiosk.spring.service.product.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;


@Getter
public class ProductCreateServiceRequest {
  @NotNull(message = "상품 타입은 필수입니다.")
  private ProductType type;
  @NotNull(message = "상품 판매 상태는 필수입니다.")
  private ProductSellingStatus sellingStatus;
  @NotBlank(message = "상품 이름은 필수입니다.")
//  @NotNull : null이면 에러
//  @NotEmpty : null과 ""(빈문자열) 에러
//  @NotBlack : null, ""(빈문자열), "   "(공백) 에러
  private String name;
  @Positive(message = "상품 가격은 양수여야 합니다.")
  private int price;


  @Builder
  private ProductCreateServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
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
