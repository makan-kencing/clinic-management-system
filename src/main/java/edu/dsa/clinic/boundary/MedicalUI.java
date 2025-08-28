package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.control.DispensaryController;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.dto.DiagnosisCounter;
import edu.dsa.clinic.dto.MedicalDetail;
import edu.dsa.clinic.dto.ProductCounter;
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
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;



public class MedicalUI extends UI {
    private final AppointmentController appointmentController = new AppointmentController();
    private final MedicalController medicalController;
    private final MedicineUI medicineUI;
    private final PatientUI patientUI;
    private final DoctorUI doctorUI;
    private final AppointmentUI appointmentUI;
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public MedicalUI(Terminal terminal, Scanner scanner) {
        super(terminal, scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(this.terminal);
        this.patientUI = new PatientUI(scanner);
        this.doctorUI = new DoctorUI(scanner);
        this.appointmentUI = new AppointmentUI(scanner);
    }

    public MedicalUI(Scanner scanner) {
        super(scanner);
        this.medicalController = new MedicalController();
        this.medicineUI = new MedicineUI(scanner);
        this.patientUI = new PatientUI(scanner);
        this.doctorUI = new DoctorUI(scanner);
        this.appointmentUI = new AppointmentUI(scanner);
    }

    public void startConsultationMenu() {
        while (true) {
            int choice;

            System.out.println("""
                    Consultation Menu
                    Please Select an Option""");
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Create Consultation Record
                    2. View Consultation Medical Record
                    3. Delete Consultation Record
                    4. List Out Consultation Record
                    5. Back""");
            System.out.println("=".repeat(30));
            System.out.print("Enter your choice :");
            Consultation consultation;

            try {

                choice = this.scanner.nextInt();
                this.scanner.nextLine();
                switch (choice) {
                    case 1:
                        createConsultationMenu();
                        break;
                    case 2:
                        medicalManagementMenu();
                        break;
                    case 3:
                        consultation = this.selectConsultation();
                        if (consultation != null)
                            this.deleteConsultation(consultation);
                        break;
                    case 4:
                         patientUI.listPatientDetail();
                         break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                this.scanner.nextLine();
            }
        }
    }

    public @Nullable Consultation selectConsultation() {
        var consultations = this.medicalController.getConsultationList();
        var table = new ConsultationTable(consultations);

        String opt;
        while (true) {
            table.display();

            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("""
                    (1) Select Consultation ID
                    (2) Filter Consultation Record
                    (3) Reset Filters
                    (4) Exit""");
            System.out.println("Use P/N arrow keys to change pages");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");

            try {
                opt = this.scanner.nextLine();
                this.scanner.nextLine();

                switch (opt) {
                    case "1":
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
                    case "2":
                        this.filterConsultationRecord(table);
                        break;
                    case "3":
                        table.resetFilters();
                        break;
                    case "4":
                        return null;
                    default:
                        if (opt.equalsIgnoreCase("N")) {
                            table.nextPage();
                            table.display();
                        } else if (opt.equalsIgnoreCase("P")) {
                            table.previousPage();
                            table.display();
                        } else {
                            System.out.println("Invalid input. Try again.");
                            System.out.println();
                            table.display();
                        }
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                this.scanner.nextLine();
            }

        }
    }

    public @Nullable Diagnosis selectDiagnosis(Consultation consultation) {
        var table = new DiagnosisTable(consultation.getDiagnoses());
        String opt;
        Diagnosis selectedDiagnosis =null;

        table.display();

        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Diagnosis ID " +
                    "\n(2) Filter Diagnosis Record " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("Use P/N arrow keys to change pages");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            System.out.println();

            if (!opt.equals("4")) {
                switch (opt) {
                    case "1": {
                        do {
                            table.display();
                            System.out.print("\nEnter Diagnosis ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                table.display();
                                break;
                            }

                            selectedDiagnosis=medicalController.selectDiagnosis(consultation,selectedId);
                            if (selectedDiagnosis == null) {
                                System.out.println("Diagnosis ID (" + selectedId + ") not found. Please re-enter Diagnosis ID...");
                            } else {
                                System.out.println("Diagnosis (" + selectedDiagnosis.getDiagnosis() + ") with ID (" + selectedDiagnosis.getId() + ") selected!");
                                return selectedDiagnosis;
                            }
                        } while (selectedDiagnosis == null);
                        break;
                    }
                    case "2": {
                        filterDiagnosisRecord(table);
                        break;
                    }
                    case "3": {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                    default: {
                        if (opt.equalsIgnoreCase("N")) {
                            table.nextPage();
                            table.display();
                        } else if (opt.equalsIgnoreCase("P")) {
                            table.previousPage();
                            table.display();
                        } else {
                            System.out.println("Invalid input. Try again.");
                            System.out.println();
                            table.display();
                        }
                        break;
                    }
                }
            } else {
                System.out.println();
                break;
            }
        } while (true);

        return selectedDiagnosis;


    }

    public @Nullable Treatment selectTreatment(Diagnosis diagnosis) {
        var table = new TreatmentTable(diagnosis.getTreatments());

        Treatment selectTreatment=null;
        table.display();
        String opt;

        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Treatment ID " +
                    "\n(2) Filter Treatment Record " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("Use P/N arrow keys to change pages");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            System.out.println();

            if (!opt.equals("4")) {
                switch (opt) {
                    case "1": {
                        do {
                            table.display();
                            System.out.print("\nEnter Treatment ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                table.display();
                                break;
                            }

                           selectTreatment=medicalController.selectTreatment(diagnosis,selectedId);
                            if ( selectTreatment == null) {
                                System.out.println("Treatment ID (" + selectedId + ") not found. Please re-enter Treatment ID...");
                            } else {
                                System.out.println("Treatment (" + selectTreatment.getDiagnosis() + ") with ID (" + selectTreatment.getId() + ") selected!");
                                return selectTreatment;
                            }
                        } while (selectTreatment == null);
                        break;
                    }
                    case "2": {
                       filterTreatmentRecord(table);
                        break;
                    }
                    case "3": {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                    default: {
                        if (opt.equalsIgnoreCase("N")) {
                            table.nextPage();
                            table.display();
                        } else if (opt.equalsIgnoreCase("P")) {
                            table.previousPage();
                            table.display();
                        } else {
                            System.out.println("Invalid input. Try again.");
                            System.out.println();
                            table.display();
                        }
                        break;
                    }
                }
            } else {
                System.out.println();
                break;
            }
        } while (true);

        return selectTreatment;
    }

    public @Nullable Prescription selectPrescription(Treatment treatment) {
        var table = new PrescriptionTable(treatment.getPrescriptions());
        table.display();
        Prescription selectedPrescription=null;
        String opt;

        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Prescription ID " +
                    "\n(2) Filter Prescription Record " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("Use P/N arrow keys to change pages");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            System.out.println();

            if (!opt.equals("4")) {
                switch (opt) {
                    case "1": {
                        do {
                            table.display();
                            System.out.print("\nEnter Treatment ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                table.display();
                                break;
                            }

                            selectedPrescription=medicalController.selectPrescription(treatment,selectedId);
                            if ( selectedPrescription == null) {
                                System.out.println("Prescription ID (" + selectedId + ") not found. Please re-enter Prescription ID...");
                            } else {
                                System.out.println("Prescription (" + selectedPrescription.getProduct().getName() + ") with ID (" + selectedPrescription.getId() + ") selected!");
                                return selectedPrescription;
                            }
                        } while (selectedPrescription == null);
                        break;
                    }
                    case "2": {
                       filterPrescriptionRecord(table);
                        break;
                    }
                    case "3": {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                    default: {
                        if (opt.equalsIgnoreCase("N")) {
                            table.nextPage();
                            table.display();
                        } else if (opt.equalsIgnoreCase("P")) {
                            table.previousPage();
                            table.display();
                        } else {
                            System.out.println("Invalid input. Try again.");
                            System.out.println();
                            table.display();
                        }
                        break;
                    }
                }
            } else {
                System.out.println();
                break;
            }
        } while (true);

        return selectedPrescription;
    }

    public void selectMedicalDetail(){
        var selectPatient =patientUI.selectPatient();
        ListInterface<MedicalDetail> rows = medicalController.getMedicalDetails(selectPatient);
        var table = new ViewMedicalDetail(rows);
        table.display();
    }

    public void viewDiagnosisDetails(Diagnosis diagnosis) {
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));
        System.out.println("Diagnosis: " + diagnosis.getDiagnosis());
        System.out.println("Description: " + diagnosis.getDescription());
        System.out.println("Consultation Type: " + diagnosis.getConsultation().getType());
        System.out.println("Notes: " + diagnosis.getNotes());
    }

    public void viewConsultationDetails(Consultation consultation) {
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


            try {
                int choice = this.scanner.nextInt();
                this.scanner.nextLine();

                switch (choice) {
                    case 1:
                        var product= this.medicineUI.selectProductInStock();
                        if (product == null)
                            break;

                        prescription.setProduct(product);
                        return;
                    case 2:

                        System.out.print("Enter new Quantity :");
                        int availableStocks= MedicineController.getAvailableStocks(prescription.getProduct());
                        int newQuantity = this.scanner.nextInt();
                        if (newQuantity < availableStocks){
                            prescription.setQuantity(newQuantity);
                        }
                        else {
                            System.out.println(prescription.getProduct().getName()+ "Quantity remain" + availableStocks +". Goods insufficient.Please reduce quantity or notify pharmacy");
                        }
                        return;
                    case 3:
                        System.out.print("Enter new Notes :");
                        String newNotes = this.scanner.nextLine();

                        prescription.setNotes(newNotes);
                        return;
                    case 4:
                        return;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please select between 1 and 5.");
                this.scanner.nextLine();
            }
        }
    }

    // delete
    public void deleteConsultation(Consultation consultation) {
        System.out.print("Are you sure you want to delete this consultation?(Y)");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Cancelled Delete Consultation.");
            return;
        }

        if (medicalController.deleteConsultation(consultation.getId())) {
            System.out.println("Deleted consultation successfully.");
        } else {
            System.out.println("Failed to delete consultation. Please try again.");
        }
    }

    public void deleteDiagnosis(Consultation consultation, Diagnosis diagnosis) {
        System.out.print("Are you sure you want to delete this diagnosis info? (Y/N): ");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Cancelled Delete treatment.");
            return;
        }

        if (medicalController.deleteDiagnosis(consultation, diagnosis.getId())) {
            System.out.println("Deleted Diagnosis successfully.");
        } else {
            System.out.println("Failed to delete Diagnosis. Please try again.");
        }
    }

    public void deleteTreatment(Diagnosis diagnosis, Treatment treatment) {
        System.out.print("Are you sure you want to delete this treatment info? (Y/N): ");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Cancelled Delete Treatment.");
            return;
        }

        if (medicalController.deleteTreatment(diagnosis, treatment.getId())) {
            System.out.println("Deleted Treatment successfully.");
        } else {
            System.out.println("Failed to delete Treatment. Please try again.");
        }
    }

    public void deletePrescription(Treatment treatment, Prescription prescription) {
        System.out.print("Are you sure you want to delete this prescription info? (Y): ");

        var confirmation = this.scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y"))
            return;

        if (medicalController.deletePrescription(treatment, prescription.getId())) {
            System.out.println("Deleted Prescription successfully.");
        } else {
            System.out.println("Failed to delete Prescription. Please try again.");
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
                    return;
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
                    return;
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

    //filter
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
                        System.out.println("Invalid input");
                    }
                }

                value = type.name();
                break;
            default:
                System.out.println();
                return;
        }

        filterConsultation(table, name, value);
    }

    public void filterConsultation(InteractiveTable<Consultation> table, String column, String value) {
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

    public void filterDiagnosisRecord(InteractiveTable<Diagnosis> table) {
        System.out.println("=".repeat(30));
        System.out.println("""
                Diagnosis filtered
                (1) Diagnosis Name
                (0) exit""");
        System.out.println("=".repeat(30));
        System.out.print("Filter by: ");

        var opt = this.scanner.nextInt();
        this.scanner.nextLine();

        String name;
        String value;
        if (opt == 1) {
            name = "Diagnosis Name";

            System.out.print("Filter by Diagnosis Name: ");
            value = scanner.nextLine();

            System.out.println();
        }else {
            System.out.println();
            return;
        }

       filterDiagnosis(table, name, value);
    }

    public void filterDiagnosis(InteractiveTable<Diagnosis> table, String column, String value) {
      if(Objects.equals(column, "Diagnosis Name"))  {
            table.addFilter(
                    "Filter by" + column + " \"" + value + "\"",
                    MedicalController.getDiagnosisFilter(value)
            );
            table.display();
        }
    }

    public void filterTreatmentRecord(InteractiveTable<Treatment> table) {
        System.out.println("=".repeat(30));
        System.out.println("""
                Diagnosis filtered
                (1) Symptom
                (0) exit""");
        System.out.println("=".repeat(30));
        System.out.print("Filter by: ");

        var opt = this.scanner.nextInt();
        this.scanner.nextLine();

        String name;
        String value;
        if (opt == 1) {
            name = "Symptom";

            System.out.print("Filter by Diagnosis Name: ");
            value = scanner.nextLine();

            System.out.println();
        }else {
            System.out.println();
            return;
        }

        filterTreatment(table, name, value);
    }

    public void filterTreatment(InteractiveTable<Treatment> table, String column, String value) {
        if(Objects.equals(column, "Symptom"))  {
            table.addFilter(
                    "Filter by" + column + " \"" + value + "\"",
                   MedicalController.getTreatmentFilter(value)
            );
            table.display();
        }
    }

    public void filterPrescriptionRecord(InteractiveTable<Prescription> table) {
        System.out.println("=".repeat(30));
        System.out.println("""
                Diagnosis filtered
                (1) Medicine
                (0) exit""");
        System.out.println("=".repeat(30));
        System.out.print("Filter by: ");

        var opt = this.scanner.nextInt();
        this.scanner.nextLine();

        String name;
        String value;
        if (opt == 1) {
            name = "Medicine";

            System.out.print("Filter by Medicine: ");
            value = scanner.nextLine();

            System.out.println();
        }else {
            System.out.println();
            return;
        }

        filterPrescription(table, name, value);

    }

    public void filterPrescription(InteractiveTable<Prescription> table, String column, String value) {
        if(Objects.equals(column, "Medicine"))  {
            table.addFilter(
                    "Filter by" + column + " \"" + value + "\"",
                    MedicalController.getPrescriptionFilter(value)
            );
            table.display();
        }
    }


    //create consultation
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

        if (medicalController.saveConsultationRecord(consultation)) {
            DispensaryController.queueConsultation(consultation);
            System.out.println("Consultation record added.");
        }
        else
            System.out.println("Consultation record failed to be added. Please try again.");
    }

    public void createConsultationInfoByAppointment() {
        var consultation = new Consultation();


        System.out.println();
        var appointment = this.appointmentUI.selectAppointment();
        if (appointment == null) {
            System.out.println("Appointment not selected.");
            return;
        }
        System.out.print("Enter Consultation Note (optional) :");
        String note = this.scanner.nextLine();
        consultation.setPatient(appointment.getPatient());
        consultation.setDoctor(appointment.getDoctor());
        consultation.setConsultedAt(appointment.getExpectedStartAt());
        consultation.setType(appointment.getAppointmentType());
        consultation.setNotes(note);

        while (true) {
            try {
                this.writeUpDiagnosis(consultation);

                System.out.print("Add another diagnosis? (y): ");

                String more = this.scanner.nextLine().trim();
                if (!more.equalsIgnoreCase("y"))
                    break;
            } catch (IllegalStateException e) {
                System.out.println("Invalid choice, please re-enter.");
            }
        }

        if (medicalController.saveConsultationRecord(consultation)) {
            appointmentController.cancelAppointment(appointment);
            DispensaryController.queueConsultation(consultation);
            System.out.println("Consultation record added.");
        }
        else
            System.out.println("Consultation record failed to be added. Please try again.");

    }

    public void createConsultationMenu() {
        while (true) {

            System.out.println("""
                    Create Consultation Menu
                    Please Select an Option""");
            System.out.println("=".repeat(30));
            System.out.println("""
                    1. Create Consultation Record by Appointment
                    2. Create Consultation Record by Manually
                    3. Back""");
            System.out.println("=".repeat(30));
            Consultation consultation;
            try {
                System.out.print("Enter your choice :");
                int choice = this.scanner.nextInt();
                this.scanner.nextLine();
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please re-enter.");
                this.scanner.nextLine();
            }
        }
    }


    //create medical
    public void writeUpDiagnosis(Consultation consultation) {
        var diagnosis = new Diagnosis();
        diagnosis.setConsultation(consultation);
        viewConsultationDetails(consultation);

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
            try {
                System.out.print("Add another prescription for this treatment? (y): ");

                String more = this.scanner.nextLine().trim();
                if (!more.equalsIgnoreCase("y")) break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please re-enter.");
            }
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
            product = medicineUI.selectProductInStock();
            if (product != null)
                break;

            System.out.println("Invalid medicine selection. Please try again.");
        }

        prescription.setProduct(product);

        int quantity;
        while (true) {
            System.out.print("Quantity (>=1): ");
            String quantityInput = this.scanner.nextLine().trim();
            int availableStocks=MedicineController.getAvailableStocks(product);
            try {
                quantity = Integer.parseInt(quantityInput);
                if (availableStocks > quantity) {
                    prescription.setQuantity(quantity);
                    break;
                } else {
                    System.out.println(product.getName()+ "Quantity remain" + availableStocks +". Goods insufficient.Please reduce quantity or notify pharmacy");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. The input must be an integer.");
            }
        }



        System.out.println("Prescription notes (optional): ");
        String notes = this.scanner.nextLine().trim();

        prescription.setNotes(notes.isEmpty() ? null : notes);

        treatment.getPrescriptions().add(prescription);
    }

    public void medicalManagementMenu(){
        while (true) {
            System.out.println(""" 
                    1. Manage Medical Record
                    2. Generate Medical Summary Report
                    3. Back""");
            System.out.print("Enter your choice :");
            Consultation consultation;
            int choice;
            try {
                choice = this.scanner.nextInt();
                this.scanner.nextLine();

                switch (choice) {
                    case 1:
                        consultation = this.selectConsultation();
                        if (consultation != null)
                            this.startMedicalSession(consultation);
                        break;
                    case 2:
                        diagnosisReport();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice, please re-enter.");
                break;
            }
        }
    }

    public void startMedicalSession(Consultation consultation) {
        while (true) {

            System.out.println(""" 
                    1. Add Medical record
                    2. Edit Medical record
                    3. Delete Medical Record
                    4. List Medical Records
                    5. Back""");
            System.out.print("Enter your choice :");
            try {
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
                        selectMedicalDetail();
                        break;
                    case 5:
                        return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please re-enter.");
                this.scanner.nextLine();
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
                    new Column("Notes", Alignment.CENTER, 70),
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
                    new Column("Diagnoses", Alignment.CENTER, 150),
                    new Column("Notes", Alignment.CENTER, 60),
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

    public static class ViewMedicalDetail extends InteractiveTable<MedicalDetail> {
        public ViewMedicalDetail(ListInterface<MedicalDetail> medicalDetails) {
            super(new Column[]{
                            new Column("Diagnosis Id", Alignment.CENTER, 15),
                            new Column("Diagnosis", Alignment.CENTER, 30),
                            new Column("Treatment Id", Alignment.CENTER, 15),
                            new Column("Symptom", Alignment.CENTER, 30),
                            new Column("Prescription Id", Alignment.CENTER, 15),
                            new Column("Medicine", Alignment.CENTER, 30),
                    },medicalDetails);
        }
        private String lastConsultationId = null;
        private String lastDiagnosisId = null;
        private String lastTreatmentId = null;
        private String lastPrescriptionId = null;
        private String lastDiagnosis = null;
        private String lastSymptom = null;
        private String lastMedicine = null;

        @Override
        protected Cell[] getRow(MedicalDetail m) {
            Consultation c = m.getConsultation();
            Diagnosis d = m.getDiagnosis();
            Treatment t = m.getTreatment();
            Prescription p = m.getPrescription();

            String consultationId = c != null ? String.valueOf(c.getId()) : "N/A";
            String diagnosisId = d != null ? String.valueOf(d.getId()) : "N/A";
            String treatmentId = t != null ? String.valueOf(t.getId()) : "N/A";
            String prescriptionId = p != null ? String.valueOf(p.getId()) : "N/A";
            String symptom = t != null && t.getSymptom() != null ? t.getSymptom() : "N/A";
            String diagnosis = d != null && d.getDiagnosis() != null ? d.getDiagnosis() : "N/A";
            String medicine = p != null && p.getProduct() != null && p.getProduct().getName() != null ? p.getProduct().getName() : "N/A";


            if (!consultationId.equals(lastConsultationId)) {
                lastDiagnosisId = null;
                lastTreatmentId = null;
                lastPrescriptionId = null;
                lastSymptom = null;
                lastDiagnosis = null;
                lastMedicine = null;
            }
            if (!diagnosisId.equals(lastDiagnosisId)) {
                lastTreatmentId = null;
                lastPrescriptionId = null;
                lastSymptom = null;
                lastDiagnosis = null;
                lastMedicine = null;
            }
            if (!treatmentId.equals(lastTreatmentId)) {
                lastPrescriptionId = null;
                lastMedicine = null;
            }
            if (!prescriptionId.equals(lastPrescriptionId)) {
                lastMedicine = null;
            }

            if (diagnosisId.equals(lastDiagnosisId)) diagnosisId = " ";
            else lastDiagnosisId = diagnosisId;

            if (treatmentId.equals(lastTreatmentId)) treatmentId = " ";
            else lastTreatmentId = treatmentId;

            if (prescriptionId.equals(lastPrescriptionId)) prescriptionId = " ";
            else lastPrescriptionId = prescriptionId;

            if (symptom.equals(lastSymptom)) symptom = " ";
            else lastSymptom = symptom;

            if (diagnosis.equals(lastDiagnosis)) diagnosis = " ";
            else lastDiagnosis = diagnosis;

            if (medicine.equals(lastMedicine)) medicine = " ";
            else lastMedicine = medicine;

            return new Cell[]{
                    new Cell(diagnosisId),
                    new Cell(diagnosis),
                    new Cell(treatmentId),
                    new Cell(symptom),
                    new Cell(prescriptionId),
                    new Cell(medicine)
            };
        };
    }

    public static  class ViewDiagnosisReport extends InteractiveTable<DiagnosisCounter>{
        public ViewDiagnosisReport(ListInterface<DiagnosisCounter> diagnosisCounters) {
            super(new Column[]{
                    new Column("Diagnosis",Alignment.CENTER,40),
                    new Column("Occurrence",Alignment.CENTER,15),
                    new Column("Medicine Product Using",Alignment.CENTER,110),
                    new Column("Product Using Num",Alignment.CENTER,50)
            },diagnosisCounters);
        }
        @Override
        protected Cell[] getRow(DiagnosisCounter d) {
            ListInterface<ProductCounter> p =d.productCounters();

            StringBuilder productNames = new StringBuilder();
            StringBuilder productCounts = new StringBuilder();

            for (ProductCounter productCounter : p) {
                productNames.append(productCounter.key().getName()).append(", ");
                productCounts.append(productCounter.count()).append(", ");
            }

            if (!productNames.isEmpty()) productNames.setLength(productNames.length() - 2);
            if (!productCounts.isEmpty()) productCounts.setLength(productCounts.length() - 2);

            return new Cell[]{
                    new Cell(d.key()),
                    new Cell(d.count()),
                    new Cell(productNames.toString()),
                    new Cell(productCounts.toString())
            };
        }

    }

    //diagnosis report
    public void diagnosisReport() {
        while(true) {
            System.out.println("=".repeat(230));
            System.out.printf("%140s\n", "TUNKU ABDUL RAMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY");
            System.out.printf("%120s\n", "MEDICAL CHECKING SUBSYSTEM");
            System.out.println();
            System.out.printf("%124s\n", "DOCTOR OCCURRENCE AND MEDICATION UTILIZATION REPORT");
            System.out.printf("Generated at: %s%n", DATE_FORMAT.format(java.time.LocalDateTime.now()));
            System.out.printf("%125s\n", "-".repeat(35));

            var counts = MedicalController.countDiagnosesOccurrence();
            var table = new ViewDiagnosisReport(counts);
            table.display();
            int total= MedicalController.getTotalProductUsage(counts);
            System.out.println("Total Diagnosis Type :" + counts.size());
            System.out.println("Total Medicine Product Usage :" + total);
            System.out.print("Use P/N arrow keys to change pages :");
            String opt ;
            try {
                opt = this.scanner.nextLine().toLowerCase();

                switch (opt) {
                    case "p":
                        table.previousPage();
                        table.display();
                        break;
                    case "n":
                        table.nextPage();
                        table.display();
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }catch (InputMismatchException e){
                    System.out.println("Input Mismatch");
                    break;
            }
            System.out.println("*".repeat(230));
            System.out.printf("%120s\n", "END OF THE REPORT");
            System.out.println("=".repeat(230));
            this.scanner.nextLine();
        }
    }

    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build()
        ) {
            var scanner = new Scanner(System.in);

            var medicalUI = new MedicalUI(terminal, scanner);
            medicalUI.startConsultationMenu();
        }
    }

}
