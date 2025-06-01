import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;

public class RandomWordTest {
    @Test
    public void testRandomWordOutput() throws Exception {
        String input = "apple banana cherry date";
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
        
        // Run main
        RandomWord.main(new String[]{});
        
        System.setIn(originalIn);
        System.setOut(originalOut);
        String result = out.toString().trim();
        // The result should be one of the input words
        assertTrue(result.equals("apple") || result.equals("banana") || result.equals("cherry") || result.equals("date"),
            "Output should be one of the input words, but was: " + result);
    }
}
