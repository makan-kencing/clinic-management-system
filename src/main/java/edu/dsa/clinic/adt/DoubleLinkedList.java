package edu.dsa.clinic.adt;

import edu.dsa.clinic.utils.Filter;
import edu.dsa.clinic.utils.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The {@link ListInterface} implementation for this project.
 *
 * @implNote A double linked list is chosen due to its efficiency at insertion anywhere in the list
 * and deletion at the head and tail of the list, in order to fulfill the Queue-ish and Stack-ish
 * property of the {@link ListInterface}
 *
 * @param <T> The datatype of the element in the list.
 * @author makan-kencing
 */
public class DoubleLinkedList<T> implements ListInterface<T> {
    protected static class Node<K> implements Iterable<Node<K>> {
        @Nullable Node<K> before;
        @Nullable Node<K> next;
        K data;

        @SuppressWarnings("unused")
        public Node() {
        }

        public Node(K data) {
            this.data = data;
        }

        /**
         * Unlinks the current node from the chain by chaining the before and next node together.
         */
        public void unlink() {
            if (this.before != null)
                this.before.next = this.next;

            if (this.next != null)
                this.next.before = this.before;
        }

        /**
         * Link two nodes together.
         *
         * @param left  The left node.
         * @param right The right node.
         * @param <K>   The generic type of both nodes.
         */
        public static <K> void link(@Nullable Node<K> left, @Nullable Node<K> right) {
            assert(left != right);

            if (left != null) {
                left.next = right;
            }
            if (right != null) {
                right.before = left;
            }
        }

        @Override
        public @NotNull Iterator<Node<K>> iterator() {
            return new NodeIterator<>(this);
        }

        protected static class NodeIterator<K> implements Iterator<Node<K>>, Iterable<Node<K>> {
            @Nullable
            private Node<K> next;

            public NodeIterator(@Nullable Node<K> startingNode) {
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
    }

    protected class Reference {
        Node<T> head;
        Node<T> tail;

        public Reference(Node<T> head, Node<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        public Reference(Node<T> starting) {
            this(starting, starting);
        }
    }

    @Nullable
    protected Reference reference = null;

    @Range(from = 0, to = Integer.MAX_VALUE)
    protected int length = 0;

    /**
     * Throw an {@link IndexOutOfBoundsException} if the index exceeds the length.
     *
     * @param index The target index to check.
     * @throws IndexOutOfBoundsException If the index exceeds the length of the list.
     */
    protected void throwIfOutOfBounds(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        if (index >= this.length)
            throw new IndexOutOfBoundsException();
    }

    /**
     * Get the {@link Node} located at the index.
     *
     * @param index The index of the {@link Node}
     * @return The {@link Node} located at the index.
     * @throws IndexOutOfBoundsException If the {@link Node} can't be found.
     */
    protected Node<T> getNode(int index) throws IndexOutOfBoundsException {
        assert(this.reference != null);

        var i = 0;
        for (var node : this.reference.head)
            if (i == index)
                return node;
            else
                i++;

        throw new IndexOutOfBoundsException();
    }

    /**
     * Add a {@link Node} to the start of the list.
     *
     * @param node The {@link Node} to be added.
     */
    protected void addNodeHead(Node<T> node) {
        if (this.reference == null)
            this.reference = new Reference(node);
        else {
            Node.link(this.reference.head, node);
            this.reference.head = node;
        }
    }

    /**
     * Add a {@link Node} to the end of the list.
     *
     * @param node The {@link Node} to be added.
     */
    protected void addNodeTail(Node<T> node) {
        if (this.reference == null)
            this.reference = new Reference(node);
        else {
            Node.link(this.reference.tail, node);
            this.reference.tail = node;
        }
    }

    /**
     * Add a {@link Node} after a specific node.
     *
     * @param before A {@link Node} to insert after.
     * @param node The {@link Node} to be inserted.
     */
    protected void addNodeAfter(Node<T> before, Node<T> node) {
        assert(this.reference != null);

        Node.link(before, node);

        if (this.reference.tail == before)
            this.reference.tail = node;
    }

    /**
     * Add a {@link Node} before a specific node.
     *
     * @param after The node that the new node will be inserted before.
     * @param node The {@link Node} to be inserted.
     */
    protected void addNodeBefore(Node<T> after, Node<T> node) {
        assert (this.reference != null);

        node.next = after;
        node.before = after.before;

        if (after.before != null) {
            after.before.next = node;
        } else {
            // inserting before the head
            this.reference.head = node;
        }

        after.before = node;
    }

    /**
     * Remove the {@link Node} from the list.
     * Also invalidates head and tail reference if affected.
     *
     * @param node The {@link Node} to remove.
     */
    protected void removeNode(Node<T> node) {
        node.unlink();

        assert(this.reference != null);

        if (this.length == 0)
            this.reference = null;

        else if (this.reference.head == node)
            this.reference.head = node.next;

        else if (this.reference.tail == node)
            this.reference.tail = node.before;
    }

    @Override
    public void add(T e) {
        var node = new Node<>(e);

        this.addNodeTail(node);
        this.length++;
    }

    @Override
    public void addLeft(T e) {
        var node = new Node<>(e);

        this.addNodeHead(node);
        this.length++;
    }

    @Override
    public void insert(int index, T e) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        var before = this.getNode(index);

