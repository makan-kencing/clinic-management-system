/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.lambda.MultiFilter;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorController {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //For Formatting Shift Time

    public DoctorController() {
    }
    
    public void addDoctorRecord(){
        
    }
    

    
    public Doctor deleteDoctorbyID(int id){
        return  Database.doctorList.removeFirst(m -> m.getId() == id);
    }

    public Doctor selectDoctorByID(int id) {
        return Database.doctorList.findFirst(d -> d.getId() == id);
    }

    public static int getDoctorCount(){
        return Database.doctorList.size();
    }

    public ListInterface<Doctor> getDoctors(){
        return Database.doctorList;
    }


    public void addShift(ListInterface<Shift> currentShifts, Shift shift) {



    }

    public Schedule getOverallSchedule() {
        return null;
    }

    public void editShift(){
        
    }

    public static Filter<Doctor> getIsOnShiftFilter(DayOfWeek dayOfWeek, Range<LocalTime> timeRange) {
        return d -> {
            var schedule = d.getSchedule();

            var shiftsOfTheDay = schedule.getShifts(dayOfWeek);
            shiftsOfTheDay = shiftsOfTheDay.filtered(s -> s.getType() == ShiftType.WORK);
            if (shiftsOfTheDay.size() == 0)
                return false;

            for (var shift : shiftsOfTheDay) {
                var workingHours = shift.getTimeRange();
                if (!workingHours.contains(timeRange))
                    return false;
            }

            return true;
        };
    }

    public static Filter<Doctor> getIsOnShiftFilter(LocalDate date, Range<LocalTime> timeRange) {
        return getIsOnShiftFilter(date.getDayOfWeek(), timeRange);
    }

    public static Filter<Doctor> getIsAvailableFilter(LocalDate date, Range<LocalTime> timeRange) {
        var filters = new DoubleLinkedList<Filter<Doctor>>();
        filters.add(DoctorController.getIsOnShiftFilter(date, timeRange));
        filters.add(AppointmentController.getNotOccupiedFilter(date, timeRange));

        return new MultiFilter<>(filters);
    }

    public void removeShift(){
        
    }



}
