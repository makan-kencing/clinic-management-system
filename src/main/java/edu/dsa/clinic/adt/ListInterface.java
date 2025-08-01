package edu.dsa.clinic.adt;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.NoSuchElementException;

public interface ListInterface<T> extends QueueLikeInterface<T>, Cloneable, Iterable<T> {
    /**
     * Insert elements after the element located at index.
     *
     * @param index The index to insert the element after.
     * @param e The new element to be added.
     */
    void insert(@Range(from = 0, to = Integer.MAX_VALUE) int index, T e);

    /**
     * Remove the element at the specified index.
     *
     * @param index The index of the element to remove.
     * @throws IndexOutOfBoundsException If the index exceed the list current size.
     */
    void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException;

    /**
     * Get the element at the specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index exceed the list current size.
     */
    T get(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException;

    /**
     * Get the last element in the list.
     *
     * @return The element in the last. If the element doesn't exist, return null.
     */
    @Nullable T getLast();

    /**
     * Remove and get the last element in the list.
     *
     * @return The element in the last.
     * @throws IndexOutOfBoundsException If the element doesn't exist.
     */
    T pop() throws IndexOutOfBoundsException;

    /**
     * Clear the elements in the list.
     */
    void clear();

    /**
     * Get the number of elements in the list.
     *
     * @return The number of elements.
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int size();

    /**
     * Get the index of the first element that matches the filter condition.
     *
     * @param filter The logic to filter against.
     * @return The index of the found element.
     * @throws NoSuchElementException If the element is not found.
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int index(Filter<T> filter) throws NoSuchElementException;

    /**
     * Get the first element that matches the filter condition.
     *
     * @param filter The logic to filter against.
     * @return The found element. If the element is not found, return null.
     */
    @Nullable T findFirst(Filter<T> filter);

    /**
     * Extend the current list with another list.
     *
     * @param other The other list to be extended with.
     */
    void extend(ListInterface<T> other);

    /**
     * Filter elements in the list that doesn't match the filter condition.
     *
     * @param filter The logic to filter against.
     */
    void filter(Filter<T> filter);

    /**
     * Sort the list based on the given comparison logic.
     *
     * @param sorter The comparison logic to sort against.
     */
    void sort(Comparator<T> sorter);

    @FunctionalInterface
    interface Filter<T> {
        boolean filter(T o);
    }
}
