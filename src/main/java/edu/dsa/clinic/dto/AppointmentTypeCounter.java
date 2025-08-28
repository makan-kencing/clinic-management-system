package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.ConsultationType;

public class AppointmentTypeCounter extends Counter<ConsultationType> {
    private final ListInterface<DoctorCounter> doctorCounters = new DoubleLinkedList<>();
    private final ListInterface<PatientCounter> patientCounters = new DoubleLinkedList<>();

    public AppointmentTypeCounter(ConsultationType key){
        super(key); // key = ConsultationType (GENERAL, SPECIALIZE, etc.)
    }

    public ConsultationType getType() {
        return super.key();
    }

    public void incrementAppointmentCount(){ super.increment(); }

    public int getAppointmentCount(){ return super.count(); }

    public ListInterface<DoctorCounter> getDoctorCounters() { return doctorCounters; }
    public ListInterface<PatientCounter> getPatientCounters() { return patientCounters; }
}
