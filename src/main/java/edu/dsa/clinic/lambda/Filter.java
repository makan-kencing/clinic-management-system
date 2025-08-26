package edu.dsa.clinic.lambda;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Filter<T> {
    boolean filter(T o);

    default Filter<T> and(@NotNull Filter<T> other) {
        return o -> this.filter(o) && other.filter(o);
    }

    default Filter<T> or(@NotNull Filter<T> other) {
        return o -> this.filter(o) || other.filter(o);
    }

    default Filter<T> not() {
        return o -> !this.filter(o);
    }

    static <T> Filter<T> not(@NotNull Filter<T> f) {
        return f.not();
    }

    static <T> Filter<T> true_() {
        return _ -> true;
    }
}
