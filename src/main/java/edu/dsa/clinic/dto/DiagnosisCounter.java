package edu.dsa.clinic.dto;

import org.jetbrains.annotations.Range;

public class DiagnosisCounter {
    private final String diagnosis;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int count = 0;

    public String diagnosis() {
        return this.diagnosis;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int count() {
        return this.count;
    }

    public DiagnosisCounter(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void increment() {
        this.count++;
    }
}
