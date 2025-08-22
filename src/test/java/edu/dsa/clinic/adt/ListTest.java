package edu.dsa.clinic.adt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class ListTest {
    abstract <K> ListInterface<K> makeList();

    @DisplayName("Test adding 1 value into list")
    @ParameterizedTest
    @ValueSource(strings = {"hello"})
    void addOnce(String arg) {
        var list = this.<String>makeList();

        list.add(arg);

        assertEquals(1, list.size());
        assertEquals(arg, list.get(0));
        assertEquals(arg, list.getFirst());
        assertEquals(arg, list.getLast());
        assertEquals(arg, list.findFirst(arg::equals));
        assertEquals(0, list.index(arg::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @DisplayName("Test adding multiple values into list")
    @ParameterizedTest
    @CsvSource({"hi,hello,hey", "bye,goodbye,farewell"})
    void addMany(String arg1, String arg2, String arg3) {
        var list = this.<String>makeList();

        list.add(arg1);
        list.add(arg2);
        list.add(arg3);

        assertEquals(3, list.size());
        assertEquals(arg1, list.get(0));
        assertEquals(arg2, list.get(1));
        assertEquals(arg3, list.get(2));
        assertEquals(arg1, list.getFirst());
        assertEquals(arg3, list.getLast());
        assertEquals(arg2, list.findFirst(arg2::equals));
        assertEquals(1, list.index(arg2::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @DisplayName("Test inserting into list")
    @ParameterizedTest
    @CsvSource({"0,hello", "2,hi", "4,hey"})
    void insert(int position, String arg) {
        var list = this.<String>makeList();

        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.insert(position, arg);

        assertEquals(6, list.size());
        assertEquals(arg, list.get(position + 1));
        assertEquals(arg, list.findFirst(arg::equals));
        assertEquals(position + 1, list.index(arg::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(6));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(6, arg));
    }

    void pop() {

    }

    void deleteOne() {

    }

    @DisplayName("Test sorting")
    @Test
    void testSort() {
        var list = this.<String>makeList();

        list.add("banana");
        list.add("apple");
        list.add("cherry");
        list.add("apple");
        list.add("date");

        assertEquals(5, list.size());

        // Ascending
        list.sort(String::compareTo);

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