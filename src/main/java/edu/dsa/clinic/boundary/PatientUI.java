package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.PatientController;
import edu.dsa.clinic.dto.PatientDetail;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PatientUI extends UI {
    private final PatientController patientController = new PatientController();
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public PatientUI(Scanner scanner) {
        super(scanner);
    }

    public void patientMenu() {
        System.out.println("=".repeat(29));
        System.out.println("| Welcome to Patient Module |");
        System.out.println("=".repeat(29));

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Display Patient List");
            System.out.println("(2) View Patient Details");
            System.out.println("(3) Patient Maintenance");
            System.out.println("(4) Consultation Queue");
            System.out.println("(5) Generate Summary Report");
            System.out.println("(6) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            switch (opt) {
                case "1" -> listPatient();
                case "2" -> listPatientDetail();
                case "3" -> {
                    switch (maintenanceMenu()) {
                        case 1 -> addPatient();
                        case 2 -> editPatient();
                        case 3 -> removePatient();
                    }
                    System.out.println();
                }
                case "4" -> {
                    // To be implemented
                }
                case "5" -> {
                    // To be implemented
                }
                case "6" -> System.out.println("Exiting Patient Module...");
                default -> System.out.println("Invalid input. Try again.\n");
            }
        } while (!opt.equals("6"));
    }

    public void listPatient() {
        var table = initializePatientTable();
        table.display();

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Sort Patient Records " +
                    "\n(2) Filter Patient Record " +
                    "\n(3) Reset Sorters " +
                    "\n(4) Reset Filters " +
                    "\n(5) Exit");
            System.out.println("Use P/N arrow keys to change pages");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            System.out.println();

            if (!opt.equals("5")) {
                switch (opt) {
                    case "1": {
                        sortPatient(table);
                        break;
                    }
                    case "2": {
                        filterPatient(table);
                        break;
                    }
                    case "3": {
                        table.resetSorters();
                        table.display();
                        break;
                    }
                    case "4": {
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
    }


    public void listPatientDetail() {
        Patient selectedPatient = selectPatient();
        ListInterface<PatientDetail> rows = flattenDetails(selectedPatient);

        var detailTable = new InteractiveTable<>(new Column[]{
                new Column("Consultation Id", Alignment.CENTER, 15),
                new Column("Diagnosis Id", Alignment.CENTER, 15),
                new Column("Treatment Id", Alignment.CENTER, 15),
                new Column("Prescription Id", Alignment.CENTER, 15),
                new Column("Doctor", Alignment.CENTER, 20),
                new Column("Consulted At", Alignment.CENTER, 25),
                new Column("Symptom", Alignment.CENTER, 30),
                new Column("Diagnosis", Alignment.CENTER, 30),
                new Column("Medicine", Alignment.CENTER, 30),
        }, rows) {

            @Override
            protected Cell[] getRow(PatientDetail r) {
                Consultation c = r.getConsultation();
                Diagnosis d = r.getDiagnosis();
                Treatment t = r.getTreatment();
                Prescription p = r.getPrescription();

                return new Cell[]{
                        new Cell(c != null ? c.getId() : "N/A"),
                        new Cell(d != null ? d.getId() : "N/A"),
                        new Cell(t != null ? t.getId() : "N/A"),
                        new Cell(p != null ? p.getId() : "N/A"),
                        new Cell(c != null && c.getDoctor() != null ? c.getDoctor().getName() : "N/A"),
                        new Cell(c != null && c.getConsultedAt() != null
                                ? DATE_FORMAT.format(c.getConsultedAt()) : "N/A"),
                        new Cell(t != null && t.getSymptom() != null ? t.getSymptom() : "N/A"),
                        new Cell(d != null && d.getDiagnosis() != null ? d.getDiagnosis() : "N/A"),
                        new Cell(p != null && p.getMedicine() != null ? p.getMedicine().toString() : "N/A")
                };
            }
        };

        detailTable.display();
    }

    public void addPatient() {
        Patient addPatient = new Patient();

        System.out.print("Enter Patient Name : ");
        addPatient.setName(this.scanner.nextLine().trim());
        System.out.println("-".repeat(33));
        System.out.println("| (M) for Male | (F) for Female |");
        System.out.println("-".repeat(33));

        String input;
        do {
            System.out.print("Enter Patient Gender: ");
            input = scanner.nextLine().trim().toUpperCase();

            if (!input.equals("M") && !input.equals("F")) {
                System.out.println("Invalid input! Please enter M or F.");
            }
        } while (!input.equals("M") && !input.equals("F"));
        Gender gender = input.equals("M") ? Gender.MALE : Gender.FEMALE;
        addPatient.setGender(gender);

        String ic;
        do {
            System.out.print("Enter Patient Identification (YYMMDD-XX-XXXX): ");
            ic = scanner.nextLine().trim();
            if (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
                System.out.println("Invalid IC format! Example: 010203-14-5678");
            }
        } while (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$"));
        addPatient.setIdentification(ic);

        String phone;
        do {
            System.out.print("Enter Patient Contact Number (+60XXXXXXXXX): ");
            phone = scanner.nextLine().trim();
            if (!phone.matches("^\\+60\\d{8,11}$")) {
                System.out.println("Invalid phone number! Example: +60123456789");
            }
        } while (!phone.matches("^\\+60\\d{8,11}$"));
        addPatient.setContactNumber(phone);

        System.out.println();
        patientController.createPatientRecord(addPatient);
        System.out.println("Patient (" + addPatient.getName() + ") with ID (" + addPatient.getId() + ") Created Successfully!\n");
    }

    public void editPatient() {
        Patient editPatient = selectPatient();
        if (editPatient == null) {
            System.out.println("No patient selected.\n");
            return;
        }

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("Select an Attribute to Modify:");
            System.out.println("(1) name");
            System.out.println("(2) gender");
            System.out.println("(3) identification");
            System.out.println("(4) contact number");
            System.out.println("(0) exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextLine();

            switch (opt) {
                case "1" -> {
                    System.out.print("Enter New Patient Name : ");
                    editPatient.setName(this.scanner.nextLine().trim());
                    break;
                }
                case "2" -> {
                    System.out.println("| (M) for Male | (F) for Female |");
                    System.out.println("-".repeat(33));

                    String input;
                    do {
                        System.out.print("Enter New Patient Gender: ");
                        input = scanner.nextLine().trim().toUpperCase();

                        if (!input.equals("M") && !input.equals("F")) {
                            System.out.println("Invalid input! Please enter M or F.");
                        }
                    } while (!input.equals("M") && !input.equals("F"));
                    Gender gender = input.equals("M") ? Gender.MALE : Gender.FEMALE;
                    editPatient.setGender(gender);
                    break;
                }
                case "3" -> {
                    String ic;
                    do {
                        System.out.print("Enter Patient Identification (YYMMDD-XX-XXXX): ");
                        ic = scanner.nextLine().trim();
                        if (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
                            System.out.println("Invalid IC format! Example: 010203-14-5678");
                        }
                    } while (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$"));
                    editPatient.setIdentification(ic);
                    break;
                }
                case "4" -> {
                    String phone;
                    do {
                        System.out.print("Enter Patient Contact Number (+60XXXXXXXXX): ");
                        phone = scanner.nextLine().trim();
                        if (!phone.matches("^\\+60\\d{8,11}$")) {
                            System.out.println("Invalid phone number! Example: +60123456789");
                        }
                    } while (!phone.matches("^\\+60\\d{8,11}$"));
                    editPatient.setContactNumber(phone);
                    break;
                }
                case "0" -> {
                    System.out.println("Exiting without changes.\n");
                    return;
                }
                default -> {
                    System.out.println("Invalid input. Try again.\n");
                    continue;
                }
            }
            System.out.println();
            var updated = patientController.editPatientRecord(editPatient);
            if (updated == null) {
                System.out.println("Patient (" + editPatient.getName() + ") with ID (" + editPatient.getId() + ") Not Found.\n");
            } else {
                System.out.println("Patient (" + updated.getName() + ") with ID (" + updated.getId() + ") Modified Successfully!\n");
            }
            return;
        } while (true);
    }

    public void removePatient() {
        Patient deletePatient = selectPatient();
        if (deletePatient == null) {
            System.out.println("No patient selected.\n");
            return;
        }

        System.out.println("Are you sure to delete this entry? (Y/N)");
        if (this.scanner.next().equalsIgnoreCase("Y")) {
            patientController.removePatientRecord(deletePatient);
            System.out.println();
            System.out.printf("Patient (" + deletePatient.getName() + ") Record Deleted.");
        }
        this.scanner.nextLine();
        System.out.println();
    }

    public Patient selectPatient() {
        Patient selectedPatient = null;

        var table = initializePatientTable();
        table.display();

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Patient ID " +
                    "\n(2) Filter Patient Record " +
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
                            System.out.print("\nEnter Patient ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                table.display();
                                break;
                            }

                            selectedPatient = patientController.performSelect(selectedId);
                            if (selectedPatient == null) {
                                System.out.println("Patient with ID (" + selectedId + ") not found. Please re-enter Patient ID...");
                            } else {
                                System.out.println("Patient (" + selectedPatient.getName() + ") with ID (" + selectedPatient.getId() + ") selected!");
                                return selectedPatient;
                            }
                        } while (selectedPatient == null);
                        break;
                    }
                    case "2": {
                        filterPatient(table);
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

        return selectedPatient;
    }

    public void sortPatient(InteractiveTable<Patient> table) {
        System.out.println("-".repeat(30));
        System.out.println("Sorters:");
        System.out.println("(1) id");
        System.out.println("(2) name");
        System.out.println("(3) identification");
        System.out.println("(4) contact number");
        System.out.println("(5) gender");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Sort by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                var value = orderByMenu();
                System.out.println();
                sort(table, "id", value);
                break;
            }
            case 2: {
                var value = orderByMenu();
                System.out.println();
                sort(table, "name", value);
                break;
            }
            case 3: {
                var value = orderByMenu();
                System.out.println();
                sort(table, "identification", value);
                break;
            }
            case 4: {
                var value = orderByMenu();
                System.out.println();
                sort(table, "contact", value);
                break;
            }
            case 5: {
                var value = orderByMenu();
                System.out.println();
                sort(table, "gender", value);
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void filterPatient(InteractiveTable<Patient> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("(1) name");
        System.out.println("(2) identification");
        System.out.println("(3) contact number");
        System.out.println("(4) gender");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                System.out.print("Search name by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "name", value);
                break;
            }
            case 2: {
                System.out.print("Search identification by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "identification", value);
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "contact", value);
            }
            case 4: {
                System.out.println();
                System.out.println("-".repeat(30));
                System.out.println("Filter gender by: ");
                System.out.println("(1) male");
                System.out.println("(2) female");
                System.out.println("-".repeat(30));
                System.out.print("Selection : ");
                var value = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (value == 1) {
                    filter(table, "gender", "male");
                } else if (value == 2) {
                    filter(table, "gender", "female");
                }
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void filter(InteractiveTable<Patient> table, String column, String value) {
        switch (column) {
            case "id": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                table.addSorter("Name Ascending", (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                table.display();
                break;
            }
            case "name": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "identification": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getIdentification().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "contact": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getContactNumber().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "gender": {
                if (value.equals(Gender.MALE.name())) {
                    table.addFilter("Male only", p -> p.getGender() == Gender.MALE);
                    table.display();
                } else if (value.equals(Gender.FEMALE.name())) {
                    table.addFilter("Female only", p -> p.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }
            default:
                break;
        }
    }

    public void sort(InteractiveTable<Patient> table, String column, boolean ascending) {
        switch (column) {
            case "id": {
                table.addSorter(
                        ascending ? "ID Ascending" : "ID Descending",
                        ascending
                                ? (p1, p2) -> Integer.compare(p1.getId(), p2.getId())
                                : (p1, p2) -> Integer.compare(p2.getId(), p1.getId())
                );
                table.display();
                break;
            }
            case "name": {
                table.addSorter(
                        ascending ? "Name Ascending" : "Name Descending",
                        ascending
                                ? (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName())
                                : (p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName())
                );
                table.display();
                break;
            }
            case "identification": {
                table.addSorter(
                        ascending ? "Identification Ascending" : "Identification Descending",
                        ascending
                                ? (p1, p2) -> p1.getIdentification().compareToIgnoreCase(p2.getIdentification())
                                : (p1, p2) -> p2.getIdentification().compareToIgnoreCase(p1.getIdentification())
                );
                table.display();
                break;
            }
            case "contact": {
                table.addSorter(
                        ascending ? "Contact Ascending" : "Contact Descending",
                        ascending
                                ? (p1, p2) -> p1.getContactNumber().compareTo(p2.getContactNumber())
                                : (p1, p2) -> p2.getContactNumber().compareTo(p1.getContactNumber())
                );
                table.display();
                break;
            }
            case "gender": {
                table.addSorter(
                        ascending ? "Gender Ascending" : "Gender Descending",
                        ascending
                                ? (p1, p2) -> p1.getGender().compareTo(p2.getGender())
                                : (p1, p2) -> p2.getGender().compareTo(p1.getGender())
                );
                table.display();
                break;
            }
            default:
                break;
        }
    }

    public int maintenanceMenu() {
        int opt;
        do {
            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("(1) Create Patient");
            System.out.println("(2) Modify Patient");
            System.out.println("(3) Delete Patient");
            System.out.println("(0) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = scanner.nextInt();
            scanner.nextLine();

            if (opt < 0 || opt > 3) {
                System.out.println("Invalid input! Try again.");
            }
        } while (opt < 0 || opt > 3);
        return opt;
    }

    public boolean orderByMenu() {
        String value;
        do {
            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("Sort name by: ");
            System.out.println("(A) ascending order");
            System.out.println("(D) descending order");
            System.out.println("-".repeat(30));
            System.out.print("Order by : ");
            value = scanner.nextLine();
            System.out.println();

            if (!value.equalsIgnoreCase("A") && !value.equalsIgnoreCase("D")) {
                System.out.println("Invalid input! Please enter A or D.");
            }
        } while (!value.equalsIgnoreCase("A") && !value.equalsIgnoreCase("D"));
        return value.equalsIgnoreCase("A");
    }

    public InteractiveTable<Patient> initializePatientTable() {
        return new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Name", Alignment.CENTER, 20),
                new Column("Gender", Alignment.CENTER, 10),
                new Column("Identification", Alignment.CENTER, 20),
                new Column("Contact No", Alignment.CENTER, 20)
        }, Database.patientsList.clone()) {
            @Override
            protected Cell[] getRow(Patient o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getName()),
                        new Cell(o.getGender(), Alignment.CENTER),
                        new Cell(o.getIdentification()),
                        new Cell(o.getContactNumber())
                };
            }
        };
    }

    private ListInterface<PatientDetail> flattenDetails(Patient patient) {
        ListInterface<PatientDetail> rows = new DoubleLinkedList<>();

        for (Consultation consult : patientController.getPatientConsultations(patient)) {
            if (consult.getDiagnoses() == null) {
                rows.add(new PatientDetail(consult, null, null, null));
            } else {
                for (Diagnosis diag : consult.getDiagnoses()) {
                    if (diag.getTreatments() == null) {
                        rows.add(new PatientDetail(consult, diag, null, null));
                    } else {
                        for (Treatment treat : diag.getTreatments()) {
                            if (treat.getPrescriptions() == null) {
                                rows.add(new PatientDetail(consult, diag, treat, null));
                            } else {
                                for (Prescription pres : treat.getPrescriptions()) {
                                    rows.add(new PatientDetail(consult, diag, treat, pres));
                                }
                            }
                        }
                    }
                }
            }
        }
        return rows;
    }
}
