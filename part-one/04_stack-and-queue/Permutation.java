import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Permutation <k>");
            return;
        }

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        if (k > queue.size()) {
            System.out.println("k cannot be greater than the number of items in the queue.");
            return;
        }

        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
