import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    /**
     * An array holding the items in the deque in a circular manner.
     */
    private Item[] items;

    /**
     * The size of the deque, which is the number of items currently in it.
     */
    private int size;

    /**
     * The capacity of the deque, which is the size of the underlying array.
     * It is always a power of 2 to facilitate circular indexing.
     */
    private int capacity; // Capacity of the deque

    /**
     * The front index is the index of the first item in the deque.
     */
    private int front;
    
    /**
     * The back index is the index of the item after the last item.
     * (Except when the deque is full, in which case it equals to front.)
     */
    private int back;

    /**
     * Creates an empty randomized queue.
     * Initializes the underlying array with a capacity of 1.
     */
    public RandomizedQueue() {
        this.capacity = 1; // Initial capacity
        this.items = (Item[]) new Object[capacity]; // Create an array of items
        this.size = 0;
        this.front = 0;
        this.back = 0;
    }

    /**
     * Checks if the randomized queue is empty.
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the randomized queue.
     * @return the size of the queue.
     */
    public int size() {
        return size;
    }

    /**
     * Adds an item to the back of the deque.
     * If the deque is full, it doubles its capacity.
     * @param item the item to be added to the back.
     * @throws IllegalArgumentException if the item is null.
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        items[back] = item; // Add item at the current back index (except when the deque is empty)
        back = (back + 1) % capacity; // Move back forward
        size++;
    }

    // remove and return a random item
    /**
     * Removes and returns a random item from the randomized queue.
     * @return the removed item.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randomIndex = StdRandom.uniformInt(size);
        Item removingItem = items[randomIndex];
        int lastItemIndex = (back - 1 + capacity) % capacity;
        items[randomIndex] = items[lastItemIndex]; // Replace the removed item with the last item
        items[lastItemIndex] = null; // Clear the last item
        back = lastItemIndex; // Move back backward
        size--;
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }
        return removingItem;
    }

    /**
     * Returns a random item from the randomized queue without removing it.
     * @return a random item.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randomIndex = StdRandom.uniformInt(size);
        return items[randomIndex];
    }

    /**
     * An iterator for the randomized queue.
     * It iterates over all items in the queue in random order.
     */
    private class RandomizedQueueIterator implements Iterator<Item> {
        /**
         * An array to hold the items in random order.
         * It is initialized with a shuffled copy of the items in the queue.
         */
        private Item[] shuffledItems;

        /**
         * The current index in the shuffled items array.
         * It starts at 0 and increments as items are iterated over.
         */
        private int currentIndex;

        /**
         * Constructor for the RandomizedQueueIterator.
         * It creates a shuffled copy of the items in the queue.
         */
        public RandomizedQueueIterator() {
            shuffledItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                shuffledItems[i] = items[(front + i) % capacity];
            }
            // Shuffle the array
            for (int i = size - 1; i > 0; i--) {
                int randomIndex = StdRandom.uniformInt(i + 1);
                Item temp = shuffledItems[i];
                shuffledItems[i] = shuffledItems[randomIndex];
                shuffledItems[randomIndex] = temp;
            }
            currentIndex = 0;
        }

        /**
         * Checks if there are more items to iterate over.
         * @return true if there are more items, false otherwise.
         */
        public boolean hasNext() {
            return currentIndex < shuffledItems.length;
        }

        /**
         * Returns the next item in the iteration.
         * @return the next item.
         * @throws NoSuchElementException if there are no more items to iterate over.
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate");
            }
            return shuffledItems[currentIndex++];
        }

        /**
         * Removes the current item from the iteration.
         * This operation is not supported in this iterator.
         * @throws UnsupportedOperationException if called.
         */
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    /**
     * Returns an independent iterator over the items in random order.
     * @return an iterator for the randomized queue.
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /**
     * Returns a string representation of the deque, showing the items from front to back.
     * @return a string representation of the deque.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Item item : this) {
            sb.append(item == null ? "null" : item).append(" ");
        }
        if (size > 0) {
            sb.setLength(sb.length() - 1); // Remove the last space
        }
        sb.append("]");
        return sb.toString().trim();
    }

    /**
     * Main method for testing the RandomizedQueue class.
     * It demonstrates the functionality of the queue with various operations.
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        // main method body
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        // Test isEmpty and size on empty queue
        System.out.println("Is empty? " + rq.isEmpty()); // true
        System.out.println("Size: " + rq.size()); // 0

        // Test enqueue
        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        System.out.println("Is empty? " + rq.isEmpty()); // false
        System.out.println("Size: " + rq.size()); // 3

        // Test sample
        System.out.println("Sample: " + rq.sample());

        // Test iterator
        System.out.printf("Iterator: %s", rq); 
        System.out.println();

        // Test dequeue
        System.out.println("Dequeued: " + rq.dequeue());
        System.out.println("Size after dequeue: " + rq.size());

        // Test iterator again
        System.out.printf("Iterator after dequeue: %s", rq);
        System.out.println();
    }

    /**
     * Resizes the underlying array to a new capacity.
     * It copies the items from the old array to the new array in a circular manner.
     * @param newCapacity the new capacity for the deque.
     */
    private void resize(int newCapacity) {
        if (newCapacity < size) {
            throw new IllegalArgumentException("New capacity must be greater than or equal to the current size");
        }
        Item[] newItems = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(front + i) % capacity];
        }
        items = newItems;
        front = 0;
        back = size; // back is now the index after the last item
        capacity = newCapacity;
    }
}
