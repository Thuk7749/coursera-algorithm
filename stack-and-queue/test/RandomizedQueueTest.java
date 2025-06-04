import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class RandomizedQueueTest {

    private RandomizedQueue<Integer> queue;

    @BeforeEach
    public void setUp() {
        queue = new RandomizedQueue<>();
    }

    @Test
    public void testConstructor() {
        assertNotNull(queue);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, queue.size());
        queue.enqueue(1);
        assertEquals(1, queue.size());
        queue.enqueue(2);
        assertEquals(2, queue.size());
        queue.dequeue();
        assertEquals(1, queue.size());
        queue.dequeue();
        assertEquals(0, queue.size());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        assertEquals(1, queue.size());
        queue.enqueue(2);
        assertEquals(2, queue.size());
        
        // Test null item
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(null));
    }

    @Test
    public void testDequeue() {
        // Test empty queue
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
        
        // Add items and verify dequeue
        queue.enqueue(1);
        assertEquals(1, queue.dequeue().intValue());
        assertTrue(queue.isEmpty());
        
        // Test with multiple items
        Set<Integer> values = new HashSet<>();
        values.add(10);
        values.add(20);
        values.add(30);
        
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        
        // Each dequeue should return one of our values
        for (int i = 0; i < 3; i++) {
            Integer value = queue.dequeue();
            assertTrue(values.contains(value));
            values.remove(value);
        }
        
        // Queue should be empty after dequeuing all items
        assertTrue(queue.isEmpty());
        assertEquals(0, values.size());
    }

    @Test
    public void testSample() {
        // Test empty queue
        assertThrows(NoSuchElementException.class, () -> queue.sample());
        
        // Test with one item
        queue.enqueue(5);
        assertEquals(5, queue.sample().intValue());
        assertEquals(1, queue.size()); // Size shouldn't change
        
        // Test with multiple items
        queue.enqueue(10);
        queue.enqueue(15);
        
        Integer sample = queue.sample();
        assertTrue(sample == 5 || sample == 10 || sample == 15);
        assertEquals(3, queue.size()); // Size shouldn't change
    }

    @Test
    public void testIterator() {
        // Test empty iterator
        Iterator<Integer> emptyIter = queue.iterator();
        assertFalse(emptyIter.hasNext());
        assertThrows(NoSuchElementException.class, () -> emptyIter.next());
        
        // Test with items
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        
        Iterator<Integer> iter = queue.iterator();
        assertTrue(iter.hasNext());
        
        Set<Integer> expected = new HashSet<>();
        expected.add(10);
        expected.add(20);
        expected.add(30);
        
        Set<Integer> actual = new HashSet<>();
        while (iter.hasNext()) {
            actual.add(iter.next());
        }
        
        assertEquals(expected, actual);
        
        // Test remove is not supported
        Iterator<Integer> iter2 = queue.iterator();
        iter2.next();
        assertThrows(UnsupportedOperationException.class, () -> iter2.remove());
    }

    @Test
    public void testMultipleIterators() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        
        Iterator<Integer> iter1 = queue.iterator();
        Iterator<Integer> iter2 = queue.iterator();
        
        // Both iterators should be independent
        assertTrue(iter1.hasNext());
        assertTrue(iter2.hasNext());
        
        // The next values might be different due to randomization
        Integer next1 = iter1.next();
        Integer next2 = iter2.next();
        
        // But both should be valid values from the queue
        assertTrue(next1 == 10 || next1 == 20 || next1 == 30);
        assertTrue(next2 == 10 || next2 == 20 || next2 == 30);
    }

    @Test
    public void testResizing() {
        // Add many items to trigger resizing
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        
        assertEquals(10, queue.size());
        
        // Remove items to potentially trigger shrinking
        for (int i = 0; i < 8; i++) {
            queue.dequeue();
        }
        
        assertEquals(2, queue.size());
    }
}