package edu.dsa.clinic.utils;

public class MultiFilter<T> implements Filter<T> {
    private final Filter<? super T>[] filters;

    @SafeVarargs
    public MultiFilter(Filter<? super T>... filters) {
        this.filters = filters;
    }

    @Override
    public boolean filter(T o) {
        for (var f : this.filters)
            if (!f.filter(o))
                return false;
        return true;
    }
}
