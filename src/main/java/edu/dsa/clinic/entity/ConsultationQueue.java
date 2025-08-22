package edu.dsa.clinic.entity;

public class ConsultationQueue {
    private static int lastQueueNo = 0;

    private int queueNo;
    private Patient patient;
    private ConsultationType type;

    public ConsultationQueue() {
        this.queueNo = ++lastQueueNo;
    }

    public int getQueueNo() {
        return queueNo;
    }

    public Patient getPatient() {
        return patient;
    }

    public ConsultationType getConsultationType() {
        return type;
    }

    public ConsultationQueue setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public ConsultationQueue setConsultationType(ConsultationType type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "Consultation Queue:" +
                " QueueNo: " + queueNo +
                ", Patient: " + (patient != null ? patient.getName() : "N/A") +
                ", Type: " + (type != null ? type : "N/A");
    }
}
