package wordle;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FrequencyComparatorTest {
  private FrequencyComparator fc;

//  eariotnslcudpmhgbfywkvxzjq

  @BeforeEach
  void setUp() {
    fc = new FrequencyComparator();
  }

  @Test
  void orderByFrequency_oneLetter() {
    assertThat(fc.compare("e", "a")).isEqualTo(-1);
    assertThat(fc.compare("a", "e")).isEqualTo(1);
    assertThat(fc.compare("a", "a")).isEqualTo(0);
  }

  @Test
  void orderByFrequency_multipleLetters_sameLength() {
    assertThat(fc.compare("ate", "all")).isEqualTo(-1);
    assertThat(fc.compare("all", "ate")).isEqualTo(1);
    assertThat(fc.compare("quick", "quick")).isEqualTo(0);
  }

  @Test
  void orderByFrequency_multipleLetters_differentLength() {
    assertThat(fc.compare("eario", "eariotnsl")).isEqualTo(-1);
    assertThat(fc.compare("eariotnsl", "eario")).isEqualTo(1);

    assertThat(fc.compare("eariotnsl", "earto")).isEqualTo(-1);
    assertThat(fc.compare("eariotnsl", "earto")).isEqualTo(-1);
  }
}