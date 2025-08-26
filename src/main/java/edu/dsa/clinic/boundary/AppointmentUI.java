package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class AppointmentUI extends UI {
    private AppointmentController appointmentController = new AppointmentController();
    private PatientUI patientUI = new PatientUI(this.scanner);
    private DoctorUI doctorUI = new DoctorUI(this.scanner);
    private MedicineUI medicineUI = new MedicineUI(this.scanner);
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentUI(Scanner scanner) {
        super(scanner);
    }

//    public void consultationMenu(){
//        medicineUI.startMenu();
//    }

    public void appointmentMenu() {
        String choice;
        do {
            System.out.println("\nAppointment MENU");
            System.out.println("1. Create Future Appointment");
            System.out.println("2. Create Walk-In Appointment");
            System.out.println("3. View All Appointments");
            System.out.println("4. Edit An Appointment");
            System.out.println("5. Cancel An Appointment");
            System.out.println("0. Quit");
            System.out.print("Enter choice: ");

            choice = this.scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1" -> createFutureAppointmentMenu();
                case "2" -> createWalkInAppointmentMenu();
                case "3" -> viewAppointmentMenu();
//                case "4"  -> editAppointmentMenu();
                case "5"  -> cancelAppointmentMenu();
                case "0"  -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid Choice");
            }

        } while (!choice.equals("0"));
    }

    public void createFutureAppointmentMenu() {
        Appointment appointment = new Appointment();

        Patient selectedPatient = patientUI.selectPatient();
        if(selectedPatient == null){
            System.out.println("Returning...");
            return;
        }

        appointment.setPatient(patientUI.selectPatient());
        appointment.setDoctor(doctorUI.selectDoctor());

        LocalDateTime startTime = inputAppointmentStartTime();
        appointment.setExpectedStartAt(startTime);

        appointment.setExpectedEndAt(generateAppointmentEndTime(startTime));
        appointment.setCreatedAt(LocalDateTime.now());

        if(updateAppointmentConfirmation("add")) {
            this.appointmentController.saveAppointment(appointment);
        }else{
            System.out.println("Operation cancelled");
        }
    }

    public void createWalkInAppointmentMenu() {
        Appointment appointment = new Appointment();

        ConsultationQueue queue = patientUI.appointQueue();
        if(queue == null){
            System.out.println("There is no one on the queue");
            return;
        }

//        Doctor selectedDoctor = doctorUI.selectDoctor();
//        if(selectedDoctor == null){
//            System.out.println("Returning...");
//            return;
//        }else appointment.setDoctor(doctorUI.selectDoctor());

        appointment.setPatient(queue.patient());
        appointment.setAppointmentType(queue.type());

        LocalDateTime now = LocalDateTime.now();
        appointment.setExpectedStartAt(now);
        appointment.setExpectedEndAt(now.plusMinutes(30));
        appointment.setCreatedAt(now);

        if(updateAppointmentConfirmation("add")) {
            this.appointmentController.saveAppointment(appointment);
        }else{
            System.out.println("Operation cancelled");
        }
    }

    //TODO: get doctor time
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

    //TODO: get doctor time
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
            timeConvert = LocalDateTime.parse(inputTime, formatter);
            return timeConvert;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm");
            timeConvert = null;
            return timeConvert;
        }
    }

    public LocalDateTime validateInputDateTime(String inputTime) {
        LocalDateTime timeConvert;
        try {
            timeConvert = LocalDateTime.parse(inputTime, formatter);
            if (timeConvert.isAfter(LocalDateTime.now())) {
                return timeConvert;
            } else {
                System.out.println("Time should be after now");
                timeConvert = null;
                return timeConvert;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm");
            timeConvert = null;
            return timeConvert;
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
        }, Database.appointmentList.clone()) {
            @Override
            protected Cell[] getRow(Appointment o) {
                return new Cell[]{
                        new Cell(o.getId(), Alignment.LEFT),
                        new Cell(o.getPatient().getName()),
                        new Cell(o.getDoctor().getName()),
                        new Cell(o.getExpectedStartAt().format(formatter)),
                        new Cell(o.getExpectedEndAt().format(formatter)),
                        new Cell(o.getCreatedAt().format(formatter)),
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
            System.out.println("Enter \"N\" Or \"P\" to change pages");
            System.out.println("0. Exit");
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
                            System.out.println("Appointment with ID (" + selectedAppointment.getId() + ")  selected");
                            return selectedAppointment;
                        }
                    } while (selectedAppointment == null);
                }
                case "2" -> filterAppointment(table, formatter);
                case "3" -> sortAppointment(table);
                case "N", "P" -> pageControls(table, option);
                case "0" -> System.out.println("Exiting...");
            }

        }while(!option.equals("0") && !option.equals("1"));

        return selectedAppointment;
    }

    public void viewAppointmentMenu() {
        var table = initializeAppointmentTable();

        String option;

        do{
            table.display();
            System.out.println("Choose an Option :");
            System.out.println("1. Filter Appointments");
            System.out.println("2. Sort Appointments");
            System.out.println("0. Exit");
            System.out.println("Enter \"N\" Or \"P\" to change pages");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option.toUpperCase()) {
                case "1" -> filterAppointment(table, formatter);
                case "2" -> sortAppointment(table);
                case "N", "P" -> pageControls(table, option);
                case "0" -> System.out.println("Exiting...");
                default -> System.out.println("Please Enter a valid Option");
            }

        }while(!option.equals("0"));
    }

    public void cancelAppointment(ListInterface<Appointment> appointmentList) {

        System.out.println("Select an appointment to cancel (enter by id) : ");
        String selectedAppointmentId = this.scanner.nextLine();
        int appointmentIndex = -1;

        for(int i = 0 ; i < appointmentList.size() ; i++){
            if(String.valueOf(appointmentList.get(i).getId()).equals(selectedAppointmentId)){
                appointmentIndex = i;
            }
        }

        if(appointmentIndex != -1){
            if(updateAppointmentConfirmation("cancel")){
                this.appointmentController.cancelAppointment(appointmentIndex);
            } else{
                System.out.println("Operation Cancelled");
            }
        }else{
            System.out.println("Appointment not found or is invalid.");
        }
    }

    public void cancelAppointmentMenu(){
        var appointments = this.appointmentController.getAppointments();

        var table = initializeAppointmentTable();

        String option;
        do{
            table.display();
            System.out.println("Select an option:");
            System.out.println("1. Cancel An Appointment");
            System.out.println("2. Filter Appointment");
            System.out.println("3. Sort Appointment");
            System.out.println("Enter \"N\" Or \"P\" to change pages");
            System.out.println("0. Return");
            System.out.print("Enter Option: ");
            option = this.scanner.nextLine();

            switch (option) {
                case "1" -> cancelAppointment(appointments);
                case "2" -> filterAppointment(table, formatter);
                case "3" -> sortAppointment(table);
                case "N", "P" -> pageControls(table, option);
                case "0" ->System.out.println("Returning...");
                default -> System.out.println("Please enter a valid option");
            }

        }while (!option.equals("0") && !option.equals("1"));
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
            System.out.println("3. Sort By Doctor");
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

        return option.equalsIgnoreCase("A");
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

                    applyAppointmentFilter(table, "start name", null, startTime1, startTime2, formatter);
                }
                case "4" -> {
                    System.out.println("Search End Time: ");
                    LocalDateTime endTime1 = inputSearchAppointmentTime("End", "(first)");
                    LocalDateTime endTime2 = inputSearchAppointmentTime("End", "(second)");

                    applyAppointmentFilter(table, "end name", null, endTime1, endTime2, formatter);
                }
                case "5" -> {
                    System.out.println("Search Created Time: ");
                    LocalDateTime createdTime1 = inputSearchAppointmentTime("Created", "(first)");
                    LocalDateTime createdTime2 = inputSearchAppointmentTime("Created", "(second)");

                    applyAppointmentFilter(table, "created name", null, createdTime1, createdTime2, formatter);
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
            }
            case "end time": {
                table.addFilter(column + " is between " + value1.format(formatter) + "\" and \"" + value2.format(formatter) + "\"",
                        a -> !a.getExpectedStartAt().isBefore(value1) &&
                                !a.getExpectedStartAt().isAfter(value2)
                );
            }
            case "created time": {
                table.addFilter(column + " is between " + value1.format(formatter) + "\" and \"" + value2.format(formatter) + "\"",
                        a -> !a.getExpectedStartAt().isBefore(value1) &&
                                !a.getExpectedStartAt().isAfter(value2)
                );
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


}