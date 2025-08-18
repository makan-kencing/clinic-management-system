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
        SortedDoubleLinkedList<T> filteredList = new SortedDoubleLinkedList<>(this.sorter);
        for (T item : this) {
            if (filter.filter(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public ListInterface<T> sorted(Comparator<T> sorter) {
        SortedDoubleLinkedList<T> sortedList = new SortedDoubleLinkedList<>(sorter);
        for (T item : this) {
            sortedList.add(item);
        }
        return sortedList;
    }
}
