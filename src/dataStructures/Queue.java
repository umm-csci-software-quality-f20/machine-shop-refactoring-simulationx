package dataStructures;

public interface Queue<Type> {
    public boolean isEmpty();

    public Type getFrontElement();

    public Type getRearElement();

    public void put(Type theObject);

    public Type remove();
}
