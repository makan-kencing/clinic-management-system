package edu.dsa.clinic.utils;

@FunctionalInterface
public interface Mapper<K, V> {
    V map (K o);
}
