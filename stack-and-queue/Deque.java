import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

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

    /*
     * Initializes an empty deque with a default capacity of 1.
     */
    public Deque() {
        this.capacity = 1; // Initial capacity
        this.items = (Item[]) new Object[capacity]; // Create an array of items
        this.size = 0;
        this.front = 0;
        this.back = 0;
    }

    /**
     * Checks if the deque is empty.
     * @return true if the deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items currently in the deque.
     * @return the size of the deque.
     */
    public int size() {
        return size;
    }

    /**
     * Adds an item to the front of the deque.
     * If the deque is full, it doubles its capacity.
     * @param item the item to be added to the front.
     * @throws IllegalArgumentException if the item is null.
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        front = (front - 1 + capacity) % capacity; // Move front back
        items[front] = item; // Add item at the current front index
        size++;
    }

    /**
     * Adds an item to the back of the deque.
     * If the deque is full, it doubles its capacity.
     * @param item the item to be added to the back.
     * @throws IllegalArgumentException if the item is null.
     */
    public void addLast(Item item) {
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

    /**
     * Removes and returns the item from the front of the deque.
     * If the deque is empty, it throws a NoSuchElementException.
     * If the size drops to a quarter of the capacity, it halves the capacity.
     * @return the item removed from the front.
     * @throws NoSuchElementException if the deque is empty.
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item removingItem = items[front];
        items[front] = null; // Clear the item
        front = (front + 1) % capacity; // Move front forward
        size--;
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }
        return removingItem;
    }

    /**
     * Removes and returns the item from the back of the deque.
     * If the deque is empty, it throws a NoSuchElementException.
     * If the size drops to a quarter of the capacity, it halves the capacity.
     * @return the item removed from the back.
     * @throws NoSuchElementException if the deque is empty.
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        back = (back - 1 + capacity) % capacity; // Move back back
        Item removingItem = items[back];
        items[back] = null; // Clear the item
        size--;
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }
        return removingItem;
    }

    /**
     * An iterator for the Deque that allows iteration from front to back.
     */
    private class DequeIterator implements Iterator<Item> {
        /**
         * The current index in the iteration, starting from the front.
         */
        private int currentIndex; // Current index in the iteration
        /**
         * The index where the iteration ends, which is front + size.
         */
        private int endIndex;

        public DequeIterator() {
            this.currentIndex = front;
            this.endIndex = front + size;
        }

        /**
         * Checks if there are more items to iterate over.
         * @return true if there are more items, false otherwise.
         */
        public boolean hasNext() {
            return currentIndex != endIndex;
        }

        /**
         * Returns the next item in the iteration.
         * @return the next item.
         * @throws NoSuchElementException if there are no more items to iterate.
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate");
            }

            return items[currentIndex++ % capacity];
        }

        /**
         * Removes the current item from the iteration.
         * This operation is not supported in this implementation.
         * @throws UnsupportedOperationException if called.
         */
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    /**
     * Returns an iterator over the items in the deque from front to back.
     * @return an iterator for the deque.
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
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
     * Returns a string representation of the raw array used to store the items in the deque.
     * It shows the indices of the front and back items, and null for empty slots.
     * @return a string representation of the raw array.
     */
    private String rawArrayToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < capacity; i++) {
            if (i == front) {
                sb.append("f=");
            }
            if (i == back) {
                sb.append("b=");
            }
            sb.append(items[i] == null ? "null" : items[i]).append(" ");
        }
        if (capacity > 0) {
            sb.setLength(sb.length() - 1); // Remove the last space
        }
        sb.append("]");
        return sb.toString().trim();
    }

    /**
     * Main method for testing the Deque implementation.
     * It demonstrates adding, removing, and iterating through the deque.
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        // Test isEmpty and size on empty deque
        System.out.println("Is empty? " + deque.isEmpty()); // true
        System.out.println("Size: " + deque.size()); // 0

        // Add elements to front and back
        deque.addFirst(1);
        System.out.printf("Deque contents after adding 1 to front: %s%n", deque.rawArrayToString()); // [1]
        deque.addLast(2);
        System.out.printf("Deque contents after adding 2 to back: %s%n", deque.rawArrayToString()); // [1, 2]
        deque.addFirst(0);
        System.out.printf("Deque contents after adding 0 to front: %s%n", deque.rawArrayToString()); // [0, 1, 2]
        deque.addLast(3);
        System.out.printf("Deque contents after adding 3 to back: %s%n", deque.rawArrayToString()); // [0, 1, 2, 3]

        // Print size and isEmpty
        System.out.println("Is empty? " + deque.isEmpty()); // false
        System.out.println("Size: " + deque.size()); // 4

        // Iterate through deque
        System.out.printf("Deque contents: %s%n", deque); // [0, 1, 2, 3]
        System.out.println();

        // Remove elements from front and back
        System.out.println("Removed from front: " + deque.removeFirst()); // 0
        System.out.println("Removed from back: " + deque.removeLast());   // 3

        // Print size after removals
        System.out.println("Size after removals: " + deque.size()); // 2

        // Final contents
        System.out.printf("Final deque contents: %s%n", deque); // [1, 2]
        System.out.printf("Final raw array contents: %s%n", deque.rawArrayToString()); // [f=1 b=2 null null]
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