package sample.cafekiosk.spring.service.product;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductNumberFactory {
  private final ProductRepository productRepository;
  public String generateNextProductNumber() {
    String productNumber = productRepository.findLatestProductNumber();
    if (productNumber == null) {
      return "001";
    }

    int productNumberInt = Integer.parseInt(productNumber);
    int nextProductNumberInt = productNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }
}
