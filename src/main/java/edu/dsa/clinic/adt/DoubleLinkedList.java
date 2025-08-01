package edu.dsa.clinic.adt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

            Node.chain(this, node);
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
         * @param left  The left node.
         * @param right The right node.
         * @param <K>   The generic type of both nodes.
         */
        private static <K> void chain(@Nullable Node<K> left, @Nullable Node<K> right) {
            if (left != null) {
                left.next = right;
            }
            if (right != null) {
                right.before = left;
            }
        }
    }

    static class NodeIterator<K> implements Iterator<Node<K>>, Iterable<Node<K>> {
        @Nullable
        private Node<K> next;

        public NodeIterator(@NotNull Node<K> startingNode) {
            this.next = startingNode;
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public Node<K> next() {
            var current = this.next;
            assert (current != null);

            this.next = current.next;

            return current;
        }

        @Override
        public @NotNull Iterator<Node<K>> iterator() {
            return this;
        }
    }

    static class LinkedListIterator<K> implements Iterator<K>, Iterable<K> {
        private final NodeIterator<K> iterator;

        public LinkedListIterator(NodeIterator<K> nodeIterator) {
            this.iterator = nodeIterator;
        }

        public LinkedListIterator(Node<K> node) {
            this(new NodeIterator<>(node));
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public K next() {
            return iterator.next().data;
        }

        @Override
        public @NotNull Iterator<K> iterator() {
            return this;
        }
    }

    private void throwIfOutOfBounds(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        if (index >= this.length - 1)
            throw new IndexOutOfBoundsException();
    }

    private Node<T> getNode(int index) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        var i = 0;
        for (var node : this.getNodeIterator())
            if (i == index)
                return node;
            else
                i++;

        throw new IndexOutOfBoundsException();
    }

    private @Nullable Node<T> findNode(Filter<T> filter) {
        for (var node : this.getNodeIterator())
            if (filter.filter(node.data))
                return node;

        return null;
    }

    private int indexNode(Filter<T> filter) throws NoSuchElementException {
        var i = 0;
        for (var node : this.getNodeIterator())
            if (filter.filter(node.data))
                return i;
            else
                i++;

        throw new NoSuchElementException();
    }

    private NodeIterator<T> getNodeIterator() {
        return new NodeIterator<>(this.first);
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
                Node.chain(this.last, linkedList.first);
            else
                this.last = linkedList.first;
        }

        // TODO:
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void filter(Filter<T> filter) {
        for (var node : this.getNodeIterator())
            if (filter.filter(node.data))
                node.destroy();
    }

    @Override
    public void sort(Comparator<T> sorter) {
        // TODO:
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new LinkedListIterator<>(this.first);
    }
}
