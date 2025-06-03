import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.time.*;

public class QuadraticThreeSum {
    public final static int DEFAULT_ARRAY_SIZE = 1000;

    /**
     * A class representing a pair of integers.
     * This is used to store the result of the pairWithSum method.
     */
    public static class Pair {
        public final int first;
        public final int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    /**
     * A class representing a triple of integers.
     * This is used to store the result of the threeSum method.
     */
    public static class Triple {
        public final int first;
        public final int second;
        public final int third;

        public Triple(int first, int second, int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ", " + third + ")";
        }
    }

    /**
     * Finds all pairs in the ordered array that sum to the targetSum.
     * The input array should be sorted in ascending order.
     * 
     * @param orderedArray The input array of integers, sorted in ascending order.
     * @param targetSum The target sum for the pairs.
     * @return A list of Pairs that sum to the targetSum.
     */
    public ArrayList<Pair> pairWithSum(int[] orderedArray, int targetSum) {
        int low = 0;
        int high = orderedArray.length - 1;
        
        ArrayList<Pair> satisfiedPairs = new ArrayList<>();
        while (low < high) {
            int currentSum = orderedArray[low] + orderedArray[high];
            if (currentSum == targetSum) {
                satisfiedPairs.add(new Pair(orderedArray[low], orderedArray[high]));
                low++;
                high--;
            } else if (currentSum < targetSum) {
                low++;
            } else {
                high--;
            }
        }
        return satisfiedPairs;
    }

    /**
     * Finds all unique triples in the array that sum to the targetSum.
     * The input array should only contain unique values (else the result may contain duplicates).
     * 
     * @param arr The input array of integers.
     * @param targetSum The target sum for the triples.
     * @return A list of Triples that sum to the targetSum.
     */
    public ArrayList<Triple> threeSum(int[] arr, int targetSum) {
        Arrays.sort(arr); // Ensure the array is sorted, with O(n log n) complexity

        ArrayList<Triple> satisfiedTriples = new ArrayList<>();
        for (int i = 0; i < arr.length - 1; i++) {
            int currentTarget = targetSum - arr[i];
            ArrayList<Pair> pairs = pairWithSum(arr, currentTarget);
            for (Pair pair : pairs) {
                satisfiedTriples.add(new Triple(arr[i], pair.first, pair.second));
            }
        }
        return satisfiedTriples;
    }

    public static int[] generateUniqueValues(int count, int maxValue) {
        if (count <= 0 || maxValue <= 0) {
            throw new IllegalArgumentException("Count and maxValue must be positive.");
        }
        if (2 * maxValue + 1 < count) {
            throw new IllegalArgumentException("Not enough unique values can be generated with the given count and maxValue.");
        }
        int generatedArraySize = 2 * maxValue + 1;
        ArrayList<Integer> arr = new ArrayList<Integer>(generatedArraySize);
        for (int i = 0; i < generatedArraySize; i++) {
            arr.add(- maxValue + i);
        }
        Collections.shuffle(arr);
        return arr.subList(0, count).stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public static void main(String[] args) {
        final float VARIANCE = 0.6f;  

        int arraySize = DEFAULT_ARRAY_SIZE;
        if (args.length > 0) {
            try {
                arraySize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid array size argument, using default size: " + DEFAULT_ARRAY_SIZE);
            }
        }
        int[] testData = generateUniqueValues(arraySize, (int) (VARIANCE * arraySize));
        System.out.println("Generated array of size " + testData.length + ": " + Arrays.toString(testData));

        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("Start time: " + startTime);
        ArrayList<Triple> result = new QuadraticThreeSum().threeSum(testData, 0);
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("End time: " + endTime);
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Duration: " + duration.toMillis() + " milliseconds");
        System.out.println("Found " + result.size() + " triples that sum to 0:");
        // for (Triple triple : result) {
        //     System.out.println("- " + triple);
        // }
    }
}