package edu.dsa.clinic.lambda;

@FunctionalInterface
public interface Mapper<K, V> {
    V map (K o);
}
