package edu.dsa.clinic.controller;

import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.sorter.ShiftSorter;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorControllerTest {
    @Test
    void testAddShiftNoOverlap() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(18, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addShift(shifts, shift2);

        assertEquals(2, shifts.size());
        assertEquals(shift1.getTimeRange(), shifts.get(0).getTimeRange());
        assertEquals(shift2.getTimeRange(), shifts.get(1).getTimeRange());
    }

    @Test
    void testAddShiftOverlapInclusively() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(16, 0), LocalTime.of(20, 0)));

        var expected = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addShift(shifts, shift2);

        assertEquals(1, shifts.size());
        assertEquals(expected.getTimeRange(), shifts.get(0).getTimeRange());
    }

//    @Test
    void testAddShiftOverlapExclusively() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(12, 0), LocalTime.of(20, 0)));

        var expected = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addShift(shifts, shift2);

        assertEquals(1, shifts.size());
        assertEquals(expected.getTimeRange(), shifts.get(0).getTimeRange());
    }

//    @Test
    void testAddShiftThreeOverlaps() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(14, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(16, 0), LocalTime.of(20, 0)));
        var shift3 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(16, 0)));

        var expected = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);
        shifts.add(shift2);

        DoctorController.addShift(shifts, shift3);

        assertEquals(1, shifts.size());
        assertEquals(expected.getTimeRange(), shifts.get(0).getTimeRange());
    }

    @Test
    void testAddBreakOverlaps() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(14, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(12, 0), LocalTime.of(16, 0)));

        var expected = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(12, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addBreak(shifts, shift2);

        assertEquals(1, shifts.size());
        assertEquals(expected.getTimeRange(), shifts.get(0).getTimeRange());
    }

    @Test
    void testAddBreakContainsInclusively() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(14, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        var expected = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(12, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addBreak(shifts, shift2);

        assertEquals(1, shifts.size());
        assertEquals(expected.getTimeRange(), shifts.get(0).getTimeRange());
    }

    @Test
    void testAddBreakContainsExclusively() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(20, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        var expected1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(12, 0)));
        var expected2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);

        DoctorController.addBreak(shifts, shift2);

        assertEquals(2, shifts.size());
        assertEquals(expected1.getTimeRange(), shifts.get(0).getTimeRange());
        assertEquals(expected2.getTimeRange(), shifts.get(1).getTimeRange());
    }

    @Test
    void testAddBreakRemoveAll() {
        var shift1 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));
        var shift2 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(18, 0), LocalTime.of(20, 0)));
        var shift3 = new Shift()
                .setType(ShiftType.WORK)
                .setTimeRange(new Range<>(LocalTime.of(8, 0), LocalTime.of(20, 0)));

        var shifts = new SortedDoubleLinkedList<>(ShiftSorter.byStartingTime());
        shifts.add(shift1);
        shifts.add(shift2);

        DoctorController.addBreak(shifts, shift3);

        assertEquals(0, shifts.size());
    }
}
