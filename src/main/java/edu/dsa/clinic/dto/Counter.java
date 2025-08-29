package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.lambda.Supplier;
import org.jetbrains.annotations.Range;

public class Counter<K> {
    private final K key;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int count = 0;

    public Counter(K key) {
        this.key = key;
    }

    public K key() {
        return key;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int count() {
        return this.count;
    }

    public void increment() {
        this.count++;
    }

    public void add(int n) {
        this.count += n;
    }

    public static <K, C extends Counter<K>> C getOrCreate(ListInterface<C> counters, K key, Supplier<C> createIfNotFound) {
        var counter = counters.findFirst(c -> key.equals(c.key()));
        if (counter == null) {
            counter = createIfNotFound.get();
            counters.add(counter);
        }
        return counter;
    }
}
