# Analysis of Algorithms

## Introduction

- Reason to analyze algorithms:

  - Main reason: **avoid performance bugs**.
  - Predict performance.
  - Compare algorithms.
  - Provide guarantees.
  - _Understand theoretical basis_.

- Challengue: How to solve a large practical input (time + memory efficiency).

- Scientific method: Observe, Hypothesize, Predict, Verify, Validate.

## Observations

- Measuring the running time (for various input sizes).
- _log-log plot_ (_plot running time `T(N)` vs. input size `N`_) + Regression: predict running time for larger inputs.
  - Fit the straight line to the log-log plot, providing $\log (T(N)) = b * \log N + c$ ($b$ is the slope, $c$ is the intercept).
  - The slope $b$ gives the **order of growth** of the running time.
  - The intercept $c$ gives the **constant / logarithmic factor** of the running time.
  - Expected running time: $T(N) = a * N^b$ (where $a = 2^c$) (_power law_).
- _double hypothesis_: quick way estimate $b$ (but cannot estimate $c$).

## Mathematical Models

- Total running time: sum of (cost x frequency) for all operation.

  - Cost depends on machine, compiler, etc.
  - Frequency depends on algorithm and input data.
  - Set of operations obtained by analyzing the algorithm.

- Cost model: use some basic operation (_the most expensive, the most frequent one, etc._) as a proxy for running time.
- Tilde notation (`~`): ignore lower order terms and constant factors.
  - Example: $freq(sum) = 3N^2 + 5N + 7 \sim 3N^2$ or $freq(assignment) = 1 + 2 + 3 + ... + N \sim \frac{1}2n^2$.

## Common Order-of-Growth Classification

| Order of growth | Name           | Description        | Example               |
| --------------- | -------------- | ------------------ | --------------------- |
| $1$             | _constant_     | statement          | add two numbers       |
| $\log N$        | _logarithmic_  | divide in half     | binary search         |
| $N$             | _linear_       | single loop        | find the maximum      |
| $N \log N$      | _linearithmic_ | divide and conquer | merge sort            |
| $N^2$           | _quadratic_    | nested loops       | bubble sort           |
| $N^3$           | _cubic_        | three nested loops | matrix multiplication |
| $2^N$           | _exponential_  | exhaustive search  | check all subsets     |

- Guiding principle: Typically, better order of growth $\implies$ faster in practice.

## Theory of algorithms

- Types of analysis:
  - **Worst-case**: upper bound on cost.
  - **Average-case**: "expected" cost.
  - **Best-case**: lower bound on cost.

| Notation  | Provides                                            | Example       | Used to                                                          |
| --------- | --------------------------------------------------- | ------------- | ---------------------------------------------------------------- |
| Big Theta | asymptotic ("approuch to infinity") order of growth | $\Theta(N^2)$ | classify algorithms                                              |
| Big Oh    | $\Theta(N^2)$ and smaller                           | $O(N^2)$      | upper bound (a specific algorithm)                               |
| Big Omega | $\Theta(N^2)$ and larger                            | $\Omega(N^2)$ | lower bound (proof that no algorithm can do better)              |
| _Bonus_   |                                                     |               |                                                                  |
| Tilde     | leading term                                        | $\sim 3N^2$   | provide approximate model (_accurately describes the algorithm_) |

## Memory (requirement)

- Typical memory usage for primitive types:

| type  | `boolean` | `byte` | `char` | `int` | `float` | `long` | `double` |
| ----- | --------- | ------ | ------ | ----- | ------- | ------ | -------- |
| bytes | 1         | 1      | 2      | 4     | 4       | 8      | 8        |

- Typical memory usage for arrays:

| type  | `char[]` | `int[]` | `double[]` | `char[][]` | `int[][]` | `double[][]` |
| ----- | -------- | ------- | ---------- | ---------- | --------- | ------------ |
| bytes | 2N + 24  | 4N + 24 | 8N + 24    | ~ 2 N M    | ~ 4 N M   | ~ 8 N M      |

(In Java, the 24 overhead is for the object header (24 bytes), array length (4 bytes), and padding (4 bytes) for alignment).
