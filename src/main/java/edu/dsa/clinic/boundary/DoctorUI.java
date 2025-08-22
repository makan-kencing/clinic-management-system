package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.entity.*;
import edu.dsa.clinic.ClinicManagementSystem;

import java.util.Scanner;

public class DoctorUI {
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

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

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

        Database.doctorsList.add(doctor);

        System.out.println("Doctor created successfully: " + doctor);
    }

//    private void viewDoctorsUI() {
//        int count = DoctorController.getDoctorCount();
//        if (count == 0) {
//            System.out.println("No doctors found.");
//            return;
//        }
//        int i = 0;
//        for (Doctor d : doctorController.getDoctorInfo()) {
//            System.out.println((i++) + ". " + d);
//        }
//    }



}
