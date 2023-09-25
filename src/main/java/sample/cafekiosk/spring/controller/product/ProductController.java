package sample.cafekiosk.spring.controller.product;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.service.product.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

  private final ProductService productService;

  @PostMapping("/api/v1/products/new")
  public void registerProduct(ProductCreateRequest productCreateRequest) {
    productService.createProduct(productCreateRequest);
  }

  @GetMapping("/api/v1/products/selling")
  public List<ProductResponse> getSellingProducts() {
    return productService.getSellingProducts();
  }
}
