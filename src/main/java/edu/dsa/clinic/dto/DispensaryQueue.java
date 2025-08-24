package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Consultation;

import java.time.Instant;

public record DispensaryQueue(Consultation consultation, Instant queueTime) {
    public DispensaryQueue(Consultation consultation) {
        this(consultation, Instant.now());
    }
}
