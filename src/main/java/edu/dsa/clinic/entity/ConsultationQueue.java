package edu.dsa.clinic.entity;

public class ConsultationQueue extends IdentifiableEntity {
    private Patient patient;
    private ConsultationType consultationType;

    public int getQueueNumber() {
        return this.getId();
    }

    @Override
    public String toString() {
        return "ConsultationQueue{" +
                "patient=" + patient +
                ", consultationType=" + consultationType +
                '}';
    }

    public Patient getPatient() {
        return patient;
    }

    public ConsultationQueue setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public ConsultationType getConsultationType() {
        return consultationType;
    }

    public ConsultationQueue setConsultationType(ConsultationType consultationType) {
        this.consultationType = consultationType;
        return this;
    }
}
