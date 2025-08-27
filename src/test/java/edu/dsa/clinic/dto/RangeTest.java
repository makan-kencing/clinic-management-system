package edu.dsa.clinic.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RangeTest {
    @Test
    void testSubtract() {
        var r1 = new Range<>(1, 5);
        var r2 = new Range<>(0, 2);

        var ranges = r1.subtract(r2);

        assertEquals(1, ranges.size());
        assertEquals(new Range<>(2, 5), ranges.get(0));

        r1 = new Range<>(1, 5);
        r2 = new Range<>(0, 2);

        ranges = r1.subtract(r2);

        assertEquals(1, ranges.size());
        assertEquals(new Range<>(2, 5), ranges.get(0));


        r1 = new Range<>(1, 10);
        r2 = new Range<>(2, 4);

        ranges = r1.subtract(r2);

        assertEquals(2, ranges.size());
        assertEquals(new Range<>(1, 2), ranges.get(0));
        assertEquals(new Range<>(4, 10), ranges.get(1));
    }
}
