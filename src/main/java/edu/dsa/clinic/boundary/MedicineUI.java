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
import edu.dsa.clinic.filter.ProductFilter;
import edu.dsa.clinic.sorter.MedicineSorter;
import edu.dsa.clinic.sorter.ProductSorter;
import edu.dsa.clinic.utils.SelectTable;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import org.jetbrains.annotations.Nullable;
import org.jline.consoleui.elements.ConfirmChoice;
import org.jline.consoleui.prompt.CheckboxResult;
import org.jline.consoleui.prompt.ConfirmResult;
import org.jline.consoleui.prompt.InputResult;
import org.jline.consoleui.prompt.ListResult;
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

    public void startMenu() throws IOException {
        this.manageProductMenu();
    }

    public void manageProductMenu() throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();

        while (true) {
            var product = this.searchProduct();
            if (product == null)
                return;

            // handle selected product
            builder.createListPrompt()
                    .name("option")
                    .message("What do you want to do with it?")
                    .newItem("view").text("View details").add()
                    .newItem("delete").text("Delete record").add()
                    .newItem("edit").text("Edit record").add()
                    .newItem("add_stock").text("Add stock").add()
                    .newItem("manage_stock").text("Manage stocks").add()
                    .newItem("cancel").text("Back").add()
                    .addPrompt();

            var result = prompt.prompt(builder.build());
            switch (result.get("option")
                    .getResult()) {
                case "view":
                    this.viewProductDetails(product);
                    break;
                case "delete":
                    this.deleteProduct(product);
                    break;
                case "edit":
                    this.editProduct(product);
                    break;
                case "add_stock":
                    this.addStockMenu(product);
                    break;
                case "manage_stock":
                    this.manageProductStockMenu(product);
                    break;
                case "cancel":
                    return;
            }
        }
    }

    public void viewProductMenu(Product product) {

    }

    public void addStockMenu(Product product) {

    }

    public void manageProductStockMenu(Product product) {

    }

    public void manageStockMenu() {

    }

    public @Nullable Medicine searchMedicine() {
        var medicines = MedicineController.getAllMedicines();
        var table = new SelectMedicineTable(medicines, this.terminal);

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable Product searchProduct() {
        var products = MedicineController.getAllProducts();
        var table = new SelectProductTable(products, this.terminal);

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable Product searchProductInStock() {
        var products = MedicineController.getAllProducts();
        var table = new SelectProductTable(products, this.terminal);
        table.addDefaultFilter("In stock", ProductFilter.hasStock());

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public void viewMedicineDetails(Medicine medicine) {

    }

    public void viewProductDetails(Product product) {
        // TODO
        this.terminal.puts(InfoCmp.Capability.clear_screen);

        var writer = this.getWriter();
        var reader = this.getLineReader();

        writer.println("Product details");
        writer.println("================");
        writer.printf("Name: %s%n", product.getName());
        writer.printf("Type: %s%n", product.getMedicine());
        writer.printf("Brand: %s%n", product.getBrand());
        writer.printf("Unit Cost: RM %f%n", product.getCost());
        writer.printf("Unit Price: RM %f%n", product.getPrice());

        writer.print("Suitable substitutes: ");
        var joiner = new StringJoiner(", ");
        for (var substitute : product.getSubstitutes())
            joiner.add(substitute.getName());
        writer.println(joiner);

        writer.flush();

        reader.readLine();
    }

    public void viewStockDetails(Stock stock) {

    }

    public @Nullable Stock searchStock() {
        return null;
    }

    public @Nullable Medicine createMedicine() {
        var medicine = new Medicine();

        var prompt = this.getPrompt();

        try {
            while (true) {
                var builder = prompt.getPromptBuilder();
                builder.createInputPrompt()
                        .name("name")
                        .message("Name: ")
                        .defaultValue(medicine.getName())
                        .addPrompt();

                var medicineTypeCheckbox = builder.createCheckboxPrompt()
                        .name("type")
                        .message("What medicine types?");
                for (var type : MedicineType.values())
                    medicineTypeCheckbox = medicineTypeCheckbox.newItem(type.name())
                            .text(type.type)
                            .checked(MedicineFilter.hasType(type).filter(medicine))
                            .add();
                medicineTypeCheckbox.addPrompt();

                var result = prompt.prompt(builder.build());

                var name = ((InputResult) result.get("name"));
                var typesResult = ((CheckboxResult) result.get("type"));

                medicine.setName(name.toString());
                for (var type : typesResult.getSelectedIds())
                    medicine.addType(MedicineType.valueOf(type));
                break;
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.flush();
        }

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
            ui.manageProductMenu();
            // Entrypoint to the main UI.
        }
    }

    public class SelectMedicineTable extends SelectTable<Medicine> {
        public SelectMedicineTable(ListInterface<Medicine> medicines, Terminal terminal) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Name", 30),
                    new Column("Types", 40),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, medicines, terminal);
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

        @Override
        protected KeyEvent handleKey(int ch) throws IOException {
            var resp = super.handleKey(ch);
            if (resp != KeyEvent.NOOP)
                return resp;

            var prompt = getPrompt();
            var builder = prompt.getPromptBuilder();

            //noinspection SwitchStatementWithTooFewBranches
            switch (ch) {
                case 'n':
                    var medicine = createMedicine();

                    builder.createConfirmPromp()
                            .name("confirm")
                            .message("")
                            .addPrompt();

                    var result = prompt.prompt(builder.build());

                    var confirmResult = (ConfirmResult) result.get("confirm");

                    if (confirmResult.getConfirmed() != ConfirmChoice.ConfirmationValue.YES)
                        break;

                    MedicineController.addMedicineEntry(medicine);
                    break;
            }
            return KeyEvent.NOOP;
        }

        @Override
        protected void promptSearch() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("query")
                    .message("Search by")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var queryResult = (InputResult) result.get("query");

            var query = queryResult.toString();

            this.clearFilter(s -> s.startsWith("Searching"));
            if (query.isEmpty() || query.equals("null"))
                return;

            this.addFilter("Searching `" + query + "`", MedicineFilter.byNameLike(query));
            this.updateData();
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

            var choiceResult = (ListResult) result.get("choice");

            switch (choiceResult.getResult()) {
                case "add":
                    this.promptAddFilter();
                    break;
                case "clear":
                    this.resetFilters();
                    this.updateData();
                    break;
                default:
                    break;
            }
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

            var choiceResult = (ListResult) result.get("choice");

            switch (choiceResult.getResult()) {
                case "add":
                    this.promptAddSorter();
                    break;
                case "clear":
                    this.resetSorters();
                    this.updateData();
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

            var filterResult = (ListResult) result.get("filter");

            switch (filterResult.getResult()) {
                case "type":
                    if (this.setTypeFilter())
                        this.updateData();
                    break;
                case "stock":
                    if (this.setStockFilter())
                        this.updateData();
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
                    .message("What medicine types?");
            for (var type : MedicineType.values())
                checkboxPrompt = checkboxPrompt.newItem(type.name())
                        .text(type.type)
                        .check()
                        .add();
            checkboxPrompt.addPrompt();

            var result = this.prompt.prompt(builder.build());

            var typesResult = (CheckboxResult) result.get("types");

            for (var type : typesResult.getSelectedIds())
                types.add(MedicineType.valueOf(type));

            // didnt filter any
            if (types.size() == 0 || types.size() == MedicineType.values().length)
                return false;

            this.clearFilter(s -> s.equals("By types"));
            this.addFilter("By types", MedicineFilter.hasTypes(types));
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

            var fromResult = (InputResult) result.get("from");
            var toResult = (InputResult) result.get("to");

            int from, to;
            try {
                from = Integer.parseInt(fromResult.getResult());
                to = toResult.getResult().equals("max")
                        ? Integer.MAX_VALUE
                        : Integer.parseInt(toResult.getResult());
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

            this.clearFilter(s -> s.startsWith("Stock "));
            this.addFilter(filterName, MedicineFilter.byStockCount(from, to));
            return true;
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
                    .message("Order direction by?")
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
                    this.clearSorter(s -> s.startsWith("By name"));

                    name = "By name (" + order + ")";
                    comparator = MedicineSorter.byName(true);
                    break;
                case "type":
                    this.clearSorter(s -> s.startsWith("By type"));

                    name = "By type (" + order + ")";
                    comparator = MedicineSorter.byType();
                    break;
                case "stock":
                    this.clearSorter(s -> s.startsWith("By stock"));

                    name = "By stock (" + order + ")";
                    comparator = MedicineSorter.byStock();
                    break;
                case "last_stock":
                    this.clearSorter(s -> s.startsWith("By latest stocked"));

                    name = "By latest stock (" + order + ")";
                    comparator = MedicineSorter.byLatestStocked();
                    break;
            }
            assert (comparator != null);

            var descFlag = "desc".equals(order);
            if (descFlag)
                comparator = comparator.reversed();

            this.addSorter(name, comparator);
            this.updateData();
        }
    }

    public class SelectProductTable extends SelectTable<Product> {
        public SelectProductTable(ListInterface<Product> products, Terminal terminal) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Name", 30),
                    new Column("Type", 30),
                    new Column("Brand", 15),
                    new Column("Unit Cost", 10),
                    new Column("Unit Price", 10),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, products, terminal);
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

        @Override
        protected KeyEvent handleKey(int ch) throws IOException {
            var resp = super.handleKey(ch);
            if (resp != KeyEvent.NOOP)
                return resp;

            var prompt = getPrompt();
            var builder = prompt.getPromptBuilder();

            //noinspection SwitchStatementWithTooFewBranches
            switch (ch) {
                case 'n':
                    var product = createProduct();

                    builder.createConfirmPromp()
                            .name("confirm")
                            .message("")
                            .addPrompt();

                    var result = prompt.prompt(builder.build());

                    var confirmResult = (ConfirmResult) result.get("confirm");

                    if (confirmResult.getConfirmed() != ConfirmChoice.ConfirmationValue.YES)
                        break;

                    MedicineController.addProductEntry(product);
                    break;
            }
            return KeyEvent.NOOP;
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
                    .message("Order direction by?")
                    .newItem("asc").text("Ascending").add()
                    .newItem("desc").text("Descending").add();

            var result = prompt.prompt(builder.build());

            var type = result.get("type").getResult();
            var order = result.get("order").getResult();

            String name = null;
            Comparator<Product> comparator = null;

            switch (type) {
                case "name":
                    this.clearSorter(s -> s.startsWith("By name"));

                    name = "By name (" + order + ")";
                    comparator = ProductSorter.byName(true);
                    break;
                case "type":
                    this.clearSorter(s -> s.startsWith("By type"));

                    name = "By type (" + order + ")";
                    comparator = ProductSorter.byMedicineName(true);
                    break;
                case "brand":
                    this.clearSorter(s -> s.startsWith("By brand"));

                    name = "By brand (" + order + ")";
                    comparator = ProductSorter.byBrandName(true);
                    break;
                case "unit_cost":
                    this.clearSorter(s -> s.startsWith("By unit cost"));

                    name = "By unit cost (" + order + ")";
                    comparator = ProductSorter.byUnitCost();
                    break;
                case "unit_price":
                    this.clearSorter(s -> s.startsWith("By unit price"));

                    name = "By unit price (" + order + ")";
                    comparator = ProductSorter.byUnitPrice();
                    break;
                case "stock":
                    this.clearSorter(s -> s.startsWith("By stock"));

                    name = "By stock (" + order + ")";
                    comparator = ProductSorter.byStock();
                    break;
                case "last_stock":
                    this.clearSorter(s -> s.startsWith("By latest stock"));

                    name = "By latest stock (" + order + ")";
                    comparator = ProductSorter.byLatestStocked();
                    break;
            }
            assert (comparator != null);

            var descFlag = "desc".equals(order);
            if (descFlag)
                comparator = comparator.reversed();

            this.addSorter(name, comparator);
            this.updateData();
        }
    }
}
