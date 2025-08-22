package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Range;

/**
 * The queue information of a walk-in {@link Patient}.
 *
 * @author bincent
 */
public class ConsultationQueue {
    private static int lastQueueNo = 0;

    @Range(from = 1, to = Integer.MAX_VALUE)
    private final int queueNo;
    private Patient patient;
    private ConsultationType type;

    public ConsultationQueue() {
        this.queueNo = ++lastQueueNo;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getQueueNo() {
        return queueNo;
    }

    public Patient getPatient() {
        return patient;
    }

    public ConsultationQueue setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public ConsultationType getType() {
        return type;
    }

    public ConsultationQueue setConsultationType(ConsultationType type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ConsultationQueue{" +
                "queueNo=" + queueNo +
                ", patient=" + patient +
                ", type=" + type +
                '}';
    }
}
