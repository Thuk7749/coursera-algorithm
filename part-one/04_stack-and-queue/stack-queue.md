# Bags, Queues, and Stacks

## Stack

```python
class Stack<T>:
    def __init__(self):
        """Initialize an empty stack."""

    def is_empty(self) -> bool:
        """Check if the stack is empty."""

    def push(self, item) -> None:
        """Add an item to the top of the stack."""

    def pop(self) -> T:
        """Remove and return the item at the top of the stack."""

    def size(self) -> int:
        """Return the number of items in the stack."""
```

- Linked-list representation

- Array implementation

## Resizing-array implementation (for stack)

- If array is full, create a new array of twice the size, and copy items.

- Shrink array to half size if it is _one-quarter (25%) full_.

### Resizing array vs. Linked list:

- Linked list: (each operation's time matters)
  - Every operation takes constant time in the worst case.
  - Uses extra time and space to deal with the links (pointers/references).
- Resizing array: (total time matters)
  - Every operation takes constant _amortized_ time.
  - Less wasted space.

## Queue

```python
class Queue<T>:
    def __init__(self):
        """Initialize an empty queue."""

    def is_empty(self) -> bool:
        """Check if the queue is empty."""

    def enqueue(self, item) -> None:
        """Add an item to the end of the queue."""

    def dequeue(self) -> T:
        """Remove and return the item at the front of the queue."""

    def size(self) -> int:
        """Return the number of items in the queue."""
```

## Generic

- In Java, array of generic type is not allowed.

  ```java
  class Stack<T> {
      private T[] items;

      public Stack() {
          items = new T[10]; // Cannot create an array of generic type
      }
  }
  ```

  - Caveat: You can create an array of `Object` type and cast it to the generic type.

    ```java
    class Stack<T> {
        private Object[] items;

        public Stack() {
            items = (T[]) new Object[10]; // Create an array of Object and cast it to T
        }
    }
    ```

## Bag

- Main application: adding items to a collection and iterating (when _order does not matter_).
- Implementation: stack (without pop operation) or queue (without dequeue operation).

```python
class Bag<T>:
    def __init__(self):
        """Initialize an empty bag."""

    def is_empty(self) -> bool:
        """Check if the bag is empty."""

    def add(self, item) -> None:
        """Add an item to the bag."""

    def size(self) -> int:
        """Return the number of items in the bag."""

    def __iter__(self):
        """Return an iterator to iterate over the items in the bag."""
```

## Application
