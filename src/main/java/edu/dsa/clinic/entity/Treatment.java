package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;

/**
 * The prescribed treatment to treat or cure symptom(s) after a {@link Consultation}
 *
 * @author TODO
 * @see Consultation
 */
public class Treatment extends IdentifiableEntity{
    private Diagnosis diagnosis;
    private ListInterface<Prescription> prescriptions;
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

    public ListInterface<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ListInterface<Prescription> prescriptions) {
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
