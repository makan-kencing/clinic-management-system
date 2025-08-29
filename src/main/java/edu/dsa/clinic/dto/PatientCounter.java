package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Patient;

public class PatientCounter extends Counter<Patient> {
    private final ListInterface<ProductCounter> productCounters = new DoubleLinkedList<>();
    private final ListInterface<ConsultationTypeCounter> consultationTypeCounters = new DoubleLinkedList<>();

    public PatientCounter(Patient key) {
        super(key); // key = Patient
    }

    public void increment() {
        super.increment();
    }

    public int getCount() {
        return super.count();
    }

    public int getPrescriptionCount() {
        int total = 0;
        for (var pc : productCounters) {
            total += pc.count();
        }
        return total;
    }

    public ListInterface<ProductCounter> productCounters() {
        return this.productCounters;
    }

    public ListInterface<ConsultationTypeCounter> consultationCounters() {
        return this.consultationTypeCounters;
    }
}
