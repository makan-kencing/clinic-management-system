package edu.dsa.clinic.utils;

import edu.dsa.clinic.adt.ListInterface;

import java.util.Comparator;

// https://stackoverflow.com/questions/4258700/collections-sort-with-multiple-fields
public class MultiComparator<T> implements Comparator<T> {
    private final Comparator<? super T>[] comparators;

    @SafeVarargs
    public MultiComparator(Comparator<? super T>... comparators) {
        this.comparators = comparators;
    }

    public MultiComparator(ListInterface<Comparator<? super T>> comparators) {
        this.comparators = comparators.toArray();
    }

    public int compare(T o1, T o2) {
        for (var c : this.comparators) {
            int result = c.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
