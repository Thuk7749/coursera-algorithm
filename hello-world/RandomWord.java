import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * The {@code RandomWord} class reads a sequence of words from standard input
 * and prints one of them, chosen uniformly at random.
 * <p>
 * This implementation uses reservoir sampling to ensure that each word
 * has an equal probability of being selected, regardless of the total number
 * of words in the input.
 * <p>
 * Usage:
 * <pre>
 *   java RandomWord
 * </pre>
 * Input: A sequence of words, one per line or separated by whitespace.
 * Output: A single word, chosen uniformly at random from the input.
 *
 */
public class RandomWord {
    public static void main(String[] args) {
        Integer i = 0;
        String champion = null;
        while (!StdIn.isEmpty()) {
            i++;
            String word = StdIn.readString();
            // Select a random word with decreasing probability
            if (StdRandom.bernoulli(1.0 / i)) {
                champion = word;
            }
        }
        StdOut.println(champion == null ? "" : champion);
    }
}
