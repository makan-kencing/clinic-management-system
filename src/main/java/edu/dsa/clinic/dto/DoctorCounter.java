package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;

public class DoctorCounter extends Counter<Doctor> {

    private final ListInterface<PatientCounter> patientCounters = new DoubleLinkedList<>();
    public ListInterface<PatientCounter> getPatientCounters() { return patientCounters; }

    public DoctorCounter(Doctor key) {
        super(key);
    }

    public void increment(){
        super.increment();
    }

    public int getCount(){
        return super.count();
    }
}
