package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.entity.*;
import edu.dsa.clinic.ClinicManagementSystem;
import edu.dsa.clinic.utils.Tabulate;

import java.util.Scanner;

public class DoctorUI extends UI {
    private final DoctorController doctorController;

    public DoctorUI(Scanner scanner) {
        super(scanner);
        doctorController = new DoctorController();
    }

    public int getDoctorMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nDoctor Menu");
            System.out.println("1. Create new Doctor");
            System.out.println("2. View Doctor List");
            System.out.println("3. Delete Doctor Record");
            System.out.println("4. Modify Doctor Information");
            System.out.println("5. Doctor Shifts Menu");
            System.out.println("0. Exit To Main Menu");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    viewDoctorsUI();
                    break;
                case 3:

                default:
                    System.out.println("Invalid choice. Try again.");
            }

            return choice;
        } while (choice != 0);
    }

    public int getShiftsMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nDoctor Shifts Menu");
            System.out.println("1. Assign Doctor Shift");
            System.out.println("2. Change Doctor Shift");
            System.out.println("3. View Weekly Schedule");
            System.out.println("0. Exit To Doctor Menu");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();


            return choice;
        } while (choice != 0);
    }

    public void createDoctor() {
        Scanner sc = new Scanner(System.in);
        Doctor doctor = new Doctor();

        System.out.println("Enter Doctor Name: ");
        doctor.setName(sc.nextLine());

        System.out.println("Enter Gender (Male/Female): ");
        doctor.setGender(Gender.valueOf(sc.nextLine().toUpperCase()));

        System.out.print("Enter Contact Number: ");
        doctor.setContactNumber(sc.nextLine());

        int option;
        do {
            System.out.print("Select Specialization: ");
            System.out.println("1. Neurosurgery");
            System.out.println("2. Pediatrics");
            System.out.println("3. Ophthalmology");
            System.out.println("4. Otorhinolaryngology");
            System.out.println("5. Orthopedics");
            option = sc.nextInt();

            if(Character.isDigit(option)) {
                switch (option){
                    case 1: doctor.setSpecialization(Specialization.Neurosurgery);
                    break;
                    case 2: doctor.setSpecialization(Specialization.Pediatrics);
                    break;
                    case 3: doctor.setSpecialization(Specialization.Ophthalmology);
                    break;
                    case 4: doctor.setSpecialization(Specialization.Otorhinolaryngology);
                    break;
                    case 5: doctor.setSpecialization(Specialization.Orthopedics);
                    break;
                    default: System.out.println("Invalid option. Try again.");
                }
            }else{
                System.out.println("Invalid option. Try again.");
            }

        }while(option<1 || option>5 || !Character.isDigit(option));



        Database.doctorList.add(doctor);

        System.out.println("Doctor created successfully: " + doctor);
    }

    public void viewDoctorsUI() {
        int count = DoctorController.getDoctorCount();
        if (count == 0) {
            System.out.println("No doctors found.");
            return;
        }

        var doctors = this.doctorController.getDoctors();

        var table = new Tabulate<>(new Tabulate.Header[]{
                new Tabulate.Header("Doctor Id", 12),
                new Tabulate.Header("Name", 20),
                new Tabulate.Header("Gender", 8),
                new Tabulate.Header("Contact Number", 20),
                new Tabulate.Header("Specialization", 20)
        }, doctors) {
            @Override
            protected Cell[] getRow(Doctor element) {
                return new Cell[] {
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getName()),
                        new Cell(element.getGender().toString()),
                        new Cell(String.valueOf(element.getContactNumber())),
                        new Cell(element.getSpecialization().toString())
                };
            }
        };
        table.display();
    }

    public void modifyDoctor() {


    }



}
