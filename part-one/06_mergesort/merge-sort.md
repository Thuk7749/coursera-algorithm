# Merge sort

## Overview

- Basic plan:

  - Divide the array into two halves.
  - Recursively sort each half.
  - Merge two (sorted) halves.

- Mergesort uses at most $N \lg N$ compares and $6 N \lg N$ array accesses to sort any array of $N$ items.

- Mergesort uses extra space proportional to $N$.

  - A sorting algorithm is _in-place_ if it uses $\leq c \log N$ extra memory, (like _insertion sort, selection sort, shell sort_).

- Practical improvements:
  - For small subarrays, use insertion sort (cutoff to insertion sort for $\approx 7$ items).
  - Stop if already sorted (check if the first item in the right half is greater than the last item in the left half) (helps for partially-ordered arrays).
  - Eliminate the copy to the auxiliary array (save time, not space).

## Bottom-up mergesort

- Basic plan:
  - Pass through the array, merging subarrays of size 1.
  - Repeat, merging subarrays of size 2, then 4, then 8, etc.
  - _Simple and non-recursive_ version of mergesort, but may about 10% slower than the recursive version.

## Sorting complexity

- Complexity of sorting - Framework for study efficienc of algorithms for solving a particular problem $X$.

  - Model of computation: Allowable operations.
  - Cost model: Operation count(s).
  - Upper bound: Cost guarantee provided by **some** algorithm for $X$.
  - Lower bound: Proven limit on cost guarantee of **all** algorithms for $X$.
  - Optimal algorithm: Algorithm with best possible cost guarantee for $X$.

- Lower bound may not hold if the algorith has information about:
  - The initial order of the input.  
    _Partially-ordered arrays_: sorting may not need $N \lg N$ compares. E.g., it only needs $N - 1$ compares for insertion sort.
  - The distribution of key values.  
    _Duplicate keys_: sorting may not need $N \lg N$ compares. E.g., 3-way partitioning quicksort.
  - The representation o the keys.  
    _Digital properties of keys_: we can use digit/character compares instead of key compares for numbers and strings. E.g., radix sort.

## Comparators

- Decouples the definition of the data type from the definition of what it means to compare two items of that type.
  - Use `Object` instead of `Comparable` interface.
  - Pass a `Comparator` object to the sorting method.
- To implement a `Comparator` (at least in Java):
  - Define a (nested) class that implements the `Comparator` interface.
  - Implement the `compare()` method.

## Stability

- A _stable_ sort preserves the relative order of items with equal keys.

  - Insertion sort and mergesort are stable.
  - **Selection sort** and **shellsort** are not stable.
