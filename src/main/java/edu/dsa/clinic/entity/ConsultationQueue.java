package edu.dsa.clinic.entity;

/**
 * The queue information of a walk-in {@link Patient}.
 *
 * @author bincent
 */
public class ConsultationQueue extends IdentifiableEntity {
    private Patient patient;
    private ConsultationType consultationType;

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

    public int getQueueNumber() {
        return this.getId();
    }

    @Override
    public String toString() {
        return "ConsultationQueue{" +
                "id=" + id +
                ", patient=" + patient +
                ", consultationType=" + consultationType +
                '}';
    }
}
