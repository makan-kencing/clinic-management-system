package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Dispensing;

import java.time.Instant;

/**
 * The queue record of a completed {@link Consultation} and its {@link Dispensing} to be made.
 * @param consultation The {@link Consultation} made.
 * @param queueTime The time at which it is queued at.
 *
 * @author makan-kencing
 */
public record DispensaryQueue(Consultation consultation, Instant queueTime) {
    public DispensaryQueue(Consultation consultation) {
        this(consultation, Instant.now());
    }
}
