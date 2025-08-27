package edu.dsa.clinic.dto;

import org.jetbrains.annotations.Range;

public class Counter<T> {
    private final T key;
    @org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE)
    private int count = 0;

    public Counter(T key) {
        this.key = key;
    }

    public T key() {
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
}
