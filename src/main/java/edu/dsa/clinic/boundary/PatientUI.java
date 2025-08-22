package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.PatientController;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.Tabulate;

import java.util.Scanner;

public class PatientUI extends UI {
    private final PatientController patientController;

    public PatientUI(Scanner scanner) {
        super(scanner);
        this.patientController = new PatientController(this, scanner);
    }

    public Patient selectPatient() {
        Patient selectedPatient = null;

        var table = new Tabulate<>(new Tabulate.Header[]{
                new Tabulate.Header("Id", 4, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Name", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Gender", 10, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Identification", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Contact No", 20, Tabulate.Alignment.CENTER)
        }, Database.patientsList.clone()) {
            @Override
            protected Cell[] getRow(Patient element) {
                return new Cell[]{
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getName()),
                        new Cell(element.getGender().toString(), Alignment.CENTER),
                        new Cell(element.getIdentification()),
                        new Cell(element.getContactNumber())
                };
            }
        };

        displayTable(table);

        int opt;
        do {
            showPatientMenu();
            opt = promptInt("Selection: ", scanner);

            if (opt != 4) {
                selectedPatient = patientController.handlePatientMenuOption(table, opt, scanner);
            }
        } while (opt != 4 && selectedPatient == null);

        return selectedPatient;
    }

    public void showPatientMenu() {
        System.out.println("==============================");
        System.out.println("(1) Select Patient ID");
        System.out.println("(2) Filter Patient Record");
        System.out.println("(3) Reset Filters");
        System.out.println("(4) Exit");
        System.out.println("==============================");
    }

    public void showFilterMenu() {
        System.out.println("\n==============================");
        System.out.println("Filter Patients By:");
        System.out.println("(1) Name");
        System.out.println("(2) Identification");
        System.out.println("(3) Contact Number");
        System.out.println("(4) Gender");
    }

    public void showGenderFilterMenu() {
        System.out.println("\n==============================");
        System.out.println("Filter by:");
        System.out.println("(1) Male");
        System.out.println("(2) Female");
    }

    public void showPatientNotFound(int id) {
        System.out.println("Patient with ID (" + id + ") not found. Please re-enter Patient ID...");
    }

    public void showPatientSelected(Patient p) {
        System.out.println("\nPatient (" + p.getName() + ") with ID (" + p.getId() + ") selected!");
    }

    public void displayTable(Tabulate<Patient> table) {
        System.out.println();
        table.display();
    }

    //prompt helper
    public String prompt(String msg, Scanner scanner) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    public int promptInt(String msg, Scanner scanner) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + msg);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public int getFilterOption(Scanner scanner) {
        System.out.println("==============================");
        return promptInt("Filter by: ", scanner);
    }

    public String getSearchValue(String field, Scanner scanner) {
        return prompt("Search " + field + " by: ", scanner);
    }

    public int getGenderOption(Scanner scanner) {
        return promptInt("Selection: ", scanner);
    }
}
