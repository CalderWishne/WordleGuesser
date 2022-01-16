package wordle;

import java.io.IOException;
import java.util.Scanner;

public class WordleGuesser {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        SuggestionGenerator generator = new SuggestionGenerator("/usr/share/dict/words", 5);

        while (!generator.getSuggestions().isEmpty()) {
          System.out.println("Enter guess. ");
          String guess = in.nextLine().toLowerCase().trim();

          System.out.println("Enter feedback. ");
          String feedback = in.nextLine().toLowerCase().trim();

          generator.addGuess(guess,feedback);

          System.out.println("\n********************\nSuggestions:\n");
          generator.getSuggestions().forEach(System.out::println);
          System.out.println("\n********************\n");
        }

        System.out.println("No more suggestions!");
        in.close();
    }
}
