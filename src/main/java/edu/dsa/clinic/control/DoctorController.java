/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.filter.DoctorFilter;
import edu.dsa.clinic.lambda.Filter;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorController {




    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //For Formatting Shift Time

    public static void addDoctorRecord(Doctor doctor) {
        Database.doctorList.add(doctor);
    }

    public static Doctor deleteDoctorByID(int id) {
        return Database.doctorList.removeFirst(DoctorFilter.byId(id));
    }

    public static @Nullable Doctor selectDoctorByID(int id) {
        return Database.doctorList.findFirst(DoctorFilter.byId(id));
    }
    public static @Nullable Doctor selectDoctorByName(String name) {
        return Database.doctorList.findFirst(DoctorFilter.byName(name));
    }

    public static int getDoctorCount() {
        return Database.doctorList.size();
    }

    public static ListInterface<Doctor> getDoctors() {
        return Database.doctorList;
    }

    public static void addShift(ListInterface<Shift> currentShifts, Shift shift) {
        // assume currentShifts is sorted by startTime
        currentShifts.add(shift);

        var iterator = currentShifts.iterator();

        Shift last = iterator.next();
        while (iterator.hasNext()) {
            var current = iterator.next();

            if (!last.getTimeRange().overlaps(current.getTimeRange())) {
                last = current;
                continue;
            }

            last.setTimeRange(
                    last.getTimeRange().combine(current.getTimeRange())
            );
            currentShifts.removeFirst(Filter.is(current));
        }
    }

    public static void addBreak(ListInterface<Shift> currentShifts, Shift shift) {
        addBreak(currentShifts, shift.getTimeRange());
    }
    //delete Shift
    public static void addBreak(ListInterface<Shift> currentShifts, Range<LocalTime> timeRange) {
        for (var currentShift : currentShifts) {
            if (currentShift.getTimeRange().overlapsExclusively(timeRange)) {
                var timeRanges = currentShift.getTimeRange().subtract(timeRange);

                if (timeRanges.size() == 0) {
                    currentShifts.removeFirst(Filter.is(currentShift));
                    continue;
                }

                currentShift.setTimeRange(timeRanges.popFirst());
                if (timeRanges.size() > 0)
                    currentShifts.add(new Shift()
                            .setType(ShiftType.WORK)
                            .setTimeRange(timeRanges.popFirst())
                    );
            }
        }
    }

    public static Schedule getAvailabilitySchedule(LocalDate date, Doctor doctor) {
        var schedule = doctor.getSchedule().clone();

        for (var appointment : AppointmentController.getDoctorAppointmentsForTheWeek(date, doctor)) {
            var shiftsOfTheDay = schedule.getShifts(appointment.getExpectedStartAt().toLocalDate());

            addBreak(shiftsOfTheDay, new Range<>(
                    appointment.getExpectedStartAt().toLocalTime(),
                    appointment.getExpectedEndAt().toLocalTime()
            ));
        }

        return schedule;
    }

}
