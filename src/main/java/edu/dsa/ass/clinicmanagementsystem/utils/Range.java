package edu.dsa.ass.clinicmanagementsystem.utils;

public record Range<T extends Comparable<T>> (
        T from,
        T to
) {
}
