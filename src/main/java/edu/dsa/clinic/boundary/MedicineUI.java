package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.utils.Filter;
import edu.dsa.clinic.utils.Pager;
import edu.dsa.clinic.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * This UI is responsible for managing medicine definitions
 */
public class MedicineUI extends UI {
    private final MedicineController medicineController = new MedicineController();

    public MedicineUI(Scanner scanner) {
        super(scanner);
    }

    public @Nullable Medicine searchMedicine() {
        var pager = new Pager<Medicine>(this.scanner) {
            @Override
            public void resetSorters() {
                super.resetSorters();
                this.sorters.add((m1, m2) -> m1.getId() - m2.getId());
            }

            @Override
            protected ListInterface<Medicine> fetch(ListInterface<Filter<Medicine>> filters, ListInterface<Comparator<Medicine>> sorters) {
                return medicineController.getAllMedicines(this.filters, this.sorters);
            }

            @Override
            protected void displayAll(ListInterface<Medicine> data) {
                var i = 0;
                for (var medicine : data)
                    if (i++ < this.numberOfRows)
                        System.out.printf("%d. %s%n", i, medicine);
            }

            @Override
            protected @Nullable Filter<Medicine> promptFilter() {
                System.out.println("Filters:");
                System.out.println("(1) name");
                System.out.println("(2) type");
                System.out.println("(3) brand");
                System.out.println("(4) unit cost");
                System.out.println("(5) unit price");
                System.out.print("Filter by: ");

                var opt = this.scanner.nextInt();

                switch (opt) {
                    case 1:
                        System.out.print("Search name by: ");

                        var name = this.scanner.nextLine();
                        return m -> m.getName().toLowerCase().contains(name.toLowerCase());
                    case 2:
                        System.out.print("Search type by: ");

                        var type = MedicineType.valueOf(this.scanner.nextLine());
                        return m -> m.getType().equals(type);
                    case 3:
                        System.out.print("Search by brand: ");

                        var brand = this.scanner.nextLine();
                        return m -> m.getName().toLowerCase().contains(brand.toLowerCase());
                    case 4:
                    case 5:
                        System.out.print("From: ");

                        var low = this.scanner.nextBigDecimal();

                        System.out.print("To: ");

                        var high = this.scanner.nextBigDecimal();

                        if (opt == 4)
                            return m -> Utils.isBetween(m.getCost(), low, high);
                        else
                            return m -> Utils.isBetween(m.getPrice(), low, high);
                    default:
                        return null;
                }
            }

            @Override
            protected @Nullable Comparator<Medicine> promptSorter() {
                System.out.println("Sorts");
                System.out.println("(1) default");
                System.out.println("(2) unit cost");
                System.out.println("(3) unit price");
                System.out.print("Sort by: ");

                var opt = this.scanner.nextInt();

                switch (opt) {
                    case 1:
                    case 2:
                    case 3:
                        System.out.println("Orderings");
                        System.out.println("(1) ASC (default)");
                        System.out.println("(2) DESC");
                        System.out.print("Order by: ");

                        var isDescending = this.scanner.nextInt() == 2 ? -1 : 1;

                        return switch (opt) {
                            case 1 -> (m1, m2) -> m1.getId() - m2.getId() * isDescending;
                            case 2 -> (m1, m2) -> m1.getCost().compareTo(m2.getCost()) * isDescending;
                            case 3 -> (m1, m2) -> m1.getPrice().compareTo(m2.getPrice()) * isDescending;
                            default -> throw new IllegalStateException("Unexpected value: " + opt);
                        };
                    default:
                        return null;
                }
            }
        };

        return pager.promptSelection();
    }

    public void viewMedicineDetails(Medicine medicine) {
        System.out.println("Medicine details");
        System.out.println("================");
        System.out.printf("Name: %s%n", medicine.getName());
        System.out.printf("Type: %s%n", medicine.getType());
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

        System.out.print("Type: ");
        medicine.setType(MedicineType.valueOf(this.scanner.nextLine()));

        System.out.print("Brand: ");
        medicine.setBrand(this.scanner.nextLine());

        System.out.print("Unit Cost (RM): ");
        medicine.setCost(this.scanner.nextBigDecimal());

        System.out.print("Unit Price (RM): ");
        medicine.setPrice(this.scanner.nextBigDecimal());

        while (true) {
            System.out.print("Does this medicine have any substitutes? (Y/N) ");

            var sentinel = this.scanner.next().equalsIgnoreCase("Y");
            if (!sentinel)
                break;

            var substitute = this.searchMedicine();
            if (substitute == null)
                continue;

            medicine.getSubstitutes().add(substitute);
            substitute.getSubstitutesFor().add(medicine);

            System.out.printf(
                    "%s added as substitute for %s.%n",
                    substitute.getName(),
                    medicine.getName()
            );
        }

        while (true) {
            System.out.print("Does this substitute any medicine? (Y/N) ");

            var sentinel = this.scanner.next().equalsIgnoreCase("Y");
            if (!sentinel)
                break;

            var substituteFor = this.searchMedicine();
            if (substituteFor == null)
                continue;

            medicine.getSubstitutesFor().add(substituteFor);
            substituteFor.getSubstitutes().add(medicine);

            System.out.printf(
                    "%s added as substitute for %s.%n",
                    medicine.getName(),
                    substituteFor.getName()
            );
        }

        medicineController.createMedicineEntry(medicine);

        System.out.printf("Medicine `%s` added.%n", medicine.getName());
        this.scanner.next();

        return medicine;
    }

    public void deleteMedicine() {
        var medicine = this.searchMedicine();
        if (medicine == null)
            return;

        this.viewMedicineDetails(medicine);

        System.out.print("Are you sure to delete this entry? (Y/N) ");

        if (this.scanner.next().equalsIgnoreCase("Y"))
            medicineController.deleteMedicineEntry(medicine);

        System.out.printf("Medicine `%s` deleted.%n", medicine.getName());
        this.scanner.next();
    }

    public void editMedicine() {

    }
}
