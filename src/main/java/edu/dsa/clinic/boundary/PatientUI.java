package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.PatientController;
import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.dto.PatientCounter;
import edu.dsa.clinic.dto.PatientDetail;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

import edu.dsa.clinic.utils.StringUtils;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import java.util.Scanner;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PatientUI extends UI {
    private final PatientController patientController = new PatientController();
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public PatientUI(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void startMenu() {
        String opt;
        do {
            System.out.println("=".repeat(29));
            System.out.println("| Welcome to Patient Module |");
            System.out.println("=".repeat(29));
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
                    String subOpt;
                    do {
                        subOpt = queueMenu();
                        switch (subOpt) {
                            case "1" -> listConsultationQueue();
                            case "2" -> addConsultationQueue();
                            case "3" -> removeConsultationQueue();
                            case "0" -> System.out.println("Exiting Queue Menu...");
                            default -> System.out.println("Invalid selection! Try again.");
                        }
                    } while (!subOpt.equals("0"));
                    System.out.println();
                }
                case "5" -> {
                    generateSummaryReport();
                }
                case "6" -> System.out.println("Exiting Patient Module...");
                default -> System.out.println("Invalid input. Try again.\n");
            }
        } while (!opt.equals("6"));
    }

    public void listConsultationQueue() {
        var table = initializeQueueTable();
        table.display();

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Sort Consultation Queue " +
                    "\n(2) Filter Consultation Queue " +
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
                        sortQueue(table);
                        break;
                    }
                    case "2": {
                        filterQueue(table);
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
                break;
            }
        } while (true);
    }

    public void addConsultationQueue() {
        Patient selectedPatient;
        ConsultationQueue addQueue;

        while (true) {
            selectedPatient = selectPatient();

            if (selectedPatient == null) {
                return;
            }

            addQueue = (ConsultationQueue) patientController.validateUnique(String.valueOf(selectedPatient.getId()), "queue");

            if (addQueue != null) {
                System.out.println("Existing queue for patient (" + addQueue.patient().getName() + ") found! Please select another patient...\n");
            } else {
                break;
            }
        }

        System.out.println("-".repeat(18));
        System.out.println("| (1) General    |");
        System.out.println("| (2) Specialist |");
        System.out.println("| (3) Emergency  |");
        System.out.println("| (4) Follow-up  |");
        System.out.println("-".repeat(18));

        int input = -1;
        while (true) {
            System.out.print("Select Consultation Type: ");
            String line = scanner.nextLine();

            if (line.isBlank()) {
                System.out.println("\nNo consultation type selected. Returning...\n");
                return; // exit method
            }

            line = line.trim();
            if (line.matches("\\d+")) {
                input = Integer.parseInt(line);
            } else {
                input = -1;
            }

            if (input >= 1 && input <= ConsultationType.values().length) {
                break;
            } else {
                System.out.println("Invalid type selected!");
                System.out.println();
            }
        }

        System.out.println();
        ConsultationType selectedType = ConsultationType.values()[input - 1];

        addQueue = new ConsultationQueue(selectedPatient, selectedType);

        System.out.println();
        patientController.createQueue(addQueue);
        System.out.println("Queue for Patient (" + addQueue.patient().getName() + ") with Consultation Type (" + addQueue.type() + ") Generated Successfully!\n");
    }

    public void removeConsultationQueue() {
        ConsultationQueue deleteQueue = selectConsultationQueue();
        if (deleteQueue == null) {
            System.out.println("No queue selected.\n");
            return;
        }

        System.out.println("Are you sure to delete this entry? (Y/N)");
        if (this.scanner.next().equalsIgnoreCase("Y")) {
            patientController.removeQueue(deleteQueue);
            System.out.println();
            System.out.println("Consultation Queue with No (" + deleteQueue.queueNo() + ") Deleted.\n");
        }
        this.scanner.nextLine();
        System.out.println();
    }

    public ConsultationQueue appointQueue() {
        ConsultationQueue popQueue = patientController.getFirstQueue();
        System.out.println();
        System.out.println("-".repeat(30));
        System.out.println("Queue to be Appointed");
        System.out.println("-".repeat(30));
        System.out.println("Queue No          : " + popQueue.queueNo());
        System.out.println("Patient ID        : " + popQueue.patient().getId());
        System.out.println("Patient Name      : " + popQueue.patient().getName());
        System.out.println("Consultation Type : " + popQueue.type().name());
        System.out.println("-".repeat(30));
        System.out.println();
        return popQueue;
    }

    public void listPatient() {
        var table = initializePatientTable();
        System.out.println("-".repeat(30));
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
        ListInterface<PatientDetail> rows = patientController.getPatientDetail(selectedPatient);

        var table = new InteractiveTable<>(new Column[]{
                new Column("Consultation Id", Alignment.CENTER, 15),
                new Column("Doctor", Alignment.CENTER, 20),
                new Column("Consulted At", Alignment.CENTER, 25),
                new Column("Diagnosis Id", Alignment.CENTER, 15),
                new Column("Diagnosis", Alignment.CENTER, 30),
                new Column("Treatment Id", Alignment.CENTER, 15),
                new Column("Symptom", Alignment.CENTER, 30),
                new Column("Prescription Id", Alignment.CENTER, 15),
                new Column("Medicine", Alignment.CENTER, 30),
        }, rows) {
            private String lastConsultationId = null;
            private String lastDiagnosisId = null;
            private String lastTreatmentId = null;
            private String lastPrescriptionId = null;
            private String lastDoctor = null;
            private String lastConsultedAt = null;
            private String lastDiagnosis = null;
            private String lastSymptom = null;
            private String lastMedicine = null;

            @Override
            protected Cell[] getRow(PatientDetail r) {
                Consultation c = r.getConsultation();
                Diagnosis d = r.getDiagnosis();
                Treatment t = r.getTreatment();
                Prescription p = r.getPrescription();

                String consultationId = c != null ? String.valueOf(c.getId()) : "N/A";
                String diagnosisId = d != null ? String.valueOf(d.getId()) : "N/A";
                String treatmentId = t != null ? String.valueOf(t.getId()) : "N/A";
                String prescriptionId = p != null ? String.valueOf(p.getId()) : "N/A";
                String doctor = c != null && c.getDoctor() != null ? c.getDoctor().getName() : "N/A";
                String consultedAt = c != null && c.getConsultedAt() != null ? DATE_FORMAT.format(c.getConsultedAt()) : "N/A";
                String symptom = t != null && t.getSymptom() != null ? t.getSymptom() : "N/A";
                String diagnosis = d != null && d.getDiagnosis() != null ? d.getDiagnosis() : "N/A";
                String medicine = p != null && p.getProduct() != null && p.getProduct().getName() != null ? p.getProduct().getName() : "N/A";

                if (!consultationId.equals(lastConsultationId)) {
                    lastDoctor = null;
                    lastConsultedAt = null;
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

                if (consultationId.equals(lastConsultationId)) consultationId = " ";
                else lastConsultationId = consultationId;

                if (diagnosisId.equals(lastDiagnosisId)) diagnosisId = " ";
                else lastDiagnosisId = diagnosisId;

                if (treatmentId.equals(lastTreatmentId)) treatmentId = " ";
                else lastTreatmentId = treatmentId;

                if (prescriptionId.equals(lastPrescriptionId)) prescriptionId = " ";
                else lastPrescriptionId = prescriptionId;

                if (doctor.equals(lastDoctor)) doctor = " ";
                else lastDoctor = doctor;

                if (consultedAt.equals(lastConsultedAt)) consultedAt = " ";
                else lastConsultedAt = consultedAt;

                if (symptom.equals(lastSymptom)) symptom = " ";
                else lastSymptom = symptom;

                if (diagnosis.equals(lastDiagnosis)) diagnosis = " ";
                else lastDiagnosis = diagnosis;

                if (medicine.equals(lastMedicine)) medicine = " ";
                else lastMedicine = medicine;

                return new Cell[]{
                        new Cell(consultationId),
                        new Cell(doctor),
                        new Cell(consultedAt),
                        new Cell(diagnosisId),
                        new Cell(diagnosis),
                        new Cell(treatmentId),
                        new Cell(symptom),
                        new Cell(prescriptionId),
                        new Cell(medicine)
                };
            }
        };

        table.addSorter("Consultation ID", (p1, p2) -> Integer.compare(p1.getConsultation().getId(), p2.getConsultation().getId()));
        table.addSorter("Diagnosis ID", (p1, p2) -> Integer.compare(p1.getDiagnosis().getId(), p2.getDiagnosis().getId()));
        table.addSorter("Treatment ID", (p1, p2) -> Integer.compare(p1.getTreatment().getId(), p2.getTreatment().getId()));
        table.addSorter("Prescription ID", (p1, p2) -> Integer.compare(p1.getPrescription().getId(), p2.getPrescription().getId()));

        System.out.println();
        System.out.println("+" + "-".repeat(30) + "+");
        System.out.println("| Patient Detail Summary Table |");
        System.out.println("+" + "-".repeat(30) + "+");

        table.display();
        System.out.println();
    }

    public void addPatient() {
        Patient addPatient = new Patient();

        System.out.println();

        // name
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        if (name.isEmpty()) return;
        while (name.trim().isEmpty()) {
            System.out.println("Name cannot be empty or spaces only!");
            System.out.print("Enter Patient Name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) return;
        }
        addPatient.setName(name.trim());

        System.out.println("-".repeat(33));
        System.out.println("| (M) for Male | (F) for Female |");
        System.out.println("-".repeat(33));

        // gender
        String input;
        do {
            System.out.print("Enter Patient Gender: ");
            input = scanner.nextLine();
            if (input.isEmpty()) return;
            input = input.trim().toUpperCase();
            if (!input.equals("M") && !input.equals("F")) {
                System.out.println("Invalid input! Please enter M or F.");
            }
        } while (!input.equals("M") && !input.equals("F"));
        Gender gender = input.equals("M") ? Gender.MALE : Gender.FEMALE;
        addPatient.setGender(gender);

        // identification
        String ic;
        do {
            System.out.print("Enter Patient Identification (YYMMDD-XX-XXXX): ");
            ic = scanner.nextLine();
            if (ic.isEmpty()) return;
            ic = ic.trim();
            if (patientController.validateUnique(ic, "identification") != null) {
                System.out.println("This identification is in-use! Example: 010203-14-5678");
            }
            if (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
                System.out.println("Invalid IC format! Example: 010203-14-5678");
            }
        } while (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$") || patientController.validateUnique(ic, "identification") != null);
        addPatient.setIdentification(ic);

        // contact
        String phone;
        do {
            System.out.print("Enter Patient Contact Number (+60XXXXXXXXX): ");
            phone = scanner.nextLine();
            if (phone.isEmpty()) return;
            phone = phone.trim();
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
                    System.out.print("Enter New Patient Name: ");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) return;
                    while (name.trim().isEmpty()) {
                        System.out.println("Name cannot be empty or spaces only!");
                        System.out.print("Enter New Patient Name: ");
                        name = scanner.nextLine();
                        if (name.isEmpty()) return;
                    }
                    editPatient.setName(name.trim());
                }
                case "2" -> {
                    System.out.println("| (M) for Male | (F) for Female |");
                    System.out.println("-".repeat(33));

                    String input;
                    do {
                        System.out.print("Enter New Patient Gender: ");
                        input = scanner.nextLine();
                        if (input.isEmpty()) return;
                        input = input.trim().toUpperCase();
                        if (!input.equals("M") && !input.equals("F")) {
                            System.out.println("Invalid input! Please enter M or F.");
                        }
                    } while (!input.equals("M") && !input.equals("F"));
                    Gender gender = input.equals("M") ? Gender.MALE : Gender.FEMALE;
                    editPatient.setGender(gender);
                }
                case "3" -> {
                    String ic;
                    do {
                        System.out.print("Enter Patient Identification (YYMMDD-XX-XXXX): ");
                        ic = scanner.nextLine();
                        if (ic.isEmpty()) return;
                        ic = ic.trim();
                        if (patientController.validateUnique(ic, "identification") != null) {
                            System.out.println("This identification is in-use! Example: 010203-14-5678");
                        }
                        if (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
                            System.out.println("Invalid IC format! Example: 010203-14-5678");
                        }
                    } while (!ic.matches("^\\d{6}-\\d{2}-\\d{4}$") || patientController.validateUnique(ic, "identification") != null);
                    editPatient.setIdentification(ic);
                }
                case "4" -> {
                    String phone;
                    do {
                        System.out.print("Enter Patient Contact Number (+60XXXXXXXXX): ");
                        phone = scanner.nextLine();
                        if (phone.isEmpty()) return;
                        phone = phone.trim();
                        if (!phone.matches("^\\+60\\d{8,11}$")) {
                            System.out.println("Invalid phone number! Example: +60123456789");
                        }
                    } while (!phone.matches("^\\+60\\d{8,11}$"));
                    editPatient.setContactNumber(phone);
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

    public void generateSummaryReport() {
        int width = 200;
        int topN = 10;

        while (true) {
            ListInterface<PatientCounter> counters = patientController.getPatientSummary();
            System.out.println();

            // header
            System.out.println("=".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY", ' ', width));
            System.out.println(StringUtils.pad("PATIENT MANAGEMENT MODULE", ' ', width));
            System.out.println(StringUtils.pad("SUMMARY OF PATIENT REPORT", ' ', width));
            System.out.println("=".repeat(width));
            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY - HIGHLY CONFIDENTIAL DOCUMENT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.printf("Generated at: %s%n", DATE_FORMAT.format(java.time.LocalDateTime.now()));
            System.out.println();

            int maxConsultationLength = "Consultations Attended".length();
            int maxMedicineLength = "Total Medicines Prescribed".length();

            for (var pc : counters) {
                String consultations = StringUtils.join(", ", patientController.getTypeList(pc));
                maxConsultationLength = Math.max(maxConsultationLength, consultations.length());

                String meds = StringUtils.join(", ", patientController.getMedicineList(pc));
                maxMedicineLength = Math.max(maxMedicineLength, meds.length());
            }

            maxConsultationLength += 2;
            maxMedicineLength += 2;

            InteractiveTable<PatientCounter> table = new InteractiveTable<>(new Column[]{
                    new Column("Patient ID", Alignment.CENTER, 15),
                    new Column("Patient Name", Alignment.CENTER, 40),
                    new Column("Consultations Attended", Alignment.CENTER, maxConsultationLength),
                    new Column("Total Medicines Prescribed", Alignment.CENTER, maxMedicineLength)
            }, counters) {
                @Override
                protected Cell[] getRow(PatientCounter pc) {
                    var patient = pc.key();

                    String patientId = String.valueOf(patient.getId());
                    String patientName = patient.getName();

                    String consultationCount = StringUtils.join(", ", patientController.getTypeList(pc));
                    String medicines = StringUtils.join(", ", patientController.getMedicineList(pc));

                    return new Cell[]{
                            new Cell(patientId, Alignment.CENTER),
                            new Cell(patientName),
                            new Cell(consultationCount),
                            new Cell(medicines)
                    };
                }
            };
            table.setPageSize(topN);
            table.display();
            System.out.println();

            ListInterface<Integer> stats = patientController.getTotalStats(topN);
            System.out.printf("Total Number of Patients (Top %d): %d%n", topN, stats.get(0));
            System.out.printf("Total Number of Consultations (Top %d): %d%n", topN, stats.get(1));
            System.out.printf("Total Number of Prescriptions (Top %d): %d%n", topN, stats.get(2));
            System.out.println();

            System.out.println("GRAPHICAL REPRESENTATION OF SUMMARY MODULE");
            System.out.println("------------------------------------------");
            var topConsultations = patientController.getTopPatientsByConsultations(topN);
            var topPrescriptions = patientController.getTopPatientsByPrescriptions(topN);

            printBarChart("Consultations", topConsultations, topN);
            printBarChart("Prescriptions", topPrescriptions, topN);
            System.out.println();

            System.out.println("Global Highlights:");
            System.out.println("Patient(s) with fewest consultations: " + StringUtils.join(", ", patientController.getExtremePatients(false)));
            System.out.println("Patient(s) with most consultations: " + StringUtils.join(", ", patientController.getExtremePatients(true)));
            System.out.println();

            // footer
            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("END OF THE REPORT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.println();

            System.out.printf("(0) Exit Report | Enter new Top filter (current %d): ", topN);
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println();
                break;
            }

            try {
                int newN = Integer.parseInt(input);
                if (newN > 0) {
                    topN = newN;
                } else {
                    System.out.println("TopN must be greater than 0. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, keeping current TopN.");
            }
        }
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

                            selectedPatient = (Patient) patientController.performSelect(selectedId, "patient");
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

    //Patient Sorters and Filters
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
                applyPatientSorter(table, "id", value);
                break;
            }
            case 2: {
                var value = orderByMenu();
                System.out.println();
                applyPatientSorter(table, "name", value);
                break;
            }
            case 3: {
                var value = orderByMenu();
                System.out.println();
                applyPatientSorter(table, "identification", value);
                break;
            }
            case 4: {
                var value = orderByMenu();
                System.out.println();
                applyPatientSorter(table, "contact", value);
                break;
            }
            case 5: {
                var value = orderByMenu();
                System.out.println();
                applyPatientSorter(table, "gender", value);
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
                applyPatientFilter(table, "name", value);
                break;
            }
            case 2: {
                System.out.print("Search identification by: ");
                var value = scanner.nextLine();
                System.out.println();
                applyPatientFilter(table, "identification", value);
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();
                System.out.println();
                applyPatientFilter(table, "contact", value);
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
                    applyPatientFilter(table, "gender", "MALE");
                } else if (value == 2) {
                    applyPatientFilter(table, "gender", "FEMALE");
                }
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void applyPatientFilter(InteractiveTable<Patient> table, String column, String value) {
        switch (column) {
            case "id": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> String.valueOf(p.getId()).contains(value));
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

    public void applyPatientSorter(InteractiveTable<Patient> table, String column, boolean ascending) {
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

    public ConsultationQueue selectConsultationQueue() {
        ConsultationQueue selectedQueue = null;

        var table = initializeQueueTable();
        table.display();

        String opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Consultation Queue " +
                    "\n(2) Filter Consultation Queue " +
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
                            System.out.print("\nEnter Queue No (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                table.display();
                                break;
                            }

                            selectedQueue = (ConsultationQueue) patientController.performSelect(selectedId, "consultation");
                            if (selectedQueue == null) {
                                System.out.println("Consultation Queue with No (" + selectedId + ") not found. Please re-enter Queue No...");
                            } else {
                                System.out.println("Consultation Queue with No (" + selectedId + ") selected!");
                                return selectedQueue;
                            }
                        } while (selectedQueue == null);
                        break;
                    }
                    case "2": {
                        filterQueue(table);
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

        return selectedQueue;
    }

    //Consultation Queue Filters and Sorters
    public void sortQueue(InteractiveTable<ConsultationQueue> table) {
        System.out.println("-".repeat(30));
        System.out.println("Sorters:");
        System.out.println("(1) queue no");
        System.out.println("(2) patient id");
        System.out.println("(3) patient id");
        System.out.println("(4) consultation type");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Sort by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                var value = orderByMenu();
                System.out.println();
                applyQueueSorter(table, "queue", value);
                break;
            }
            case 2: {
                var value = orderByMenu();
                System.out.println();
                applyQueueSorter(table, "id", value);
                break;
            }
            case 3: {
                var value = orderByMenu();
                System.out.println();
                applyQueueSorter(table, "name", value);
                break;
            }
            case 4: {
                var value = orderByMenu();
                System.out.println();
                applyQueueSorter(table, "type", value);
                break;
            }
            case 5: {
                var value = orderByMenu();
                System.out.println();
                applyQueueSorter(table, "gender", value);
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void filterQueue(InteractiveTable<ConsultationQueue> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("(1) patient id");
        System.out.println("(2) patient name");
        System.out.println("(3) consultation type");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                System.out.print("Search patient id by: ");
                var value = scanner.nextLine();
                System.out.println();
                applyQueueFilter(table, "id", value);
                break;
            }
            case 2: {
                System.out.print("Search patient name by: ");
                var value = scanner.nextLine();
                System.out.println();
                applyQueueFilter(table, "name", value);
                break;
            }
            case 3: {
                System.out.println();
                System.out.println("-".repeat(30));
                System.out.println("Filter consultation type by: ");
                System.out.println("(1) general");
                System.out.println("(2) specialist");
                System.out.println("(3) emergency");
                System.out.println("(4) follow-up");
                System.out.println("-".repeat(30));
                System.out.print("Selection : ");
                var value = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (value == 1) {
                    applyQueueFilter(table, "type", "GENERAL");
                } else if (value == 2) {
                    applyQueueFilter(table, "type", "SPECIALIST");
                } else if (value == 3) {
                    applyQueueFilter(table, "type", "EMERGENCY");
                } else if (value == 4) {
                    applyQueueFilter(table, "type", "FOLLOW_UP");
                }
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void applyQueueFilter(InteractiveTable<ConsultationQueue> table, String column, String value) {
        switch (column) {
            case "id": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        c -> String.valueOf(c.patient().getId()).contains(value));
                table.display();
                break;
            }
            case "name": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        c -> c.patient().getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "type": {
                if (value.equals(ConsultationType.GENERAL.name())) {
                    table.addFilter("General only", c -> c.type() == ConsultationType.GENERAL);
                    table.display();
                } else if (value.equals(ConsultationType.SPECIALIST.name())) {
                    table.addFilter("Specialist only", c -> c.type() == ConsultationType.SPECIALIST);
                    table.display();
                } else if (value.equals(ConsultationType.EMERGENCY.name())) {
                    table.addFilter("Emergency only", c -> c.type() == ConsultationType.EMERGENCY);
                    table.display();
                } else if (value.equals(ConsultationType.FOLLOW_UP.name())) {
                    table.addFilter("Follow-up only", c -> c.type() == ConsultationType.FOLLOW_UP);
                    table.display();
                }
                break;
            }
            default:
                break;
        }
    }

    public void applyQueueSorter(InteractiveTable<ConsultationQueue> table, String column, boolean ascending) {
        switch (column) {
            case "queue": {
                table.addSorter(
                        ascending ? "Queue No Ascending" : "Queue No Descending",
                        ascending
                                ? (c1, c2) -> Integer.compare(c1.queueNo(), c2.queueNo())
                                : (c1, c2) -> Integer.compare(c2.queueNo(), c1.queueNo())
                );
                table.display();
                break;
            }
            case "id": {
                table.addSorter(
                        ascending ? "ID Ascending" : "ID Descending",
                        ascending
                                ? (c1, c2) -> Integer.compare(c1.patient().getId(), c2.patient().getId())
                                : (c1, c2) -> Integer.compare(c2.patient().getId(), c1.patient().getId())
                );
                table.display();
                break;
            }
            case "name": {
                table.addSorter(
                        ascending ? "Name Ascending" : "Name Descending",
                        ascending
                                ? (c1, c2) -> c1.patient().getName().compareToIgnoreCase(c2.patient().getName())
                                : (c1, c2) -> c2.patient().getName().compareToIgnoreCase(c1.patient().getName())
                );
                table.display();
                break;
            }
            case "type": {
                table.addSorter(
                        ascending ? "Type Ascending" : "Type Descending",
                        ascending
                                ? (c1, c2) -> c1.type().compareTo(c2.type())
                                : (c1, c2) -> c2.type().compareTo(c1.type())
                );
                table.display();
                break;
            }
            default:
                break;
        }
    }

    public String queueMenu() {
        String opt;
        do {
            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("(1) List Consultation Queue");
            System.out.println("(2) Generate Consultation Queue");
            System.out.println("(3) Remove Consultation Queue");
            System.out.println("(0) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = scanner.nextLine().trim();

            if (!opt.matches("[0-3]")) {
                System.out.println("Invalid input! Try again.");
            }
        } while (!opt.matches("[0-3]"));
        System.out.println();
        return opt;
    }

    public int maintenanceMenu() {
        String input;
        do {
            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("(1) Create Patient");
            System.out.println("(2) Modify Patient");
            System.out.println("(3) Delete Patient");
            System.out.println("(0) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            input = scanner.nextLine().trim();

            if (!input.matches("[0-3]")) {
                System.out.println("Invalid input! Try again.");
            }
        } while (!input.matches("[0-3]"));

        return Integer.parseInt(input);
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

    public InteractiveTable<ConsultationQueue> initializeQueueTable() {
        return new InteractiveTable<>(new Column[]{
                new Column("Queue No", Alignment.CENTER, 10),
                new Column("Patient Id", Alignment.CENTER, 10),
                new Column("Patient Name", Alignment.CENTER, 30),
                new Column("Consultation Type", Alignment.CENTER, 20)
        }, Database.queueList.clone()) {
            @Override
            protected Cell[] getRow(ConsultationQueue o) {
                return new Cell[]{
                        new Cell(o.queueNo()),
                        new Cell(o.patient() != null ? o.patient().getId() : "N/A"),
                        new Cell(o.patient() != null ? o.patient().getName() : "N/A"),
                        new Cell(o.type() != null ? o.type().toString() : "N/A")
                };
            }
        };
    }

    private void printBarChart(String title, ListInterface<PatientCounter> list, int topN) {

        int maxValue = 0;
        for (var pc : list) {
            int val = 0;
            if (title.equalsIgnoreCase("Consultations")) {
                val = pc.getCount();
            } else if (title.equalsIgnoreCase("Prescriptions")) {
                for (var prod : pc.productCounters()) val += prod.count();
            }
            if (val > maxValue) maxValue = val;
        }

        int barWidth = 70; // maximum bar length

        String headerText = "Top " + topN + " Patients by " + title;
        int borderLength = headerText.length() + 2;

        System.out.println("+" + "-".repeat(borderLength) + "+");
        System.out.println("| " + headerText + " |");
        System.out.println("+" + "-".repeat(borderLength) + "+");

        int shown = 0;
        for (var pc : list) {
            if (shown++ >= topN) break;

            int value = 0;
            if (title.equalsIgnoreCase("Consultations")) {
                value = pc.getCount();
            } else if (title.equalsIgnoreCase("Prescriptions")) {
                for (var prod : pc.productCounters()) value += prod.count();
            }

            int scaled = maxValue == 0 ? 0 : (int) Math.round((value / (double) maxValue) * barWidth);

            final String GREEN = "\u001B[34m";
            final String RESET = "\u001B[0m";

            System.out.printf("%-20s | %s%s%s (%d)%n",
                    pc.key().getName(),
                    GREEN,
                    "█".repeat(Math.max(1, scaled)),
                    RESET,
                    value
            );
        }
        System.out.println();
    }
}