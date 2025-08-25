package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.lambda.Filter;
import org.jetbrains.annotations.Nullable;

public class MedicalController {

    MedicineController medicineController = new MedicineController();

    public static Filter<Consultation> getConsultationTypeFilter(ConsultationType type) {
        return c -> c.getType() == type;
    }

    public static Filter<Consultation> getConsultationDoctorFilter(String doctorName) {
        return c -> c.getDoctor().getName().toLowerCase().contains(doctorName.toLowerCase());
    }

    public static Filter<Consultation> getConsultationPatientFilter(String patientName) {
        return c -> c.getPatient().getName().toLowerCase().contains(patientName.toLowerCase());
    }

    public boolean saveConsultationRecord(Consultation consultation) {
        if (consultation == null) {
            return false;
        }
        Database.consultationsList.add(consultation);
        return true;
    }

    public ListInterface<Consultation> getConsultationList() {
        return Database.consultationsList;
    }

    public Consultation selectConsultationById(int id) {
        return Database.consultationsList.findFirst(c -> c.getId() == id);
    }

    public @Nullable Diagnosis selectDiagnosis(ListInterface<Diagnosis> diagnosis, int id) {
        return diagnosis.findFirst(d -> d.getId() == id);
    }

    public @Nullable Diagnosis selectDiagnosis(Consultation consultation, int id) {
        return this.selectDiagnosis(consultation.getDiagnoses(), id);
    }

    public @Nullable Treatment selectTreatment(ListInterface<Treatment> treatment, int id) {
        return treatment.findFirst(t -> t.getId() == id);
    }

    public @Nullable Treatment selectTreatment(Diagnosis diagnosis, int id) {
        return this.selectTreatment(diagnosis.getTreatments(), id);
    }

    public @Nullable Prescription selectPrescription(ListInterface<Prescription> prescription, int id) {
        return prescription.findFirst(t -> t.getId() == id);
    }

    public @Nullable Prescription selectPrescription(Treatment treatment, int id) {
        return this.selectPrescription(treatment.getPrescriptions(), id);
    }

    public Product selectProduct() {
        var allMedicine = MedicineController.getAllProducts();
        return allMedicine.findFirst(m -> m.getId() == 1);
    }

    public boolean deleteConsultation(int id) {
        var removed = Database.consultationsList.removeFirst(c -> c.getId() == id);
        return removed != null;
    }

    public boolean deleteDiagnosis(ListInterface<Diagnosis> diagnosis, int id) {

        var removed = diagnosis.removeFirst(d -> d.getId() == id);
        return removed != null;
    }

    public boolean deleteDiagnosis(Consultation consultation, int id) {

        return this.deleteDiagnosis(consultation.getDiagnoses(), id);
    }

    public void deleteAllTreatment(Diagnosis diagnosis) {
        for (Treatment treatment : diagnosis.getTreatments()) {
            deleteTreatment(diagnosis, treatment.getId());
        }
    }

    public boolean deleteTreatment(ListInterface<Treatment> treatment, int id) {
        var removed = treatment.removeFirst(t -> t.getId() == id);
        return removed != null;
    }

    public boolean deleteTreatment(Diagnosis diagnosis, int id) {
        return this.deleteTreatment(diagnosis.getTreatments(), id);
    }

    public void deleteAllPrescription(Treatment treatment) {
        for (Prescription prescription : treatment.getPrescriptions()) {
            deletePrescription(treatment, prescription.getId());
        }
    }

    public boolean deletePrescription(ListInterface<Prescription> prescriptions, int id) {
        var remove = prescriptions.removeFirst(p -> p.getId() == id);
        return remove != null;
    }

    public boolean deletePrescription(Treatment treatment, int id) {
        return this.deletePrescription(treatment.getPrescriptions(), id);
    }
}
