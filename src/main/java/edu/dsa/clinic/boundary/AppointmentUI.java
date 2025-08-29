package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.dto.AppointmentTypeCounter;
import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.dto.DoctorCounter;
import edu.dsa.clinic.dto.PatientCounter;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.StringUtils;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Scanner;

public class AppointmentUI extends UI {
    private final AppointmentController appointmentController = new AppointmentController();
    private final PatientUI patientUI = new PatientUI(this.scanner);
    private final DoctorUI doctorUI = new DoctorUI(this.scanner);
    private MedicalUI medicalUI;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentUI(Scanner scanner) {
        super(scanner);
    }

    public AppointmentUI setMedicalUI(MedicalUI medicalUI) {
        this.medicalUI = medicalUI;
        return this;
    }

    @Override
    public void startMenu(){
        String option;
        do{
            System.out.println("Select an Option");
            System.out.println("1. Manage Appointment");
            if (this.medicalUI != null) System.out.println("2. Manage Consultation");
            System.out.println("0. Exit");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch(option) {
                case "1":
                    appointmentMenu();
                    break;
                case "2":
                    if (this.medicalUI != null) medicalUI.startMenu();
                    break;
                case "0":
                    System.out.println("Returning...");
                    return;
            }
        }while (true);
    }

    public void appointmentMenu() {
        String choice;
        do {
            System.out.println("\nAppointment MENU");
            System.out.println("1. Create Future Appointment");
            System.out.println("2. Create Walk-In Appointment");
            System.out.println("3. View All Appointments");
            System.out.println("4. Edit An Appointment");
            System.out.println("5. Cancel An Appointment");
            System.out.println("6. Generate Summary Report");
            System.out.println("0. Quit");
            System.out.print("Enter choice: ");

            choice = this.scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1" -> createFutureAppointmentMenu();
                case "2" -> createWalkInAppointmentMenu();
                case "3" -> viewAppointmentMenu();
                case "4"  -> editAppointmentMenu();
                case "5"  -> cancelAppointmentMenu();
                case "6" -> generateAppointmentSummaryReport();
                case "0"  -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid Choice");
            }

        } while (!choice.equals("0"));
    }

    public void createFutureAppointmentMenu() {
        String option;
        Appointment appointment = new Appointment();
        Patient selectedPatient = null;
        Doctor selectedDoctor = null;
        ConsultationType appointmentType = null;
        LocalDateTime appointmentStartTime = null, appointmentEndTime = null;

        do {
            System.out.println("Creating Future Appointment: ");

            if(selectedPatient == null){
                System.out.println("1. Select A Patient");
            }else System.out.println("1. Select A Patient (Selected: " + selectedPatient.getName() + ")");

            if(selectedDoctor == null){
                System.out.println("2. Select A Doctor");
            }else System.out.println("2. Select A Doctor (Selected: " + selectedDoctor.getName() + ")");

            if(appointmentStartTime == null){
                System.out.println("3. Enter Appointment Time");
            }else System.out.println("3. Enter Appointment Time (Selected: \"" + appointmentStartTime.format(DATE_FORMAT) + " to " + appointmentEndTime.format(DATE_FORMAT) + "\")");

            if(appointmentType == null){
                System.out.println("4. Enter Appointment Type");
            }else System.out.println("4. Enter Appointment Type (Selected: " + appointmentType + ")");

            System.out.println("5. View Doctor Available Schedule");
            System.out.println("6. Confirm Appointment");
            System.out.println("0. Return");
            System.out.print("Option: ");
            option = this.scanner.nextLine();

            switch (option) {
                case "1" -> selectedPatient = patientUI.selectPatient();
                case "2" -> selectedDoctor = doctorUI.selectDoctor();
                case "3" -> {
                    if(selectedDoctor == null){
                        System.out.println("Please Select A Doctor First");
                    }else{
                        appointmentStartTime = inputAppointmentStartTime();
                        if(appointmentStartTime == null){
                            System.out.println("Returning...");
                            break;
                        }

                        appointmentEndTime = generateAppointmentEndTime(appointmentStartTime);

                        if (appointmentController.isAvailable(
                                selectedDoctor,
                                appointmentStartTime.toLocalDate(),
                                new Range<>(appointmentStartTime.toLocalTime(), appointmentEndTime.toLocalTime())
                        )) {
                            System.out.println("Doctor Is Available");

                        } else {
                            System.out.println("Doctor Is Not Available");
                            appointmentStartTime = null;
                        }
                    }
                }
                case "4" -> appointmentType =  selectAppointmentType();
                case "5" -> {
                    if (selectedDoctor != null) {
                        doctorUI.viewDoctorAvailabilitySchedule(LocalDate.now(), selectedDoctor);
                    }else{
                        System.out.println("Please Select A Doctor Before checking his/hers Schedule");
                    }
                }
                case "6" -> {
                    if (selectedPatient == null) {
                        System.out.println("Invalid: No patient selected.");
                        break;
                    }

                    if (selectedDoctor == null) {
                        System.out.println("Invalid: No doctor selected.");
                        break;
                    }

                    if (appointmentStartTime == null) {
                        System.out.println("Invalid: No appointment time selected.");
                        break;
                    }

                    if(appointmentType == null){
                        System.out.println("Invalid: No appointment type selected.");
                        break;
                    }

                    if (updateAppointmentConfirmation("add")) {
                        appointment.setPatient(selectedPatient);
                        appointment.setDoctor(selectedDoctor);
                        appointment.setExpectedStartAt(appointmentStartTime);
                        appointment.setExpectedEndAt(appointmentEndTime);
                        appointment.setAppointmentType(appointmentType);
                        appointment.setCreatedAt(LocalDateTime.now());
                        this.appointmentController.saveAppointment(appointment);
                        System.out.println("Operation Successful");
                        return;
                    } else {
                        System.out.println("Operation cancelled");
                        return;
                    }

                }
                case "0" -> System.out.println("Returning...");
                default -> System.out.println("Invalid Choice");
            }

        }while(!option.equals("0"));
    }

    public void createWalkInAppointmentMenu() {
        Appointment appointment = new Appointment();

        ConsultationQueue queue = patientUI.appointQueue();
        if(queue == null){
            System.out.println("There is no one on the queue");
            return;
        }

        String option;
        Patient appointedPatient = queue.patient();
        Doctor selectedDoctor = null;
        ConsultationType appointmentType = null;
        LocalDateTime appointmentStartTime = null, appointmentEndTime = null;

        do {
            System.out.println("Creating Walk-In Appointment: ");
            System.out.println("Patient will be auto appointed by the system");

            if(selectedDoctor == null){
                System.out.println("1. Select A Doctor");
            }else System.out.println("1. Select A Doctor (Selected: "+ selectedDoctor.getName() +")");

            if(appointmentType == null){
                System.out.println("2. Select Appointment Type");
            }else System.out.println("2. Select Appointment Type (Selected: "+ appointmentType +")");

            System.out.println("3. Confirm Appointment");
            System.out.print("Option: ");
            option = this.scanner.nextLine();

            switch (option) {
                case "1" -> {
                    selectedDoctor = doctorUI.selectDoctor();

                    if(selectedDoctor == null){
                        break;
                    }

                    appointmentStartTime = LocalDateTime.now();
                    appointmentEndTime = generateAppointmentEndTime(appointmentStartTime);

                    if (appointmentController.isAvailable(
                            selectedDoctor,
                            appointmentStartTime.toLocalDate(),
                            new Range<>(appointmentStartTime.toLocalTime(), appointmentEndTime.toLocalTime())
                    )) {
                        System.out.println("Doctor Is Available");

                    } else {
                        System.out.println("Doctor Is Not Available");
                        selectedDoctor = null;
                    }
                }
                case "2" -> appointmentType = selectAppointmentType();
                case "3" -> {
                    if (selectedDoctor == null) {
                        System.out.println("Invalid: No doctor selected.");
                        break;
                    }

                    if (appointmentType == null) {
                        System.out.println("Invalid: No Appointment Type selected.");
                        break;
                    }

                    appointment.setPatient(appointedPatient);
                    appointment.setDoctor(selectedDoctor);
                    appointment.setExpectedStartAt(appointmentStartTime);
                    appointment.setExpectedEndAt(appointmentEndTime);
                    appointment.setAppointmentType(appointmentType);
                    appointment.setCreatedAt(LocalDateTime.now());
                    this.appointmentController.saveAppointment(appointment);
                    System.out.println("Operation Successful");

                }
                default -> System.out.println("Invalid Choice");
            }

        }while(!option.equals("3"));

    }

    public void viewAppointmentMenu() {
        var table = initializeAppointmentTable();

        String option;

        do{
            table.display();
            System.out.println("View Appointment Menu");
            System.out.println("1. Filter Appointments");
            System.out.println("2. Sort Appointments");
            System.out.println("0. Exit");
            System.out.println("Enter \"N\" Or \"P\" to change pages");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option.toUpperCase()) {
                case "1" -> filterAppointment(table, DATE_FORMAT);
                case "2" -> sortAppointment(table);
                case "N", "P" -> pageControls(table, option);
                case "0" -> System.out.println("Exiting...");
                default -> System.out.println("Please Enter a valid Option");
            }

        }while(!option.equals("0"));
    }

    public void editAppointmentMenu(){

        Appointment selectedAppointment = null;
        String option;

        do{
            System.out.println("\nEdit Appointment Menu:");

            if(selectedAppointment == null){
                System.out.println("1. Select An Appointment To Edit");
            }else System.out.println("1. Select An Appointment To Edit (Selected ID: " + selectedAppointment.getId() + ")");

            System.out.println("2. Edit Selected Appointment");
            System.out.println("0. Return");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option) {
                case "1" -> selectedAppointment = selectAppointment();
                case "2" -> {
                    if(selectedAppointment != null){
                        editAppointmentDetails(selectedAppointment);
                        return;
                    }else{
                        System.out.println("Invalid: Please select an appointment first");
                    }
                }
                case "0" ->System.out.println("Returning...");
                default -> System.out.println("Please enter a valid option");
            }

        }while (!option.equals("0"));
    }

    public void cancelAppointmentMenu(){
        Appointment selectedAppointment = null;
        String option;

        do{
            System.out.println("\nCancel Appointment Menu:");

            if(selectedAppointment == null){
                System.out.println("1. Select An Appointment To Cancel");
            }else System.out.println("1. Select An Appointment To Cancel (Selected ID: "+ selectedAppointment.getId() +")");

            System.out.println("2. Cancel Selected Appointment");
            System.out.println("0. Return");
            System.out.println("Enter \"N\" OR \"P\" to change pages");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option) {
                case "1" -> selectedAppointment = selectAppointment();
                case "2" -> {
                    if(selectedAppointment != null){
                        if(updateAppointmentConfirmation("cancel")){
                            appointmentController.cancelAppointment(selectedAppointment);
                            System.out.println("Operation Successful");

                        }else System.out.println("Operation Cancelled");

                        return;
                    }else{
                        System.out.println("Invalid: Please select an appointment first");
                    }
                }
                case "0" ->System.out.println("Returning...");
                default -> System.out.println("Please enter a valid option");
            }

        }while (!option.equals("0"));
    }

    public void generateAppointmentSummaryReport() {
        // Get counters
        ListInterface<AppointmentTypeCounter> summaries = appointmentController.getAppointmentSummary();

        int width = 200;

        // Header
        System.out.println("=".repeat(width));
        System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY", ' ', width));
        System.out.println(StringUtils.pad("APPOINTMENT MANAGEMENT MODULE", ' ', width));
        System.out.println(StringUtils.pad("SUMMARY OF APPOINTMENT REPORT", ' ', width));
        System.out.println("=".repeat(width));
        System.out.printf("Generated at: %s%n", DATE_FORMAT.format(LocalDateTime.now()));
        System.out.println();

        // Build table
        InteractiveTable<AppointmentTypeCounter> table = new InteractiveTable<>(new Column[]{
                new Column("Appointment Type", Alignment.CENTER, 20),
                new Column("Doctors", Alignment.CENTER, 70),
                new Column("Patients", Alignment.CENTER, 70)
        }, summaries) {
            @Override
            protected Cell[] getRow(AppointmentTypeCounter atc) {
                // --- Doctors ---
                StringBuilder doctorsBuilder = new StringBuilder();
                for (int i = 0; i < atc.getDoctorCounters().size(); i++) {
                    DoctorCounter dc = atc.getDoctorCounters().get(i);
                    doctorsBuilder.append(dc.key().getName())
                            .append("(").append(dc.getCount()).append(")");
                    if (i < atc.getDoctorCounters().size() - 1) {
                        doctorsBuilder.append(", ");
                    }
                }
                String doctors = doctorsBuilder.toString();

                // --- Patients ---
                StringBuilder patientsBuilder = new StringBuilder();
                for (int i = 0; i < atc.getPatientCounters().size(); i++) {
                    PatientCounter pc = atc.getPatientCounters().get(i);
                    patientsBuilder.append(pc.key().getName())
                            .append("(").append(pc.getCount()).append(")");
                    if (i < atc.getPatientCounters().size() - 1) {
                        patientsBuilder.append(", ");
                    }
                }
                String patients = patientsBuilder.toString();

                // Return row
                return new Cell[]{
                        new Cell(atc.getType().name(), Alignment.CENTER),
                        new Cell(StringUtils.trimEarly(doctors, 70, "..."), Alignment.LEFT),
                        new Cell(StringUtils.trimEarly(patients, 70, "..."), Alignment.LEFT)
                };
            }
        };

        table.setPageSize(summaries.size());
        table.display();

        System.out.println();

        // Appointment totals by type (Bar Chart)
        printAppointmentTotalsBarChart(summaries);

        // Stats
        int totalAppointments = appointmentController.getAppointments().size();
        System.out.printf("Total Appointments: %d%n", totalAppointments);

        // Footer
        System.out.println("*".repeat(width));
        System.out.println(StringUtils.pad("END OF APPOINTMENT REPORT", ' ', width));
        System.out.println("*".repeat(width));
        System.out.println();
    }

    public void editAppointmentDetails(Appointment appointment){
        String option;
        Doctor newDoctor = null;
        LocalDateTime newAppointmentStartTime = null, newAppointmentEndTime = null;

        //Temporary remove the selected appointment
        appointmentController.cancelAppointment(appointment);

        do{
            System.out.println("Select An Option:");

            if(newDoctor == null){
                System.out.println("1. Edit Doctor");
            }else{
                System.out.println("1. Edit Doctor (New: " + newDoctor.getName() + ")");
            }

            if(newAppointmentStartTime == null && newAppointmentEndTime == null){
                System.out.println("2. Edit Appointment Time");
            }else{
                System.out.println("2. Edit Appointment Time (New: \"" + newAppointmentStartTime.format(DATE_FORMAT) + " to " + newAppointmentEndTime.format(DATE_FORMAT) + "\")");
            }

            System.out.println("3. View Doctor Available Schedule");
            System.out.println("4. Confirm Changes");
            System.out.println("0. Return");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option){
                case "1" -> newDoctor = doctorUI.selectDoctor();
                case "2" -> {
                    if(newDoctor != null) {
                        newAppointmentStartTime = inputAppointmentStartTime();
                        if (newAppointmentStartTime == null) {
                            System.out.println("Returning...");
                            break;
                        }
                        newAppointmentEndTime = generateAppointmentEndTime(newAppointmentStartTime);

                        if (appointmentController.isAvailable(
                                newDoctor,
                                newAppointmentStartTime.toLocalDate(),
                                new Range<>(newAppointmentStartTime.toLocalTime(), newAppointmentEndTime.toLocalTime())
                        )) {
                            System.out.println("Doctor Is Available");

                        } else {
                            System.out.println("Doctor Is Not Available");
                            newAppointmentStartTime = null;
                            newAppointmentEndTime = null;
                        }
                    }
                }
                case "3" -> {
                    if(newDoctor != null){
                        doctorUI.viewDoctorAvailabilitySchedule(LocalDate.now(), newDoctor);
                    }else{
                        System.out.println("Please Select A Doctor First");
                    }


                }
                case "4" -> {
                    if(updateAppointmentConfirmation("edit")){
                        if (newAppointmentStartTime != null && newAppointmentEndTime != null) {
                            appointment.setExpectedStartAt(newAppointmentStartTime);
                            appointment.setExpectedEndAt(newAppointmentEndTime);
                        }

                        if (newDoctor != null) {
                            appointment.setDoctor(newDoctor);
                        }

                        System.out.println("Operation Successful");

                    }else System.out.println("Operation Cancelled");

                    //save all changes
                    appointmentController.saveAppointment(appointment);
                }
                case "0" -> {
                    System.out.println("Returning...");

                    //re-adding back the selected appointment
                    appointmentController.saveAppointment(appointment);
                }
            }
        }while(!option.equals("0") && !option.equals("4"));
    }

    public LocalDateTime inputAppointmentStartTime() {
        String inputTime;
        LocalDateTime startTime = null;
        do {
            System.out.print("Enter Appointment Start Time (yyyy-MM-dd HH:mm) (type 0 to return): ");
            inputTime = this.scanner.nextLine();

            if(inputTime.equals("0")){
                break;
            }

            startTime = validateInputDateTime(inputTime);

        } while (startTime == null);

        return startTime;
    }

    public LocalDateTime generateAppointmentEndTime(LocalDateTime startTime) {
        return startTime.plusMinutes(30);
    }

    public LocalDateTime inputSearchAppointmentTime(String when, String position) {
        String inputTime;
        LocalDateTime time;
        do {
            System.out.print("Enter Appointment " + when + " Time " + position + " (yyyy-MM-dd HH:mm): ");
            inputTime = this.scanner.nextLine();

            time = validateInputSearchDateTime(inputTime);

        } while (time == null);

        return time;
    }

    public LocalDateTime validateInputSearchDateTime(String inputTime) {
        LocalDateTime timeConvert;
        try {
            timeConvert = LocalDateTime.parse(inputTime, DATE_FORMAT);
            return timeConvert;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm");
            return null;
        }
    }

    public LocalDateTime validateInputDateTime(String inputTime) {
        LocalDateTime timeConvert;
        try {
            timeConvert = LocalDateTime.parse(inputTime, DATE_FORMAT);
//            if (!timeConvert.isAfter(LocalDateTime.now())) {
//                System.out.println("Time should be after now");
//                timeConvert = null;
//            }
            return timeConvert;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm");
            return null;
        }
    }

    public InteractiveTable<Appointment> initializeAppointmentTable (){
        return new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 5),
                new Column("Patient Name", Alignment.CENTER, 25),
                new Column("Doctor", Alignment.CENTER, 25),
                new Column("Start At", Alignment.CENTER, 25),
                new Column("End At", Alignment.CENTER, 25),
                new Column("Created At", Alignment.CENTER, 25),
                new Column("Appointment Type", Alignment.CENTER, 25),
        }, appointmentController.getAppointments()) {
            @Override
            protected Cell[] getRow(Appointment o) {
                return new Cell[]{
                        new Cell(o.getId(), Alignment.LEFT),
                        new Cell(o.getPatient().getName()),
                        new Cell(o.getDoctor().getName()),
                        new Cell(o.getExpectedStartAt().format(DATE_FORMAT)),
                        new Cell(o.getExpectedEndAt().format(DATE_FORMAT)),
                        new Cell(o.getCreatedAt().format(DATE_FORMAT)),
                        new Cell(o.getAppointmentType().name(), Alignment.CENTER)
                };
            }
        };
    }

    public Appointment selectAppointment() {
        Appointment selectedAppointment = null;

        var table = initializeAppointmentTable();

        String option;

        do{
            table.display();
            System.out.println("Choose an Option :");
            System.out.println("1. Select An Appointment");
            System.out.println("2. Filter Appointments");
            System.out.println("3. Sort Appointments");
            System.out.println("0. Exit");
            System.out.println("Enter \"N\" Or \"P\" to change pages");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option.toUpperCase()) {
                case "1" -> {
                    do {
                        System.out.print("\nEnter Appointment ID (0 to return): ");
                        String selectedId = scanner.nextLine();

                        if (!selectedId.matches("\\d+")) {
                            System.out.println("Invalid input. Please enter An Appointment ID.");
                            continue;
                        }

                        if (Integer.parseInt(selectedId) == 0) {
                            System.out.println("-".repeat(30));
                            System.out.println();
                            break;
                        }

                        selectedAppointment = (Appointment) appointmentController.performSelect(Integer.parseInt(selectedId), "appointment");
                        if (selectedAppointment == null) {
                            System.out.println("Appointment with ID (" + selectedId + ") not found. Please re-enter Appointment ID...");
                        } else {
                            System.out.println("Appointment with ID (" + selectedAppointment.getId() + ") selected");
                            return selectedAppointment;
                        }
                    } while (selectedAppointment == null);
                }
                case "2" -> filterAppointment(table, DATE_FORMAT);
                case "3" -> sortAppointment(table);
                case "N", "P" -> pageControls(table, option);
                case "0" -> System.out.println("Exiting...");
            }

        }while(!option.equals("0") && !option.equals("1"));

        return selectedAppointment;
    }

    public ConsultationType selectAppointmentType(){
        String option;
        ConsultationType type = null;
        do {
            System.out.println("Select Appointment Type : ");
            System.out.println("1. General");
            System.out.println("2. Specialist");
            System.out.println("3. Emergency");
            System.out.println("4. Follow up");
            System.out.print("\nSelection: ");
            option = this.scanner.nextLine();

            type = switch (option) {
                case "1" -> ConsultationType.GENERAL;
                case "2" -> ConsultationType.SPECIALIST;
                case "3" -> ConsultationType.EMERGENCY;
                case "4" -> ConsultationType.FOLLOW_UP;
                default -> null;
            };

            if (type == null) {
                System.out.println("Invalid option. Try again.");
                continue;
            }

        } while (!option.matches("[1-4]"));
        return type;
    }

    public void pageControls(InteractiveTable<Appointment> table, String opt) {
        if(opt.equalsIgnoreCase("N")) {
            table.nextPage();
        }else{
            table.previousPage();
        }
    }

    public Boolean updateAppointmentConfirmation(String state){
        String choice;

        do{
            System.out.println("Confirm to " + state + " this appointment?");
            System.out.println("\"Y\" to confirm");
            System.out.println("\"N\" to cancel");
            System.out.print("Choice : ");
            choice = this.scanner.nextLine();

            if((!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"))){
                System.out.println("Invalid choice. Please try again.");
            }

        }while((!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")));

        return choice.equalsIgnoreCase("Y");
    }

    public String getSortByOption(){
        String option;
        do{
            System.out.println("Sorting by:");
            System.out.println("1. Sort By Id");
            System.out.println("2. Sort By Patient Name");
            System.out.println("3. Sort By Doctor Name");
            System.out.println("4. Sort By Start Time");
            System.out.println("5. Sort By End Time");
            System.out.println("6. Sort By Created Time");
            System.out.println("7. Sort By Appointment Type");
            System.out.println("8. Reset sorters");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            option = this.scanner.nextLine();

            if (!option.matches("[0-8]")) {
                System.out.println("Invalid choice. Please enter 0–6.");
            }
        } while (!option.matches("[0-8]"));

        return option;
    }

    public boolean getSortOrderOption() {
        String option;
        do {
            System.out.println("Select a sort order :");
            System.out.println("1. Ascending order");
            System.out.println("2. Descending order");
            System.out.print("Select an Option: ");
            option = this.scanner.nextLine();

            if (!option.matches("[0-2]")) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (!option.matches("[0-2]"));

        return option.equalsIgnoreCase("1");
    }

    public String getFilterByOption() {
        String option;
        do {
            System.out.println("Search by:");
            System.out.println("1. Patient name");
            System.out.println("2. Doctor name");
            System.out.println("3. Start Time");
            System.out.println("4. End Time");
            System.out.println("5. Created Time");
            System.out.println("6. Appointment Type");
            System.out.println("7. Reset Filters");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            option = this.scanner.nextLine();

            if (!option.matches("[0-7]")) {
                System.out.println("Invalid choice. Please enter 0–6.");
            }
        } while (!option.matches("[0-7]"));

        return option;
    }

    public void sortAppointment(InteractiveTable<Appointment> table) {
        String sortOption;
        do{
            table.display();
            sortOption = getSortByOption();
            switch (sortOption){
                case "1" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "id", orderBy);
                }
                case "2" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "patient name", orderBy);
                }
                case "3" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "doctor name", orderBy);
                }
                case "4" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "start time", orderBy);
                }
                case "5" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "end time", orderBy);
                }
                case "6" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "created time", orderBy);
                }
                case "7" -> {
                    var orderBy = getSortOrderOption();
                    applyAppointmentSorter(table, "appointment type", orderBy);
                }
                case "8" -> table.resetSorters();
                case "0" -> System.out.println("Returning...");
            }
        }while(!sortOption.equals("0"));
    }

    public void filterAppointment(InteractiveTable<Appointment> table, DateTimeFormatter formatter){
        String filterOption;
        do {
            table.display();
            filterOption = getFilterByOption();
            switch (filterOption) {
                case "1" -> {
                    System.out.println("Search Patient name: ");
                    String name = this.scanner.nextLine();

                    applyAppointmentFilter(table, "patient name", name);
                }
                case "2" -> {
                    System.out.println("Search Doctor name: ");
                    String name = this.scanner.nextLine();

                    applyAppointmentFilter(table, "doctor name", name);
                }
                case "3" -> {
                    System.out.println("Search Start Time: ");
                    LocalDateTime startTime1 = inputSearchAppointmentTime("Start", "(first)");
                    LocalDateTime startTime2 = inputSearchAppointmentTime("Start", "(second)");

                    applyAppointmentFilter(table, "start time", null, startTime1, startTime2, formatter);
                }
                case "4" -> {
                    System.out.println("Search End Time: ");
                    LocalDateTime endTime1 = inputSearchAppointmentTime("End", "(first)");
                    LocalDateTime endTime2 = inputSearchAppointmentTime("End", "(second)");

                    applyAppointmentFilter(table, "end time", null, endTime1, endTime2, formatter);
                }
                case "5" -> {
                    System.out.println("Search Created Time: ");
                    LocalDateTime createdTime1 = inputSearchAppointmentTime("Created", "(first)");
                    LocalDateTime createdTime2 = inputSearchAppointmentTime("Created", "(second)");

                    applyAppointmentFilter(table, "created time", null, createdTime1, createdTime2, formatter);
                }
                case "6" -> applyAppointmentFilter(table, "appointment type", null);
                case "7" -> table.resetFilters();
                case "0" ->System.out.println("Returning...");
                default -> System.out.println("Please enter a valid option");

            }
        }while(!filterOption.equals("0"));
    }

    public String filterAppointmentTypeMenu() {
        String option;
        do {
            System.out.println();
            System.out.println("-".repeat(30));
            System.out.println("Filter Type by: ");
            System.out.println("(1) general");
            System.out.println("(2) specialist");
            System.out.println("(3) emergency");
            System.out.println("(4) follow-up");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            option = scanner.nextLine();

            if (!option.matches("[0-4]")) {
                System.out.println("Invalid choice. Please enter 0–4.");
            }

        }while(!option.matches("[0-4]"));

        return option;
    }

    public void applyAppointmentFilter(InteractiveTable<Appointment> table, String column, String value) {
        applyAppointmentFilter(table, column, value, null, null, null);
    }

    public void applyAppointmentFilter(InteractiveTable<Appointment> table, String column, String value, LocalDateTime value1, LocalDateTime value2, DateTimeFormatter formatter){
        switch (column) {
            case "patient name": {
                table.addFilter(column + " contains " + "\"" + value.trim() + "\"",
                        a -> a.getPatient().getName().toLowerCase().contains(value.toLowerCase().trim()));
                break;
            }
            case "doctor name": {
                table.addFilter(column + " contains " + "\"" + value.trim() + "\"",
                        a -> a.getDoctor().getName().toLowerCase().contains(value.toLowerCase().trim()));
                break;
            }
            case "start time": {
                table.addFilter(column + " is between " + value1.format(formatter) + "\" and \"" + value2.format(formatter) + "\"",
                        a -> !a.getExpectedStartAt().isBefore(value1) &&
                                !a.getExpectedStartAt().isAfter(value2)
                );
                break;
            }
            case "end time": {
                table.addFilter(column + " is between " + value1.format(formatter) + "\" and \"" + value2.format(formatter) + "\"",
                        a -> !a.getExpectedEndAt().isBefore(value1) &&
                                !a.getExpectedEndAt().isAfter(value2)
                );
                break;
            }
            case "created time": {
                table.addFilter(column + " is between " + value1.format(formatter) + "\" and \"" + value2.format(formatter) + "\"",
                        a -> !a.getCreatedAt().isBefore(value1) &&
                                !a.getCreatedAt().isAfter(value2)
                );
                break;
            }
            case "appointment type": {
                String opt = filterAppointmentTypeMenu();
                switch (opt) {
                    case "1" ->
                            table.addFilter("General only", c -> c.getAppointmentType() == ConsultationType.GENERAL);
                    case "2" ->
                            table.addFilter("Specialist only", c -> c.getAppointmentType() == ConsultationType.SPECIALIST);
                    case "3" ->
                            table.addFilter("Emergency only", c -> c.getAppointmentType() == ConsultationType.EMERGENCY);
                    case "4" ->
                            table.addFilter("Follow-up only", c -> c.getAppointmentType() == ConsultationType.FOLLOW_UP);
                }
                break;
            }
            default:
                break;
        }
    }

    public void applyAppointmentSorter(InteractiveTable<Appointment> table, String column, boolean ascending) {
        switch (column) {
            case "id": {
                table.addSorter(
                        ascending ? "by Id (Asc)" : "by Id (Desc)",
                        ascending
                                ? (p1, p2) -> Integer.compare(p1.getId(), p2.getId())
                                : (p1, p2) -> Integer.compare(p2.getId(), p1.getId())
                );
                break;
            }
            case "patient name": {
                table.addSorter(
                        ascending ? "by Patient name (Asc)" : "by Patient name (Desc)",
                        ascending
                                ? (p1, p2) -> p1.getPatient().getName().compareToIgnoreCase(p2.getPatient().getName())
                                : (p1, p2) -> p1.getPatient().getName().compareToIgnoreCase(p2.getPatient().getName())
                );
                break;
            }
            case "doctor name": {
                table.addSorter(
                        ascending ? "by Doctor name (Asc)" : "by Doctor name (Desc)",
                        ascending
                                ? (p1, p2) -> p1.getDoctor().getName().compareToIgnoreCase(p2.getDoctor().getName())
                                : (p1, p2) -> p1.getDoctor().getName().compareToIgnoreCase(p2.getDoctor().getName())
                );
                break;
            }
            case "start time": {
                table.addSorter(
                        ascending ? "by Start Time (Asc)" : "by Start Time (Desc)",
                        ascending
                                ? Comparator.comparing(Appointment::getExpectedStartAt)
                                : Comparator.comparing(Appointment::getExpectedStartAt).reversed()
                );
                break;
            }
            case "end time": {
                table.addSorter(
                        ascending ? "by End Time (Asc)" : "by End Time (Desc)",
                        ascending
                                ? Comparator.comparing(Appointment::getExpectedEndAt)
                                : Comparator.comparing(Appointment::getExpectedEndAt).reversed()
                );
                break;
            }
            case "created time": {
                table.addSorter(
                        ascending ? "by Created Time (Asc)" : "by Created Time (Desc)",
                        ascending
                                ? Comparator.comparing(Appointment::getCreatedAt)
                                : Comparator.comparing(Appointment::getCreatedAt).reversed()
                );
                break;
            }
            case "appointment type": {
                table.addSorter(
                        ascending ? "by Appointment Type (Asc)" : "by Appointment Type (Desc)",
                        ascending
                                ? (c1, c2) -> String.valueOf(c1.getAppointmentType()).compareTo(String.valueOf(c2.getAppointmentType()))
                                : (c1, c2) -> String.valueOf(c2.getAppointmentType()).compareTo(String.valueOf(c1.getAppointmentType()))
                );
                break;
            }
            default:
                break;
        }
    }

    private void printAppointmentTotalsBarChart(ListInterface<AppointmentTypeCounter> summaries) {
        System.out.println("============================================================");
        System.out.println("   Total Appointments by Type (Bar Chart)   ");
        System.out.println("============================================================");

        int max = 0;
        for (int i = 0; i < summaries.size(); i++) {
            AppointmentTypeCounter atc = summaries.get(i);
            if (atc.getAppointmentCount() > max) {
                max = atc.getAppointmentCount();
            }
        }

        // print rows
        for (int i = 0; i < summaries.size(); i++) {
            AppointmentTypeCounter atc = summaries.get(i);
            int count = atc.getAppointmentCount();

            // simple scaling (avoid divide by zero)
            int barLength = (max == 0) ? 0 : (count * 40 / max);

            final String BLUE = "\u001B[34m";
            final String RESET = "\u001B[0m";

            System.out.printf("%-12s | %s%s%s (%d)%n",
                    atc.getType().name(),
                    BLUE,
                    "█".repeat(barLength),
                    RESET,
                    count);
        }

        System.out.println("------------------------------------------------------------");
    }


}