import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

/**
 * PercolationStats class performs statistical analysis on percolation experiments.
 * It calculates the mean, standard deviation, and confidence intervals for the
 * percolation threshold based on multiple trials on an n-by-n grid.
 */
public class PercolationStats {
    /**
     * Z-value for 95% confidence interval.
     */
    private static final double CONFIDENCE_95 = 1.96; // Z-value for 95% confidence interval
    
    /**
     * Default value for trials size.
     * This is used if the user does not specify it in the command line arguments.
     */
    private static final int DEFAULT_TRIALS = 100; // Default number of trials if not specified
    
    /**
     * Default value for grid size.
     * This is used if the user does not specify it in the command line arguments.
     */
    private static final int DEFAULT_N = 200; // Default grid size if not specified

    /**
     * Stores the percolation thresholds for each trial.
     * Each threshold is the fraction of sites that need to be opened
     */
    private final double[] mThresholds; // Store percolation thresholds for trials
    
    /**
     * Number of independent trials performed.
     * Used to calculate mean, standard deviation, and confidence intervals.
     */
    private final int mTrials; // Number of trials performed

    /**
     * Constructs a PercolationStats object that performs trials on an n-by-n grid.
     * It initializes the thresholds for each trial by performing percolation trials.
     *
     * @param n      the size of the grid (n x n)
     * @param trials the number of independent trials to perform
     * @throws IllegalArgumentException if n or trials is less than or equal to 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        
        mTrials = trials;
        mThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            mThresholds[i] = performPercolationTrial(n);
        }
    }

    /**
     * Calculates the mean of the percolation thresholds from the trials.
     *
     * @return the mean of the percolation thresholds
     */
    public double mean() {
        return StdStats.mean(mThresholds);
    }

    /**
     * Calculates the sample standard deviation of the percolation thresholds from the trials.
     *
     * @return the standard deviation of the percolation thresholds
     */
    public double stddev() {
        if (mTrials == 1) {
            return Double.NaN; // Standard deviation is undefined for a single trial
        }
        return StdStats.stddev(mThresholds);
    }

    /**
     * Calculates the low endpoint of the 95% confidence interval for the mean of the percolation thresholds.
     *
     * @return the low endpoint of the 95% confidence interval
     */
    public double confidenceLo() {
        if (mTrials <= 1) {
            return Double.NaN; // Confidence interval is undefined for a single trial
        }
        double mean = mean();
        double stddev = stddev();
        return mean - (CONFIDENCE_95 * stddev / Math.sqrt(mTrials));
    }

    /**
     * Calculates the high endpoint of the 95% confidence interval for the mean of the percolation thresholds.
     *
     * @return the high endpoint of the 95% confidence interval
     */
    public double confidenceHi() {
        if (mTrials <= 1) {
            return Double.NaN; // Confidence interval is undefined for a single trial
        }
        double mean = mean();
        double stddev = stddev();
        return mean + (CONFIDENCE_95 * stddev / Math.sqrt(mTrials));
    }

    /**
     * Main method to run the PercolationStats program.
     * It accepts command line arguments for grid size and number of trials.
     * If no arguments are provided, it uses default values.
     *
     * @param args command line arguments: [grid size] [number of trials]
     */
    public static void main(String[] args) {
        int n = DEFAULT_N; // Default grid size
        int trials = DEFAULT_TRIALS; // Default number of trials

        // Parse command line arguments if provided
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
                if (n <= 0) {
                    throw new IllegalArgumentException("n must be greater than 0");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid grid size, using default: " + DEFAULT_N);
            }
        }
        if (args.length > 1) {
            try {
                trials = Integer.parseInt(args[1]);
                if (trials <= 0) {
                    throw new IllegalArgumentException("trials must be greater than 0");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number of trials, using default: " + DEFAULT_TRIALS);
            }
        }

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.printf("mean                    = %.16f%n", stats.mean());
        System.out.printf("stddev                  = %.16f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]%n", stats.confidenceLo(), stats.confidenceHi());
    }

    /**
     * Performs a single percolation trial on an n-by-n grid.
     * It opens sites in a random order until the system percolates.
     * Returns the fraction of sites that were opened when percolation occurred.
     *
     * @param n the size of the grid (n x n)
     * @return the fraction of sites opened when percolation occurred
     */
    private static double performPercolationTrial(int n) {
      Percolation perc = new Percolation(n);
      int[] pickingSiteOrder = StdRandom.permutation(n * n);
      for (int i = 0; i < pickingSiteOrder.length; i++) {
          int row = pickingSiteOrder[i] / n + 1; // Convert to 1-based index
          int col = pickingSiteOrder[i] % n + 1; // Convert to 1-based index
          perc.open(row, col);
          if (perc.percolates()) {
              return (double) (i + 1) / (n * n); // Return the percolation threshold
          }
      }
      return -1.0; // This path should not be reached
    }
}
