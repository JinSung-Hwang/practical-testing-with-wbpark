package sample.cafekiosk.spring.docs.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.cafekiosk.spring.controller.product.ProductController;
import sample.cafekiosk.spring.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.service.product.ProductService;
import sample.cafekiosk.spring.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.service.product.response.ProductResponse;

public class ProductControllerDocsTest extends RestDocsSupport {

  private final ProductService productService = mock(ProductService.class);

  @Override
  protected Object initController() {
    return new ProductController(productService);
  }

  @DisplayName("신규 상품을 등록하는 API")
  @Test
  void createProduct() throws Exception {
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(1000)
        .build();

    given(productService.createProduct(any(ProductCreateServiceRequest.class)))
        .willReturn(ProductResponse.builder()
            .id(1L)
            .productNumber("001")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(4000)
            .build());

    mockMvc.perform(
            post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)) // note: controller를 호출할때는 object를 직렬화해서 호출해야하기 때문에 ObjectMapper를 사용했다.
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andDo(
            document("product-create",
                preprocessRequest(prettyPrint()), // note: Json이 줄바꿈이 되어 출력되도록 설정
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("type").type(JsonFieldType.STRING).description("상품 타입"),
                    fieldWithPath("sellingStatus").type(JsonFieldType.STRING).description("판매 상태"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("응답 데이터"),
                    fieldWithPath("data.productNumber").type(JsonFieldType.STRING).description("응답 데이터"),
                    fieldWithPath("data.type").type(JsonFieldType.STRING).description("상품 타입"),
                    fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING).description("상품 판매 상태"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격")
                ))
        );
  }

}
