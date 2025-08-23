package edu.dsa.clinic.lambda;

@FunctionalInterface
public interface Filter<T> {
    boolean filter(T o);
}
