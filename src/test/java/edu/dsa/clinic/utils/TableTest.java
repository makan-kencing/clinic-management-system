package edu.dsa.clinic.utils;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.Table;

public class TableTest {
    public static void main(String[] args) {
        Initializer.initialize();

        var table = new Table<>(new Column[]{
                new Column("Id", Alignment.CENTER, 4),
                new Column("Name", Alignment.CENTER, 20),
                new Column("Gender", Alignment.CENTER, 10),
                new Column("Identification", Alignment.CENTER, 20),
                new Column("Contact No", Alignment.CENTER, 20)
        }, Database.patientsList.clone()) {
            @Override
            protected Cell[] getRow(Patient o) {
                return new Cell[] {
                        new Cell(o.getId()),
                        new Cell(o.getName()),
                        new Cell(o.getGender(), Alignment.CENTER),
                        new Cell(o.getIdentification()),
                        new Cell(o.getContactNumber())
                };
            }
        };
        table.display();
    }
}
