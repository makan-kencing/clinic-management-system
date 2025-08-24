package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.dto.Inventory;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.filter.MedicineFilter;
import edu.dsa.clinic.sorter.MedicineSorter;
import edu.dsa.clinic.sorter.ProductSorter;
import edu.dsa.clinic.utils.SelectTable;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * This UI is managing everything medicinal related. (e.g. {@link Medicine}, {@link Product},
 * {@link Stock}, {@link Inventory})
 *
 * @author makan-kencing
 */
public class MedicineUI extends UI {
    public MedicineUI(Terminal terminal) {
        super(terminal);
    }

    public MedicineUI(Scanner scanner) {
        super(scanner);
    }

    public void startMenu() {

    }

    public @Nullable Medicine searchMedicine() {
        var medicines = MedicineController.getAllMedicines();
        var table = new MedicineTable(medicines);

        var selector = new SelectMedicineTable(this.terminal, table);
        return selector.select();
    }

    public @Nullable Product searchProduct() {
        var products = MedicineController.getAllProducts();
        var table = new ProductTable(products);

        var selector = new SelectProductTable(this.terminal, table);
        return selector.select();
    }

    public void viewMedicineDetails(Medicine medicine) {

    }

    public void viewProductDetails(Product product) {
        // TODO
        System.out.println("Medicine details");
        System.out.println("================");
        System.out.printf("Name: %s%n", product.getName());
        System.out.printf("Type: %s%n", product.getMedicine());
        System.out.printf("Brand: %s%n", product.getBrand());
        System.out.printf("Unit Cost: RM %f%n", product.getCost());
        System.out.printf("Unit Price: RM %f%n", product.getPrice());

        System.out.print("Suitable substitutes: ");
        var joiner = new StringJoiner(", ");
        for (var substitute : product.getSubstitutes())
            joiner.add(substitute.getName());
        System.out.println(joiner);
    }

    public void viewStockDetails(Stock stock) {

    }

    public @Nullable Stock searchStock() {
        return null;
    }

    public @Nullable Medicine createMedicine() {
        var medicine = new Medicine();

        System.out.print("Name: ");
        medicine.setName(this.scanner.nextLine());

        System.out.print("Types: ");
        medicine.addType(MedicineType.valueOf(this.scanner.nextLine()));

        MedicineController.createMedicineEntry(medicine);

        System.out.printf("Medicine `%s` added.%n", medicine.getName());
        this.scanner.next();

        return medicine;
    }

    public @Nullable Product createProduct() {
        return null;
    }

    public @Nullable Stock createStock() {
        return null;
    }

    public void editMedicine(Medicine medicine) {

    }

    public void editProduct(Product product) {

    }

    public void editStock(Stock stock) {

    }

    public void addStock(Product product) {

    }

    public void deleteMedicine(Medicine medicine) {

    }

