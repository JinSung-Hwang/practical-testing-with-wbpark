package sample.cafekiosk.spring.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.service.product.response.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;
import sample.cafekiosk.spring.service.product.request.ProductCreateServiceRequest;


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
  public ProductResponse createProduct(ProductCreateServiceRequest productCreateRequest) {
    // note: controller는 ProductCreateRequest를 service는 ProductCreateServiceRequest로 DTO를 분리해두었다.
    // note: 이렇게 dto를 분리해두면 service layer에서는 controller에 의존하지 않게 되고 관심사와 책임 분리가 더 명확해진다.
    // note: 이렇게 관심사와 책임 분리가 명확해지면 controller가 여러 이유로 인해서 바뀌게 되더라도 service에는 영향을 끼치지 않게 된다.
    // note:   예를들어 app, web, pc, kiosk, sdk 등등 다양한 클라이언트때문에 controller가 바뀌더라도 controller dto는 ProductCreateServiceRequest만 만들어서 service를 호출하면 된다.
    // note: --> 이것을 왜 이렇게 만들었는지 이해는 간다. 내가 아직 이렇게 큰 서비스를 만들지 않아서 그런지는 모르겠지만 납득이 되지는 않는다.
    // note:     지금 회사에서는 client는 web, android만 있는 상황인데 이렇게 만들필요가 있을까 싶다.... 클라이언트가 다양한 서비스이고 큰 서비스이어야 사용할만하지 않을까 싶다.
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
