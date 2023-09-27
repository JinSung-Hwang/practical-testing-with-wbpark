package sample.cafekiosk.spring.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.service.product.ProductService;

// note: @SpringBootTest는 모든 Spring Bean들을(설정 Bean, 테스트와 관련 없는 Bean) 탐색해서 다 등록하고 테스트를 진행한다. 모든 Bean을 등록하려니 테스트가 많이 느리다.
@WebMvcTest(controllers = ProductController.class)  // note: 이거는 SpringBoot의 많은 Bean들중에 MVC와 관련된 Bean만 띄운다.
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService productService;

  @DisplayName("신규 상품을 등록한다.")
  @Test
  void registerProduct() throws Exception {
    // Given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(1000)
        .build();

    // When // Then
    mockMvc.perform(
              post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)) // note: controller를 호출할때는 object를 직렬화해서 호출해야하기 때문에 ObjectMapper를 사용했다.
            )
            .andDo(print())
            .andExpect(status().isOk());
  }


  @DisplayName("신규 상품을 등록할때 상품 타입은 필수값이다.")
  @Test
  void registerProductWithoutProductType() throws Exception {
    // Given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(1000)
        .build();

    // When // Then
    mockMvc.perform(
            post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("신규 상품을 등록할때 판매 상태는 필수값이다.")
  @Test
  void registerProductWithoutSellingType() throws Exception {
    // Given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .name("아메리카노")
        .price(1000)
        .build();

    // When // Then
    mockMvc.perform(
            post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 판매 상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("신규 상품을 등록할때 상품 이름은 필수값이다.")
  @Test
  void registerProductWithoutName() throws Exception {
    // Given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .price(1000)
        .build();

    // When // Then
    mockMvc.perform(
            post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }


  @DisplayName("신규 상품을 등록할때 상품 가격은 필수값이다.")
  @Test
  void registerProductWithZeroPrice() throws Exception {
    // Given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(0)
        .build();

    // When // Then
    mockMvc.perform(
            post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("판매 중인 상품을 조회한다.")
  @Test
  void getSellingProducts() throws Exception {
    // Given
    List<ProductResponse> result = List.of();
    when(productService.getSellingProducts())
        .thenReturn(result);

    // When // Then
    mockMvc.perform(
            get("/api/v1/products/selling")
//                .queryParam("name", "아메리카노")
//                .queryParam("price", "1000")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
  }


}