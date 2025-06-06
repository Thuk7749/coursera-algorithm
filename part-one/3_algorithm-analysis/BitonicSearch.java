import java.util.Random;

public class BitonicSearch {
    public final static int DEFAULT_ARRAY_SIZE = 1000;

    public static class FindPeakResult {
        public int peakIndex;
        public int stepCount;
        public FindPeakResult(int peakIndex, int stepCount) {
            this.peakIndex = peakIndex;
            this.stepCount = stepCount;
        }
    }

    public static FindPeakResult findPeakIndex(int[] arr) {
        int low = 0;
        int high = arr.length - 1;
        int stepCount = 0;

        while (low < high) {
            stepCount++;
            int mid = low + (high - low) / 2;
            if (arr[mid] > arr[mid + 1]) {
                high = mid; // Peak is in the left half
            } else {
                low = mid + 1; // Peak is in the right half
            }
        }
        return new FindPeakResult(low, stepCount); 
    }

    public static class BinarySearchResult {
        public int index;
        public int stepCount;
        public BinarySearchResult(int index, int stepCount) {
            this.index = index;
            this.stepCount = stepCount;
        }
    }

    public static BinarySearchResult binarySearch(
      int[] arr, int targetValue, int startIndex, int endIndex, boolean ascending
      ) {
        int stepCount = 0;
        while (startIndex <= endIndex) {
            stepCount++;
            int mid = startIndex + (endIndex - startIndex) / 2;
            // mid is the middle left index if the array has an even number of elements
  
            if (arr[mid] == targetValue) {
                return new BinarySearchResult(mid, stepCount); // Target found
            }
            if (ascending) {
                if (arr[mid] < targetValue) {
                    startIndex = mid + 1; // Search in the right half
                } else {
                    endIndex = mid - 1; // Search in the left half
                }
            } else {
                if (arr[mid] > targetValue) {
                    startIndex = mid + 1; // Search in the right half
                } else {
                    endIndex = mid - 1; // Search in the left half
                }
            }
        }
        return new BinarySearchResult(-1, stepCount); // Target not found
    }

    public static class BitonicSearchResult {
        public int index;
        public int alternateIndex;
        public int stepCount;

        public BitonicSearchResult(int index, int alternateIndex, int stepCount) {
            this.index = index;
            this.alternateIndex = alternateIndex;
            this.stepCount = stepCount;
        }

        @Override
        public String toString() {
            if (index == -1) {
                return "Target not found, steps: " + stepCount;
            } else if (alternateIndex == -1){
                return "Target found at index: " + index + ", steps: " + stepCount;
            } else {
                return "Target found at index: " + index + " or " + alternateIndex + ", steps: " + stepCount;
            }
        }
    }

    public static BitonicSearchResult bitonicSearch(int[] arr, int targetValue) {
        FindPeakResult peakResult = findPeakIndex(arr);
        int peakIndex = peakResult.peakIndex;
        int stepCount = peakResult.stepCount;

        // Search in the ascending part
        BinarySearchResult ascResult = binarySearch(arr, targetValue, 0, peakIndex, true);

        // Search in the descending part
        BinarySearchResult descResult = binarySearch(arr, targetValue, peakIndex + 1, arr.length - 1, false);

        return new BitonicSearchResult(
            ascResult.index != -1 ? ascResult.index : descResult.index,
            ascResult.index != -1 ? descResult.index : ascResult.index,
            stepCount + ascResult.stepCount + descResult.stepCount
        );
    }

    public static int[] generateBitonicArray(
      int count, int peakValue, float stepVariance
      ) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        if (stepVariance < 1) {
            throw new IllegalArgumentException("Step variance must be at least 1");
        }
        int[] arr = new int[count];
        int n = arr.length;
        int peakIndex = count - 1 - (int) (Math.random() * count) / 2;
        arr[peakIndex] = peakValue;
        // Fill the ascending part
        for (int i = peakIndex - 1; i >= 0; i--) {
            arr[i] = arr[i + 1] - Math.max(1, (int) (Math.random() * stepVariance));
        }
        // Fill the descending part
        for (int i = peakIndex + 1; i < n; i++) {
            arr[i] = arr[i - 1] - Math.max(1, (int) (Math.random() * stepVariance));
        }
        return arr;
    }

    public static void main(String[] args) {
        final float STEP_VARIANCE = 4f;
        final int PEAK_PENALTY = 2;

        int arraySize = DEFAULT_ARRAY_SIZE;
        if (args.length > 0) {
            try {
                arraySize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid array size, using default: " + DEFAULT_ARRAY_SIZE);
            }
        }

        int peakValue = arraySize * (int) (Math.ceil(STEP_VARIANCE)) / PEAK_PENALTY;
        int[] bitonicArray = generateBitonicArray(arraySize, peakValue, STEP_VARIANCE);
        System.out.println("Generated Bitonic Array of size " + bitonicArray.length);
        System.out.print("[ ");
        if (arraySize <= 10000) {
            for (int i = 0; i < bitonicArray.length; i++) {
                System.out.print(bitonicArray[i] + " ");
            }
        } else {
            System.out.print(" ... ");
        }
        System.out.println(" ]");

        Random rand = new Random();
        int targetValue = rand.nextInt(peakValue);
        System.out.println("Searching for target value: " + targetValue);

        BitonicSearchResult searchResult = bitonicSearch(bitonicArray, targetValue);
        System.out.println("Search Result - " + searchResult);
    }
}
