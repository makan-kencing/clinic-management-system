package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Doctor;

public class DoctorCounter extends Counter<Doctor> {
    public DoctorCounter(Doctor key){ super(key); }

    public void increment(){
        super.increment();
    }

    public int getCount(){
        return super.count();
    }
}
