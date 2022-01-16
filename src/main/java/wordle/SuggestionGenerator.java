package wordle;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static java.lang.Character.isUpperCase;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static wordle.Color.BLACK;
import static wordle.Color.GREEN;
import static wordle.Color.YELLOW;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class SuggestionGenerator {

  private List<String> suggestions;

  private final int wordLength = 5;

  public SuggestionGenerator(String filename, int wordLength) throws IOException {
    parseFile(filename, wordLength);
  }

  public void addGuess(String guessString, String feedback) {
    addGuess(convertToGuess(guessString, feedback));
  }

  private void addGuess(List<LetterGuess> guess) {
    if (!isValidGuess(guess)) {
      throw new IllegalArgumentException(String.format("Guess must have length %s", wordLength));
    }

    Pattern greenPattern = greenRegex(guess);
    Pattern yellowExcludePattern = yellowRegexExclude(guess);

    boolean guessContainsGreen = guessContains(guess, GREEN);
    boolean guessContainsBlack = guessContains(guess, BLACK);
    boolean guessContainsYellow = guessContains(guess, YELLOW);

    suggestions = suggestions.stream()
        .filter(word ->
            (!guessContainsGreen || greenPattern.matcher(word).find())
                && (!guessContainsBlack || blackExclude(guess, word))
                && (!guessContainsYellow || yellowExcludePattern.matcher(word).find())
                && (!guessContainsYellow || yellowInclude(guess, word))
        ).collect(toList());
  }

  private boolean isValidGuess(List<LetterGuess> guess) {
    return null != guess
        && guess.size() == wordLength
        && guess.stream().allMatch(lg -> lg.isValid(wordLength));
  }

  private boolean guessContains(List<LetterGuess> guess, Color color) {
    return guess.stream().anyMatch(lg -> lg.getColor() == color);
  }

  public Pattern greenRegex(List<LetterGuess> guess) {
    StringBuilder regex = new StringBuilder();
    Map<Integer, Character> greenGuesses = guess.stream()
        .filter(letterGuess -> letterGuess.getColor() == GREEN)
        .collect(toMap(LetterGuess::getIndex, LetterGuess::getLetter, (lg1, lg2) -> lg1));

    for (int i = 0; i < wordLength; i++) {
      regex.append(greenGuesses.containsKey(i) ? greenGuesses.get(i) : ".");
    }

    return Pattern.compile(regex.toString());
  }

  public boolean blackExclude(List<LetterGuess> guess, String word) {
    return guess.stream()
        .filter(letterGuess -> letterGuess.getColor() == BLACK)
        .map(LetterGuess::getLetter)
        .map(String::valueOf)
        .noneMatch(word::contains);
  }

  public Pattern yellowRegexExclude(List<LetterGuess> guess) {
    StringBuilder regex = new StringBuilder();
    Map<Integer, Character> yellowGuesses = guess.stream()
        .filter(letterGuess -> letterGuess.getColor() == YELLOW)
        .collect(toMap(LetterGuess::getIndex, LetterGuess::getLetter, (lg1, lg2) -> lg1));

    for (int i = 0; i < wordLength; i++) {
      regex.append(yellowGuesses.containsKey(i) ? "[^" + yellowGuesses.get(i) + "]" : ".");
    }

    return Pattern.compile(regex.toString());
  }

  public boolean yellowInclude(List<LetterGuess> guess, String word) {
    return guess.stream()
        .filter(letterGuess -> letterGuess.getColor() == YELLOW)
        .map(LetterGuess::getLetter)
        .map(String::valueOf)
        .allMatch(word::contains);
  }

  private List<LetterGuess> convertToGuess(String guessString, String feedback) {
    List<LetterGuess> guess = new ArrayList<>();
    for (int i = 0; i < wordLength; i++) {
      switch (feedback.charAt(i)) {
        case 'g':
          guess.add(LetterGuess.builder()
              .index(i)
              .letter(guessString.charAt(i))
              .color(GREEN)
              .build());
          break;
        case 'b':
          guess.add(LetterGuess.builder()
              .index(i)
              .letter(guessString.charAt(i))
              .color(BLACK)
              .build());
          break;
        case 'y':
          guess.add(LetterGuess.builder()
              .index(i)
              .letter(guessString.charAt(i))
              .color(YELLOW)
              .build());
          break;
        default:
          throw new IllegalArgumentException("Feedback string can contain only the chars  g, b, y");
      }
    }
    return guess;
  }

  private void parseFile(String filename, int wordLength) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      this.suggestions = lines
          .map(String::trim)
          .map(String::toLowerCase)
          .filter(w -> !isUpperCase(w.charAt(0)))
          .filter(w -> w.length() == wordLength)
          .sorted(new FrequencyComparator())
          .collect(toList());
    }
  }
}
