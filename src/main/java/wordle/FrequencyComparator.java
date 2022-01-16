package wordle;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static java.lang.Math.min;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FrequencyComparator implements Comparator<String> {

  public static final List<String> FREQUENCY_ORDER = Arrays.asList("eariotnslcudpmhgbfywkvxzjq".split(""));

  public int compare(String a, String b) {
    int l = min(a.length(), b.length());
    for (int i = 0; i < l; i++) {
      if (FREQUENCY_ORDER.indexOf(String.valueOf(a.charAt(i))) < FREQUENCY_ORDER.indexOf(String.valueOf(b.charAt(i)))) {
        return -1;
      }
      if (FREQUENCY_ORDER.indexOf(String.valueOf(a.charAt(i))) > FREQUENCY_ORDER.indexOf(String.valueOf(b.charAt(i)))) {
        return 1;
      }
    }
    return Integer.compare(a.length(), b.length());
  }
}
