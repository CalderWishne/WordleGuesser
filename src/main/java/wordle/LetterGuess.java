package wordle;

import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LetterGuess {
  private Character letter;
  private int index;
  private Color color;

  public boolean isValid(int wordLength) {
    return Pattern.compile("[A-Za-z]").matcher(String.valueOf(letter)).find()
        && 0 <= index && index < wordLength;
  }
}
