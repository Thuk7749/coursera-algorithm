# Union Find

## Steps to developing a usable algorithm\*

1. Model the problem.
2. Find an algorithm to solve it.
3. Fast enough? Fits in memory?
4. If not, figure out why.
5. Find a way to address the problem.
6. Iterate until satisfied.

## Dynamic connectivity (problem)

- Given a set of N objects.

  - Union command: connect two objects.
  - Find query: is there a path connecting two objects?

---

- Assumption: _"Is connect to" is an equivalence relation_ (reflexive, symmetric, transitive).
- Definition - _Connected components_: a maximal set of objects that are connected to each other.
  - Union command: Replace components containing two objects with their union.
  - Find query: Check if two objects are in the same component.

## Union-find data structure

```python
class UnionFind:
    def __init__(self, n) -> None:
        """
        Initialize the union-find structure with a given number of objects `n`.
        """

    def union(self, p, q) -> None:
        """
        Add a connection between two objects `p` and `q`.
        """

    def is_connected(self, p, q) -> bool:
        """
        Check if two objects `p` and `q` are connected (or whether they are in the same component).
        """
```

## Quick-find algorithm [eager approach]

- Data structure:

  - array `id[]` of size `n`,  
    _where `p` and `q` are connected if and only if they have the same id_.

- Find operation: Check if `id[p] == id[q]` $\rightarrow$ _$2$ array accesses_.
- Union operation: To connect `p` and `q`, change all entries whose id equals `id[p]` to `id[q]` $\rightarrow$ _at most $2n + 2$ array accesses_.

```python
class UnionFind:
    def __init__(self, n) -> None:
        """
        Initialize the union-find structure with a given number of objects `n`.
        """
        self.id = list(range(n))  # Each object is its own root initially.

    def union(self, p, q) -> None:
        """
        Add a connection between two objects `p` and `q`.
        """
        pid = self.id[p]
        qid = self.id[q]
        for i in range(len(self.id)):
            if self.id[i] == pid:
                self.id[i] = qid  # Change all entries with id[p] to id[q].

    def is_connected(self, p, q) -> bool:
        """
        Check if two objects `p` and `q` are connected (or whether they are in the same component).
        """
        return self.id[p] == self.id[q]

```

## Quick-union algorithm [lazy approach]

- Data structure:

  - array `id[]` of size `n`,  
    _where `id[i]` is the parent of `i`_  
    _and a **root** of object `i` is `id[id[...id[i] ..]]` (until `id[i] == i`)._ $\rightarrow$ _up to $n$ array accesses_.

- Find operation: Check if `root(p) == root(q)` $\rightarrow$ _$2$ **root** accesses_.
- Union operation: To connect `p` and `q`, set `id[root(p)] = root(q)` (_set the id of `p`'s root to the id of `q`'s root_) $\rightarrow$ _$2$ **root** accesses_.

```python
class UnionFind:
    def __init__(self, n) -> None:
        """
        Initialize the union-find structure with a given number of objects `n`.
        """
        self.id = list(range(n))  # Each object is its own root initially.

    def _root(self, i) -> int:
        """
        Find the root of object `i`.
        """
        while self.id[i] != i:
            i = self.id[i]  # Follow the chain until we reach the root.
        return i

    def union(self, p, q) -> None:
        """
        Add a connection between two objects `p` and `q`.
        """
        root_p = self._root(p)
        root_q = self._root(q)
        if root_p != root_q: # Optional
            self.id[root_p] = root_q  # Connect the roots.

    def is_connected(self, p, q) -> bool:
        """
        Check if two objects `p` and `q` are connected (or whether they are in the same component).
        """
        return self._root(p) == self._root(q)

```

## Improvements

### Weighted quick-union

- Idea: Balance the size of the trees, by always attaching the smaller tree to **the root** of the larger tree.

- Data structure: Same as quick-union, but additionally maintain:

  - `size[]` array of size `n`,  
    _where `size[i]` is the size of the tree rooted at `i`_.

  ```python
  def __init__(self, n) -> None:
      """
      Initialize the union-find structure with a given number of objects `n`.
      """
      self.id = list(range(n))  # Each object is its own root initially.
      self.size = [1] * n  # Initialize size of each tree to 1.
  ```

- Find operation: Same as quick-union.

- Union operation: Additionally link root of smaller tree to root of larger tree, and update the size array $\rightarrow$ _depth of any node $x$ is at most_ $log_{2}N$.

  ```python
  def union(self, p, q) -> None:
      """
      Add a connection between two objects `p` and `q`.
      """
      root_p = self._root(p)
      root_q = self._root(q)

      if root_p == root_q:
          return
      if self.size[root_p] < self.size[root_q]:
          self.id[root_p] = root_q  # Attach smaller tree to larger tree.
          self.size[root_q] += self.size[root_p]  # Update size.
      else:
          self.id[root_q] = root_p
          self.size[root_p] += self.size[root_q]
  ```

### Path compression

- Idea: Just after computing the root of `p`, set the id of each examined node to point to that root.

  - _Two-pass implementation_: add a second loop to `_root()` to set the id of each examined node to the root.
  - _One-pass implementation_ (simpler): make every other node in path point to its grandparent, effectively halving the path length.

  ```python
  def _root(self, i) -> int:
      """
      Find the root of object `i` with path compression.
      """
      while self.id[i] != i:
          id[i] = self.id[self.id[i]]  # Make every other node point to its grandparent.
          i = self.id[i]
      return i
  ```

## Complexity analysis

- With $M$ union-find operations on a set of $N$ objects:

  | Algorithm                         | Worst-case time  |
  | --------------------------------- | ---------------- |
  | quick-find                        | $MN$             |
  | quick-union (QU)                  | $MN$             |
  | weighted QU                       | $N + M \log N$   |
  | QU with path compression          | $N + M \log N$   |
  | weighted QU with path compression | $N + M \log_2 N$ |
