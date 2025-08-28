package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;

/**
 * The prescribed treatment to treat or cure symptom(s) after a {@link Consultation}
 *
 * @author tan
 * @see Consultation
 */
public class Treatment extends IdentifiableEntity{
    private Diagnosis diagnosis;
    private String symptom;
    private @Nullable String notes;
    private ListInterface<Prescription> prescriptions = new DoubleLinkedList<>();

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public Treatment setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public String getSymptom() {
        return symptom;
    }

    public Treatment setSymptom(String symptom) {
        this.symptom = symptom;
        return this;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public Treatment setNotes(@Nullable String notes) {
        this.notes = notes;
        return this;
    }

    public ListInterface<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public Treatment setPrescriptions(ListInterface<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        return this;
    }

    public Treatment addPrescription(Prescription prescription) {
        prescription.setTreatment(this);

        this.prescriptions.add(prescription);
        return this;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", diagnosis=" + diagnosis +
                ", symptom='" + symptom + '\'' +
                ", notes='" + notes + '\'' +
                ", prescriptions=" + prescriptions +
                '}';
    }
}
