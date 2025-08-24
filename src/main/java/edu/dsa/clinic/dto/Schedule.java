package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.Doctor;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * The representation of a weekly schedule for a {@link Doctor}
 *
 * @author makan-kencing
 */
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
        return new SortedDoubleLinkedList<>((s1, s2) ->
                s1.getTimeRange().from()
                        .compareTo(s2.getTimeRange().from())
        );
    }

    public ListInterface<Shift> getShifts(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> this.monday();
            case TUESDAY -> this.tuesday();
            case WEDNESDAY -> this.wednesday();
            case THURSDAY -> this.thursday();
            case FRIDAY -> this.friday();
            case SATURDAY -> this.saturday();
            case SUNDAY -> this.sunday();
        };
    }

    public ListInterface<Shift> getShifts(LocalDate date) {
        return this.getShifts(date.getDayOfWeek());
    }

    public Schedule addMondayShift(Shift shift) {
        this.monday.add(shift);
        return this;
    }

    public Schedule addTuesdayShift(Shift shift) {
        this.tuesday.add(shift);
        return this;
    }

    public Schedule addWednesdayShift(Shift shift) {
        this.wednesday.add(shift);
        return this;
    }

    public Schedule addThursdayShift(Shift shift) {
        this.thursday.add(shift);
        return this;
    }

    public Schedule addFridayShift(Shift shift) {
        this.friday.add(shift);
        return this;
    }

    public Schedule addSaturdayShift(Shift shift) {
        this.saturday.add(shift);
        return this;
    }

    public Schedule addSundayShift(Shift shift) {
        this.sunday.add(shift);
        return this;
    }

    public Schedule addShift(DayOfWeek dayOfWeek, Shift shift) {
        var shifts = this.getShifts(dayOfWeek);
        shifts.add(shift);
        return this;
    }
}
