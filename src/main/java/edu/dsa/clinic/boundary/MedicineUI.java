package edu.dsa.clinic.boundary;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * This UI is responsible for managing medicine definitions
 */
public class MedicineUI extends UI {
    private final MedicineController medicineController = new MedicineController();

    public MedicineUI(Scanner scanner, Terminal terminal) {
        super(scanner, terminal);
    }

    public MedicineUI(Scanner scanner) {
        super(scanner);
    }

    public void startMenu() {
        this.searchProduct();
    }

    public @Nullable Product searchProduct() {
        var medicines = this.medicineController.getAllProducts();

        var table = new InteractiveTable<>(new Column[]{
                new Column("Id", 4),
                new Column("Name", 30),
                new Column("Type", 30),
                new Column("Brand", 15),
                new Column("Unit Cost", 10),
                new Column("Unit Price", 10),
                new Column("In stock", 10),
                new Column("Last stocked", 20)
        }, medicines) {
            @Override
            protected Cell[] getRow(Product o) {
                return new Cell[]{
                        new Cell(o.getId()),
                        new Cell(o.getName()),
                        new Cell(o.getMedicine().getName()),
                        new Cell(o.getBrand()),
                        new Cell(o.getCost()),
                        new Cell(o.getPrice()),
                        new Cell(MedicineController.getAvailableStocks(o)),
                        new Cell(MedicineController.getLatestStocked(o))
                };
            }
        };

        try (var writer = this.terminal.writer();
             var reader = this.terminal.reader()) {
            while (true) {
                this.terminal.puts(InfoCmp.Capability.clear_screen);

                table.display(false);

                writer.println("[<] Previous page  [>] Next page");
                writer.println("[n] New record");
                writer.println("[q] Quit");

                this.terminal.flush();

                var read = reader.read();
                switch (read) {
                    case '<':
                        table.previousPage();
                        break;
                    case '>':
                        table.nextPage();
                        break;
                    case 'n':
                        break;
                    case 'q': return null;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewMedicineDetails(Product medicine) {
        // TODO
        System.out.println("Medicine details");
        System.out.println("================");
        System.out.printf("Name: %s%n", medicine.getName());
        System.out.printf("Type: %s%n", medicine.getMedicine());
        System.out.printf("Brand: %s%n", medicine.getBrand());
        System.out.printf("Unit Cost: RM %f%n", medicine.getCost());
        System.out.printf("Unit Price: RM %f%n", medicine.getPrice());

        System.out.print("Suitable substitutes: ");
        var joiner = new StringJoiner(", ");
        for (var substitute : medicine.getSubstitutes())
            joiner.add(substitute.getName());
        System.out.println(joiner);
    }

    public Medicine createMedicine() {
        var medicine = new Medicine();

        System.out.print("Name: ");
        medicine.setName(this.scanner.nextLine());

        System.out.print("Types: ");
        medicine.addType(MedicineType.valueOf(this.scanner.nextLine()));

        medicineController.createMedicineEntry(medicine);

        System.out.printf("Medicine `%s` added.%n", medicine.getName());
        this.scanner.next();

        return medicine;
    }
}
