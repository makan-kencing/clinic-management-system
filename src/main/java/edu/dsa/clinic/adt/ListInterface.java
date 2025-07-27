package edu.dsa.clinic.adt;

import java.util.Comparator;

public interface ListInterface<T> extends Cloneable, Iterable<T> {
    void add(T e);
    void push(T e);
    void insert(int index, T e);
    void remove(T e);
    T get(int index);
    T getFirst();
    T getLast();
    T pop();
    T popFirst();
    void clear();
    int size();
    int index(T e);
    void extend(ListInterface<T> other);
    void subtract(ListInterface<T> other);
    void filter(Filter<T> filter);
    void sort(Comparator<T> sorter);

    @FunctionalInterface
    interface Filter<T> {
        boolean filter(T o);
    }
}
