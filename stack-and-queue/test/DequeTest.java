import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;

public class DequeTest {

    @Test
    public void testIsEmptyAndSize() {
        Deque<Integer> deque = new Deque<>();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        deque.addFirst(1);
        assertFalse(deque.isEmpty());
        assertEquals(1, deque.size());
    }

    @Test
    public void testAddFirstAndRemoveFirst() {
        Deque<String> deque = new Deque<>();
        deque.addFirst("A");
        deque.addFirst("B");
        assertEquals(2, deque.size());
        assertEquals("B", deque.removeFirst());
        assertEquals("A", deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testAddLastAndRemoveLast() {
        Deque<String> deque = new Deque<>();
        deque.addLast("A");
        deque.addLast("B");
        assertEquals(2, deque.size());
        assertEquals("B", deque.removeLast());
        assertEquals("A", deque.removeLast());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testAddFirstAndRemoveLast() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        assertEquals(2, deque.size());
        assertEquals(1, deque.removeLast());
        assertEquals(2, deque.removeLast());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testAddLastAndRemoveFirst() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        assertEquals(2, deque.size());
        assertEquals(1, deque.removeFirst());
        assertEquals(2, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testNullAddFirstThrows() {
        Deque<Integer> deque = new Deque<>();
        assertThrows(IllegalArgumentException.class, () -> deque.addFirst(null));
    }

    @Test
    public void testNullAddLastThrows() {
        Deque<Integer> deque = new Deque<>();
        assertThrows(IllegalArgumentException.class, () -> deque.addLast(null));
    }

    @Test
    public void testRemoveFirstOnEmptyThrows() {
        Deque<Integer> deque = new Deque<>();
        assertThrows(java.util.NoSuchElementException.class, () -> deque.removeFirst());
    }

    @Test
    public void testRemoveLastOnEmptyThrows() {
        Deque<Integer> deque = new Deque<>();
        assertThrows(java.util.NoSuchElementException.class, () -> deque.removeLast());
    }

    @Test
    public void testIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        Iterator<Integer> it = deque.iterator();
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertTrue(it.hasNext());
        assertEquals(3, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorRemoveThrows() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        Iterator<Integer> it = deque.iterator();
        assertThrows(UnsupportedOperationException.class, () -> it.remove());
    }

    @Test
    public void testMainMethod() {
        // Just ensure main runs without exceptions
        Deque.main(new String[]{});
    }
}