    public void deleteProduct(Product product) {

    }

    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build()
        ) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            var ui = new MedicineUI(terminal);
            ui.startMenu();
            // Entrypoint to the main UI.
        }
    }

    public static class MedicineTable extends InteractiveTable<Medicine> {
        public MedicineTable(ListInterface<Medicine> medicines) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Name", 30),
                    new Column("Types", 40),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, medicines);
        }

        @Override
        protected Cell[] getRow(Medicine o) {
            var types = new StringJoiner(", ");
            for (var type : o.getTypes())
                types.add(type.type);

            return new Cell[]{
                    new Cell(o.getId()),
                    new Cell(o.getName()),
                    new Cell(types),
                    new Cell(MedicineController.getAvailableStocks(o)),
                    new Cell(Objects.requireNonNullElse(MedicineController.getLatestStocked(o), ""))
            };
        }
    }

    public static class SelectMedicineTable extends SelectTable<Medicine> {
        public SelectMedicineTable(Terminal terminal, InteractiveTable<Medicine> table) {
            super(terminal, table);
        }

        @Override
        protected void promptSearch() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("query")
                    .message("Search by")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());
            var query = result.get("query")
                    .getResult();

            this.table.clearFilter(s -> s.startsWith("Searching"));
            if (query.isEmpty() || query.equals("null"))
                return;

            this.table.addFilter("Searching `" + query + "`", MedicineFilter.byNameLike(query));
            this.table.updateData();
        }

        @Override
        protected void promptFilterOption() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("choice")
                    .message("Filter")
                    .newItem("add").text("Add").add()
                    .newItem("clear").text("Clear all").add()
                    .newItem("back").text("Back").add()
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());
            switch (result.get("choice")
                    .getResult()) {
                case "add":
                    this.promptAddFilter();
                    break;
                case "clear":
                    this.table.resetFilters();
                    this.table.updateData();
                    break;
                default:
                    break;
            }
        }

        protected void promptAddFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("filter")
                    .message("Filter by?")
                    .newItem("type").text("Medicine Type").add()
                    .newItem("stock").text("Stock Count").add()
                    .newItem("cancel").text("Cancel").add()
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());
            switch (result.get("filter")
                    .getResult()) {
                case "type":
                    if (this.setTypeFilter())
                        this.table.updateData();
                    break;
                case "stock":
                    if (this.setStockFilter())
                        this.table.updateData();
                    break;
                default:
                    break;
            }
        }

        protected boolean setTypeFilter() throws IOException {
            var types = new DoubleLinkedList<MedicineType>();

            // build checklist
            var builder = this.prompt.getPromptBuilder();
            var checkboxPrompt = builder.createCheckboxPrompt()
                    .name("types")
                    .message("Types");
            for (var type : MedicineType.values())
                checkboxPrompt = checkboxPrompt.newItem(type.name())
                        .text(type.type)
                        .check()
                        .add();
            checkboxPrompt.addPrompt();

            var result = this.prompt.prompt(builder.build());

            var typesInput = result.get("types")
                    .getResult();  // returns a string format of `["item1", "item2", ...]`
            typesInput = typesInput.substring(1, typesInput.length() - 1);

            // if not selected any
            if (typesInput.isEmpty())
                return false;

            for (var type : typesInput.split(", "))
                types.add(MedicineType.valueOf(type));

            // didnt filter any
            if (types.size() == MedicineType.values().length)
                return false;

            this.table.clearFilter(s -> s.equals("By types"));
            this.table.addFilter("By types", MedicineFilter.hasTypes(types));
            return true;
        }

        protected boolean setStockFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("from")
                    .message("From (default 0)")
                    .defaultValue("0")
                    .addPrompt()
                    .createInputPrompt()
                    .name("to")
                    .message("To (default no limit)")
                    .defaultValue("max")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            int from, to;
            try {
                from = Integer.parseInt(result.get("from").getResult());
                to = result.get("to").getResult().equals("max")
                        ? Integer.MAX_VALUE
                        : Integer.parseInt(result.get("to").getResult());
            } catch (NumberFormatException _) {
                var writer = terminal.writer();
                writer.write("Invalid quantity amount.");
                writer.flush();

                return false;
            }

            var filterName = "";
            if (from <= 0)
                filterName = "Stock <=" + to;
            else if (to == Integer.MAX_VALUE)
                filterName = "Stock >= " + from;
            else
                filterName = "Stock between " + from + " and " + to;

            this.table.clearFilter(s -> s.startsWith("Stock "));
            this.table.addFilter(filterName, MedicineFilter.byStockCount(from, to));
            return true;
        }

        @Override
        protected void promptSorterOption() throws IOException {
            var builder = prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("choice")
                    .message("Order by")
                    .newItem("add").text("Add").add()
                    .newItem("clear").text("Clear all").add()
                    .newItem("back").text("Back").add()
                    .addPrompt();

            var result = prompt.prompt(builder.build());
            switch (result.get("choice")
                    .getResult()) {
                case "add":
                    this.promptAddSorter();
                    break;
                case "clear":
                    this.table.resetSorters();
                    this.table.updateData();
                    break;
            }
        }

        protected @Nullable Medicine promptSelect() {
            var lineReader = LineReaderBuilder.builder()
                    .terminal(this.terminal)
                    .build();

            int id;
            try {
                id = Integer.parseInt(lineReader.readLine("Select Medicine ID: "));

                var selected = this.table.getData().findFirst(MedicineFilter.byId(id));
                if (selected != null)
                    return selected;
            } catch (NumberFormatException _) {
            }

            var writer = this.terminal.writer();
            writer.println("Medicine is not found");
            writer.flush();

            lineReader.readLine();

            return null;
        }

        protected void promptAddSorter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("type")
                    .message("Order by?")
                    .newItem("name").text("Name").add()
                    .newItem("type").text("Medicine Type").add()
                    .newItem("stock").text("Stock Count").add()
                    .newItem("last_stock").text("Last Stocked").add()
                    .addPrompt()
                    .createListPrompt()
                    .name("order")
                    .message("")
                    .newItem("asc").text("Ascending").add()
                    .newItem("desc").text("Descending").add()
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());
            var type = result.get("type").getResult();
            var order = result.get("order").getResult();

            String name = null;
            Comparator<Medicine> comparator = null;

            switch (type) {
                case "name":
                    this.table.clearSorter(s -> s.startsWith("By name"));

                    name = "By name (" + order + ")";
                    comparator = MedicineSorter.byName(true);
                    break;
                case "type":
                    this.table.clearSorter(s -> s.startsWith("By type"));

                    name = "By type (" + order + ")";
                    comparator = MedicineSorter.byType();
                    break;
                case "stock":
                    this.table.clearSorter(s -> s.startsWith("By stock"));

                    name = "By stock (" + order + ")";
                    comparator = MedicineSorter.byStock();
                    break;
                case "last_stock":
                    this.table.clearSorter(s -> s.startsWith("By latest stocked"));

                    name = "By latest stock (" + order + ")";
                    comparator = MedicineSorter.byLatestStocked();
                    break;
            }
            assert (comparator != null);

            var descFlag = "desc".equals(order);
            if (descFlag)
                comparator = comparator.reversed();

            this.table.addSorter(name, comparator);
            this.table.updateData();
        }
    }

    public static class ProductTable extends InteractiveTable<Product> {
        public ProductTable(ListInterface<Product> products) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Name", 30),
                    new Column("Type", 30),
                    new Column("Brand", 15),
                    new Column("Unit Cost", 10),
                    new Column("Unit Price", 10),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, products);
        }

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
                    new Cell(Objects.requireNonNullElse(MedicineController.getLatestStocked(o), ""))
            };
        }
    }

    public static class SelectProductTable extends SelectTable<Product> {
        public SelectProductTable(Terminal terminal, InteractiveTable<Product> table) {
            super(terminal, table);
        }

        @Override
        protected void promptSearch() throws IOException {

        }

        @Override
        protected void promptFilterOption() throws IOException {

        }

        @Override
        protected void promptSorterOption() throws IOException {

        }

        protected void promptAddSorter() throws IOException {
            var builder = prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("type")
                    .message("Order by?")
                    .newItem("name").text("Name").add()
                    .newItem("type").text("Medicine Type").add()
                    .newItem("brand").text("Brand").add()
                    .newItem("unit_cost").text("Unit Cost").add()
                    .newItem("unit_price").text("Unit Price").add()
                    .newItem("stock").text("Stock Count").add()
                    .newItem("last_stock").text("Last Stocked").add()
                    .addPrompt()
                    .createListPrompt()
                    .name("order")
                    .message("")
                    .newItem("asc").text("Ascending").add()
                    .newItem("desc").text("Descending").add();

            var result = prompt.prompt(builder.build());
            var type = result.get("type").getResult();
            var order = result.get("order").getResult();

            String name = null;
            Comparator<Product> comparator = null;

            switch (type) {
                case "name":
                    this.table.clearSorter(s -> s.startsWith("By name"));

                    name = "By name (" + order + ")";
                    comparator = ProductSorter.byName(true);
                    break;
                case "type":
                    this.table.clearSorter(s -> s.startsWith("By type"));

                    name = "By type (" + order + ")";
                    comparator = ProductSorter.byMedicineName(true);
                    break;
                case "brand":
                    this.table.clearSorter(s -> s.startsWith("By brand"));

                    name = "By brand (" + order + ")";
                    comparator = ProductSorter.byBrandName(true);
                    break;
                case "unit_cost":
                    this.table.clearSorter(s -> s.startsWith("By unit cost"));

                    name = "By unit cost (" + order + ")";
                    comparator = ProductSorter.byUnitCost();
                    break;
                case "unit_price":
                    this.table.clearSorter(s -> s.startsWith("By unit price"));

                    name = "By unit price (" + order + ")";
                    comparator = ProductSorter.byUnitPrice();
                    break;
                case "stock":
                    this.table.clearSorter(s -> s.startsWith("By stock"));

                    name = "By stock (" + order + ")";
                    comparator = ProductSorter.byStock();
                    break;
                case "last_stock":
                    this.table.clearSorter(s -> s.startsWith("By latest stock"));

                    name = "By latest stock (" + order + ")";
                    comparator = ProductSorter.byLatestStocked();
                    break;
            }
            assert (comparator != null);

            var descFlag = "desc".equals(order);
            if (descFlag)
                comparator = comparator.reversed();

            this.table.addSorter(name, comparator);
            this.table.updateData();
        }

        @Override
        protected @Nullable Product promptSelect() throws IOException {
            return null;
        }
    }
}
