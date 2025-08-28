package edu.dsa.clinic;

import edu.dsa.clinic.boundary.AppointmentUI;
import edu.dsa.clinic.boundary.DoctorUI;
import edu.dsa.clinic.boundary.MedicalUI;
import edu.dsa.clinic.boundary.MedicineUI;
import edu.dsa.clinic.boundary.PatientUI;
import edu.dsa.clinic.control.PatientController;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.StringUtils;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;

import java.io.IOException;
import java.util.Scanner;

public class ClinicManagementSystem {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {

            String[] menuItems = {
                    "1. Patient Management Module",
                    "2. Doctor Management Module",
                    "3. Appointment and Consultation Management Module",
                    "4. Medical Record Management Module",
                    "5. Medicine Management Module",
                    "0. Exit"
            };

            int maxLength = 0;
            for (String item : menuItems) {
                if (item.length() > maxLength) {
                    maxLength = item.length();
                }
            }

            System.out.println();
            System.out.println("=".repeat(maxLength + 10));
            System.out.println(StringUtils.pad("CLINIC MANAGEMENT SYSTEM", ' ', maxLength + 10));
            System.out.println("=".repeat(maxLength + 10));

            for (String item : menuItems) {
                System.out.println(StringUtils.padRight(item, ' ', maxLength));
            }

            System.out.println("=".repeat(maxLength + 10));
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            System.out.println("-".repeat(maxLength + 10));
            System.out.println();

            switch (choice) {
                case "1":
                    var patientUI = new PatientUI(scanner);
                    patientUI.startMenu();
                    break;
                case "2":
                    var doctorUI = new DoctorUI(scanner);
                    doctorUI.startMenu();
                    break;
                case "3":
                    var appointmentUI = new AppointmentUI(scanner);
                    appointmentUI.startMenu();
                    pause(scanner);
                    break;
                case "4":
                    var medicalUI = new MedicalUI(scanner);
                    // medicalUI.startMenu();
                    pause(scanner);
                    break;
                case "5":
                    var medicineUI = new MedicineUI(scanner);
                    medicineUI.startMenu();
                    pause(scanner);
                    break;
                case "0":
                    running = false;
                    System.out.println("\nThank you for using Clinic Management System!");
                    break;
                default:
                    System.out.println("\nInvalid choice, please try again!");
                    pause(scanner);
            }
        }
        scanner.close();
    }

    private static void pause(Scanner scanner) {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}
