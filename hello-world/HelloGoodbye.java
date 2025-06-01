/**
 * The {@code HelloGoodbye} class prints personalized greeting and farewell messages.
 * <p>
 * It expects two command-line arguments representing names. If arguments are not provided,
 * default names "Anonymous1" and "Anonymous2" are used.
 * </p>
 *
 * <p>
 * Example output with arguments "Alice" and "Bob":
 * <pre>
 * Hello Alice and Bob.
 * Goodbye Bob and Alice.
 * </pre>
 * </p>
 *
 * <p>
 * Example output with no arguments:
 * <pre>
 * Hello Anonymous1 and Anonymous2.
 * Goodbye Anonymous2 and Anonymous1.
 * </pre>
 * </p>
 *
 * Usage:
 * <pre>
 * java HelloGoodbye Alice Bob
 * </pre>
 *
 */
public class HelloGoodbye {

    public static void main(String[] args) {
        String firstName = args.length > 0 && args[0] != null ? args[0] : "Anonymous1";
        String lastName = args.length > 1 && args[1] != null ? args[1] : "Anonymous2";

        System.out.println("Hello " + firstName + " and " + lastName + ".");
        System.out.println("Goodbye " + lastName + " and " + firstName + ".");
    }

}
