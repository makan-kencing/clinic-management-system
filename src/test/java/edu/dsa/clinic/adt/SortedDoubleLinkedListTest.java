package edu.dsa.clinic.adt;

import java.util.Comparator;

public class SortedDoubleLinkedListTest extends SortedListTest{
    @Override
    <K> ListInterface<K> makeList(Comparator<K> sorter) {
        return new SortedDoubleLinkedList<>(sorter);
    }
}
