package edu.dsa.clinic.utils;

import edu.dsa.clinic.entity.Shift;

import java.util.ArrayList;
import java.util.List;

public record Schedule(
        List<Shift> monday,
        List<Shift> tuesday,
        List<Shift> wednesday,
        List<Shift> thursday,
        List<Shift> friday,
        List<Shift> saturday,
        List<Shift> sunday
) {
    public Schedule() {
        this(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
