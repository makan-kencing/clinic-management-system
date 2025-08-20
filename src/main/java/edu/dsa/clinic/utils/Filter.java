package edu.dsa.clinic.utils;

@FunctionalInterface
public interface Filter<T> {
    boolean filter(T o);
}
