package edu.dsa.clinic.utils;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.Shift;

public record Schedule(
        ListInterface<Shift> monday,
        ListInterface<Shift> tuesday,
        ListInterface<Shift> wednesday,
        ListInterface<Shift> thursday,
        ListInterface<Shift> friday,
        ListInterface<Shift> saturday,
        ListInterface<Shift> sunday
) {
    public Schedule() {
        this(
                newList(),
                newList(),
                newList(),
                newList(),
                newList(),
                newList(),
                newList()
        );
    }

    private static ListInterface<Shift> newList() {
        return new SortedDoubleLinkedList<>();
    }
}
