package sample.cafekiosk.unit.beaverage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmericanoTest {

  @Test
  void americano() {
    Americano americano = new Americano();

//    Assertions.assertEquals(americano.getName(), "americano");
    Assertions.assertThat(americano.getName()).isEqualTo("Americano");
    // note: A가 B와 같다. 더 영어와 비슷해서 가독성이 좋다. 또한 AssertJ는 많은 API를 지원하여 이것을 사용하는것을 선호한다.
   }

}