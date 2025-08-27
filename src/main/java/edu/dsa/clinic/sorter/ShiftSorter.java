package edu.dsa.clinic.sorter;

import edu.dsa.clinic.dto.Shift;

import java.util.Comparator;

public interface ShiftSorter {
    static Comparator<Shift> byStartingTime() {
        return Comparator.comparing(s -> s.getTimeRange().from());
    }
}
