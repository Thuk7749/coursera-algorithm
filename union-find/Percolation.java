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
     * Constructs a Percolation object for an n-by-n grid, with all sites initially blocked.
     *
     * @param n the size of the grid (n x n)
     * @throws IllegalArgumentException if n is less than or equal to 0
     */
    public Percolation(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        // Initialize the grid and other necessary data structures
        _n = n;
        _isBlocked = new boolean[n * n];
        _openSitesCount = 0;
        _virtualBottomSite = n * n;
        _virtualTopSite = n * n + 1;
        _openSites = new WeightedQuickUnionUF(n * n + 2);

        // All sites are initially blocked
        for (int i = 0; i < n * n; i++) {
            _isBlocked[i] = true; // true means blocked 
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
    public void open(int row, int col) throws IllegalArgumentException {
        _validateIndices(row, col);
        if (isOpen(row, col)) {
            return; // Site is already open 
        }

        // Open the site
        _isBlocked[_flattenedIndexOf(row, col)] = false;
        _openSitesCount++;

        // Connect to adjacent open sites
        _tryConnectSites(row, col, row - 1, col);
        _tryConnectSites(row, col, row + 1, col);
        _tryConnectSites(row, col, row, col - 1);
        _tryConnectSites(row, col, row, col + 1);
    }

    /**
     * Checks if the site at the specified row and column is open.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return true if the site is open, false otherwise
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public boolean isOpen(int row, int col) throws IllegalArgumentException {
        _validateIndices(row, col);
        return !_isBlocked[_flattenedIndexOf(row, col)];
    }

    /**
     * Checks if the site at the specified row and column is full.
     * A site is considered full if it is open and connected to the virtual top site.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return true if the site is full, false otherwise
     * @throws IllegalArgumentException if row or col is out of bounds
     */
    public boolean isFull(int row, int col) throws IllegalArgumentException {
        _validateIndices(row, col);
        return _isBlocked[_flattenedIndexOf(row, col)];
    }

    /**
     * Returns the number of open sites in the grid.
     *
     * @return the count of open sites
     */
    public int numberOfOpenSites() {
        return _openSitesCount;
    }

    /**
     * Checks if the system percolates.
     * The system percolates if there is a path of open sites from the top row to the bottom row.
     *
     * @return true if the system percolates, false otherwise
     */
    public boolean percolates() {
        int topCanonicalIndex = _openSites.find(_virtualTopSite);
        int bottomCanonicalIndex = _openSites.find(_virtualBottomSite);
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
                System.out.println("Percolation threshold: " + (double)(i + 1) / (n * n));
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
    private void _tryConnectSites(int connectingRow, int connectingCol, int connectedRow, int connectedCol) {
        int connectingIndex = _flattenedIndexOf(connectingRow, connectingCol);

        // Connect to virtual top or bottom site if applicable
        if (connectedRow == 0 && connectedCol > 0 && connectedCol <= _n) {
            _openSites.union(connectingIndex, _virtualTopSite);
            return;
        }
        if (connectedRow == _n + 1 && connectedCol > 0 && connectedCol <= _n) {
            _openSites.union(connectingIndex, _virtualBottomSite);
            return;
        }
        
        if (!_isValidIndex(connectedRow, connectedCol) || !isOpen(connectedRow, connectedCol)) {
            return; // Connected site is out of bounds or blocked
        }

        _openSites.union(connectingIndex, _flattenedIndexOf(connectedRow, connectedCol));
    }

    /**
     * Validates the given row and column indices.
     * Throws an IllegalArgumentException if the indices are out of bounds.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @throws IllegalArgumentException if the indices are invalid
     */
    private void _validateIndices(int row, int col) throws IllegalArgumentException {
        if (row < 1 || row > _n || col < 1 || col > _n) {
            throw new IllegalArgumentException("row and col must be between 1 and " + _n);
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
    private boolean _isValidIndex(int row, int col) {
        return row >= 1 && row <= _n && col >= 1 && col <= _n;
    }

    /**
     * Converts 2D grid coordinates (row, col) to a 1D index for the blocked grid.
     * The conversion is based on a 1-based index system.
     *
     * @param row the row index (1-based)
     * @param col the column index (1-based)
     * @return the flattened index corresponding to the site at (row, col)
     */
    private int _flattenedIndexOf(int row, int col) {
        return (row - 1) * _n + (col - 1);
    }

    /**
     * The size of the grid (n x n).
     */
    private final int _n;

    /**
     * The grid represented as a 1D array where each index corresponds to a site.
     * A site is blocked if the value is true, and open if false.
     */
    private boolean[] _isBlocked;

    /**
     * The count of open sites in the grid.
     */
    private int _openSitesCount;

    /**
     * Union-Find data structure to manage connectivity of open sites.
     * It includes virtual top and bottom sites for efficient percolation checks.
     */
    private WeightedQuickUnionUF _openSites;

    /**
     * Virtual site connected to the top row to simplify percolation checking.
     */
    private final int _virtualTopSite;

    /**
     * Virtual site connected to the bottom row to simplify percolation checking.
     */
    private final int _virtualBottomSite;

    /**
     * Default grid size if not specified in the command line arguments.
     */
    private static final int DEFAULT_GRID_SIZE = 20; // Default grid size if not specified
}
