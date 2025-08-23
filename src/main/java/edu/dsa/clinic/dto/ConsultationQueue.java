package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Patient;
import org.jetbrains.annotations.NotNull;

public record ConsultationQueue(int queueNo, Patient patient, ConsultationType type) {
    private static int lastQueueNo = 0;

    public ConsultationQueue(Patient patient, ConsultationType type) {
        this(++lastQueueNo, patient, type);
    }

    @Override
    public @NotNull String toString() {
        return "Consultation Queue:" +
                " QueueNo: " + queueNo +
                ", Patient: " + (patient != null ? patient.getName() : "N/A") +
                ", Type: " + (type != null ? type : "N/A");
    }
}
