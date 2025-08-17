package edu.dsa.clinic.boundary;

import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;

import java.util.Scanner;

public class MedicalUi {

    Scanner sc = new Scanner(System.in);
    MedicalController medicals = new MedicalController();
    Patient patients = new Patient();
    Gender genders;

    public void viewMedical(){

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


    public Patient insertPatientInform(){
        System.out.println("Enter Patient Name: ");
        String name = sc.nextLine();
        patients.setName(name);
        System.out.println("Select Patient Gender: ");
        System.out.printf("%s,%s,%s","| [1] Male |"," [2] Female |"," [3] Unknown |");
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
        patients.setContactNumber(contactNumber);
        return patients;
    }
}
