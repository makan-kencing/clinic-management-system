package edu.dsa.clinic.lambda;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
