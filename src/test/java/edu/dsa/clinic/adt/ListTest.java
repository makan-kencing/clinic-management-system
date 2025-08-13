package edu.dsa.clinic.adt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

abstract class ListTest<T extends ListInterface<String>> {
    private final T impl;

    public ListTest(T impl) {
        this.impl = impl;
    }

    @BeforeEach
    void clearList() {
        this.impl.clear();

        assertEquals(0, impl.size());
    }

    @DisplayName("Test adding 1 value into list")
    @ParameterizedTest
    @ValueSource(strings = {"hello"})
    void addOnce(String arg) {
        impl.add(arg);

        assertEquals(1, impl.size());
        assertEquals(arg, impl.get(0));
        assertEquals(arg, impl.getFirst());
        assertEquals(arg, impl.getLast());
        assertEquals(arg, impl.findFirst(arg::equals));
        assertEquals(0, impl.index(arg::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> impl.get(1));
    }

    @DisplayName("Test adding multiple values into list")
    @ParameterizedTest
    @CsvSource({"hi,hello,hey", "bye,goodbye,farewell"})
    void addMany(String arg1, String arg2, String arg3) {
        impl.add(arg1);
        impl.add(arg2);
        impl.add(arg3);

        assertEquals(3, impl.size());
        assertEquals(arg1, impl.get(0));
        assertEquals(arg2, impl.get(1));
        assertEquals(arg3, impl.get(2));
        assertEquals(arg1, impl.getFirst());
        assertEquals(arg3, impl.getLast());
        assertEquals(arg2, impl.findFirst(arg2::equals));
        assertEquals(1, impl.index(arg2::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> impl.get(3));
    }

    @DisplayName("Test inserting into list")
    @ParameterizedTest
    @CsvSource({"0,hello", "2,hi", "4,hey"})
    void insert(int position, String arg) {
        impl.add("0");
        impl.add("1");
        impl.add("2");
        impl.add("3");
        impl.add("4");
        impl.insert(position, arg);

        assertEquals(6, impl.size());
        assertEquals(arg, impl.get(position + 1));
        assertEquals(arg, impl.findFirst(arg::equals));
        assertEquals(position + 1, impl.index(arg::equals));
        assertThrows(IndexOutOfBoundsException.class, () -> impl.get(6));
        assertThrows(IndexOutOfBoundsException.class, () -> impl.insert(6, arg));
    }

    void pop() {

    }

    void deleteOne() {

    }

}