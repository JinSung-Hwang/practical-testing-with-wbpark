package sample.cafekiosk.spring.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration; // note: 문서를 만들기위한 설정이 들어가있음

@ExtendWith(RestDocumentationExtension.class)
//@SpringBootTest // note: 첫번쨰 방법에서만 필요함
public abstract class RestDocsSupport {

  protected MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp(
//      WebApplicationContext WebApplicationContext, // note: 첫번쨰 방법에서만 필요함
      RestDocumentationContextProvider provider
  ) {
    // note: 첫번쨰 방법: 스프링을 띄워서 테스트를 진행하고 문서를 만들어준다.
//    this.mockMvc = MockMvcBuilders.webAppContextSetup(WebApplicationContext)
//        .apply(documentationConfiguration(provider))
//        .build();
    // note: 두번쨰 방법: 스프링을 띄우지 않고 문서를 만들어준다.
    this.mockMvc = MockMvcBuilders.standaloneSetup(initController()) // note: 여기에는 문서를 만들고 싶은 컨트롤러 객체를 넣어주면 된다.
        .apply(documentationConfiguration(provider))
        .build();
  }

  protected abstract Object initController(); // note: 두번쨰 방법에서만 필요함

}
