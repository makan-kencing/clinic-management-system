package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class MedicalUI extends UI {
    private final MedicalController medicalController;
    private final MedicineUI medicineUI;
    private final PatientUI patientUI;
    private final DoctorUI doctorUI;
    private final AppointmentUI appointmentUI;


    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(scanner);
        this.patientUI = new PatientUI(scanner);
        this.doctorUI = new DoctorUI(scanner);
        this.appointmentUI = new AppointmentUI(scanner);
    }

    public void startMenu() {
        while (true) {
            System.out.println("""
                    Consultation Menu
                    Please Select an Option""");
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Create Consultation Record
                    2. View Consultation Record
                    3. Delete Consultation Record
                    4. List Out Consultation Record
                    5. Back""");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice :");

            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            Consultation consultation;
            switch (choice) {
                case 1:
                    createConsultationMenu();
                    break;
                case 2:
                    consultation = this.selectConsultation();
                    if (consultation != null)
                        this.startConsultationSession(consultation);

                    break;
                case 3:
                    consultation = this.selectConsultation();
                    if (consultation != null)
                        this.deleteConsultation(consultation);

                    break;
                case 4:
                    //
                case 5:
                    return;
            }
        }
    }

    public @Nullable Consultation selectConsultation() {
        var consultations = this.medicalController.getConsultationList();
        var table = new ConsultationTable(consultations);

        int opt;
        while (true) {
            table.display();

            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("""
                    (1) Select Patient ID
                    (2) Filter Patient Record
                    (3) Reset Filters
                    (4) Exit""");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");

            opt = this.scanner.nextInt();
            this.scanner.nextLine();

            System.out.println();

            switch (opt) {
                case 1:
                    System.out.print("\nEnter Consultation ID (0 to exit): ");

                    int selectedId = this.scanner.nextInt();
                    this.scanner.nextLine();

                    System.out.println();

                    if (selectedId == 0) {
                        System.out.println("-".repeat(30));
                        System.out.println();
                        break;
                    }

                    var consultation = medicalController.selectConsultationById(selectedId);
                    if (consultation == null) {
                        System.out.println("Consultation with ID (" + selectedId + ") not found. Please re-enter Patient ID...");
                    } else {
                        System.out.println("Consultation with ID (" + consultation.getId() + ") selected!");
                        return consultation;
                    }
                    break;
                case 2:
                    this.filterConsultationRecord(table);
                    break;
                case 3:
                    table.resetFilters();
                    break;
                case 4:
                    return null;
            }
        }
    }

    public @Nullable Diagnosis selectDiagnosis(Consultation consultation) {
        var table = new DiagnosisTable(consultation.getDiagnoses());
        int id;

        table.display();

        System.out.print("Select the diagnosis:");
        id = this.scanner.nextInt();

        var diagnosis = medicalController.selectDiagnosis(consultation, id);
        if (diagnosis == null)
            System.out.println("The diagnosis specified is not found.");
        return diagnosis;

    }

    public @Nullable Treatment selectTreatment(Diagnosis diagnosis) {
        var table = new TreatmentTable(diagnosis.getTreatments());

        table.display();

        System.out.print("Select the treatment: ");
        int id = this.scanner.nextInt();

        var treatment = medicalController.selectTreatment(diagnosis, id);
        if (treatment == null)
            System.out.println("The treatment specified is not found.");
        return treatment;
    }

    public @Nullable Prescription selectPrescription(Treatment treatment) {
        var table = new PrescriptionTable(treatment.getPrescriptions());
        table.display();

        System.out.print("Select the part wanted edit:");
        int id = this.scanner.nextInt();

        var prescription = this.medicalController.selectPrescription(treatment, id);
        if (prescription == null)
            System.out.println("The prescription specified is not found.");
        return prescription;
    }

    public void viewDiagnosisDetails(Diagnosis diagnosis) {
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));
        System.out.println("Diagnosis: " + diagnosis.getDiagnosis());
        System.out.println("Description: " + diagnosis.getDescription());
        System.out.println("Consultation Type: " + diagnosis.getConsultation().getType());
        System.out.println("Notes: " + diagnosis.getNotes());
    }

    public void editDiagnosis(Diagnosis diagnosis) {
        while (true) {
            System.out.println("-".repeat(30));
            this.viewDiagnosisDetails(diagnosis);
            System.out.println("-".repeat(30));
            System.out.println("""
                    1. Edit diagnosis
                    2. Edit description
                    3. Edit Notes
                    4. Edit treatment
                    5. Back""");

            System.out.print("Select the part you want to edit: ");

            int choice;
            while (true) {
                try {
                    choice = this.scanner.nextInt();
                    this.scanner.nextLine();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number between 1 and 5.");
                }
            }

            switch (choice) {
                case 1:
                    String diagnosisDetails;
                    do {
                        System.out.print("Enter new Diagnosis: ");
                        diagnosisDetails = this.scanner.nextLine().trim();

                        if (!diagnosisDetails.isEmpty())
                            break;

                        System.out.println("Diagnosis cannot be empty. Please enter a valid diagnosis.");
                    } while (true);

                    diagnosis.setDiagnosis(diagnosisDetails);
                    break;
                case 2:
                    String description;
                    do {
                        System.out.print("Enter new Description: ");
                        description = this.scanner.nextLine().trim();

                        if (!description.isEmpty())
                            break;

                        System.out.println("Description cannot be empty. Please enter a valid description.");
                    } while (true);

                    diagnosis.setDescription(description);
                    break;
                case 3:
                    String notes;
                    do {
                        System.out.print("Enter new Notes: ");
                        notes = this.scanner.nextLine().trim();

                        if (!notes.isEmpty())
                            break;

                        System.out.println("Notes cannot be empty. Please enter valid notes.");
                    } while (true);

                    diagnosis.setNotes(notes);
                    break;
                case 4:
                    var treatment = this.selectTreatment(diagnosis);
                    if (treatment != null)
                        this.editTreatmentPage(treatment);

                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
    }

    public void editTreatmentPage(Treatment treatment) {
        while (true) {
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Edit Symptom
                    2. Edit Note
                    3. Edit Prescriptions
                    4. Back""");

            System.out.print("Select the Treatment wanted edit: ");

            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new Symptom :");
                    String symptoms = this.scanner.nextLine();

                    treatment.setSymptom(symptoms);
                    break;
                case 2:
                    System.out.print("Enter new Note :");
                    String notes = this.scanner.nextLine();

                    treatment.setNotes(notes);
                    break;
                case 3:
                    var prescription = this.selectPrescription(treatment);
                    if (prescription != null)
                        this.editPrescriptionPage(prescription);

                    break;
                case 4:
                    return;
            }
        }
    }

    public void editPrescriptionPage(Prescription prescription) {
        while (true) {
            System.out.println();
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Edit Medicine
                    2. Edit Quantity
                    3. Edit Notes
                    4. Back""");
            System.out.println("=".repeat(30));
            System.out.print("Select the Treatment wanted edit: ");

            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    var medicine = this.medicineUI.searchProduct();
                    if (medicine == null)
                        break;

                    prescription.setProduct(medicine);
                    return;
                case 2:
                    System.out.print("Enter new Quantity :");
                    int newQuantity = this.scanner.nextInt();

                    prescription.setQuantity(newQuantity);
                    return;
                case 3:
                    System.out.print("Enter new Notes :");
                    String newNotes = this.scanner.nextLine();

                    prescription.setNotes(newNotes);
                    return;
                case 4:
                    return;
            }
        }
    }

    public void deleteConsultation(Consultation consultation) {
        System.out.println("Are you sure you want to delete this consultation?(Y/N)");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y"))
            return;

        if (medicalController.deleteConsultation(consultation.getId())) {
            System.out.println("Deleted consultation successfully.");
        } else {
            System.out.println("Failed to delete consultation. Please try again.");
        }
    }

    public void deleteDiagnosis(Consultation consultation, Diagnosis diagnosis) {
        if (diagnosis == null) {
            System.out.println("Diagnosis data is empty.");
        } else {
            System.out.print("Are you sure you want to delete this diagnosis info? (Y/N): ");

            var confirmation = this.scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("y"))
                return;

            if (medicalController.deleteDiagnosis(consultation, diagnosis.getId())) {
                medicalController.deleteAllTreatment(diagnosis);
                System.out.println("Deleted consultation successfully.");
            } else {
                System.out.println("Failed to delete consultation. Please try again.");
            }

        }
    }

    public void deleteTreatment(Diagnosis diagnosis, Treatment treatment) {
        System.out.print("Are you sure you want to delete this treatment info? (Y/N): ");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y"))
            return;

        if (medicalController.deleteTreatment(diagnosis, treatment.getId())) {
            medicalController.deleteAllPrescription(treatment);
            System.out.println("Deleted consultation successfully.");
        } else {
            System.out.println("Failed to delete consultation. Please try again.");
        }
    }

    public void deletePrescription(Treatment treatment, Prescription prescription) {
        System.out.print("Are you sure you want to delete this treatment info? (Y/N): ");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y"))
            return;

        if (medicalController.deletePrescription(treatment, prescription.getId())) {
            System.out.println("Deleted consultation successfully.");
        } else {
            System.out.println("Failed to delete consultation. Please try again.");
        }
    }

    public void deleteDiagnosisMenu(Consultation consultation, Diagnosis diagnosis) {
        while (true) {
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Delete Diagnosis
                    2. Delete Treatment
                    3. Cancel""");
            System.out.println("=".repeat(30));

            System.out.print("Select the info you want to delete: ");

            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    this.deleteDiagnosis(consultation, diagnosis);
                    break;
                case 2:
                    var treatment = this.selectTreatment(diagnosis);
                    if (treatment != null)
                        deleteTreatmentMenu(diagnosis, treatment);
                    break;
                case 3:
                    return;
            }

        }
    }

    public void deleteTreatmentMenu(Diagnosis diagnosis, Treatment treatment) {
        while (true) {
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Delete Treatment
                    2. Delete Prescription
                    3. Cancel""");
            System.out.println("=".repeat(30));

            System.out.print("Select the info you want to delete: ");

            int choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1:
                    this.deleteTreatment(diagnosis, treatment);
                    break;
                case 2:
                    var prescription = this.selectPrescription(treatment);
                    if (prescription != null)
                        this.deletePrescription(treatment, prescription);
                    break;
                case 3:
                    return;
            }

        }
    }

    public void filterConsultationRecord(InteractiveTable<Consultation> table) {
        System.out.println("=".repeat(30));
        System.out.println("""
                Consultations filtered
                (1) Patient Name
                (2) Doctor Name
                (3)Consultation Type
                (0) exit""");
        System.out.println("=".repeat(30));
        System.out.print("Filter by: ");

        var opt = this.scanner.nextInt();
        this.scanner.nextLine();

        String name;
        String value;
        switch (opt) {
            case 1:
                name = "patient name";

                System.out.print("Filter by Patient Name: ");
                value = scanner.nextLine();

                System.out.println();
                break;
            case 2:
                name = "doctor name";

                System.out.print("Filter by Doctor Name: ");
                value = scanner.nextLine();

                System.out.println();
                break;
            case 3:
                name = "ConsultationType";

                ConsultationType type = null;
                while (type == null) {
                    System.out.println("Filter by Consultation Type: ");
                    System.out.println("(1) GENERAL");
                    System.out.println("(2) SPECIALIST");
                    System.out.println("(3) EMERGENCY");
                    System.out.println("(4) FOLLOW_UP");

                    opt = scanner.nextInt();
                    this.scanner.nextLine();


                    try {
                        type = switch (opt) {
                            case 1 -> ConsultationType.GENERAL;
                            case 2 -> ConsultationType.SPECIALIST;
                            case 3 -> ConsultationType.EMERGENCY;
                            case 4 -> ConsultationType.FOLLOW_UP;
                            default -> throw new IllegalStateException("Unexpected value: " + opt);
                        };
                    } catch (IllegalStateException e) {
                        // TODO: handle invalid value
                    }
                }

                value = type.name();
                break;
            default:
                System.out.println();
                return;
        }

        filter(table, name, value);
    }

    public void filter(InteractiveTable<Consultation> table, String column, String value) {
        switch (column) {
            case "patient name": {
                table.addFilter(
                        "Filter by" + column + " \"" + value + "\"",
                        MedicalController.getConsultationPatientFilter(value)
                );
                table.display();
                break;
            }
            case "doctor name": {
                table.addFilter(
                        "Filter by" + column + " \"" + value + "\"",
                        MedicalController.getConsultationDoctorFilter(value)
                );
                table.display();
                break;
            }
            case "ConsultationType": {
                var consultationType = ConsultationType.valueOf(value);

                table.addFilter(consultationType.name(), MedicalController.getConsultationTypeFilter(consultationType));
                table.display();
            }
            default:
                break;
        }
    }

    public void createConsultationInfo() {
        var consultation = new Consultation();
        System.out.println("=".repeat(30));
        System.out.println("Consultation created");
        System.out.println("=".repeat(30));
        System.out.println("Patient Info");
        System.out.println("=".repeat(30));
        var selectPatient = this.patientUI.selectPatient();
        System.out.println();

        System.out.println("=".repeat(30));
        System.out.println("Doctor Info");
        System.out.println("=".repeat(30));
        var selectDoctor = this.doctorUI.selectDoctor();
        System.out.println();

        System.out.println("=".repeat(30));
        System.out.println("Consultation Type");
        System.out.println("=".repeat(30));

        ConsultationType type;
        while (true) {
            System.out.println("""
                    (1) GENERAL
                    (2) SPECIALIST
                    (3) EMERGENCY
                    (4) FOLLOW_UP""");
            System.out.print("Please select consultation type: ");

            var option = this.scanner.nextInt();
            this.scanner.nextLine();

            try {
                type = switch (option) {
                    case 1 -> ConsultationType.GENERAL;
                    case 2 -> ConsultationType.SPECIALIST;
                    case 3 -> ConsultationType.EMERGENCY;
                    case 4 -> ConsultationType.FOLLOW_UP;
                    default -> throw new IllegalStateException("Unexpected value: " + option);
                };
                break;
            } catch (IllegalStateException e) {
                System.out.println("Invalid choice, please re-enter.");
            }
        }

        System.out.print("Enter Consultation Note (optional) :");
        String note = this.scanner.nextLine();

        System.out.println();

        consultation.setPatient(selectPatient);
        consultation.setDoctor(selectDoctor);
        consultation.setNotes(note);
        consultation.setType(type);
        consultation.setConsultedAt(LocalDateTime.now());

        while (true) {
            this.writeUpDiagnosis(consultation);

            System.out.print("Add another diagnosis? (y/n): ");

            String more = this.scanner.nextLine().trim();
            if (!more.equalsIgnoreCase("y"))
                break;
        }

        if (medicalController.saveConsultationRecord(consultation))
            System.out.println("Consultation record added.");
        else
            System.out.println("Consultation record failed to be added. Please try again.");
    }

    public void createConsultationInfoByAppointment() {
        var consultation = new Consultation();


        System.out.println();
        var appointment = this.appointmentUI.selectAppointment();
        System.out.print("Enter Consultation Note (optional) :");
        String note = this.scanner.nextLine();
        consultation.setPatient(appointment.getPatient());
        consultation.setDoctor(appointment.getDoctor());
        consultation.setConsultedAt(appointment.getExpectedStartAt());
        consultation.setType(appointment.getAppointmentType());
        consultation.setNotes(note);

        while (true) {
            this.writeUpDiagnosis(consultation);

            System.out.print("Add another diagnosis? (y/n): ");

            String more = this.scanner.nextLine().trim();
            if (!more.equalsIgnoreCase("y"))
                break;
        }

        if (medicalController.saveConsultationRecord(consultation))
            System.out.println("Consultation record added.");
        else
            System.out.println("Consultation record failed to be added. Please try again.");

    }

    public void createConsultationMenu(){
       while(true){
           System.out.println("""
                    Create Consultation Menu
                    Please Select an Option""");
           System.out.println("=".repeat(30));
           System.out.println("""
                    1. Create Consultation Record by Appointment
                    2. Create Consultation Record by Manually
                    3. Back""");
           System.out.println("=".repeat(30));
           System.out.print("Enter your choice :");

           int choice = this.scanner.nextInt();
           this.scanner.nextLine();

           Consultation consultation;
           switch (choice) {
               case 1:
                   createConsultationInfoByAppointment();
                   break;
               case 2:
                   createConsultationInfo();
                   break;
               case 3:
                  return;

           }

       }
    }

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
            }

            diagnosis.setDiagnosis(diagnosisRecord);

            System.out.print("Enter diagnosis description: ");
            String description = this.scanner.nextLine().trim();

            if (description.isEmpty()) {
                System.out.println("Description cannot be empty. Please enter again.");
                continue;
            }

            diagnosis.setDescription(description);
            break;
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

        System.out.print("Symptom for this treatment (e.g., Fever/Cough/Nasal Congestion): ");
        while (true) {
            var symptom = this.scanner.nextLine().trim();

            if (symptom.isEmpty()) {
                System.out.println("Symptom cannot be empty. Please enter again:");
                continue;
            }

            treatment.setSymptom(symptom);
            break;
        }

        while (true) {
            this.writeUpPrescription(treatment);

            System.out.print("Add another prescription for this treatment? (y/n): ");

            String more = this.scanner.nextLine().trim();
            if (!more.equalsIgnoreCase("y")) break;
        }


        diagnosis.getTreatments().add(treatment);
    }

    public void writeUpPrescription(Treatment treatment) {
        var prescription = new Prescription();
        prescription.setTreatment(treatment);

        System.out.println("-".repeat(30));
        System.out.println("| Prescription |");
        System.out.println("-".repeat(30));

        Product product;
        while (true) {
            //product = medicineUI.searchProduct();
            product =medicalController.selectProduct();
            if (product != null)
                break;

            System.out.println("Invalid medicine selection. Please try again.");
        }

        prescription.setProduct(product);

        int quantity;
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

        System.out.println("Prescription notes (optional): ");
        String notes = this.scanner.nextLine().trim();

        prescription.setNotes(notes.isEmpty() ? null : notes);

        treatment.getPrescriptions().add(prescription);
    }

    public void startConsultationSession(Consultation consultation) {
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

            Diagnosis diagnosis;
            switch (choice) {
                case 1:
                    this.writeUpDiagnosis(consultation);
                    break;
                case 2:
                    diagnosis = this.selectDiagnosis(consultation);
                    if (diagnosis != null)
                        this.editDiagnosis(diagnosis);

                    break;
                case 3:
                    diagnosis = this.selectDiagnosis(consultation);
                    if (diagnosis != null)
                        this.deleteDiagnosisMenu(consultation, diagnosis);

                    break;
                case 4:
                    //writeNote
                case 5:
                    return;
            }
        }
    }

    public static class ConsultationTable extends InteractiveTable<Consultation> {
        public ConsultationTable(ListInterface<Consultation> consultations) {
            super(new Column[]{
                    new Column("Id", Alignment.CENTER, 4),
                    new Column("Patient Name", Alignment.CENTER, 20),
                    new Column("Doctor Name", Alignment.CENTER, 20),
                    new Column("Consultation Type", Alignment.CENTER, 20),
                    new Column("Consulted At", Alignment.CENTER, 20),
                    new Column("Notes", Alignment.CENTER, 40),
                    new Column("Diagnoses", Alignment.CENTER, 40)
            }, consultations);
        }

        @Override
        protected Cell[] getRow(Consultation o) {
            return new Cell[]{
                    new Cell(o.getId()),
                    new Cell(o.getPatient().getName()),
                    new Cell(o.getDoctor().getName()),
                    new Cell(o.getType()),
                    new Cell(o.getConsultedAt()),
                    new Cell(o.getNotes()),
                    new Cell(o.getDiagnoses().size()),
            };
        }
    }

    public static class PrescriptionTable extends InteractiveTable<Prescription> {
        public PrescriptionTable(ListInterface<Prescription> prescriptions) {
            super(new Column[]{
                    new Column("Id", Alignment.CENTER, 4),
                    new Column("Medicine", Alignment.CENTER, 40),
                    new Column("Quantity", Alignment.CENTER, 10),
                    new Column("Notes", Alignment.CENTER, 50)
            }, prescriptions);
        }

        @Override
        protected Cell[] getRow(Prescription o) {
            return new Cell[]{
                    new Cell(o.getId()),
                    new Cell(o.getProduct().getName()),
                    new Cell(o.getQuantity()),
                    new Cell(o.getNotes())
            };
        }
    }

    public static class DiagnosisTable extends InteractiveTable<Diagnosis> {
        public DiagnosisTable(ListInterface<Diagnosis> diagnoses) {
            super(new Column[]{
                    new Column("Id", Alignment.CENTER, 4),
                    new Column("Diagnosis", Alignment.CENTER, 40),
                    new Column("Diagnoses", Alignment.CENTER, 40),
                    new Column("Notes", Alignment.CENTER, 40),
                    new Column("Treatment", Alignment.CENTER, 10)
            }, diagnoses);
        }

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
    }

    public static class TreatmentTable extends InteractiveTable<Treatment> {

        public TreatmentTable(ListInterface<Treatment> treatments) {
            super(new Column[]{
                    new Column("Id", Alignment.CENTER, 4),
                    new Column("Symptom", Alignment.CENTER, 20),
                    new Column("Prescriptions", Alignment.CENTER, 40),
                    new Column("Notes", Alignment.CENTER, 50)
            }, treatments);
        }

        @Override
        protected Cell[] getRow(Treatment o) {
            return new Cell[]{
                    new Cell(o.getId()),                    // id
                    new Cell(o.getSymptom() != null ? o.getSymptom() : ""),          // symptom
                    new Cell(o.getPrescriptions() != null ? o.getPrescriptions().size() : 0),  // prescriptions count
                    new Cell(o.getNotes() != null ? o.getNotes() : "")               // notes
            };
        }
    }
}
