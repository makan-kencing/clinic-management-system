package edu.dsa.clinic.boundary;

import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

import java.util.Scanner;


public class MedicalUI extends UI {
    private final MedicalController medicalController;
    private final MedicineUI medicineUI;

    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(scanner);
    }

    public void viewMenu() {
        // TODO:
        String[] menu = {
                "Create Consultation Record",
                "List Out Consultation Record",
                "Back"

        };
    }

    public void writeUpDiagnosis(Consultation consultation) {
        var diagnosis = new Diagnosis();
        diagnosis.setConsultation(consultation);

        System.out.println(consultation.getDoctor().toString());
        System.out.println("-".repeat(30));
        System.out.println(consultation.getPatient().toString());
        System.out.println("-".repeat(30));
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));

        while (true) {
            System.out.print("Enter diagnosis description: ");
            String description = this.scanner.nextLine().trim();

            if (description.isEmpty())
                System.out.println("Description cannot be empty. Please enter again.");
            else {
                diagnosis.setDescription(description);
                break;
            }
        }

        System.out.println("Diagnosis notes (optional): ");
        String notes = this.scanner.nextLine();

        diagnosis.setNotes(notes.isBlank() ? null : notes);

        while (true) {
            this.writeUpTreatment(diagnosis);

            System.out.print("Add another treatment? (y/n): ");

            String more = this.scanner.nextLine().trim();
            if (!more.equalsIgnoreCase("y"))
                break;
        }

        consultation.getDiagnoses().add(diagnosis);
    }

    public void writeUpTreatment(Diagnosis diagnosis) {
        var treatment = new Treatment();
        treatment.setDiagnosis(diagnosis);

        System.out.println("-".repeat(50));
        System.out.println("| Treatment |");
        System.out.println("-".repeat(50));

        System.out.println("Symptom for this treatment (e.g., Fever/Cough/Nasal Congestion): ");
        while (true) {
            var symptom = this.scanner.nextLine();

            if (symptom.isEmpty())
                System.out.println("Symptom cannot be empty. Please enter again:");
            else {
                treatment.setSymptom(symptom);
                break;
            }
        }

        // create prescriptions
        while (true) {
            var prescription = new Prescription();
            prescription.setTreatment(treatment);

            System.out.println("-".repeat(30));
            System.out.println("| Prescription |");
            System.out.println("-".repeat(30));

            var medicine = this.medicineUI.searchMedicine();
            if (medicine == null)
                break;

            System.out.print("Quantity (>=1): ");
            var quantity = this.scanner.nextInt();

            System.out.println("Prescription notes (optional): ");
            String notes = this.scanner.nextLine();

            prescription.setMedicine(medicine);
            prescription.setQuantity(quantity);
            prescription.setNotes(notes.isBlank() ? null : notes);

            treatment.getPrescriptions().add(prescription);

            System.out.print("Add another prescription for this treatment? (y/n): ");
            String moreP = this.scanner.nextLine().trim();
            if (!moreP.equalsIgnoreCase("y")) break;
        }

        diagnosis.getTreatment().add(treatment);
    }

    public void startConsultationSession() {
        //
        // create consultation object
        var consultation = new Consultation();
        // set consultation info (doctor, patient)

        // display consultation full details
        // let doctor choose to
        // 1. make diagnosis (add, edit, delete)
        // 2. write notes

        //
        // case make diagnosis
        // - add
        //   - add treatments
        //     - add prescription
        // - edit
        //   - edit

        // save consultation
    }
}
