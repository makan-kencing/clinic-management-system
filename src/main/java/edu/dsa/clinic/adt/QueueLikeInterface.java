package edu.dsa.clinic.adt;

import org.jetbrains.annotations.Nullable;

public interface QueueLikeInterface<T> {
    /**
     * Add the element to the last of the queue
     * @param e The element to add to the queue.
     */
    void add(T e);

    /**
     * Get the first element in the queue.
     *
     * @return The first element. If the element doesn't exist, return null.
     */
    @Nullable T getFirst();

    /**
     * Remove and get the first element in the queue.
     *
     * @return The first element.
     * @throws IndexOutOfBoundsException If the element doesn't exist.
     */
    T popFirst() throws IndexOutOfBoundsException;
}
