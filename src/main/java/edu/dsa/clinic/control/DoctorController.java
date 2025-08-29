/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.DoctorCounter;
import edu.dsa.clinic.dto.PatientCounter;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.filter.DoctorFilter;
import edu.dsa.clinic.lambda.Filter;
import org.jetbrains.annotations.Nullable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorController {
    public static final ListInterface<DayOfWeek> days = new DoubleLinkedList<>();



    public static ListInterface<DayOfWeek> getDays() {
        for (DayOfWeek day : DayOfWeek.values()) {
            days.add(day);
        }
        return days;
    }

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


    public static ListInterface<Doctor> getDoctors() {
        return Database.doctorList;
    }


    public static boolean addShift(ListInterface<Shift> currentShifts, Shift shift) {
        // Sort shifts by start time
        currentShifts.sort((a, b) -> a.getTimeRange().from().compareTo(b.getTimeRange().from()));

        for (Shift existing : currentShifts) {
            // Reject if overlapping
            if (existing.getTimeRange().overlapsExclusively(shift.getTimeRange())) {
                return false;
            }
        }

        // Check for adjacency and merge if needed
        boolean merged = false;
        var iterator = currentShifts.iterator();
        while (iterator.hasNext()) {
            Shift existing = iterator.next();
            // If shift is adjacent after existing
            if (existing.getTimeRange().to().equals(shift.getTimeRange().from())) {
                existing.setTimeRange(existing.getTimeRange().combine(shift.getTimeRange()));
                merged = true;
                break;
            }
            // If shift is adjacent before existing
            if (shift.getTimeRange().to().equals(existing.getTimeRange().from())) {
                existing.setTimeRange(existing.getTimeRange().combine(shift.getTimeRange()));
                merged = true;
                break;
            }
        }

        // If not merged, add as a new shift
        if (!merged) {
            currentShifts.add(shift);
        }

        // Resort after possible merge
        currentShifts.sort((a, b) -> a.getTimeRange().from().compareTo(b.getTimeRange().from()));
        return true;
    }

    public static boolean additionalShift(ListInterface<Shift> currentShifts, Shift shift) {
        var iterator = currentShifts.iterator();

        while (iterator.hasNext()) {
            var existing = iterator.next();

            if (existing.getTimeRange().overlaps(shift.getTimeRange())) {

                // Calculate possible new combined range
                Range<LocalTime> combined = existing.getTimeRange().combine(shift.getTimeRange());

                // Check how much earlier/later the shift extends
                long extendEarlier = Duration.between(shift.getTimeRange().from(), existing.getTimeRange().from()).toMinutes();
                long extendLater = Duration.between(existing.getTimeRange().to(), shift.getTimeRange().to()).toMinutes();

                // Only allow merge if extension is >= 30 mins
                if (extendEarlier >= 30 || extendLater >= 30) {
                    existing.setTimeRange(combined);
                    return true; // merged, exit
                } else {
                    return false; // reject
                }
            }
        }
        currentShifts.add(shift);
        return true;
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

    public static ListInterface<String> getExtremeDoctors(boolean most) {
        ListInterface<DoctorCounter> counters = getDoctorSummary();
        ListInterface<String> extremeDoctors = new DoubleLinkedList<>();
        if (counters.size() == 0) return extremeDoctors;

        int extremeValue = most ? counters.get(0).getCount() : counters.get(counters.size() - 1).getCount();
        for (var dc : counters) {
            if (dc.getCount() == extremeValue) {
                extremeDoctors.add(dc.key().getName());
            }
        }
        return extremeDoctors;
    }


    public static ListInterface<DoctorCounter> getTopDoctorCountersByConsultations(int topN) {
        ListInterface<DoctorCounter> counters = getDoctorSummary();
        ListInterface<DoctorCounter> topCounters = new DoubleLinkedList<>();
        for (int i = 0; i < Math.min(topN, counters.size()); i++) {
            topCounters.add(counters.get(i));
        }
        return topCounters;
    }

    public static ListInterface<DoctorCounter> getTopDoctorCountersByPatients(int topN) {
        ListInterface<DoctorCounter> counters = getDoctorSummary();
        counters.sort((a, b) -> Integer.compare(b.getPatientCounters().size(), a.getPatientCounters().size()));
        ListInterface<DoctorCounter> topCounters = new DoubleLinkedList<>();
        for (int i = 0; i < Math.min(topN, counters.size()); i++) {
            topCounters.add(counters.get(i));
        }
        return topCounters;
    }


    public static ListInterface<String> getTypeList(DoctorCounter dc) {
        ListInterface<String> list = new DoubleLinkedList<>();
        list.add(String.valueOf(dc.getCount())); // Number of consultations attended
        return list;
    }

    public static ListInterface<String> getPatientList(DoctorCounter dc) {
        ListInterface<String> list = new DoubleLinkedList<>();
        for (PatientCounter pc : dc.getPatientCounters()) {
            list.add(pc.key().getName());
        }
        return list;
    }

    public static ListInterface<Integer> getTotalStats(int topN) {
        ListInterface<DoctorCounter> counters = getDoctorSummary();
        int totalDoctors = Math.min(topN, counters.size());
        int totalConsultations = 0;
        int totalPatients = 0;
        for (int i = 0; i < totalDoctors; i++) {
            DoctorCounter dc = counters.get(i);
            totalConsultations += dc.getCount();
            totalPatients += dc.getPatientCounters().size();
        }
        ListInterface<Integer> stats = new DoubleLinkedList<>();
        stats.add(totalDoctors);
        stats.add(totalConsultations);
        stats.add(totalPatients);
        return stats;
    }

    public static ListInterface<DoctorCounter> getDoctorSummary() {

        ListInterface<DoctorCounter> doctorCounters = new DoubleLinkedList<>();
        ListInterface<PatientCounter> patientCounters = new DoubleLinkedList<>();
        for (var consult : Database.consultationsList) {
            var doctor = consult.getDoctor();
            var patient = consult.getPatient();

            // Find or create DoctorCounter for this doctor
            var existingDoctor = doctorCounters.findFirst(dc -> dc.key().equals(doctor));
            if (existingDoctor == null) {
                existingDoctor = new DoctorCounter(doctor);
                doctorCounters.add(existingDoctor);
            }

            existingDoctor.increment();

            // Find or create PatientCounter for this patient (global, if needed elsewhere)
            var existingPatient = patientCounters.findFirst(pc -> pc.key().equals(patient));
            if (existingPatient == null) {
                existingPatient = new PatientCounter(patient);
                patientCounters.add(existingPatient);
            }
            existingPatient.increment();

            // Add patient to this doctor's patientCounters if not already present
            if (existingDoctor.getPatientCounters().findFirst(pc -> pc.key().equals(patient)) == null) {
                existingDoctor.getPatientCounters().add(new PatientCounter(patient));
            }
        }

        doctorCounters.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        return doctorCounters;
    }


}
