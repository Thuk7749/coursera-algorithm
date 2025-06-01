import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * RandomWord.java
 * <p>
 * Reads words from standard input and prints one of them at random.
 * The probability of each word being selected is inversely proportional to its position in the input.
 * This implementation uses reservoir sampling to achieve the desired behavior.
 * 
 * 
 * To compile: javac -cp lib/algs4.jar hello-world/RandomWord.java
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
