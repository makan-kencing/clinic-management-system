package edu.dsa.clinic.adt;

import java.util.Comparator;

/**
 * The sorted version of {@link DoubleLinkedList}.
 *
 * @param <T> The datatype of the element in the list.
 * @author dizzynight
 */
public class SortedDoubleLinkedList<T> extends DoubleLinkedList<T> {
    protected Comparator<T> sorter;

    public SortedDoubleLinkedList(Comparator<T> sorter) {
        super();
        this.sorter = sorter;
    }

    protected void addNodeSorted(Node<T> node) {
        if (this.reference == null) {
            this.reference = new Reference(node);
            return;
        }
        Node<T> current = this.reference.head;
        while (current != null) {
            if (sorter.compare(node.data, current.data) < 0) {
                super.addNodeBefore(current, node);
                return;
            }
            current = current.next;
        }
        super.addNodeAfter(this.reference.tail, node);
    }

    @Override
    public void add(T e) {
        var node = new Node<>(e);

        this.addNodeSorted(node);
        this.length++;
    }

    @Override
    public void addLeft(T e) {
        this.add(e);
    }

    @Override
    public void insert(int index, T e) {
        this.add(e);
    }

    @Override
    public void sort(Comparator<T> sorter) {
        this.sorter = sorter;
        super.sort(sorter);
    }
}
