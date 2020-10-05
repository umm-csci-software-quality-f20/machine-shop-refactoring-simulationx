package dataStructures;

import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedQueueAcceptanceTest {

    /**
     * A simple acceptance test based on the one little test that
     * Sahni provided with the code.
     */
    @Test
    public void sahniTest() {
        LinkedQueue<Integer> q = new LinkedQueue<Integer>(3);
        // add a few elements
        q.put(Integer.valueOf(1));
        q.put(Integer.valueOf(2));
        q.put(Integer.valueOf(3));
        q.put(Integer.valueOf(4));

        assertTrue(4 == q.getRearElement());
        assertTrue(1 == q.getFrontElement());
        assertTrue(1 == q.remove());
        assertTrue(4 == q.getRearElement());
        assertTrue(2 == q.getFrontElement());
        assertTrue(2 == q.remove());
        assertTrue(4 == q.getRearElement());
        assertTrue(3 == q.getFrontElement());
        assertTrue(3 == q.remove());
        assertTrue(4 == q.getRearElement());
        assertTrue(4 == q.getFrontElement());
        assertTrue(4 == q.remove());
        assertTrue(q.isEmpty());
    }

    @Test
    public void emptyQueueTests() {
        LinkedQueue<Object> q = new LinkedQueue<Object>();
        assertTrue(q.isEmpty());
        assertNull(q.getFrontElement());
        assertNull(q.getRearElement());
        assertNull(q.remove());
    }
}
