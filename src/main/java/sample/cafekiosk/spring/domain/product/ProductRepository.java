package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * select *
   * from product
   * where selling_status in ("SELLING", "HOLD");
   */
  List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
  // note: QueryMethod를 테스트
  // note:   사실 QueryMethod는 JPA에서 지원해주는 기능인데 왜 테스트 해야하는지 의문이 들 수 있다.
  // note:   하지만 아래 2가지 이유로 테스트를 진행한다.
  // note:     1. 간단하지만 인터페이스를 선언한것도 내가 만든 코드이며 잘 동작하는지 테스트가 필요하다.
  // note:     2. 만든 쿼리 메소드가 변형이 일어날수 있고 메소드명은 그대로지만 queryDsl, nativeQuery, mybatis로 기술이 변경되어 테스트가 필요할 수도 있다.

  List<Product> findAllProductByProductNumberIn(List<String> productNumbers);


}
