import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

/**
 * The {@code Percolation} class models an n-by-n percolation system using the union-find data structure.
 * Each site is either open or blocked. The system percolates if there is a path of open sites from the top row to the bottom row.
 * <p>
 * Features:
 * <ul>
 *   <li>Supports opening sites, checking if a site is open or full, counting open sites, and checking percolation.</li>
 *   <li>Uses virtual top and bottom sites to efficiently determine percolation.</li>
 *   <li>Implements union-find (WeightedQuickUnionUF) to manage site connectivity.</li>
 * </ul>
 * <p>
 * Indices for rows and columns are 1-based.
 * <p>
 * Example usage:
 * <pre>
 *     Percolation perc = new Percolation(5);
 *     perc.open(1, 3);
 *     boolean isOpen = perc.isOpen(1, 3);
 *     boolean doesPercolate = perc.percolates();
 * </pre>
 *
 * <h2>Implementation Notes</h2>
 * <ul>
 *   <li>Sites are represented as a 1D array, where {@code true} means blocked and {@code false} means open.</li>
 *   <li>Virtual top and bottom sites are used to simplify percolation checks.</li>
 *   <li>Union-Find structure (WeightedQuickUnionUF) is used to efficiently manage connectivity.</li>
 *   <li>All indices are 1-based for public API methods.</li>
 * </ul>
 *
 * <h2>API</h2>
 * <ul>
 *   <li>{@link #Percolation(int)}: Constructs an n-by-n grid, with all sites initially blocked.</li>
 *   <li>{@link #open(int, int)}: Opens the site at the specified row and column.</li>
 *   <li>{@link #isOpen(int, int)}: Checks if the site at the specified row and column is open.</li>
 *   <li>{@link #isFull(int, int)}: Checks if the site at the specified row and column is full (connected to the top).</li>
 *   <li>{@link #numberOfOpenSites()}: Returns the number of open sites in the grid.</li>
 *   <li>{@link #percolates()}: Checks if the system percolates.</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>
 *     Percolation perc = new Percolation(5);
 *     perc.open(1, 3);
 *     boolean isOpen = perc.isOpen(1, 3);
 *     boolean doesPercolate = perc.percolates();
 * </pre>
 *
 * <h2>Exceptions</h2>
 * <ul>
 *   <li>Throws {@link IllegalArgumentException} if grid size is less than or equal to 0, or if indices are out of bounds.</li>
 * </ul>
 */
public class Percolation {
    /**
     * Default grid size if not specified in the command line arguments.
     */
    private static final int DEFAULT_GRID_SIZE = 20; // Default grid size if not specified

    /**
     * The size of the grid (n x n).
     */
    private final int mGridSize;

    /**
     * The grid represented as a 1D array where each index corresponds to a site.
     * A site is blocked if the value is true, and open if false.
     */
    private boolean[] mIsBlocked;

    /**
     * The count of open sites in the grid.
     */
    private int mOpenSitesCount;

    /**
     * Union-Find data structure to manage connectivity of open sites.
     * It includes virtual top and bottom sites for efficient percolation checks.
     */
    private WeightedQuickUnionUF mOpenSites;

    /**
     * Virtual site connected to the top row to simplify percolation checking.
     */
    private final int mVirtualTopSite;

    /**
     * Virtual site connected to the bottom row to simplify percolation checking.
     */
    private final int mVirtualBottomSite;

    /**
     * Constructs a Percolation object for an n-by-n grid, with all sites initially blocked.
     *
     * @param n the size of the grid (n x n)
     * @throws IllegalArgumentException if n is less than or equal to 0
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        // Initialize the grid and other necessary data structures
        mGridSize = n;
        mIsBlocked = new boolean[n * n];
        mOpenSitesCount = 0;
        mVirtualBottomSite = n * n;
        mVirtualTopSite = n * n + 1;
        mOpenSites = new WeightedQuickUnionUF(n * n + 2);

        // All sites are initially blocked
        for (int i = 0; i < n * n; i++) {
            mIsBlocked[i] = true; // true means blocked 
        }
    }

    /**
     * Opens the site at the specified row and column.
     * If the site is already open, it does nothing.
     * It connects the site to adjacent open sites if they exist.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public void open(int row, int col) {
        validateIndices(row, col);
        if (isOpen(row, col)) {
            return; // Site is already open 
        }

        // Open the site
        mIsBlocked[flattenedIndexOf(row, col)] = false;
        mOpenSitesCount++;

        // Connect to adjacent open sites
        tryConnectSites(row, col, row - 1, col);
        tryConnectSites(row, col, row + 1, col);
        tryConnectSites(row, col, row, col - 1);
        tryConnectSites(row, col, row, col + 1);
    }

    /**
     * Checks if the site at the specified row and column is open.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return true if the site is open, false otherwise
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return !mIsBlocked[flattenedIndexOf(row, col)];
    }

    /**
     * Checks if the site at the specified row and column is full.
     * A full site is an open site that can be connected to an open site in the top row
     * via a chain of neighboring (left, right, up, down) open sites
     *
     * @implNote Backwash: Once a system percolates,
     * sites connected to the bottom are indirectly connected to the top through the virtual sites.
     * @implNote Solution link cause i'm lazy:
     * https://stackoverflow.com/questions/61396690/how-to-handle-the-backwash-problem-in-percolation-without-creating-an-extra-wuf
     * 
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return true if the site is full, false otherwise
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return isOpen(row, col) && 
               mOpenSites.find(flattenedIndexOf(row, col)) == mOpenSites.find(mVirtualTopSite);
    }

    /**
     * Returns the number of open sites in the grid.
     *
     * @return the count of open sites
     */
    public int numberOfOpenSites() {
        return mOpenSitesCount;
    }

