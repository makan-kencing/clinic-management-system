package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.PatientController;
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
            System.out.print("==============================");
            System.out.println("\n (1) Select Patient ID \n (2) Filter Patient Record \n (3) Reset Filters \n (4) Exit");
            System.out.print("==============================");
            System.out.println();
            System.out.print("Selection : ");
            opt = this.scanner.nextInt();
            this.scanner.nextLine();

            System.out.println();

            if (opt != 4) {
                selectedPatient = patientController.selectPatientWithFilter(table, opt, this.scanner);
            } else {
                break;
            }
        } while (opt > 1 && opt < 4);

        return selectedPatient;
    }
}
