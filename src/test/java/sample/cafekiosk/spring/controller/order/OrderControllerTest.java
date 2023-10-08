package sample.cafekiosk.spring.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.controller.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.service.order.OrderService;

//@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest extends ControllerTestSupport {

//  note: 아래 의존성이 ControllerTestSupport로 올라감
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @MockBean
//  private OrderService orderService;

  @DisplayName("신규 주문을 등록한다.")
  @Test
  void createOrder() throws Exception {
    // Given
    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
        .productNumbers(List.of("001", "002"))
        .build();

    // When // Then
    mockMvc.perform(
          post("/api/v1/orders/new")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)) // note: controller를 호출할때는 object를 직렬화해서 호출해야하기 때문에 ObjectMapper를 사용했다.
        )
        .andDo(print())
        .andExpect(status().isOk())
        ;
  }

  @DisplayName("주문을 접수할때 상품 번호는 1개 이상을 입력해야합니다.")
  @Test
  void createOrderWithEmptyProductNumber() throws Exception {
    // Given
    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
        .productNumbers(List.of())
        .build();

    // When // Then
    mockMvc.perform(
            post("/api/v1/orders/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 번호는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

}