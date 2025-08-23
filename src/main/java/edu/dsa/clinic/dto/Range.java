package edu.dsa.clinic.dto;

public record Range<T extends Comparable<T>> (
        T from,
        T to
) {
}
