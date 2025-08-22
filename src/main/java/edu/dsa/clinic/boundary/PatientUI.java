package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.PatientController;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.Tabulate;

import java.util.Scanner;

public class PatientUI extends UI {
    private final PatientController patientController = new PatientController();

    public PatientUI(Scanner scanner) {
        super(scanner);
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
        table.display();

        int opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Patient ID " +
                    "\n(2) Filter Patient Record " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextInt();
            this.scanner.nextLine();

            System.out.println();

            if (opt != 4) {
                switch (opt) {
                    case 1: {
                        do {
                            table.display();
                            System.out.print("\nEnter Patient ID (0 to exit): ");
                            int selectedId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (selectedId == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                break;
                            }
                            selectedPatient = patientController.performSelect(selectedId);
                            if (selectedPatient == null) {
                                System.out.println("Patient with ID (" + selectedId + ") not found. Please re-enter Patient ID...");
                            } else {
                                System.out.println("Patient (" + selectedPatient.getName() + ") with ID (" + selectedPatient.getId() + ") selected!");
                            }
                        } while (selectedPatient == null);
                        break;
                    }
                    case 2: {
                        filterPatient(table);
                        break;
                    }
                    case 3: {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                }
            } else {
                System.out.println();
                table.display();
                break;
            }
        } while (opt > 1 && opt < 4);

        return selectedPatient;
    }

    public void filterPatient(Tabulate<Patient> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("(1) name");
        System.out.println("(2) identification");
        System.out.println("(3) contact number");
        System.out.println("(4) gender");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                System.out.print("Search name by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "name", value);
                break;
            }
            case 2: {
                System.out.print("Search identification by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "identification", value);
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "contact", value);
            }
            case 4: {
                System.out.println();
                System.out.println("-".repeat(30));
                System.out.println("Filter gender by: ");
                System.out.println("(1) male");
                System.out.println("(2) female");
                System.out.println("-".repeat(30));
                System.out.print("Selection : ");
                var value = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (value == 1) {
                    filter(table, "gender", null, "male");
                } else if (value == 2) {
                    filter(table, "gender", null, "female");
                }
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void filter(Tabulate<Patient> table, String column, String value) {
        filter(table, column, value, null);
    }

    public void filter(Tabulate<Patient> table, String column, String value, String gender) {
        switch (column) {
            case "name": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "identification": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getIdentification().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "contact": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getContactNumber().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "gender": {
                if (gender.equals("male")) {
                    table.addFilter("Male only", p -> p.getGender() == Gender.MALE);
                    table.display();
                } else if (gender.equals("female")) {
                    table.addFilter("Female only", p -> p.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }
            default:
                break;
        }
    }
}
