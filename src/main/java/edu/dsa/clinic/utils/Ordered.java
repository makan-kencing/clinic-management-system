package edu.dsa.clinic.utils;

// https://stackoverflow.com/questions/51265954/min-max-function-of-two-comparables
public interface Ordered<T> extends Comparable<T> {

    static <T extends Comparable<? super T>> T min(T a, T b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    static <T extends Comparable<? super T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static <T extends Comparable<? super T>> boolean isBetween(T value, T start, T end) {
        return isGreaterOrEqualTo(value, start) && isLessOrEqualTo(value, end);
    }
    
    static <T extends Comparable<? super T>> boolean isLessThan(T a, T b) {
        return a.compareTo(b) < 0;
    }

    static <T extends Comparable<? super T>> boolean isLessOrEqualTo(T a, T b) {
        return a.compareTo(b) <= 0;
    }

    static <T extends Comparable<? super T>> boolean isGreaterThan(T a, T b) {
        return a.compareTo(b) > 0;
    }

    static <T extends Comparable<? super T>> boolean isGreaterOrEqualTo(T a, T b) {
        return a.compareTo(b) >= 0;
    }
}
