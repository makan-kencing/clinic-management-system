package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import edu.dsa.clinic.utils.table.Table;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class MedicalUI extends UI {
    private final MedicalController medicalController;
    private final MedicineController medicineController;
    private final MedicineUI medicineUI;
    private final PatientUI patientUI;
    private final DoctorUI doctorUI;


    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineController = new MedicineController();
        this.medicineUI = new MedicineUI(scanner);
        this.patientUI = new PatientUI(scanner);
        this.doctorUI = new DoctorUI(scanner);
    }


    public Consultation viewConsultationRecord() {
        var consultations = this.medicalController.getConsultationList();

        Consultation selectionConsultation = null;
        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Patient Name", Alignment.CENTER, 20),
                new Column("Doctor Name", Alignment.CENTER, 20),
                new Column("Consultation Type", Alignment.CENTER, 20),
                new Column("Consulted At", Alignment.CENTER, 20),
                new Column("Notes", Alignment.CENTER, 40),
                new Column("Diagnoses", Alignment.CENTER, 40)
        }, consultations) {
            @Override
            protected Cell[] getRow(Consultation o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getPatient().getName()),
                        new Cell(o.getDoctor().getName()),
                        new Cell(o.getType()),
                        new Cell(DateTimeFormatter.ISO_INSTANT.format(o.getConsultedAt())),
                        new Cell(o.getNotes()),
                        new Cell(o.getDiagnoses().size()),
                };
            }
        };

        int opt;
        do {
            table.display();
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Patient ID " +
                    "\n(2) Filter Patient Record " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextInt();
            this.scanner.nextLine();

            System.out.println();

            if (opt != 4) {
                switch (opt) {
                    case 1: {
                        do {
                            table.display();
                            System.out.print("\nEnter Consultation ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            this.scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                break;
                            }
                            selectionConsultation = medicalController.selectConsultationById(selectedId);
                            if (selectionConsultation == null) {
                                System.out.println("Consultation with ID (" + selectedId + ") not found. Please re-enter Patient ID...");
                            } else {
                                System.out.println("Consultation with ID (" + selectionConsultation.getId() + ") selected!");
                            }
                        } while (selectionConsultation == null);
                        break;
                    }
                    case 2: {
                        filterConsultationRecord(table);
                        break;
                    }
                    case 3: {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                }
            } else {
                System.out.println();
                table.display();
                break;
            }
        } while (opt > 4 || opt < 1);

        return selectionConsultation;
    }

    public void filterConsultationRecord(InteractiveTable<Consultation> table) {
        System.out.println("=".repeat(30));
        System.out.println("Consultations filtered");
        System.out.println("(1)Patient Name");
        System.out.println("(2)Doctor Name");
        System.out.println("(3)Consultation Type");
        System.out.println("(0) exit");
        System.out.println("=".repeat(30));
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();
        switch (opt) {
            case 1: {
                System.out.print("Filter by Patient Name: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "patient name", value);
                break;
            }
            case 2: {
                System.out.print("Filter by Doctor Name: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "doctor name", value);
                break;
            }
            case 3: {
                System.out.println("Filter by Consultation Type: ");
                System.out.println("(1) GENERAL");
                System.out.println("(2) SPECIALIST");
                System.out.println("(3) EMERGENCY");
                System.out.println("(4) FOLLOW_UP");
                int value = scanner.nextInt();
                this.scanner.nextLine();
                if (value == 1) {
                    filter(table, "ConsultationType", null, "GENERAL");
                } else if (value == 2) {
                    filter(table, "ConsultationType", null, "SPECIALIST");
                } else if (value == 3) {
                    filter(table, "ConsultationType", null, "EMERGENCY");
                } else if (value == 4) {
                    filter(table, "ConsultationType", null, "FOLLOW_UP");
                }
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }


    }

    public void filter(InteractiveTable<Consultation> table, String column, String value) {
        filter(table, column, value, null);
    }

    public void filter(InteractiveTable<Consultation> table, String column, String value, String type) {
        switch (column) {
            case "patient name": {
                table.addFilter("Filter by" + column + " \"" + value + "\"",
                        c -> c.getPatient().getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "doctor name": {
                table.addFilter("Filter by" + column + " \"" + value + "\"",
                        c -> c.getDoctor().getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "ConsultationType": {
                if (type.equals("GENERAL")) {
                    table.addFilter("GENERAL", c -> c.getType() == ConsultationType.GENERAL);
                    table.display();
                } else if (type.equals("SPECIALIST")) {
                    table.addFilter("SPECIALIST", c -> c.getType() == ConsultationType.SPECIALIST);
                    table.display();

                } else if (type.equals("EMERGENCY")) {
                    table.addFilter("EMERGENCY", c -> c.getType() == ConsultationType.EMERGENCY);
                    table.display();
                } else if (type.equals("FOLLOW_UP")) {
                    table.addFilter("FOLLOW_UP", c -> c.getType() == ConsultationType.FOLLOW_UP);
                    table.display();
                }

            }
            default:
                break;
        }
    }

    public void viewMenu() {
        while (true) {
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
            this.scanner.nextLine();
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
                case 4:
                    //
                case 5:
                    return;

            }
        }

    }

    // create consultation
    public void createConsultationInfo() {
        var consultation = new Consultation();
        System.out.println("=".repeat(30));
        System.out.println("Consultation created");
        System.out.println("=".repeat(30));
        //patient selection
        System.out.println("Patient Info");
        System.out.println("=".repeat(30));
        var selectPatient = this.patientUI.selectPatient();
        System.out.println();
        System.out.println("=".repeat(30));
        //doctor selection
        System.out.println("Doctor Info");
        System.out.println("=".repeat(30));
        System.out.println();
        var selectDoctor = this.doctorUI.selectDoctor();
        System.out.println();
        System.out.println("=".repeat(30));
        System.out.println("Consultation Type");
        System.out.println("=".repeat(30));
        int choice;
        do {
            System.out.println(
                    "(1) GENERAL\n" +
                            "(2) SPECIALIST\n" +
                            "(3) EMERGENCY\n" +
                            "(4) FOLLOW_UP"
            );
            System.out.print("Please select consultation type: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    consultation.setType(ConsultationType.GENERAL);
                    break;
                case 2:
                    consultation.setType(ConsultationType.SPECIALIST);
                    break;
                case 3:
                    consultation.setType(ConsultationType.EMERGENCY);
                    break;
                case 4:
                    consultation.setType(ConsultationType.FOLLOW_UP);
                    break;
                default:
                    System.out.println("Invalid choice, please re-enter.");
                    choice = -1;
            }
        } while (choice < 1 || choice > 4);

        System.out.print("Enter Consultation Note(Option) :");
        String note = this.scanner.nextLine();


        System.out.println();

        consultation.setPatient(selectPatient);
        consultation.setDoctor(selectDoctor);
        consultation.setNotes(note);
        consultation.setConsultedAt(Instant.now());
        writeUpDiagnosis(consultation);
        boolean check = medicalController.saveConsultationRecord(consultation);
        if (!check) {
            System.out.println("Consultation record Field added. Please try again.");
        } else {
            System.out.println("Consultation record added. ");

        }
    }

    //consultation
    public void writeUpDiagnosis(Consultation consultation) {

        var diagnosis = new Diagnosis();
        diagnosis.setConsultation(consultation);
        System.out.println("-".repeat(30));
        System.out.println("Doctor");
        System.out.println("-".repeat(30));
        System.out.println("Name :" + consultation.getDoctor().getName());
        System.out.println("Specialization :" + consultation.getDoctor().getSpecialization());
        System.out.println("Gender :" + consultation.getDoctor().getGender());
        System.out.println("-".repeat(30));
        System.out.println("Patient");
        System.out.println("-".repeat(30));
        System.out.println("Name :" + consultation.getPatient().getName());
        System.out.println("Identification :" + consultation.getPatient().getIdentification());
        System.out.println("Gender :" + consultation.getPatient().getGender());
        System.out.println("Consultation Type :" + consultation.getType());
        System.out.println("-".repeat(30));
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));

        while (true) {
            System.out.print("Enter diagnosis:");
            String diagnosisRecord = this.scanner.nextLine().trim();
            if (diagnosisRecord.isEmpty()) {
                System.out.println("Description cannot be empty. Please enter again.");
                continue;
            } else {
                diagnosis.setDiagnosis(diagnosisRecord);
            }

            System.out.print("Enter diagnosis description: ");
            String description = this.scanner.nextLine().trim();

            if (description.isEmpty())
                System.out.println("Description cannot be empty. Please enter again.");
            else {
                diagnosis.setDescription(description);
                break;
            }
        }

        System.out.print("Diagnosis notes (optional): ");
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

// Validate symptom input
        System.out.print("Symptom for this treatment (e.g., Fever/Cough/Nasal Congestion): ");
        while (true) {
            var symptom = this.scanner.nextLine().trim();

            if (symptom.isEmpty()) {
                System.out.println("Symptom cannot be empty. Please enter again:");
            } else {
                treatment.setSymptom(symptom);
                break;
            }
        }

// Create prescriptions
        while (true) {
            var prescription = new Prescription();
            prescription.setTreatment(treatment);

            System.out.println("-".repeat(30));
            System.out.println("| Prescription |");
            System.out.println("-".repeat(30));

            // Validate medicine selection
            //var medicine = this.medicineUI.searchProduct();
            var medicine =medicalController.selectProduct();
            if (medicine == null) {
                System.out.println("Invalid medicine selection. Please try again.");
                continue;
            }

            prescription.setProduct(medicine);

            // Validate quantity
            int quantity = 0;
            while (true) {
                System.out.print("Quantity (>=1): ");
                String quantityInput = this.scanner.nextLine().trim();  // 获取输入并去除空格

                try {
                    quantity = Integer.parseInt(quantityInput);
                    if (quantity >= 1) {
                        break;
                    } else {
                        System.out.println("Quantity must be >= 1. Please enter again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. The input must be an integer.");
                }
            }

            prescription.setQuantity(quantity);

            // Validate notes
            System.out.println("Prescription notes (optional): ");
            String notes = this.scanner.nextLine().trim();
            prescription.setNotes(notes.isEmpty() ? null : notes);

            // Add prescription to treatment
            treatment.getPrescriptions().add(prescription);

            // Ask if the user wants to add another prescription
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
        var selectConsultation = viewConsultationRecord();
        if (selectConsultation != null) {
            startConsultationSession(selectConsultation);
        }
    }

    //diagnosis
    public void startConsultationSession(@Nullable Consultation consultation) {

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
                    return;
                case 2:
                    this.viewEditDiagnosis(consultation);
                    break;
                case 3:
                    this.deleteDiagnosisAndTreatmentPage(consultation);
                    break;
                case 4:
                    //writeNote
                case 5:
                    return;
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
            System.out.println("Consultation Type: " + diagnosis.getConsultation().getType());
            System.out.println("Notes: " + diagnosis.getNotes());
            System.out.println("-".repeat(30));
            System.out.println("1. Edit diagnosis");
            System.out.println("2. Edit description");
            System.out.println("3. Edit Notes");
            System.out.println("4. Edit treatment");
            System.out.println("5. Back");
            System.out.print("Select the part you want to edit: ");

            int choice = -1;
            while (true) {
                try {
                    choice = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (choice < 1 || choice > 5) {
                        System.out.println("Invalid choice. Please select between 1 and 5.");
                    } else {
                        break;  // valid choice
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number between 1 and 5.");
                    this.scanner.nextLine();  // clear the buffer
                }
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new Diagnosis: ");
                    String newDiagnosis = this.scanner.nextLine().trim();
                    while (newDiagnosis.isEmpty()) {
                        System.out.println("Diagnosis cannot be empty. Please enter a valid diagnosis.");
                        newDiagnosis = this.scanner.nextLine().trim();
                    }
                    diagnosis.setDiagnosis(newDiagnosis);
                    return;
                case 2:
                    System.out.print("Enter new Description: ");
                    String newDescription = this.scanner.nextLine().trim();
                    while (newDescription.isEmpty()) {
                        System.out.println("Description cannot be empty. Please enter a valid description.");
                        newDescription = this.scanner.nextLine().trim();
                    }
                    diagnosis.setDescription(newDescription);
                    return;
                case 3:
                    System.out.print("Enter new Notes: ");
                    String newNotes = this.scanner.nextLine().trim();
                    while (newNotes.isEmpty()) {
                        System.out.println("Notes cannot be empty. Please enter valid notes.");
                        newNotes = this.scanner.nextLine().trim();
                    }
                    diagnosis.setNotes(newNotes);
                    return;
                case 4:
                    editTreatmentPage(diagnosis);
                    break;
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
                        new Cell(o.getId()),                    // id
                        new Cell(o.getSymptom() != null ? o.getSymptom() : ""),          // symptom
                        new Cell(o.getPrescriptions() != null ? o.getPrescriptions().size() : 0),  // prescriptions count
                        new Cell(o.getNotes() != null ? o.getNotes() : "")               // notes
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
                new Column("Quantity", Alignment.CENTER, 10),
                new Column("Notes", Alignment.CENTER, 50)
        }, treatment.getPrescriptions().clone()) {
            @Override
            protected Cell[] getRow(Prescription o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getProduct().getName()),
                        new Cell(o.getQuantity()),
                        new Cell(o.getNotes())
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
                    var medicine = this.medicineUI.searchProduct();
                    if (medicine == null)
                        break;
                    if (editPrescription != null) {
                        editPrescription.setProduct(medicine);
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
