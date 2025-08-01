package edu.dsa.clinic.adt;

import java.util.Comparator;
public interface ListInterface<T> extends QueueLikeInterface<T>, Cloneable, Iterable<T> {

    void insert(int index, T e);
    void remove(T e);
    T get(int index);
    T getLast();
    T pop();
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
