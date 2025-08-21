package edu.dsa.clinic.adt;

import edu.dsa.clinic.utils.Filter;
import edu.dsa.clinic.utils.Mapper;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.NoSuchElementException;

public interface ListInterface<T> extends Cloneable, Iterable<T> {
    /**
     * Add the element to the last of the list.
     *
     * @param e The element to add to the list.
     */
    void add(T e);

    /**
     * Add the element to the start of the list.
     *
     * @param e The element to add to the list.
     */
    void addLeft(T e);

    /**
     * Insert elements after the element located at index.
     *
     * @param index The index to insert the element after.
     * @param e The new element to be added.
     * @throws IndexOutOfBoundsException If the index does not point to an element.
     */
    void insert(@Range(from = 0, to = Integer.MAX_VALUE) int index, T e) throws IndexOutOfBoundsException;

    /**
     * Get the element at the specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index exceed the list current size.
     */
    T get(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException;

    /**
     * Get the first element in the list.
     *
     * @return The first element. If the element doesn't exist, return null.
     */
    @Nullable T getFirst();

    /**
     * Get the last element in the list.
     *
     * @return The element in the last. If the element doesn't exist, return null.
     */
    @Nullable T getLast();

    /**
     * Remove the element at the specified index.
     *
     * @param index The index of the element to remove.
     * @throws IndexOutOfBoundsException If the index exceed the list current size.
     */
    void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException;

    /**
     * Remove the first element that matches the condition
     *
     * @param filter The boolean logic to match against.
     * @return The removed element.
     */
    @Nullable T removeFirst(Filter<T> filter);

    /**
     * Remove and get the first element in the list.
     *
     * @return The first element.
     * @throws IndexOutOfBoundsException If the element doesn't exist.
     */
    T popFirst() throws IndexOutOfBoundsException;

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
     * Map all the element in the list to another value.
     *
     * @param mapper The mapping function to convert the list.
     * @return The new list with the values mapped.
     * @param <M> The datatype to map to.
     */
    <M> ListInterface<M> map(Mapper<T, M> mapper);

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

    /**
     * Filter elements in the list that doesn't match the filter condition into a new list.
     *
     * @param filter The logic to filter against.
     * @return The new list with the filtered contents.
     */
    ListInterface<T> filtered(Filter<T> filter);

    /**
     * Sort the list based on the given comparison logic into a new list.
     *
     * @param sorter The comparison logic to sort against.
     * @return THe new list with the sorted contents.
     */
    ListInterface<T> sorted(Comparator<T> sorter);

    /**
     * Create a shallow copy of the existing list.
     *
     * @return The shallow copy of the list.
     */
    ListInterface<T> clone();
}
