/**
 * The HelloGoodbye class prints personalized greeting and farewell messages.
 * <p>
 * It expects two command-line arguments representing names. If arguments are not provided,
 * it uses default names "Anonymous1" and "Anonymous2".
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *   java HelloGoodbye Alice Bob
 *   // Output:
 *   // Hello Alice and Bob.
 *   // Goodbye Bob and Alice.
 * </pre>
 * </p>
 *
 * Arguments:
 * <ul>
 *   <li>args[0] - First person's name (default: "Anonymous1")</li>
 *   <li>args[1] - Second person's name (default: "Anonymous2")</li>
 * </ul>
 */
public class HelloGoodbye {

    public static void main(String[] args) {
        String firstName = args.length > 0 && args[0] != null ? args[0] : "Anonymous1";
        String lastName = args.length > 1 && args[1] != null ? args[1] : "Anonymous2";

        System.out.println("Hello " + firstName + " and " + lastName + ".");
        System.out.println("Goodbye " + lastName + " and " + firstName + ".");
    }

}
