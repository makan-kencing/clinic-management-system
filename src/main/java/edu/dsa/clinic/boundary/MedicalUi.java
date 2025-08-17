package edu.dsa.clinic.boundary;

import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;

import java.io.IOException;
import java.util.Scanner;

public class MedicalUi {



    public void viewMedical() throws IOException {
        Scanner sc = new Scanner(System.in);
        String [] menu ={
                "Create Consultation Record",
                "List Out Consultation Record",
                "Back"
        };
        while (true) {
            System.out.println("\nMedicine Function");
            System.out.println("Please select the function:");
            System.out.println("-".repeat(30));
            for (int i = 0; i < menu.length; i++) {
                System.out.printf("%d. %s%n", i + 1, menu[i]);
            }
            System.out.println("-".repeat(30));
            System.out.printf("Enter choice [1-%d]: ", menu.length);

            int choice;
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            choice = sc.nextInt();

            if (choice < 1 || choice > menu.length) {
                System.out.println("Out of range. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> insertPatientInform();
                case 2 -> System.out.println(">> Viewing consultation records...");
                case 9 -> {
                    System.out.println(">> Back");
                    return;
                }
            }
        }
    }

    public void mainInsert() throws IOException {
        MedicalController medicalController = new MedicalController();
        Patient patient = insertPatientInform();
        Doctor doctor= insertAttendingDoctor();
        insertDiagnosisForm(patient);
        medicalController.createConsultationRecord(patient,doctor);
    }

    public Patient insertPatientInform() throws IOException {
        Scanner sc = new Scanner(System.in);
        Patient patients = new Patient();
        Gender genders = null;

        Doctor doctors = new Doctor();

        System.out.println("Enter Patient Name: ");
        String name = sc.nextLine();
        patients.setName(name);
        System.out.println("Select Patient Gender: ");
        System.out.println("-".repeat(30));
        System.out.printf("%s,%s,%s","| [1] Male |"," [2] Female |"," [3] Unknown |");
        System.out.println("-".repeat(30));
        String choice = sc.nextLine();
        switch (choice) {
            case "1"-> genders =Gender.MALE;
            case "2"-> genders =Gender.FEMALE;
            case "3"-> genders=Gender.IDK;
        }
        patients.setGender(genders);
        System.out.println("Enter identification: ");
        String identification = sc.nextLine();
        patients.setIdentification(identification);
        System.out.println("Enter Contact Number: ");
        String contactNumber= sc.nextLine();
        System.out.println("-".repeat(30));
        patients.setContactNumber(contactNumber);
       return patients;

    }
    public Doctor insertAttendingDoctor() throws IOException {
        Scanner sc = new Scanner(System.in);
        Doctor doctor = new Doctor();
        Gender genders = null;
        System.out.println("Enter Doctor Name: ");
        String name = sc.nextLine();
        System.out.println("Select Doctor Gender: ");
        System.out.println("-".repeat(30));
        System.out.printf("%s,%s,%s","| [1] Male |"," [2] Female |"," [3] Unknown |");
        System.out.println("-".repeat(30));
        String choice = sc.nextLine();
        switch (choice) {
            case "1"-> genders =Gender.MALE;
            case "2"-> genders =Gender.FEMALE;
            case "3"-> genders=Gender.IDK;
        }
        doctor.setGender(genders);
        System.out.println("-".repeat(30));
        System.out.println("Enter Contact Number: ");
        String contactNumber= sc.nextLine();
        System.out.println("-".repeat(30));
        doctor.setContactNumber(contactNumber);
        return doctor;
    }
    public void insertDiagnosisForm(Patient patient) throws IOException {
        Scanner sc = new Scanner(System.in);
        Diagnosis diagnosis = new Diagnosis();
        MedicalController medicalController = new MedicalController();
        Consultation c=medicalController.listConsultations(patient);


    }


}
