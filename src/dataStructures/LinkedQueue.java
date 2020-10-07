/** a linked queue class */

package dataStructures;

public class LinkedQueue<Type> implements Queue<Type> {
    // data members
    protected ChainNode<Type> front;
    protected ChainNode<Type> rear;

    // constructors
    /** create an empty queue */
    public LinkedQueue(int initialCapacity) {
        // the default initial value of front is null
    }

    public LinkedQueue() {
        this(0);
    }

    // methods
    /** @return true iff queue is empty */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * @return the element at the front of the queue
     * @return null if the queue is empty
     */
    public Type getFrontElement() {
        if (isEmpty())
            return null;
        else
            return front.element;
    }

    /**
     * @return the element at the rear of the queue
     * @return null if the queue is empty
     */
    public Type getRearElement() {
        if (isEmpty())
            return null;
        else
            return rear.element;
    }

    /** insert theElement at the rear of the queue */
    public void put(Type theElement) {
        // create a node for theElement
        ChainNode<Type> p = new ChainNode<Type>(theElement, null);

        // append p to the chain
        if (front == null)
            front = p; // empty queue
        else
            rear.next = p; // nonempty queue
        rear = p;
    }

    /**
     * remove an element from the front of the queue
     *
     * @return removed element
     * @return null if the queue is empty
     */
    public Type remove() {
        if (isEmpty())
            return null;
        Type frontElement = front.element;
        front = front.next;
        if (isEmpty())
            rear = null; // enable garbage collection
        return frontElement;
    }

    public int size() {
        int result = 0;

        ChainNode<Type> currentNode = front;
        while (currentNode != null) {
            ++result;
            currentNode = currentNode.next;
        }

        return result;
    }

    /** test program */
    public static void main(String[] args) {
        LinkedQueue<Integer> q = new LinkedQueue<Integer>(3);
        // add a few elements
        q.put(1);
        q.put(2);
        q.put(3);
        q.put(4);

        // delete all elements
        while (!q.isEmpty()) {
            System.out.println("Rear element is " + q.getRearElement());
            System.out.println("Front element is " + q.getFrontElement());
            System.out.println("Removed the element " + q.remove());
        }
    }

    @Override public String toString() {
        String result = "<";
        ChainNode<Type> node = front;
        while (node != null) {
            result += node.element.toString() + ", ";
            node = node.next;
        }
        result += ">";
        return result;
    }
}
