package edu.dsa.clinic;

import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.Tabulate;

public class ClinicManagementSystem {
    public static void main(String[] args) {
        Initializer.initialize();

        var table = new Tabulate<>(new Tabulate.Header[]{
                new Tabulate.Header("Id", 4, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Name", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Gender", 10, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Identification", 20, Tabulate.Alignment.CENTER),
                new Tabulate.Header("Contact No", 20, Tabulate.Alignment.CENTER)
        }, Database.patientsList.clone()) {
            @Override
            protected Cell[] getRow(Patient element) {
                return new Cell[] {
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getName()),
                        new Cell(element.getGender().toString(), Alignment.CENTER),
                        new Cell(element.getIdentification()),
                        new Cell(element.getContactNumber())
                };
            }
        };
        table.setPageSize(2);
        table.display();

        System.out.println();
        System.out.println();

        table.nextPage();

        table.display();
        // Entrypoint to the main UI.
    }
}