    /**
     * Checks if the system percolates.
     * The system percolates if there is a path of open sites from the top row to the bottom row.
     *
     * @return true if the system percolates, false otherwise
     */
    public boolean percolates() {
        int topCanonicalIndex = mOpenSites.find(mVirtualTopSite);
        int bottomCanonicalIndex = mOpenSites.find(mVirtualBottomSite);
        return topCanonicalIndex == bottomCanonicalIndex;
    }

    /**
     * The main method to run a Monte Carlo simulation for percolation.
     * It opens sites in a random order until the system percolates.
     * The percolation threshold is printed as a fraction of open sites to total sites.
     *
     * @param args command line arguments, where the first argument is the grid size (optional)
     */
    public static void main(String[] args) {
        // Take command line arguments for grid size
        int n = DEFAULT_GRID_SIZE;
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid grid size provided. Using default grid size: " + DEFAULT_GRID_SIZE);
            }
        }

        Percolation perc = new Percolation(n);
        int[] pickingSiteOrder = StdRandom.permutation(n * n);
        for (int i = 0; i < pickingSiteOrder.length; i++) {
            int row = pickingSiteOrder[i] / n + 1; // Convert to 1-based index
            int col = pickingSiteOrder[i] % n + 1; // Convert to 1-based index
            perc.open(row, col);
            if (perc.percolates()) {
                System.out.println("Percolation occurred after opening " + (i + 1) + " sites.");
                System.out.println("Percolation threshold: " + (double) (i + 1) / (n * n));
                break;
            }
        }
    }

    /**
     * Attempts to connect the site at (connectingRow, connectingCol) to the site at (connectedRow, connectedCol).
     * If the connected site is a virtual top or bottom site, it connects accordingly.
     * If the connected site is blocked or out of bounds, it does nothing.
     * 
     * @implNote The connecting site should be open before calling this method.
     *
     * @param connectingRow    the row index of the site to connect from (1-based)
     * @param connectingCol    the column index of the site to connect from (1-based)
     * @param connectedRow     the row index of the site to connect to (1-based)
     * @param connectedCol     the column index of the site to connect to (1-based)
     */
    private void tryConnectSites(int connectingRow, int connectingCol, int connectedRow, int connectedCol) {
        int connectingIndex = flattenedIndexOf(connectingRow, connectingCol);

        // Connect to virtual top or bottom site if applicable
        if (connectedRow == 0 && connectedCol > 0 && connectedCol <= mGridSize) {
            mOpenSites.union(connectingIndex, mVirtualTopSite);
            return;
        }
        if (connectedRow == mGridSize + 1 && connectedCol > 0 && connectedCol <= mGridSize) {
            mOpenSites.union(connectingIndex, mVirtualBottomSite);
            return;
        }
        
        if (!isValidIndex(connectedRow, connectedCol) || !isOpen(connectedRow, connectedCol)) {
            return; // Connected site is out of bounds or blocked
        }

        mOpenSites.union(connectingIndex, flattenedIndexOf(connectedRow, connectedCol));
    }

    /**
     * Validates the given row and column indices.
     * Throws an IllegalArgumentException if the indices are out of bounds.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @throws IllegalArgumentException if the indices are invalid
     */
    private void validateIndices(int row, int col) {
        if (row < 1 || row > mGridSize || col < 1 || col > mGridSize) {
            throw new IllegalArgumentException("row and col must be between 1 and " + mGridSize);
        }
    }

    /**
     * Checks if the given row and column indices are valid for the grid.
     * The indices are 1-based, so they must be between 1 and n (inclusive).
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return true if the indices are valid, false otherwise
     */
    private boolean isValidIndex(int row, int col) {
        return row >= 1 && row <= mGridSize && col >= 1 && col <= mGridSize;
    }

    /**
     * Converts 2D grid coordinates (row, col) to a 1D index for the blocked grid.
     * The conversion is based on a 1-based index system.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return the flattened index corresponding to the site at (row, col)
     */
    private int flattenedIndexOf(int row, int col) {
        return (row - 1) * mGridSize + (col - 1);
    }
}
