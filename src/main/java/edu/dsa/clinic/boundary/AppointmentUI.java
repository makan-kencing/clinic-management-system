package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.entity.Appointment;
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
import java.util.Scanner;


public class AppointmentUI extends UI {
    private AppointmentController appointmentController = new AppointmentController();

    public AppointmentUI(Scanner scanner) {
        super(scanner);
    }

    public void getMenuChoice() {
        String choice;
        do {
            System.out.println("\nAppointment MENU");
            System.out.println("1. Schedule An Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. Edit An Appointment");
            System.out.println("4. Cancel An Appointment");
            System.out.println("0. Quit");
            System.out.print("Enter choice: ");

            choice = this.scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1" -> createAppointment();
                case "2" -> viewAppointment();
//                case 3  -> editAppointment();
//                case 4  -> cancelAppointment();
//                case 0  -> quit();
                default -> System.out.println("Invalid Choice");
            }

        } while (!choice.equals("0"));
    }

    public void createAppointment() {
        Appointment appointment = new Appointment();
        appointment.setDoctor(selectAppointmentDoctor());
        appointment.setPatient(selectAppointmentPatient());

        LocalDateTime startTime = inputAppointmentStartTime();
        appointment.setExpectedStartAt(startTime);

        appointment.setExpectedEndAt(inputAppointmentEndTime(startTime));
        appointment.setCreatedAt(LocalDateTime.now());

        this.appointmentController.saveAppointment(appointment);
    }

    public Doctor selectAppointmentDoctor() {
        String selectedDoctorId;
        Doctor selectedDoctor = null;
        boolean valid = false;

        do {
            var table = new InteractiveTable<>(new Column[]{
                    new Column("Id", Alignment.RIGHT, 5),
                    new Column("Name", Alignment.CENTER, 20),
                    new Column("Gender", Alignment.CENTER, 10),
                    new Column("Contact No", Alignment.CENTER, 15),
                    new Column("Specialization", Alignment.CENTER, 20)
            }, Database.doctorList.clone()) {
                @Override
                protected Cell[] getRow(Doctor o) {
                    return new Cell[] {
                            new Cell(o.getId(), Alignment.CENTER),
                            new Cell(o.getName()),
                            new Cell(o.getGender(), Alignment.CENTER),
                            new Cell(o.getContactNumber()),
                            new Cell(o.getSpecialization(), Alignment.CENTER)
                    };
                }
            };

            table.display();
            System.out.print("Select a doctor (enter id): ");
            selectedDoctorId = this.scanner.nextLine();

            for (int i = 0; i < Database.doctorList.size(); i++) {
                if (String.valueOf(Database.doctorList.get(i).getId()).equals(selectedDoctorId)) {
                    valid = true;
                    selectedDoctor = Database.doctorList.get(i);
                }
            }

            if (!valid) {
                System.out.println("Invalid doctor ID! Please try again.");
            }

        } while (!valid);

        return selectedDoctor;
    }

    //Apointment queue from consaltation
    public Patient selectAppointmentPatient() {
        String selectedPatientId;
        Patient selectedPatient = null;
        boolean valid = false;

        do {
            var table = new InteractiveTable<>(new Column[]{
                    new Column("Id", Alignment.RIGHT, 5),
                    new Column("Name", Alignment.CENTER, 20),
                    new Column("Gender", Alignment.CENTER, 10),
                    new Column("Identification", Alignment.CENTER, 20),
                    new Column("Contact No", Alignment.CENTER, 20)
            }, Database.patientsList.clone()) {
                @Override
                protected Cell[] getRow(Patient o) {
                    return new Cell[] {
                            new Cell(o.getId(), Alignment.CENTER),
                            new Cell(o.getName()),
                            new Cell(o.getGender(), Alignment.CENTER),
                            new Cell(o.getIdentification()),
                            new Cell(o.getContactNumber())
                    };
                }
            };

            table.display();
            System.out.print("Select a patient (enter id): ");
            selectedPatientId = this.scanner.nextLine();

            for (int i = 0; i < Database.patientsList.size(); i++) {
                if (String.valueOf(Database.patientsList.get(i).getId()).equals(selectedPatientId)) {
                    valid = true;
                    selectedPatient = Database.patientsList.get(i);
                }
            }

            if (!valid) {
                System.out.println("Invalid patient ID! Please try again.");
            }

        } while (!valid);

        return selectedPatient;
    }

    //TODO: get doctor time
    public LocalDateTime inputAppointmentStartTime() {
        String inputTime;
        LocalDateTime startTime;
        do {
            System.out.print("Enter Appointment Start Time (yyyy-MM-dd HH:mm): ");
            inputTime = this.scanner.nextLine();

            startTime = validateInputDateTime(inputTime);

        } while (startTime == null);

        return startTime;
    }

    //TODO: get doctor time
    public LocalDateTime inputAppointmentEndTime(LocalDateTime startTime) {
        String inputTime;
        LocalDateTime endTime;

        do {
            System.out.print("Enter Appointment End Time (yyyy-MM-dd HH:mm): ");
            inputTime = this.scanner.nextLine();

            endTime = validateInputDateTime(inputTime);
            if(endTime.isBefore(startTime)){
                System.out.println("Time should be after Start Time");
            }

        } while ((endTime == null) || endTime.isBefore(startTime));

        return endTime;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

    public void viewAppointment() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        var appointments = this.appointmentController.getAppointments();
        String option, searchOption;

        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", Alignment.CENTER, 5),
                new Column("Patient Name", Alignment.CENTER, 25),
                new Column("Doctor", Alignment.CENTER, 25),
                new Column("Start At", Alignment.CENTER, 25),
                new Column("End At", Alignment.CENTER, 25),
                new Column("Created At", Alignment.CENTER, 25)
        }, appointments) {
            @Override
            protected Cell[] getRow(Appointment o) {
                return new Cell[] {
                        new Cell(o.getId(), Alignment.LEFT),
                        new Cell(o.getPatient().getName()),
                        new Cell(o.getDoctor().getName()),
                        new Cell(o.getExpectedStartAt().format(formatter)),
                        new Cell(o.getExpectedEndAt().format(formatter)),
                        new Cell(o.getCreatedAt().format(formatter))
                };
            }
        };

        do{
            table.display();

            System.out.println("Choose an Option :");
            System.out.println("1. Search An Appointment");
            System.out.println("2. Sort By Id");
            System.out.println("3. Sort By Patent Name");
            System.out.println("4. Sort By Doctor");
            System.out.println("5. Sort By Start Time");
            System.out.println("6. Sort By End Time");
            System.out.println("7. Sort By Created Time");
            System.out.println("8. Reset sorters");
            System.out.println("0. Exit");

//            table.addSorter("Patient", (a1, a2) -> a1.getPatient().getName().compareToIgnoreCase(a2.getPatient().getName()));
//            table.addSorter("Appointment time", Comparator.comparing(Appointment::getExpectedStartAt));

            option = this.scanner.nextLine();
            switch (option) {
                case "1" -> {
                    do {
                        table.display();
                        searchOption = getSearchByOption();
                        switch (searchOption) {
                            case "1" -> {
                                System.out.println("Search Patient name: ");
                                String name = this.scanner.nextLine();

                                table.addFilter(
                                        "patien name is \"" + name + "\"",
                                        a -> a.getPatient().getName().toLowerCase().contains(name.toLowerCase())
                                );
                            }
                            case "2" -> {
                                System.out.println("Search Doctor name: ");
                                String name = this.scanner.nextLine();

                                table.addFilter(
                                        "doctor name is \"" + name + "\"",
                                        a -> a.getDoctor().getName().toLowerCase().contains(name.toLowerCase())
                                );
                            }
                            case "3" -> {
                                System.out.println("Search Start Time: ");
                                LocalDateTime startTime1 = inputSearchAppointmentTime("Start", "(first)");
                                LocalDateTime startTime2 = inputSearchAppointmentTime("Start", "(second)");

                                table.addFilter(
                                        "start time between \"" + startTime1.format(formatter) + "\" and \"" + startTime2.format(formatter) + "\"",
                                        a -> !a.getExpectedStartAt().isBefore(startTime1) &&
                                                !a.getExpectedStartAt().isAfter(startTime2)
                                );
                            }
                            case "4" -> {
                                System.out.println("Search End Time: ");
                                LocalDateTime endTime1 = inputSearchAppointmentTime("End", "(first)");
                                LocalDateTime endTime2 = inputSearchAppointmentTime("End", "(second)");

                                table.addFilter(
                                        "end time between \"" + endTime1.format(formatter) + "\" and \"" + endTime2.format(formatter) + "\"",
                                        a -> !a.getExpectedStartAt().isBefore(endTime1) &&
                                                !a.getExpectedStartAt().isAfter(endTime2)
                                );
                            }
                            case "5" -> {
                                System.out.println("Search Created Time: ");
                                LocalDateTime createdTime1 = inputSearchAppointmentTime("Created", "(first)");
                                LocalDateTime createdTime2 = inputSearchAppointmentTime("Created", "(second)");

                                table.addFilter(
                                        "created time between \"" + createdTime1.format(formatter) + "\" and \"" + createdTime2.format(formatter) + "\"",
                                        a -> !a.getExpectedStartAt().isBefore(createdTime1) &&
                                                !a.getExpectedStartAt().isAfter(createdTime2)
                                );
                            }
                            case "6" -> {
                                table.resetFilters();
                            }
                            case "0" -> {
                                System.out.println("Returing...");
                            }
                            default -> {
                                System.out.println("Please enter a valid option");
                            }
                        }
                    }while(!searchOption.equals("0"));
                }
                case "2" -> {
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("by Id (Asc)", Comparator.comparingInt(Appointment::getId));
                        }
                        case "2" -> {
                            table.addSorter("by Id (Desc)", Comparator.comparingInt(Appointment::getId).reversed());
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "3" ->{
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("By Patient Name (Asc)", (a1, a2) -> a1.getPatient().getName().
                                    compareToIgnoreCase(a2.getPatient().getName()));

                        }
                        case "2" -> {
                            table.addSorter("By Patient Name (Desc)", (a1, a2) -> a2.getPatient().getName().
                                    compareToIgnoreCase(a1.getPatient().getName()));
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "4" ->{
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("By Doctor Name (Asc)", (a1, a2) -> a1.getDoctor().getName().
                                    compareToIgnoreCase(a2.getDoctor().getName()));
                        }
                        case "2" -> {
                            table.addSorter("By Doctor Name (Desc)", (a1, a2) -> a2.getDoctor().getName().
                                    compareToIgnoreCase(a1.getDoctor().getName()));
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "5" -> {
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("By Start Time (Asc)", Comparator.comparing(Appointment::getExpectedStartAt));
                        }
                        case "2" -> {
                            table.addSorter("By Start Time (Desc)", Comparator.comparing(Appointment::getExpectedStartAt).reversed());
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "6" ->{
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("By End Time (Asc)", Comparator.comparing(Appointment::getExpectedEndAt));
                        }
                        case "2" -> {
                            table.addSorter("By End Time (Desc)", Comparator.comparing(Appointment::getExpectedEndAt).reversed());
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "7" -> {
                    switch (getSortOrderOption()) {
                        case "1" -> {
                            table.addSorter("By Created Time (Asc)", Comparator.comparing(Appointment::getCreatedAt));
                        }
                        case "2" -> {
                            table.addSorter("By Created Time (Asc)", Comparator.comparing(Appointment::getCreatedAt).reversed());
                        }
                        case "0" -> {
                            System.out.println("Returning...");
                        }
                    }
                }
                case "8" -> {
                    table.resetSorters();
                }
                case "0" -> {
                    System.out.println("Returning...");
                }
                default -> System.out.println("Invalid Choice");

            }

        }while(!option.equals("0"));

    }

    public String getSortOrderOption() {
        String option;
        do {
            System.out.println("Select a sort order :");
            System.out.println("1. Ascending");
            System.out.println("2. Descending");
            System.out.println("0. Return");
            System.out.print("Select an Option: ");
            option = this.scanner.nextLine();

            if (!option.matches("[0-2]")) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (!option.equals("1") && !option.equals("2") && !option.equals("0"));

        return option;
    }

    public String getSearchByOption() {
        String option;
        do {
            System.out.println("Search by:");
            System.out.println("1. Patient name");
            System.out.println("2. Doctor name");
            System.out.println("3. Start Time");
            System.out.println("4. End Time");
            System.out.println("5. Created Time");
            System.out.println("6. Reset Filters");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            option = this.scanner.nextLine();

            if (!option.matches("[0-6]")) {
                System.out.println("Invalid choice. Please enter 0–6.");
            }
        } while (!option.matches("[0-6]"));

        return option;
    }
}