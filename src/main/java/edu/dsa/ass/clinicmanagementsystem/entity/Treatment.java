package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The prescribed treatment to treat or cure symptom(s) after a {@link Consultation}
 *
 * @author TODO
 * @see Consultation
 */
public class Treatment extends IdentifiableEntity{
    private Diagnosis diagnosis;
    private List<Prescription> prescriptions;
    private String symptom;

    @Override
    public String toString() {
        return "Treatment{" +
                "diagnosis=" + diagnosis +
                ", prescriptions=" + prescriptions +
                ", symptom='" + symptom + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    private @Nullable String notes;
}
