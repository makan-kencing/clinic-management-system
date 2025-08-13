package edu.dsa.clinic.adt;

import java.util.Comparator;

public class SortedDoubleLinkedList<T> extends DoubleLinkedList<T> {
    protected Comparator<T> sorter;

    public SortedDoubleLinkedList(Comparator<T> sorter) {
        super();
        this.sorter = sorter;
    }

    @Override
    protected void addNode(Node<T> newNode) {
        for (var node : this.getNodeIterator())
            if (sorter.compare(newNode.data, node.data) > 0)
                super.addNode(node, newNode);
    }

    @Override
    protected void addNode(Node<T> at, Node<T> node) {
        this.addNode(node);
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

    @Override
    public ListInterface<T> filtered(Filter<T> filter) {
        // TODO: redo implementation for more efficient adding
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public ListInterface<T> sorted(Comparator<T> sorter) {
        // TODO: redo implementation for more efficient adding
        throw new UnsupportedOperationException("Not implemented");
    }
}
