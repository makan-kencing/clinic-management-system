package edu.dsa.clinic.adt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DoubleLinkedList<T> implements ListInterface<T> {
    private Node<T> first;
    private Node<T> last;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int length = 0;

    protected static class Node<K> {
        @Nullable Node<K> before;
        @Nullable Node<K> next;
        K data;

        /**
         * Add a new entry to the chain.
         *
         * @param e The new data to be added to the chain.
         */
        private void chain(K e) {
            var node = new Node<K>();
            node.data = e;

            this.chainRight(node);
        }

        /**
         * Unlinks the current node from the chain by chaining the before and next node together.
         */
        private void destroy() {
            if (this.before != null)
                this.before.next = this.next;

            if (this.next != null)
                this.next.before = this.before;
        }

        /**
         * Chain two nodes together.
         *
         * @param right The node to chain on the right.
         */
        private void chainRight(@Nullable Node<K> right) {
            this.next = right;

            if (right != null)
                right.before = this;
        }

        /**
         * Chain two nodes together.
         *
         * @param left The node to chain on the left.
         */
        private void chainLeft(@Nullable Node<K> left) {
            this.before = left;

            if (left != null)
                left.next = this;
        }
    }

    private void throwIfOutOfBounds(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        if (index >= this.length - 1)
            throw new IndexOutOfBoundsException();
    }

    private Node getNode(int index) {
        this.throwIfOutOfBounds(index);

        var current = this.first;
        for (int i = 0; i < index; i++) {
            assert(current.next != null);
            current = current.next;
        }

        return current;
    }

    private @Nullable Node findNode(Filter<T> filter) {
        for (var current = this.first; current != null; current = current.next)
            if (filter.filter(current.data))
                return current;

        return null;
    }

    private int indexNode(Filter<T> filter) throws NoSuchElementException {
        var current = this.first;

        for (int i = 0; i < this.length && current != null; i++, current = current.next)
            if (filter.filter(current.data))
                return i;

        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(T e) {
        this.last.chain(e);
    }

    @Override
    public void insert(int index, T e) {
        var node = this.getNode(index);
        node.chain(e);

        length++;
    }

    @Override
    public void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        var node = this.getNode(index);
        node.destroy();
    }

    @Override
    public T get(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        return this.getNode(index).data;
    }

    @Override
    public @Nullable T getFirst() {
        if (this.first != null) return this.first.data;
        else return null;
    }

    @Override
    public @Nullable T getLast() {
        if (this.last != null) return this.last.data;
        else return null;
    }

    @Override
    public T pop() throws IndexOutOfBoundsException {
        if (this.last == null) throw new IndexOutOfBoundsException();

        var popped = this.last;
        this.last.destroy();
        this.last = popped.before;

        return popped.data;
    }

    @Override
    public T popFirst() throws IndexOutOfBoundsException {
        if (this.first == null) throw new IndexOutOfBoundsException();

        var popped = this.first;
        this.first.destroy();
        this.first = popped.next;

        return popped.data;
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int size() {
        return this.length;
    }

    @Override
    public @Nullable T findFirst(Filter<T> filter) {
        var node = this.findNode(filter);

        if (node != null) return node.data;
        else return null;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int index(Filter<T> filter) throws NoSuchElementException {
        return this.indexNode(filter);
    }

    @Override
    public void extend(ListInterface<T> other) {
        if (other instanceof DoubleLinkedList<T> linkedList) {
            if (this.last != null)
                this.last.chainRight(linkedList.first);
            else
                this.last = linkedList.first;
        }

        // TODO:
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void filter(Filter<T> filter) {
        // TODO:
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void sort(Comparator<T> sorter) {
        // TODO:
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        // TODO:
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Spliterator<T> spliterator() {
        // TODO
        throw new RuntimeException("Not implemented");
    }
}
