package edu.dsa.clinic.adt;

public class DoubleLinkedListTest extends ListTest {
    @Override
    <K> ListInterface<K> makeList() {
        return new DoubleLinkedList<>();
    }
}
