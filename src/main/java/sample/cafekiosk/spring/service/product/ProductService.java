package sample.cafekiosk.spring.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.controller.product.response.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;


/**
 * readOnly = true: 읽기전용 트랜잭션
 * CRUD에서 CUD 동작 X / only Read
 * JPA: CUD 스냅샷 저장, 변경 감지 x (성능 향상)
 *
 * CQRS - Command / Query
 * 대부분의 Application에서 Query가 휠씬더 빈번히 발생하는데 query에 많은 부하를 감당하기 위해서 db에master - slave 아키텍처를 둘 수 있다.
 * 이럴때 db에 transaction의 readOnly 값에 따라서 master에 query가 날아가게 할지 slave에 query가 날아가게 할지 정할 수 있다.
 */
@Transactional(readOnly = true) // note: 서비스에는 readOnly를 걸고 CUD에는 @Transactional을 걸자.
@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository productRepository;

  // 동시성 이슈
  // UUID
  @Transactional
  public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
    String newProductNumber = generateNextProductNumber();

    Product product = productCreateRequest.toEntity(newProductNumber);
    Product savedProduct = productRepository.save(product);

    return ProductResponse.of(savedProduct);
  }

  private String generateNextProductNumber() {
    String productNumber = productRepository.findLatestProductNumber();
    if (productNumber == null) {
      return "001";
    }

    int productNumberInt = Integer.parseInt(productNumber);
    int nextProductNumberInt = productNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }


  public List<ProductResponse> getSellingProducts() {
    List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

    return products.stream()
      .map(ProductResponse::of)
      .collect(Collectors.toList());
  }


}
