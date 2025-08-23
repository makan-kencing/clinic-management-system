package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import edu.dsa.clinic.utils.table.Table;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;


public class MedicalUI extends UI {
    private final MedicalController medicalController;
    private final MedicineUI medicineUI;
    private final PatientUI patientUI;

    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(scanner);
        this.patientUI = new PatientUI(scanner);
    }

    public void viewConsultationRecord() {
        var consultations = this.medicalController.getConsultationList();

        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Patient Name", Alignment.CENTER, 20),
                new Column("Doctor Name", Alignment.CENTER, 20),
                new Column("Consulted At", Alignment.CENTER, 20),
                new Column("Notes", Alignment.CENTER, 40),
                new Column("Diagnoses", Alignment.CENTER, 20)
        }, consultations) {
            @Override
            protected Cell[] getRow(Consultation o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getPatient().getName()),
                        new Cell(o.getDoctor().getName()),
                        new Cell(o.getConsultedAt()),
                        new Cell(o.getNotes()),
                        new Cell(o.getDiagnoses().size()),
                };
            }
        };
        table.display();
    }


    public void viewMenu() {
        System.out.println("Consultation Menu");
        System.out.println("Please Select an Option");
        System.out.println("=".repeat(30));
        System.out.println("1. Create Consultation Record");
        System.out.println("2. View Consultation Record");
        System.out.println("3. Delete Consultation Record");
        System.out.println("4. List Out Consultation Record");
        System.out.println("5. Back");
        System.out.println("=".repeat(30));
        System.out.print("Enter your choice :");
        int choice = this.scanner.nextInt();
        switch (choice) {
            case 1:
                createConsultationInfo();
                break;
            case 2:
                viewSelectConsultationRecord();
                break;
            case 3:
                deleteConsultationPage();
                break;
            case 5:
                break;

        }

    }

    // create consultation
    public @Nullable Consultation createConsultationInfo() {
        var consultation = new Consultation();
        // set consultation info (doctor, patient)

        //patient selection
        var selectPatient = this.patientUI.selectPatient();

        //doctor selection
        //var selectDoctor;

        consultation.setPatient(selectPatient);
        consultation.getDoctor();
        return consultation;
    }

    //consultation
    public void writeUpDiagnosis(Consultation consultation) {
        var diagnosis = new Diagnosis();
        diagnosis.setConsultation(consultation);

        System.out.println(consultation.getDoctor().getName());
        System.out.println(consultation.getDoctor().getSpecialization());
        System.out.println(consultation.getDoctor().getGender());
        System.out.println("-".repeat(30));
        System.out.println(consultation.getPatient().getName());
        System.out.println(consultation.getPatient().getIdentification());
        System.out.println(consultation.getPatient().getGender());
        System.out.println(consultation.getType());
        System.out.println("-".repeat(30));
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));

        while (true) {
            System.out.print("Enter diagnosis description: ");
            String description = this.scanner.nextLine().trim();
            this.scanner.nextLine();

            if (description.isEmpty())
                System.out.println("Description cannot be empty. Please enter again.");
            else {
                diagnosis.setDescription(description);
                break;
            }
        }

        System.out.print("Diagnosis notes (optional): ");
        String notes = this.scanner.nextLine();
        this.scanner.nextLine();
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

        System.out.print("Symptom for this treatment (e.g., Fever/Cough/Nasal Congestion): ");
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

    public void startConsultationSession() {
        this.startConsultationSession(null);
    }

    public void viewSelectConsultationRecord() {
        viewConsultationRecord();
        System.out.print("Select the consultation need to edit:");
        int id = this.scanner.nextInt();  // TODO: get actual id
        var selectConsultation = medicalController.selectConsultationById(id);
        if (selectConsultation != null) {
            startConsultationSession(selectConsultation);
        }
    }

    //diagnosis
    public void startConsultationSession(@Nullable Consultation consultation) {
        if (consultation == null)
            consultation = this.createConsultationInfo();

        while (true) {
            // let doctor choose to
            // 1. make diagnosis (add, edit, *delete)
            // 2. write notes

            System.out.println("Please Select an Option");
            System.out.println("1. Add diagnosis");
            System.out.println("2. Edit diagnosis");
            System.out.println("3. Delete Diagnosis Record");
            System.out.println("4. Write Note");
            System.out.println("5. Back");
            System.out.print("Enter your choice :");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (choice) {
                case 1:
                    this.writeUpDiagnosis(consultation);
                    break;
                case 2:
                    this.viewEditDiagnosis(consultation);
                    break;
                case 3:
                    this.deleteDiagnosisAndTreatmentPage(consultation);
                    break;
                case 4:
                    //writeNote
                case 5:
                    viewMenu();
            }


            //
            // case make diagnosis
            // - add
            //   - add treatments
            //     - add prescription
            // - edit
            //   - edit

        }
    }

    // edit diagnosis
    public void viewDiagnosis(Consultation consultation) {
        var table = new Table<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Diagnosis", Alignment.CENTER, 40),
                new Column("Diagnoses", Alignment.CENTER, 40),
                new Column("Notes", Alignment.CENTER, 40),
                new Column("Treatment", Alignment.CENTER, 10)
        }, consultation.getDiagnoses().clone()) {
            @Override
            protected Cell[] getRow(Diagnosis o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getDiagnosis()),
                        new Cell(o.getDescription()),
                        new Cell(o.getNotes() != null ? o.getNotes() : ""),
                        new Cell(o.getTreatments().size())
                };
            }
        };

        table.display();
    }

    public void viewEditDiagnosis(Consultation consultation) {
        // show this consultation diagnoses
        viewDiagnosis(consultation);
        var diagnosis = consultation.getDiagnoses();
        // choose the diagnosis (getId)
        System.out.print("Select the part wanted edit:");
        int id = this.scanner.nextInt();
        var editDiagnosis = medicalController.selectDiagnosis(diagnosis, id);
        if (editDiagnosis != null) {
            editDiagnosis(editDiagnosis);
        }
    }

    // viewMenu -> viewSelectConsultationRecord -> startConsultationSession
    public void editDiagnosis(Diagnosis diagnosis) {
        while (true) {
            System.out.println("-".repeat(30));
            System.out.println("| Diagnosis |");
            System.out.println("-".repeat(30));
            System.out.println("Diagnosis: " + diagnosis.getDiagnosis());
            System.out.println("Description: " + diagnosis.getDescription());
            System.out.println("Notes: " + diagnosis.getNotes());
            System.out.println("-".repeat(30));
            System.out.println("1. Edit diagnosis");
            System.out.println("2. Edit description");
            System.out.println("3. Edit Note");
            System.out.println("4. Edit treatment");
            System.out.println("5. Back");
            System.out.print("Select the part wanted edit: ");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new Diagnosis :");
                    String newDiagnosis = this.scanner.nextLine();
                    diagnosis.setDiagnosis(newDiagnosis);
                    return;
                case 2:
                    System.out.print("Enter new Description :");
                    String newDescription = this.scanner.nextLine();
                    diagnosis.setDescription(newDescription);
                    return;
                case 3:
                    System.out.print("Enter new Notes :");
                    String newNotes = this.scanner.nextLine();
                    diagnosis.setNotes(newNotes);
                    return;
                case 4:
                    editTreatmentPage(diagnosis);
                case 5:
                    return;
            }
        }
    }

    //edit treatment
    public void viewTreatment(Diagnosis diagnosis) {
        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Symptom", Alignment.CENTER, 20),
                new Column("Prescriptions", Alignment.CENTER, 40),
                new Column("Notes", Alignment.CENTER, 50)
        }, diagnosis.getTreatments().clone()) {
            @Override
            protected Cell[] getRow(Treatment o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getSymptom()),
                        new Cell(o.getPrescriptions().size()),
                        new Cell(o.getNotes())
                };
            }
        };
        table.display();
    }

    public void editTreatmentPage(Diagnosis diagnosis) {
        while (true) {
            viewTreatment(diagnosis);
            ListInterface<Treatment> treatments = diagnosis.getTreatments();
            System.out.print("Select the part wanted edit:");
            int id = this.scanner.nextInt();
            var editTreatment = medicalController.selectTreatment(treatments, id);
            System.out.println();
            System.out.println("=".repeat(30));
            System.out.println("1. Edit Symptom");
            System.out.println("2. Edit Note");
            System.out.println("3. Edit Prescriptions");
            System.out.println("4. Back");
            System.out.print("Select the Treatment wanted edit: ");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter new Symptom :");
                    String newDiagnosis = this.scanner.nextLine();
                    if (editTreatment != null) {
                        editTreatment.setSymptom(newDiagnosis);
                    }
                    return;
                case 2:
                    System.out.print("Enter new Note :");
                    String newDescription = this.scanner.nextLine();
                    if (editTreatment != null) {
                        editTreatment.setNotes(newDescription);
                    }
                    return;
                case 3:
                    //prescription
                    if (editTreatment != null) {
                        editPrescriptionPage(editTreatment);
                    }
                    return;
                case 4:
                    return;
            }
        }
    }

    //edit prescription
    public void viewPrescription(Treatment treatment) {
        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Medicine", Alignment.CENTER, 40),
                new Column("Quantity", Alignment.CENTER, 4),
                new Column("Notes", Alignment.CENTER, 50)
        }, treatment.getPrescriptions().clone()) {
            @Override
            protected Cell[] getRow(Prescription o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        //new Cell(o.getMedicine().getName()),
                        new Cell(o.getQuantity()),
                        new Cell(o.getNotes()),
                };
            }
        };
        table.display();
    }

    public void editPrescriptionPage(Treatment treatment) {
        while (true) {
            var prescriptions = treatment.getPrescriptions();
            viewPrescription(treatment);
            System.out.print("Select the part wanted edit:");
            int id = this.scanner.nextInt();
            var editPrescription = medicalController.selectPrescription(prescriptions, id);
            System.out.println();
            System.out.println("=".repeat(30));
            System.out.println("1. Edit Medicine");
            System.out.println("2. Edit Quantity");
            System.out.println("3. Edit Notes");
            System.out.println("4. Back");
            System.out.println("=".repeat(30));
            System.out.print("Select the Treatment wanted edit: ");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (choice) {
                case 1:
                    var medicine = this.medicineUI.searchMedicine();
                    if (medicine == null)
                        break;
                    if (editPrescription != null) {
                        editPrescription.setMedicine(medicine);
                    }
                    return;
                case 2:
                    System.out.print("Enter new Quantity :");
                    int newQuantity = this.scanner.nextInt();
                    if (editPrescription != null) {
                        editPrescription.setQuantity(newQuantity);
                    }
                    return;
                case 3:
                    System.out.print("Enter new Notes :");
                    String newNotes = this.scanner.nextLine();
                    if (editPrescription != null) {
                        editPrescription.setNotes(newNotes);
                    }
                    return;
                case 4:
                    return;
            }
        }
    }

    //delete

    public void deleteConsultationPage() {
        while (true) {
            viewConsultationRecord();
            System.out.print("Select the consultation ID you want to delete: ");
            int id = this.scanner.nextInt();

            if (id < 1) {
                System.out.println("Invalid ID. Please enter a valid ID.");
                continue;
            }
            boolean deleted = medicalController.deleteConsultation(id);

            if (deleted) {
                System.out.println("Deleted consultation successfully.");
                viewMenu();
                break;
            } else {
                System.out.println("Failed to delete consultation. Please try again.");
            }
        }
    }

    public void deleteDiagnosisAndTreatmentPage(Consultation consultation) {
        while (true) {
            viewDiagnosis(consultation);
            var diagnosis = consultation.getDiagnoses();
            System.out.print("Select the diagnosis you want to delete: ");
            int id = this.scanner.nextInt();
            System.out.println("=".repeat(30));
            System.out.println("1. Delete Diagnosis");
            System.out.println("2. Delete Treatment");
            System.out.println("=".repeat(30));
            System.out.print("Select the info you want to delete: ");
            int choice = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Are you sure you want to delete this diagnosis info(Y/N): ");
                    String selection = this.scanner.nextLine();

                    if (selection.equals("Y") || selection.equals("y")) {
                        boolean deleted = medicalController.deleteDiagnosis(diagnosis, id);
                        if (deleted) {
                            System.out.println("Deleted consultation successfully.");
                            return;
                        } else {
                            System.out.println("Failed to delete consultation. Please try again.");
                        }
                    } else if (selection.equals("N") || selection.equals("n")) {
                        return;
                    } else {
                        System.out.println("Invalid selection. Please try again.");
                        continue;
                    }
                case 2:
                    var selectDiagnosis = medicalController.selectDiagnosis(diagnosis, id);
                    deleteTreatment(selectDiagnosis);
                    break;
            }

        }
    }

    public void deleteTreatment(Diagnosis diagnosis) {
        while (true) {
            viewTreatment(diagnosis);
            var treatment = diagnosis.getTreatments();
            System.out.print("Select the treatment you want to delete: ");
            int id = this.scanner.nextInt();
            this.scanner.nextLine();
            if (id < 1) {
                System.out.println("Invalid ID. Please enter a valid ID.");
                continue;
            } else {
                System.out.print("Are you sure you want to delete this treatment info(Y/N): ");
                String selection = this.scanner.nextLine();
                if (selection.equals("Y") || selection.equals("y")) {
                    boolean deleted = medicalController.deleteTreatment(treatment, id);
                    if (deleted) {
                        System.out.println("Deleted consultation successfully.");
                        viewMenu();
                        return;
                    } else {
                        System.out.println("Failed to delete consultation. Please try again.");
                        break;
                    }
                } else if (selection.equals("N") || selection.equals("n")) {
                    return;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                    return;
                }

            }

        }

    }
}
