package wordle;

import java.util.ArrayList;
import java.util.List;
import static wordle.Color.BLACK;
import static wordle.Color.GREEN;
import static wordle.Color.YELLOW;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SuggestionGeneratorTest {

  private List<LetterGuess> guess;
  private final SuggestionGenerator guesser = new SuggestionGenerator();

  @BeforeEach
  void setUp() {
    guess = new ArrayList<>();
    guess.add(LetterGuess.builder().letter('t').color(GREEN).index(0).build());
    guess.add(LetterGuess.builder().letter('h').color(BLACK).index(1).build());
    guess.add(LetterGuess.builder().letter('i').color(YELLOW).index(2).build());
    guess.add(LetterGuess.builder().letter('n').color(BLACK).index(3).build());
    guess.add(LetterGuess.builder().letter('g').color(YELLOW).index(4).build());
  }

  @Test
  void greenRegex() {
    assert(guesser.greenRegex(guess).toString()).equals("t....");
  }

  @Test
  void yellowRegexExclude() {
    assert(guesser.yellowRegexExclude(guess).toString()).equals("..[^i].[^g]");
  }
}