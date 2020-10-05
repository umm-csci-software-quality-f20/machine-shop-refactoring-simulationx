/** Node class used by linked structures.
 * This class and its data members are
 * visible only within the package dataStructures. */

package dataStructures;

class ChainNode<Type> {
    // package visible data members
    Type element;
    ChainNode<Type> next;

    // package visible constructors
    ChainNode() {
    }

    ChainNode(Type element) {
        this.element = element;
    }

    ChainNode(Type element, ChainNode<Type> next) {
        this.element = element;
        this.next = next;
    }
}
