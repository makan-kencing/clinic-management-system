package edu.dsa.clinic.lambda;

import edu.dsa.clinic.adt.ListInterface;

public class MultiFilter<T> implements Filter<T> {
    private final ListInterface<Filter<T>> filters;

    public MultiFilter(ListInterface<Filter<T>> filters) {
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
