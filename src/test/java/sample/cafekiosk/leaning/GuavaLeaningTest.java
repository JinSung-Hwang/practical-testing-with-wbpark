package sample.cafekiosk.leaning;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;


public class GuavaLeaningTest {
  // note: #### 학습 테스트는 저장소에 남겨두는가?
  // note:   학습테스트는 학습후 삭제하면 된다.
  // note:   하지만 학습한 테스트한 내용을 팀에 공유하려면 남겨도 좋다.
  // note:   나는 지우면 아까우니 학습 테스트하고 Gist에 저장해두면 좋을거같다.

  @DisplayName("파티션 테스트")
  @Test
  void PartitionTest1() {
    // Given
    List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
    
    // When
    List<List<Integer>> partition = Lists.partition(integers, 4);

    // Then
    assertThat(partition).hasSize(2)
        .isEqualTo(
            List.of(
                List.of(1, 2, 3, 4),
                List.of(5, 6, 7, 8)
            )
        );
  }

  @DisplayName("파티션 테스트2")
  @Test
  void PartitionTest2() {
    // Given
    List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

    // When
    List<List<Integer>> partition = Lists.partition(integers, 5);

    // Then
    assertThat(partition).hasSize(2)
        .isEqualTo(
            List.of(
                List.of(1, 2, 3, 4, 5),
                List.of(6, 7, 8)
            )
        );
  }

  @DisplayName("멀티맵 기능 확인1")
  @Test
  void multiMapTest1() {
    // Given
    Multimap<String, String> multimap = ArrayListMultimap.create();
    multimap.put("커피", "아메리카노");
    multimap.put("커피", "카페라떼");
    multimap.put("커피", "카푸치노");
    multimap.put("베이커리", "크루아상");
    multimap.put("베이커리", "식빵");

    // When
    Collection<String> strings = multimap.get("커피");

    // Then
    assertThat(strings).hasSize(3)
        .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
  }

  @DisplayName("멀티맵 기능 확인2")
  @TestFactory
  Collection<DynamicTest> multimapTest2() {
    // Given
    Multimap<String, String> multimap = ArrayListMultimap.create();
    multimap.put("커피", "아메리카노");
    multimap.put("커피", "카페라떼");
    multimap.put("커피", "카푸치노");
    multimap.put("베이커리", "크루아상");
    multimap.put("베이커리", "식빵");

    return List.of(
        DynamicTest.dynamicTest("같은 key에 여러 엘리멘트 중에 1개를 삭제한다.", () -> {
          // When
          multimap.remove("커피", "카푸치노");

          // Then
          Collection<String> strings = multimap.get("커피");
          assertThat(strings).hasSize(2)
              .isEqualTo(List.of("아메리카노", "카페라떼"));
        }),
        DynamicTest.dynamicTest("key안에 여러 엘리멘트가 있어도 모두 삭제한다.", () -> {
          // When
          multimap.removeAll("커피");

          // Then
          Collection<String> strings = multimap.get("커피");
          assertThat(strings).isEmpty();
        })
    );
  }
}
