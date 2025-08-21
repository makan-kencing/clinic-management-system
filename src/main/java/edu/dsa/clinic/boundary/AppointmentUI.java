package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.Tabulate;


public class AppointmentUI extends UI{
    private AppointmentController appointmentController = new AppointmentController();

    public AppointmentUI(Scanner scanner) {
        super(scanner);
    }

    public void getMenuChoice() {
        int choice;
        do {
            System.out.println("\nAppointment MENU");
            System.out.println("1. Schedule An Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. Edit An Appointment");
            System.out.println("4. Cancel An Appointment");
            System.out.println("0. Quit");
            System.out.print("Enter choice: ");

            choice = this.scanner.nextInt();
            this.scanner.nextLine();
            System.out.println();

            switch(choice) {
                case 1  -> createAppointment();
//                case 2  -> viewAllAppointments();
//                case 3  -> editAppointment();
//                case 4  -> cancelAppointment();
//                case 0  -> quit();
                default -> System.out.println("Invalid Choice");
            }

        } while (choice != 0);
    }

    public void createAppointment(){
        Appointment appointment = new Appointment();
        appointment.setDoctor(selectAppointmentDoctor());
        appointment.setPatient(selectAppointmentPatient());

        LocalDateTime startTime = inputAppointmentStartTime();
        appointment.setExpectedStartAt(startTime);

        appointment.setExpectedEndAt(inputAppointmentEndTime(startTime));
        appointment.setCreatedAt(LocalDateTime.now());

        Database.appointmentList.add(appointment);
    }

    public Doctor selectAppointmentDoctor() {
        int selectedDoctorId;
        Doctor selectedDoctor = null;
        boolean valid = false;

        do {
            var table = new Tabulate<>(new Tabulate.Header[]{
                    new Tabulate.Header("Id", 5, Tabulate.Alignment.RIGHT),
                    new Tabulate.Header("Name", 20, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Gender", 10, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Contact No", 15, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Specialization", 20, Tabulate.Alignment.CENTER)
            }, Database.doctorList.clone()) {
                @Override
                protected Cell[] getRow(Doctor element) {
                    return new Cell[] {
                            new Cell(String.valueOf(element.getId()), Alignment.CENTER),
                            new Cell(element.getName()),
                            new Cell(element.getGender().toString(), Alignment.CENTER),
                            new Cell(element.getContactNumber()),
                            new Cell(element.getSpecialization().toString(), Alignment.CENTER)
                    };
                }
            };

            table.display();
            System.out.print("Select a doctor (enter id): ");
            selectedDoctorId = this.scanner.nextInt();

            for (int i = 0; i < Database.doctorList.size(); i++) {
                if (Database.doctorList.get(i).getId() == selectedDoctorId) {
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

    public Patient selectAppointmentPatient() {
        int selectedPatientId;
        Patient selectedPatient = null;
        boolean valid = false;

        do {
            var table = new Tabulate<>(new Tabulate.Header[]{
                    new Tabulate.Header("Id", 5, Tabulate.Alignment.RIGHT),
                    new Tabulate.Header("Name", 20, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Gender", 10, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Identification", 20, Tabulate.Alignment.CENTER),
                    new Tabulate.Header("Contact No", 20, Tabulate.Alignment.CENTER)
            }, Database.patientsList.clone()) {
                @Override
                protected Cell[] getRow(Patient element) {
                    return new Cell[] {
                            new Cell(String.valueOf(element.getId()), Alignment.CENTER),
                            new Cell(element.getName()),
                            new Cell(element.getGender().toString(), Alignment.CENTER),
                            new Cell(element.getIdentification()),
                            new Cell(element.getContactNumber())
                    };
                }
            };

            table.display();
            System.out.print("Select a patient (enter id): ");
            selectedPatientId = this.scanner.nextInt();

            for (int i = 0; i < Database.patientsList.size(); i++) {
                if (Database.patientsList.get(i).getId() == selectedPatientId) {
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
    public LocalDateTime inputAppointmentStartTime(){
        String inputTime;
        LocalDateTime startTime;
        do{
            System.out.print("Enter Appointment Start Time (yyyy-MM-dd HH:mm): ");
            inputTime = this.scanner.nextLine();

            startTime = validateInputDateTime(inputTime);

        }while(startTime != null);

        return startTime;
    }

    //TODO: get doctor time
    public LocalDateTime inputAppointmentEndTime(LocalDateTime startTime){
        String inputTime;
        LocalDateTime endTime;

        do{
            System.out.print("Enter Appointment End Time (yyyy-MM-dd HH:mm): ");
            inputTime = this.scanner.nextLine();

            endTime = validateInputDateTime(inputTime);

        }while((endTime != null) &&  endTime.isBefore(startTime));

        return endTime;
    }

    public LocalDateTime validateInputDateTime (String inputTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime timeConvert;
        try {
            timeConvert = LocalDateTime.parse(inputTime, formatter);
            if (timeConvert.isBefore(LocalDateTime.now())){
                return timeConvert;
            } else{
                System.out.println("Time should be before now");
                timeConvert = null;
                return timeConvert;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format! Please use yyyy-MM-dd HH:mm");
            timeConvert = null;
            return timeConvert;
        }
    }




}