        var node = new Node<>(e);

        this.addNodeAfter(before, node);
        this.length++;
    }

    @Override
    public T get(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        var node = this.getNode(index);
        return node.data;
    }

    @Override
    public @Nullable T getFirst() {
        if (this.reference == null) return null;

        return this.reference.head.data;
    }

    @Override
    public @Nullable T getLast() {
        if (this.reference == null) return null;

        return this.reference.tail.data;
    }

    @Override
    public void remove(@Range(from = 0, to = Integer.MAX_VALUE) int index) throws IndexOutOfBoundsException {
        this.throwIfOutOfBounds(index);

        var node = this.getNode(index);

        this.removeNode(node);
        this.length--;
    }

    @Override
    public T removeFirst(Filter<T> filter) {
        if (this.reference == null) return null;

        for (var node : this.reference.head)
            if (filter.filter(node.data)) {
                this.removeNode(node);
                this.length--;

                return node.data;
            }

        return null;
    }

    @Override
    public T pop() throws IndexOutOfBoundsException {
        if (this.reference == null) throw new IndexOutOfBoundsException();

        var node = this.reference.tail;

        this.removeNode(node);
        this.length--;

        return node.data;
    }

    @Override
    public T popFirst() throws IndexOutOfBoundsException {
        if (this.reference == null) throw new IndexOutOfBoundsException();

        var node = this.reference.head;

        this.removeNode(node);
        this.length--;

        return node.data;
    }

    @Override
    public void clear() {
        this.reference = null;
        this.length = 0;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int size() {
        return this.length;
    }

    @Override
    public @Nullable T findFirst(Filter<T> filter) {
        for (var data : this)
            if (filter.filter(data))
                return data;

        return null;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int index(Filter<T> filter) throws NoSuchElementException {
        var index = 0;
        for (var data : this)
            if (filter.filter(data))
                return index;
            else
                index++;

        throw new NoSuchElementException();
    }

    @Override
    public void extend(ListInterface<T> other) {
        for (var data : other) {
            var node = new Node<>(data);

            this.addNodeTail(node);
            this.length++;
        }
    }

    @Override
    public <M> ListInterface<M> map(Mapper<T, M> mapper) {
        var mapped = new DoubleLinkedList<M>();

        for (var data : this) {
            var mappedData = mapper.map(data);

            var node = new Node<>(mappedData);
            mapped.addNodeTail(node);
            mapped.length++;
        }

        return mapped;
    }

    @Override
    public void filter(Filter<T> filter) {
        if (this.reference == null) return;

        for (var node : this.reference.head)
            if (!filter.filter(node.data)) {
                this.removeNode(node);
                this.length--;
            }
    }

    @Override
    public void sort(Comparator<T> sorter) {
        if (reference == null || length < 2) {
            return;
        }

        reference.head = mergeSort(reference.head, sorter);

        Node<T> tail = reference.head;
        while (tail.next != null) {
            tail = tail.next;
        }
        reference.tail = tail;
    }

    private Node<T> mergeSort(Node<T> head, Comparator<T> sorter) {
        if (head == null || head.next == null) {
            return head;
        }

        Node<T> middle = getMiddle(head);
        Node<T> nextOfMiddle = middle.next;

        middle.next = null;
        if (nextOfMiddle != null) nextOfMiddle.before = null;

        Node<T> left = mergeSort(head, sorter);
        Node<T> right = mergeSort(nextOfMiddle, sorter);

        return merge(left, right, sorter);
    }

    private Node<T> merge(Node<T> left, Node<T> right, Comparator<T> sorter) {
        if (left == null) return right;
        if (right == null) return left;

        Node<T> result;

        if (sorter.compare(left.data, right.data) <= 0) {
            result = left;
            result.next = merge(left.next, right, sorter);
            if (result.next != null) result.next.before = result;
        } else {
            result = right;
            result.next = merge(left, right.next, sorter);
            if (result.next != null) result.next.before = result;
        }

        return result;
    }

    private Node<T> getMiddle(Node<T> head) {
        if (head == null) return head;

        Node<T> slow = head;
        Node<T> fast = head;

        while (fast.next != null && fast.next.next != null) {
            assert slow != null;
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    @Override
    public ListInterface<T> filtered(Filter<T> filter) {
        DoubleLinkedList<T> clone;
        clone = (DoubleLinkedList<T>) this.clone();

        clone.filter(filter);

        return clone;
    }

    @Override
    public ListInterface<T> sorted(Comparator<T> sorter) {
        DoubleLinkedList<T> clone;
        clone = (DoubleLinkedList<T>) this.clone();

        clone.sort(sorter);

        return clone;

    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new LinkedListIterator<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ListInterface<T> clone() {
        var clone = new DoubleLinkedList<T>();
        clone.extend(this);

        return clone;
    }

    protected static class LinkedListIterator<K> implements Iterator<K>, Iterable<K> {
        private final Node.NodeIterator<K> iterator;

        public LinkedListIterator(Node.NodeIterator<K> nodeIterator) {
            this.iterator = nodeIterator;
        }

        public LinkedListIterator(Node<K> node) {
            this(new Node.NodeIterator<>(node));
        }

        public LinkedListIterator(DoubleLinkedList<K> linkedList) {
            this(linkedList.reference != null ? linkedList.reference.head : null);
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
}
