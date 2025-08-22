package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.utils.Tabulate;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;


public class MedicalUI extends UI {
    private final MedicalController medicalController;
    private final MedicineUI medicineUI;

    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(scanner);
    }

    public void viewConsultationRecord() {
        var table = new Tabulate<>(new Tabulate.Header[]{
                new Tabulate.Header("Id", 4, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Name", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Gender", 10, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Identification", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Contact No", 20, Tabulate.Alignment.CENTER)
        }, Database.consultationsList.clone()) {
            @Override
            protected Cell[] getRow(Consultation element) {
                return new Cell[]{
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getPatient().getName()),
                        new Cell(element.getDoctor().getName()),
                        new Cell(element.getConsultedAt().toString()),
                        new Cell(element.getNotes()),
                        new Cell(element.getDiagnoses().toString() != null ? element.getDiagnoses().toString() : ""),
                };
            }
        };
    }

    public void viewSelectConsultationRecord() {
        viewConsultationRecord();
        System.out.println("Select the part wanted edit:");
        int id = this.scanner.nextInt();  // TODO: get actual id
        var selectConsultation = Database.consultationsList.findFirst(c -> c.getId() == id);
        viewMenu(selectConsultation);
    }

    public void viewMenu(Consultation consultation) {
        System.out.println("Consultation Menu");
        System.out.println("-".repeat(30));
        System.out.println(" Patient ");
        System.out.println("-".repeat(30));
        System.out.println("Name: " + consultation.getPatient().getName());
        System.out.println("Gender: " + consultation.getPatient().getGender());
        System.out.println("Identification: " + consultation.getPatient().getIdentification());
        System.out.println("-".repeat(30));

        System.out.println("Doctor: " + consultation.getDoctor().getName());
        System.out.println("Please Select an Option");
        System.out.println("-".repeat(30));
        System.out.println("1.Consultation Record");
        System.out.println("2 Delete Consultation Record");
        System.out.println("3. List Out Consultation Record");
        System.out.println("5. Back");
        int choice = this.scanner.nextInt();
        switch (choice) {
            case 1:
                startConsultationSession();
                break;
            case 2:
                //list consultation
                break;
        }

    }


    public Consultation createConsultationInfo() {
        var consultation = new Consultation();
        // set consultation info (doctor, patient)
        //

        return consultation;
    }

    public void startConsultationSession(@Nullable Consultation consultation) {
        if (consultation == null)
            consultation = this.createConsultationInfo();

        // let doctor choose to
        // 1. make diagnosis (add, edit, *delete)
        // 2. write notes
        System.out.println("Please Select an Option");
        System.out.println("1. Add diagnosis");
        System.out.println("2 Edit diagnosis");
        System.out.println("3 Delete Consultation Record");
        System.out.println("4 Write Note");
        System.out.println("5. Back");
        int choice = this.scanner.nextInt();
        switch (choice) {
            case 1:
                writeUpDiagnosis(consultation);
                break;
            case 2:
                editDiagnosis(consultation);
                break;
            case 3:
                deleteDiagnosis(consultation);
            case 4:
                //writeNote
            case 5:
                viewMenu(consultation);

        }


        //
        // case make diagnosis
        // - add
        //   - add treatments
        //     - add prescription
        // - edit
        //   - edit

        // save consultation
    }

    public void startConsultationSession() {
        this.startConsultationSession(null);
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

        diagnosis.getTreatments().add(treatment);
    }

    public void editDiagnosis(Consultation consultation) {
        viewDiagnosis(consultation);
        var diagnosis = consultation.getDiagnoses();

        // show this consultation diagnoses

        // choose the diagnosis (getId)
        System.out.println("Select the part wanted edit:");
        int id = 1;  // TODO: get actual id

        var editDiagnosis = diagnosis.findFirst(d -> d.getId() == id);


    }

    public void deleteDiagnosis(Consultation consultation) {

    }

    public void deleteConsultation(Consultation consultation) {

    }

    public void viewDiagnosis(Consultation consultation) {
        var table = new Tabulate<>(new Tabulate.Header[]{
                new Tabulate.Header("Id", 4, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Diagnosis", 10, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Diagnoses", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Notes", 20, Tabulate.Alignment.CENTER)
        }, consultation.getDiagnoses().clone()) {
            @Override
            protected Cell[] getRow(Diagnosis element) {
                return new Cell[]{
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getDiagnosis()),
                        new Cell(element.getDescription()),
                        new Cell(element.getNotes() != null ? element.getNotes() : "")
                };
            }
        };
    }


}
