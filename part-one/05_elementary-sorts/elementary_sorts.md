# Elementary Sorts

## Rules of the game

- Goal: Sort _any_ type of data.
- Callback - reference to executable code

  - Java: interfaces.
  - C: function pointers.
  - C++: class-type functors
  - C#: delegate.
  - Python, JavaScript: first-class functions.

- Total order: is a binary relation $\leq$ that satisfies:

  - Antisymmetry: $a \leq b$ and $b \leq a$ implies $a = b$.
  - Transitivity: $a \leq b$ and $b \leq c$ implies $a \leq c$.
  - Totality: for any $a$ and $b$, either $a \leq b$ or $b \leq a$ (or both, if they are equal).
  - _E.g.: Standard order for numbers, chronological order for dates or times, lexicographic order for strings._
  - _The $\leq$ operator for `double` and `float` types is not a total order! (`Double.NaN <= Double.NaN` is false.)_

- `Comparable` API:
  - Implement `compareTo()` so that `v.compareTo(w)`:
    - Is a total order.
    - Returns a negative number if `v < w`, zero if `v == w`, and a positive number if `v > w`.
    - Throws an exception if incompatible types (or either is `null`).

## Selection Sort

- Idea: In iteration `i`, find index `min` of the smallest element in the subarray `a[i..N-1]`, and swap it with `a[i]`.

- Invariant: At the start of iteration `i`, the subarray `a[0..i-1]` is sorted; No elements in `a[i..N-1]` are smaller than any element in `a[0..i-1]`.

- Analysis:
  - Uses $\sim N^2 / 2$ compares and $N$ exchanges.
  - Time: $O(N^2)$.
  - Space: $O(1)$ (in-place).
  - Stability: Not stable (equal elements may change relative order).
  - _Insensitivity to input_: Quadratic time for all inputs, including sorted and reverse-sorted.
  - _Minimal data movement_: Linear number of exchanges.

## Insertion Sort

- Idea: In iteration `i`, swap `a[i]` with each larger entry to its left (from right to left).

- Invariant: At the start of iteration `i`, the subarray `a[0..i-1]` is sorted; elements `a[i..N-1]` _have not yet been seen_.

- Analysis:

  - Uses $\sim N^2 / 4$ compares and $\sim N^2 / 4$ exchanges on average.
  - Time: $O(N^2)$.
  - Space: $O(1)$ (in-place).
  - Stability: Stable (equal elements retain relative order).
  - _Best case_: If the array is already sorted, only $N-1$ compares and no exchanges.
  - _Worst case_: If the array is reverse-sorted (and no duplicates), $\sim N^2 / 2$ compares and $\sim N^2 / 2$ exchanges.
  - _Adaptive_: Linear time for nearly sorted data.

- An array is **_partially sorted_** if the number of _inversions_ is $\leq c N$.

  - An _inversion_ is a pair of keys that are out of order.
  - _E.g.: In the array `[1, 5, 2, 4]`, the inversions are `(5, 2)`, `(5, 4)`, and `(2, 4)`._

- For **_partially sorted_** arrays, insertion sort runs **_in linear time_** (with number of exchanges equals the number of inversions).

## Shellsort

- Idea: Move entries more than one position at a time by _h-sorting_ the array.

  - Basically, _insertion sort, with stride length `h`_.

- Proposition: A g-sorted array remains g-sorted after h-sorting it.

- Shellsort: Choose a sequence of `h` values, and h-sort the array for each `h` (from max value to `1`). (Ok-ish sequence: $h = 3x + 1$, until $h < N$.)

- Analysis:

  - Uses $O(N^{3/2})$ number of compares in the worst case with the $3x + 1$ increment.
  - Time: $O(N^{3/2})$.
  - Space: $O(1)$ (in-place).

- Practice usage:
  - Fast for small arrays.
  - Tiny, fixed footprint for code (used in embedded systems).

## Shuffling

- Goal: Rearrange array so that the result is a uniformly random permutation.

- Shuffle sort: Generate a random real number for each array entry, then sort the array by these numbers.

- Knuth shuffle: In iteration `i`, pick integer `r` between `0` and `i`, and swap `a[i]` with `a[r]`.

  - _Also a correct variant: pick between `i` and `N-1`._
  - _Common bug: pick between `0` and `N-1`_

  - (_Fisher-Yates_) Knuth shuffling algorithm produces a uniformly random permutation _in linear time_.

## Convex hull

- Convex hull: The smallest convex polygon that contains all points in a set (whose vertices are points in the set).

  - Output: Sequence of points in counter-clockwise order.

- Applications:

  - Motion planning: find shortest path (from 2 points) around obstacles.
  - Farthest pair: find the two points that are farthest apart (those are extreme points of the convex hull).

- Graham scan demonstration:

  - Find the point with the lowest y-coordinate (and leftmost in case of a tie).
  - Sort the points by polar angle relative to this point.
  - Traverse the sorted points, maintaining a stack of points that form the convex hull.
  - _Points that are in convex hull are those that create a **CCW turn**_

- CCW - Given 3 points $a$, $b$ and $c$, is $a \to b \to c$ a counter-clockwise turn? (_whether c is to the left of the line from a to b_)

  - _Determinant_ (cross product) gives 2x signed area of planar triangle.
  - If the determinant is positive, then the turn is CCW; if negative, it is CW; if zero, the points are collinear.
