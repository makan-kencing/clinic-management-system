package edu.dsa.clinic.adt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract public class SortedListTest {
    abstract <K> ListInterface<K> makeList(Comparator<K> sorter);

    @DisplayName("Test adding into sorted list")
    @Test
    void testAdd() {
        var list = this.makeList(Integer::compareTo);

        list.add(4);
        list.add(1);
        list.add(140);
        list.add(-12);
        list.add(3);

        list.add(13);

        assertEquals(6, list.size());

        assertEquals(-12, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(13, list.get(4));
        assertEquals(140, list.get(5));
    }

    @DisplayName("Test sorting")
    @Test
    void testSort() {
        // Ascending
        var list = this.makeList(String::compareTo);

        list.add("banana");
        list.add("apple");
        list.add("cherry");
        list.add("apple");
        list.add("date");

        assertEquals(5, list.size());

        assertEquals("apple", list.get(0));
        assertEquals("apple", list.get(1));
        assertEquals("banana", list.get(2));
        assertEquals("cherry", list.get(3));
        assertEquals("date", list.get(4));

        // Descending
        list.sort((a, b) -> b.compareTo(a));

        assertEquals("date", list.get(0));
        assertEquals("cherry", list.get(1));
        assertEquals("banana", list.get(2));
        assertEquals("apple", list.get(3));
        assertEquals("apple", list.get(4));
    }
}
