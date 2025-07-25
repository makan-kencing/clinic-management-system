package edu.dsa.clinic.utils;

public record Range<T extends Comparable<T>> (
        T from,
        T to
) {
}
