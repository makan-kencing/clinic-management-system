package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.DispensaryController;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.dto.CreateMedicineDTO;
import edu.dsa.clinic.dto.CreateProductDTO;
import edu.dsa.clinic.dto.CreateStockDTO;
import edu.dsa.clinic.dto.DispensaryQueue;
import edu.dsa.clinic.dto.ProductTreatedUsage;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineAdministrationType;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.filter.MedicineFilter;
import edu.dsa.clinic.filter.MedicineTypeFilter;
import edu.dsa.clinic.filter.ProductFilter;
import edu.dsa.clinic.filter.StockFilter;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.lambda.Mapper;
import edu.dsa.clinic.lambda.Supplier;
import edu.dsa.clinic.sorter.MedicineSorter;
import edu.dsa.clinic.sorter.ProductSorter;
import edu.dsa.clinic.sorter.StockSorter;
import edu.dsa.clinic.utils.SelectTable;
import edu.dsa.clinic.utils.StringUtils;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;
import org.jline.consoleui.elements.ConfirmChoice;
import org.jline.consoleui.prompt.CheckboxResult;
import org.jline.consoleui.prompt.ConfirmResult;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.InputResult;
import org.jline.consoleui.prompt.ListResult;
import org.jline.consoleui.prompt.builder.CheckboxPromptBuilder;
import org.jline.consoleui.prompt.builder.ConfirmPromptBuilder;
import org.jline.consoleui.prompt.builder.InputValueBuilder;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.consoleui.prompt.builder.TextBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This UI is managing everything medicinal related. (e.g. {@link Medicine}, {@link Product},
 * {@link Stock})
 *
 * @author makan-kencing
 */
public class MedicineUI extends UI {
    public MedicineUI(Terminal terminal) {
        super(terminal);
    }

    @Override
    public void startMenu() throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();

        builder.createText()
                .addLine("Medicine Menu")
                .addLine("=============")
                .addPrompt()
                .createListPrompt()
                .name("option")
                .message("Choose a menu")
                .newItem("product").text("Manage Product").add()
                .newItem("medicine").text("Manage Medicine").add()
                .newItem("stock").text("Manage Stock").add()
                .newItem("dispensary").text("Show Dispensary Queue").add()
                .newItem("reports").text("Show Reports").add()
                .newItem("back").text("Back").add()
                .addPrompt();

        while (true) {
            var result = prompt.prompt(builder.build());

            switch (result.get("option").getResult()) {
                case "product":
                    this.manageProductMenu();
                    break;
                case "medicine":
                    this.manageMedicineMenu();
                    break;
                case "stock":
                    this.manageStockMenu();
                    break;
                case "dispensary":
                    this.manageDispensaryMenu();
                    break;
                case "reports":
                    this.viewSummaryReport();
                    break;
                case "back":
                    return;
            }
        }
    }

    public void manageMedicineMenu() throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("option")
                .message("What do you want to do with it?")
                .newItem("view").text("View details").add()
                .newItem("delete").text("Delete record").add()
                .newItem("edit").text("Edit record").add()
                .newItem("add_product").text("Add product").add()
                .newItem("manage_product").text("Manage product").add()
                .newItem("cancel").text("Back").add()
                .addPrompt();

        while (true) {
            var medicine = this.selectMedicine();
            if (medicine == null)
                return;

            var result = prompt.prompt(builder.build());
            switch (result.get("option")
                    .getResult()) {
                case "view":
                    this.viewMedicineDetails(medicine);
                    break;
                case "delete":
                    this.deleteMedicine(medicine);
                    break;
                case "edit":
                    this.editMedicine(medicine);
                    break;
                case "add_product":
                    var product = this.createProduct(medicine);
                    if (product != null)
                        MedicineController.addProductEntry(product);

                    break;
                case "manage_product":
                    this.manageProductMenu(medicine);
                    break;
                case "cancel":
                    break;
            }
        }
    }

    public void manageProductMenu() throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();
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

        while (true) {
            var product = this.selectProduct();
            if (product == null)
                return;

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
                    var stock = this.createStock(product);
                    if (stock != null)
                        MedicineController.addStockEntry(stock);

                    break;
                case "manage_stock":
                    this.manageStockMenu(product);
                    break;
                case "cancel":
                    break;
            }
        }
    }

    public void manageStockMenu() throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("option")
                .message("What do you want to do with it?")
                .newItem("view").text("View details").add()
                .newItem("edit").text("Edit record").add()
                .newItem("cancel").text("Back").add()
                .addPrompt();

        while (true) {
            var stock = this.selectStock();
            if (stock == null)
                return;

            var result = prompt.prompt(builder.build());
            switch (result.get("option")
                    .getResult()) {
                case "view":
                    this.viewStockDetails(stock);
                    break;
                case "edit":
                    this.editStock(stock);
                    break;
                case "cancel":
                    break;
            }
        }
    }

    public void manageProductMenu(Medicine medicine) throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();
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

        while (true) {
            var product = this.selectProduct(medicine);
            if (product == null)
                return;

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
                    var stock = this.createStock(product);
                    if (stock != null)
                        MedicineController.addStockEntry(stock);

                    break;
                case "manage_stock":
                    this.manageStockMenu(product);
                    break;
                case "cancel":
                    break;
            }
        }
    }

    public void manageStockMenu(Product product) throws IOException {
        var prompt = this.getPrompt();
        var builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("option")
                .message("What do you want to do with it?")
                .newItem("view").text("View details").add()
                .newItem("edit").text("Edit record").add()
                .newItem("cancel").text("Back").add()
                .addPrompt();

        while (true) {
            var stock = this.selectStock(product);
            if (stock == null)
                return;

            var result = prompt.prompt(builder.build());
            switch (result.get("option")
                    .getResult()) {
                case "view":
                    this.viewStockDetails(stock);
                    break;
                case "edit":
                    this.editStock(stock);
                    break;
                case "cancel":
                    break;
            }
        }
    }

    public void manageDispensaryMenu() throws IOException {
        var prompt = this.getPrompt();

        var current = DispensaryController.handleNextDispense();

        while (true) {
            if (current != null) {
                var upcoming = DispensaryController.getDispensaryQueue();

                var builder = new DispensaryQueuePromptBuilder();
                builder.createDispensaryText(current)
                        .addPrompt();
                builder.createText()
                        .addLine("")
                        .addLine("")
                        .addPrompt();
                builder.createUpcomingDispensaries(upcoming)
                        .addPrompt();
                builder.createOptionPrompt()
                        .addPrompt();

                switch (builder.promptOption(prompt)) {
                    case COMPLETED:
                        current = DispensaryController.handleNextDispense();
                        break;
                    case EXIT:
                        DispensaryController.addBackDispense(current);
                        return;
                }
            } else {
                var reader = this.getLineReader();

                reader.readLine("""
                        
                        No dispensing queued at the moment.
                        Press to continue""");
                break;
            }
        }
    }

    public void viewSummaryReport() {
        var topN = 5;
        var reader = this.getLineReader();
        var i = new AtomicInteger(0);

        while (true) {
            this.terminal.puts(InfoCmp.Capability.clear_screen);
            this.terminal.flush();

            var width = this.terminal.getWidth();

            System.out.println("=".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY", ' ', width));
            System.out.println(StringUtils.pad("MEDICINE MANAGEMENT MODULE", ' ', width));
            System.out.println(StringUtils.pad("SUMMARY OF MEDICINE PRESCRIPTIONS REPORT", ' ', width));
            System.out.println("=".repeat(width));
            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY - HIGHLY CONFIDENTIAL DOCUMENT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.printf("Generated at: %s%n", PatientUI.DATE_FORMAT.format(LocalDateTime.now()));
            System.out.println();

            var usages = MedicineController.getProductTreatedUsage();
            int finalTopN = topN;

            i.set(0);
            var bottomUsages = usages.sorted(Comparator.comparing(MedicineController.ProductCounter::quantity));
            bottomUsages.filter(_ -> i.getAndIncrement() < finalTopN);

            i.set(0);
            var topUsages = usages.filtered(_ -> i.getAndIncrement() < finalTopN);
            var table = new ProductReportTable(MedicineController.flatten(topUsages));
            table.setPageSize(9999);

            table.display();
            System.out.println();

            System.out.printf("Total Number of Product (Top %d): %d%n", topN, topUsages.size());
            System.out.printf("Total Prescribed for Consultations (Top %d): %d%n", topN, MedicineController.countDoctors(topUsages));
            System.out.println();

            System.out.println("GRAPHICAL REPRESENTATION OF SUMMARY MODULE");
            System.out.println("------------------------------------------");
            System.out.println();

            final String BLUE = "\u001B[34m";
            final String RESET = "\u001B[0m";
            final int barWidth = terminal.getWidth() - 60;

            System.out.println("+-------------------------------------+");
            System.out.printf("| Top %d Products by Prescribed Amount |%n", topN);
            System.out.println("+-------------------------------------+");
            if (usages.size() > 0) {
                var max = topUsages.getFirst().quantity();
                for (var usage : topUsages) {
                    int scaled = max == 0 ? 0 : (int) Math.round((usage.quantity() / (double) max) * barWidth);
                    System.out.printf("%-30s | %s%s%s (%d)%n",
                            StringUtils.trimEarly(usage.key().getName(), 30, "..."),
                            BLUE,
                            "█".repeat(Math.max(1, scaled)),
                            RESET,
                            usage.quantity()
                    );
                }
            }

            System.out.println();
            System.out.println("Global Highlights:");
            System.out.println("Products(s) with fewest usage: " + StringUtils.join(", ", bottomUsages.map(c -> c.key().getName() + " (" + c.quantity() + ")")));
            System.out.println("Product(s) with most usage: " + StringUtils.join(", ", topUsages.map(c -> c.key().getName() + " (" + c.quantity() + ")")));
            System.out.println();

            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("END OF THE REPORT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.println();

            System.out.printf("(0) Exit Report | Enter new Top filter (current %d): ", topN);

            var input = reader.readLine();
            if (input.equals("0")) {
                System.out.println();
                return;
            }

            try {
                var newN = Integer.parseInt(input);
                if (newN <= 0) {
                    System.out.println("TopN must be greater than 0. Keeping current value.");
                    continue;
                }
                topN = newN;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, keeping current TopN.");
            }

            this.terminal.puts(InfoCmp.Capability.clear_screen);
            this.terminal.flush();
        }
    }

    public static class ProductReportTable extends InteractiveTable<ProductTreatedUsage> {
        private int top = 0;
        private String lastDoctorString = "";
        private Product lastProduct = null;

        public ProductReportTable(ListInterface<ProductTreatedUsage> data) {
            super(new Column[]{
                    new Column("No.", 4),
                    new Column("Product Name", 30),
                    new Column("Medicine", 25),
                    new Column("Total Used", 10),
                    new Column("Consultation Count", 25),
                    new Column("Most Used By Doctor (count) [usage]", 50),
                    new Column("Symptom", 20),
                    new Column("Prescribed Count", 20),
                    new Column("Prescribed Amount", 20)
            }, data);

            this.addDefaultSorter("Top Treated Symptoms",
                    Comparator.comparing(ProductTreatedUsage::totalUsage).reversed()
                            .thenComparing(ptu -> ptu.product().getId())
                            .thenComparing(Comparator.comparing(ProductTreatedUsage::treatmentUsage).reversed())
            );
        }

        @Override
        protected Cell[] getRow(ProductTreatedUsage o) {
            var sameProduct = o.product().equals(this.lastProduct);

            if (!sameProduct) {
                if (lastProduct != null)
                    System.out.println(this.renderBorder());
                this.top++;
                this.lastDoctorString = StringUtils.join(", ", o.doctors().map(
                        d -> String.format("%s (%d) [%d]",
                                d.doctor().getName(),
                                d.doctorCount(),
                                d.doctorUsage())
                ));
            }

            try {
                return new Cell[]{
                        new Cell(sameProduct ? "" : this.top),
                        new Cell(sameProduct ? "" : StringUtils.trimEarly(o.product().getName(), 30, "...")),
                        new Cell(sameProduct ? "" : StringUtils.trimEarly(o.product().getMedicine().getName(), 25, "...")),
                        new Cell(sameProduct ? "" : o.totalUsage()),
                        new Cell(sameProduct ? "" : o.appearedCount()),
                        new Cell(sameProduct ? "" : StringUtils.trimEarly(lastDoctorString, 50, "...")),
                        new Cell(o.treatedSymptom()),
                        new Cell(o.nUniqueTreatments()),
                        new Cell(o.treatmentUsage())
                };
            } finally {
                this.lastProduct = o.product();
                if (this.lastDoctorString.length() > 40)
                    this.lastDoctorString = this.lastDoctorString.substring(39);
            }
        }

        @Override
        public void display() {
            this.top = 0;
            super.display();
        }
    }

    public @Nullable Medicine selectMedicine(
            String title,
            ListInterface<InteractiveTable.NamedFilter<Medicine>> defaultFilters,
            ListInterface<InteractiveTable.NamedSorter<Medicine>> defaultSorters
    ) {
        var table = new SelectMedicineTable(MedicineController::getAllMedicines, this.terminal);
        if (title != null)
            table.setTitle(title);
        if (defaultFilters != null && defaultFilters.size() > 0) {
            table.setDefaultFilters(defaultFilters);
            table.resetFilters();
        }
        if (defaultSorters != null && defaultSorters.size() > 0) {
            table.setDefaultSorters(defaultSorters);
            table.resetSorters();
        }
        table.updateData();

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable Medicine selectMedicine() {
        return this.selectMedicine("Selecting Medicine", null, null);
    }

    public @Nullable Product selectProduct(
            String title,
            ListInterface<InteractiveTable.NamedFilter<Product>> defaultFilters,
            ListInterface<InteractiveTable.NamedSorter<Product>> defaultSorters
    ) {
        var table = new SelectProductTable(MedicineController::getAllProducts, this.terminal);
        if (title != null)
            table.setTitle(title);
        if (defaultFilters != null && defaultFilters.size() > 0) {
            table.setDefaultFilters(defaultFilters);
            table.resetFilters();
        }
        if (defaultSorters != null && defaultSorters.size() > 0) {
            table.setDefaultSorters(defaultSorters);
            table.resetSorters();
        }
        table.updateData();

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable Product selectProduct() {
        return this.selectProduct("Selecting Product", null, null);
    }

    public @Nullable Product selectProduct(Medicine medicine) {
        var productFilters = new DoubleLinkedList<InteractiveTable.NamedFilter<Product>>();
        productFilters.add(new InteractiveTable.NamedFilter<>("Medicine is " + medicine.getName(), ProductFilter.byMedicine(selectMedicine())));

        return this.selectProduct("Selecting Product", productFilters, null);
    }


    public @Nullable Product selectProductInStock() {
        var productFilters = new DoubleLinkedList<InteractiveTable.NamedFilter<Product>>();
        productFilters.add(new InteractiveTable.NamedFilter<>("In Stock", ProductFilter.hasStock()));

        return this.selectProduct("Selecting Product In Stock", productFilters, null);
    }

    public @Nullable Stock selectStock(
            String title,
            ListInterface<InteractiveTable.NamedFilter<Stock>> defaultFilters,
            ListInterface<InteractiveTable.NamedSorter<Stock>> defaultSorters
    ) {
        var table = new SelectStockTable(MedicineController::getAllStocks, this.terminal);
        if (title != null)
            table.setTitle(title);
        if (defaultFilters != null && defaultFilters.size() > 0) {
            table.setDefaultFilters(defaultFilters);
            table.resetFilters();
        }
        if (defaultSorters != null && defaultSorters.size() > 0) {
            table.setDefaultSorters(defaultSorters);
            table.resetSorters();
        }
        table.updateData();

        try {
            return table.select();
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable Stock selectStock() {
        return this.selectStock("Selecting Stock", null, null);
    }

    public @Nullable Stock selectStock(Product product) {
        var stockFilters = new DoubleLinkedList<InteractiveTable.NamedFilter<Stock>>();
        stockFilters.add(new InteractiveTable.NamedFilter<>("Product is " + product.getName(), StockFilter.byProduct(product)));

        return this.selectStock("Selecting Stock", stockFilters, null);
    }

    // alias for `selectProduct()`
    @Deprecated
    public @Nullable Product searchProduct() {
        return this.selectProduct();
    }


    public void viewMedicineDetails(Medicine medicine) {
        var prompt = this.getPrompt();

        var builder = new MedicinePromptBuilder();

        builder.createMedicineInfoText(medicine)
                .addPrompt()
                .createInputPrompt()
                .message("")
                .addPrompt();

        try {
            prompt.prompt(builder.build());
        } catch (IOException _) {
        }
    }

    public void viewProductDetails(Product product) {
        var prompt = this.getPrompt();

        var builder = new ProductPromptBuilder();

        builder.createProductInfoText(product)
                .addPrompt()
                .createInputPrompt()
                .message("")
                .addPrompt();

        try {
            prompt.prompt(builder.build());
        } catch (IOException _) {
        }
    }

    public void viewStockDetails(Stock stock) {
        var prompt = this.getPrompt();

        var builder = new StockPromptBuilder();

        builder.createStockInfoText(stock)
                .addPrompt()
                .createInputPrompt()
                .message("")
                .addPrompt();

        try {
            prompt.prompt(builder.build());
        } catch (IOException _) {
        }
    }

    public @Nullable Medicine createMedicine() {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateMedicineDTO(
                    "",
                    new DoubleLinkedList<>()
            );
            while (true) {
                var builder = new MedicinePromptBuilder();
                builder.createText()
                        .addLine("Creating new Medicine")
                        .addPrompt();
                builder.createMedicineNamePrompt()
                        .defaultValue(dto.name())
                        .addPrompt();
                builder.createMedicineTypePrompt(MedicineTypeFilter.isIn(dto.types()))
                        .addPrompt();

                dto = builder.promptCreateMedicine(prompt);

                builder = new MedicinePromptBuilder();
                builder.createMedicineInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new MedicinePromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return null;

                return dto.create();
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();

            return null;
        }
    }

    public @Nullable Product createProduct() {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateProductDTO(
                    null,
                    null,
                    null,
                    null,
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(0),
                    Integer.MAX_VALUE
            );
            while (true) {
                var builder = new ProductPromptBuilder();

                if (dto.medicine() != null) {
                    builder.createProductInfoText(dto)
                            .addPrompt();
                    builder.createConfirmationPrompt("Change Product's medicine? (" + dto.medicine().getName() + ")")
                            .addPrompt();

                    if (builder.promptConfirmation(prompt)) {
                        var medicine = this.selectMedicine("Changing Medicine of the Product", null, null);
                        if (medicine != null)
                            dto = new CreateProductDTO(
                                    dto.name(),
                                    dto.brand(),
                                    medicine,
                                    dto.administrationType(),
                                    dto.cost(),
                                    dto.price(),
                                    dto.autoOrderThreshold()
                            );
                    }
                } else {
                    var medicine = this.selectMedicine("Select Medicine type of the new Product", null, null);
                    if (medicine != null)
                        dto = new CreateProductDTO(
                                dto.name(),
                                dto.brand(),
                                medicine,
                                dto.administrationType(),
                                dto.cost(),
                                dto.price(),
                                dto.autoOrderThreshold()
                        );
                }
                assert(dto.medicine() != null);

                builder.createText()
                        .addLine("Creating new Product")
                        .addPrompt();
                builder.createProductNamePrompt()
                        .defaultValue(dto.name())
                        .addPrompt();
                builder.createBrandPrompt()
                        .defaultValue(dto.brand())
                        .addPrompt();
                builder.createChosenMedicineText(dto.medicine())
                        .addPrompt();
                builder.createAdministrationTypePrompt()
                        .addPrompt();
                builder.createUnitCostPrompt()
                        .defaultValue(dto.cost().toString())
                        .addPrompt();
                builder.createUnitPricePrompt()
                        .defaultValue(dto.price().toString())
                        .addPrompt();
                builder.createOrderThresholdPrompt()
                        .defaultValue(dto.autoOrderThreshold() == Integer.MAX_VALUE ? "No set" : String.valueOf(dto.autoOrderThreshold()))
                        .addPrompt();

                try {
                    dto = builder.promptCreateProduct(prompt, dto.medicine());
                } catch (NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("The numbers entered were invalid");
                    continue;
                }

                builder = new ProductPromptBuilder();
                builder.createProductInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new ProductPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return null;

                return dto.create();
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();

            return null;
        }
    }

    public @Nullable Product createProduct(Medicine medicine) {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateProductDTO(
                    null,
                    null,
                    medicine,
                    null,
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(0),
                    Integer.MAX_VALUE
            );
            while (true) {
                var builder = new ProductPromptBuilder();

                builder.createText()
                        .addLine("Creating new Product")
                        .addPrompt();
                builder.createProductNamePrompt()
                        .defaultValue(dto.name())
                        .addPrompt();
                builder.createBrandPrompt()
                        .defaultValue(dto.brand())
                        .addPrompt();
                builder.createChosenMedicineText(dto.medicine())
                        .addPrompt();
                builder.createAdministrationTypePrompt()
                        .addPrompt();
                builder.createUnitCostPrompt()
                        .defaultValue(dto.cost().toString())
                        .addPrompt();
                builder.createUnitPricePrompt()
                        .defaultValue(dto.price().toString())
                        .addPrompt();
                builder.createOrderThresholdPrompt()
                        .defaultValue(dto.autoOrderThreshold() == Integer.MAX_VALUE ? "No set" : String.valueOf(dto.autoOrderThreshold()))
                        .addPrompt();

                try {
                    dto = builder.promptCreateProduct(prompt, dto.medicine());
                } catch (NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("The numbers entered were invalid");
                    continue;
                }

                builder = new ProductPromptBuilder();
                builder.createProductInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new ProductPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return null;

                return dto.create();
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();

            return null;
        }
    }

    public @Nullable Stock createStock() {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateStockDTO(
                    null,
                    1,
                    LocalDateTime.now()
            );
            while (true) {
                var builder = new StockPromptBuilder();

                if (dto.product() != null) {
                    builder.createStockInfoText(dto)
                            .addPrompt();
                    builder.createConfirmationPrompt("Change Stock's product? ([" + dto.product().getBrand() + "] " + dto.product().getName() + ")")
                            .addPrompt();

                    if (builder.promptConfirmation(prompt)) {
                        var product = this.selectProduct("Changing Stock's Product", null, null);
                        if (product != null)
                            dto = new CreateStockDTO(
                                    product,
                                    dto.stockInQuantity(),
                                    dto.stockInDate()
                            );
                    }
                } else {
                    var product = this.selectProduct("Select new Stock's Product", null, null);
                    if (product != null)
                        dto = new CreateStockDTO(
                                product,
                                dto.stockInQuantity(),
                                dto.stockInDate()
                        );
                }
                assert(dto.product() != null);

                builder = new StockPromptBuilder();
                builder.createText()
                        .addLine("Creating new Stock")
                        .addPrompt();
                builder.createChosenProductText(dto.product())
                        .addPrompt();
                builder.createStockInPrompt()
                        .defaultValue(String.valueOf(dto.stockInQuantity()))
                        .addPrompt();
                builder.createStockedAtPrompt()
                        .defaultValue(dto.stockInDate().format(StockPromptBuilder.DATETIME_FORMAT))
                        .addPrompt();

                try {
                    dto = builder.promptCreateStock(prompt, dto.product());
                } catch(NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The number entered is invalid.");
                } catch (DateTimeParseException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The time entered is invalid");
                    continue;
                }

                builder = new StockPromptBuilder();
                builder.createStockInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new StockPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return null;

                return dto.create();
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();

            return null;
        }
    }

    public @Nullable Stock createStock(Product product) {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateStockDTO(
                    product,
                    1,
                    LocalDateTime.now()
            );
            while (true) {
                var builder = new StockPromptBuilder();

                builder = new StockPromptBuilder();
                builder.createText()
                        .addLine("Creating new Stock")
                        .addPrompt();
                builder.createChosenProductText(dto.product())
                        .addPrompt();
                builder.createStockInPrompt()
                        .defaultValue(String.valueOf(dto.stockInQuantity()))
                        .addPrompt();
                builder.createStockedAtPrompt()
                        .defaultValue(dto.stockInDate().format(StockPromptBuilder.DATETIME_FORMAT))
                        .addPrompt();

                try {
                    dto = builder.promptCreateStock(prompt, dto.product());
                } catch(NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The number entered is invalid.");
                } catch (DateTimeParseException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The time entered is invalid");
                    continue;
                }

                builder = new StockPromptBuilder();
                builder.createStockInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new StockPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return null;

                return dto.create();
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();

            return null;
        }
    }

    public void editMedicine(Medicine medicine) {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateMedicineDTO(
                    medicine.getName(),
                    medicine.getTypes()
            );
            while (true) {
                var builder = new MedicinePromptBuilder();
                builder.createText()
                        .addLine("Editing Medicine #" + medicine.getId() + " - " + medicine.getName())
                        .addPrompt();
                builder.createMedicineNamePrompt()
                        .defaultValue(dto.name())
                        .addPrompt();
                builder.createMedicineTypePrompt(MedicineTypeFilter.isIn(dto.types()))
                        .addPrompt();

                dto = builder.promptCreateMedicine(prompt);

                builder = new MedicinePromptBuilder();
                builder.createMedicineInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new MedicinePromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return;

                dto.update(medicine);
                return;
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();
        }
    }

    public void editProduct(Product product) {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateProductDTO(
                    product.getName(),
                    product.getBrand(),
                    product.getMedicine(),
                    product.getAdministrationType(),
                    product.getCost(),
                    product.getPrice(),
                    product.getAutoOrderThreshold()
            );
            while (true) {
                var builder = new ProductPromptBuilder();

                builder.createProductInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Change Product's medicine? (" + dto.medicine().getName() + ")")
                        .addPrompt();

                if (builder.promptConfirmation(prompt)) {
                    var medicine = this.selectMedicine("Select Medicine type of the Product", null, null);
                    if (medicine != null)
                        dto = new CreateProductDTO(
                                dto.name(),
                                dto.brand(),
                                medicine,
                                dto.administrationType(),
                                dto.cost(),
                                dto.price(),
                                dto.autoOrderThreshold()
                        );
                }

                builder.createText()
                        .addLine("Editing Product #" + product.getId())
                        .addPrompt();
                builder.createProductNamePrompt()
                        .defaultValue(dto.name())
                        .addPrompt();
                builder.createBrandPrompt()
                        .defaultValue(dto.brand())
                        .addPrompt();
                builder.createChosenMedicineText(dto.medicine())
                        .addPrompt();
                builder.createAdministrationTypePrompt()
                        .addPrompt();
                builder.createUnitCostPrompt()
                        .defaultValue(dto.cost().toString())
                        .addPrompt();
                builder.createUnitPricePrompt()
                        .defaultValue(dto.price().toString())
                        .addPrompt();
                builder.createOrderThresholdPrompt()
                        .defaultValue(String.valueOf(dto.autoOrderThreshold()))
                        .addPrompt();

                try {
                    dto = builder.promptCreateProduct(prompt, dto.medicine());
                } catch (NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("The numbers entered were invalid");
                    continue;
                }

                builder = new ProductPromptBuilder();
                builder.createProductInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new ProductPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return;

                dto.update(product);
                return;
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();
        }
    }

    public void editStock(Stock stock) {
        var prompt = this.getPrompt();

        try {
            var dto = new CreateStockDTO(
                    stock.getProduct(),
                    stock.getStockInQuantity(),
                    stock.getStockInDate()
            );
            while (true) {
                var builder = new StockPromptBuilder();
                builder.createStockInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Update the product information? ([" + dto.product().getBrand() + "] " + dto.product().getName() + ")")
                        .addPrompt();

                if (builder.promptConfirmation(prompt)) {
                    var product = this.selectProduct("Select Stock's Product", null, null);
                    if (product != null)
                        dto = new CreateStockDTO(
                                product,
                                dto.stockInQuantity(),
                                dto.stockInDate()
                        );
                }

                builder = new StockPromptBuilder();
                builder.createText()
                        .addLine("Editing Stock #" + stock.getId())
                        .addPrompt();
                builder.createChosenProductText(dto.product())
                        .addPrompt();
                builder.createStockInPrompt()
                        .defaultValue(String.valueOf(dto.stockInQuantity()))
                        .addPrompt();
                builder.createStockedAtPrompt()
                        .defaultValue(dto.stockInDate().format(StockPromptBuilder.DATETIME_FORMAT))
                        .addPrompt();

                try {
                    dto = builder.promptCreateStock(prompt, dto.product());
                } catch(NumberFormatException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The number entered is invalid.");
                } catch (DateTimeParseException _) {
                    var reader = this.getLineReader();

                    reader.readLine("!!! The time entered is invalid");
                    continue;
                }

                builder = new StockPromptBuilder();
                builder.createStockInfoText(dto)
                        .addPrompt();
                builder.createConfirmationPrompt("Continue editing?")
                        .addPrompt();

                if (builder.promptConfirmation(prompt))
                    continue;

                builder = new StockPromptBuilder();
                builder.createConfirmationPrompt("Save?")
                        .addPrompt();

                if (!builder.promptConfirmation(prompt))
                    return;

                dto.update(stock);
                return;
            }
        } catch (IOException e) {
            var writer = this.getWriter();

            writer.println("Failed to read the inputs.");
            writer.println("Press to continue");
            writer.flush();
        }
    }

    public void deleteMedicine(Medicine medicine) {
        var prompt = this.getPrompt();

        var builder = new UIPromptBuilder();
        builder.createConfirmationPrompt("Are you sure to delete the medicine " + medicine.getName() + "?")
                .addPrompt();

        var writer = this.getWriter();
        var reader = this.getLineReader();
        try {
            if (!builder.promptConfirmation(prompt))
                return;
        } catch (IOException e) {
            writer.println("Failed to retrieve input.");
            writer.println("Press to continue");
            writer.flush();
            reader.readLine();

            return;
        }

        MedicineController.deleteMedicineEntry(medicine);

        writer.println();
        writer.println("Deleted medicine " + medicine.getName() + ".");
        writer.println("Press to continue");
        writer.flush();
        reader.readLine();
    }

    public void deleteProduct(Product product) {
        var prompt = this.getPrompt();

        var builder = new UIPromptBuilder();
        builder.createConfirmationPrompt("Are you sure to delete Product #" + product.getId() + " - [" + product.getBrand() + "] " + product.getName() + "?")
                .addPrompt();

        var writer = this.getWriter();
        var reader = this.getLineReader();
        try {
            if (!builder.promptConfirmation(prompt))
                return;
        } catch (IOException e) {
            writer.println("Failed to retrieve input.");
            writer.println("Press to continue");
            writer.flush();
            reader.readLine();

            return;
        }

        MedicineController.deleteProductEntry(product);

        writer.println();
        writer.println("Deleted product " + product.getName() + ".");
        writer.println("Press to continue");
        writer.flush();
        reader.readLine();
    }

    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build()
        ) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            var ui = new MedicineUI(terminal);
            ui.viewSummaryReport();
            // Entrypoint to the main UI.
        }
    }

    public class SelectMedicineTable extends SelectTable<Medicine> {
        public SelectMedicineTable(ListInterface<Medicine> medicines, Terminal terminal) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Name", 40),
                    new Column("Types", 40),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, medicines, terminal);
        }

        public SelectMedicineTable(Supplier<ListInterface<Medicine>> supplier, Terminal terminal) {
            this(supplier.get(), terminal);
            this.supplier = supplier;
        }

        @Override
        protected Cell[] getRow(Medicine o) {
            return new Cell[]{
                    new Cell(o.getId()),
                    new Cell(StringUtils.trimEarly(o.getName(), 40, "...")),
                    new Cell(StringUtils.trimEarly(StringUtils.join(", ", o.getTypes().map(mt -> mt.type)), 40, "...")),
                    new Cell(MedicineController.getAvailableStocks(o)),
                    new Cell(Objects.requireNonNullElse(MedicineController.getLatestStocked(o), ""))
            };
        }

        @Override
        public String renderExtraControls() {
            return "[n] New Medicine";
        }

        @Override
        protected KeyEvent handleKey(int ch) throws IOException {
            var resp = super.handleKey(ch);
            if (resp != KeyEvent.NOOP)
                return resp;

            var writer = getWriter();
            var reader = getLineReader();

            //noinspection SwitchStatementWithTooFewBranches
            switch (ch) {
                case 'n':
                    var medicine = createMedicine();
                    if (medicine == null)
                        break;

                    MedicineController.addMedicineEntry(medicine);
                    this.updateData();

                    writer.println();
                    writer.println("Added Medicine #" + medicine.getId() + " - " + medicine.getName());
                    writer.println("Press to continue");
                    writer.flush();

                    reader.readLine();
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

            var query = queryResult.getResult();

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
                    .newItem("last_stocked").text("Last Stocked").add()
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
                case "last_stocked":
                    if (this.setLastStockedFilter())
                        this.updateData();
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

        protected boolean setLastStockedFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("from")
                    .message("From (YYYY-MM-DD hh:mm:ss)")
                    .defaultValue("min")
                    .addPrompt()
                    .createInputPrompt()
                    .name("to")
                    .message("To (YYYY-MM-DD hh:mm:ss)")
                    .defaultValue("max")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var fromResult = (InputResult) result.get("from");
            var toResult = (InputResult) result.get("to");

            LocalDateTime from, to;
            try {
                from = fromResult.getResult().equals("min")
                        ? LocalDateTime.MIN
                        : LocalDateTime.parse(fromResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
                to = toResult.getResult().equals("max")
                        ? LocalDateTime.MAX
                        : LocalDateTime.parse(toResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
            } catch (DateTimeParseException _) {
                var writer = terminal.writer();
                writer.write("Invalid date format.");
                writer.flush();

                return false;
            }

            var filterName = "";
            if (from == LocalDateTime.MIN)
                filterName = "Stocked before " + to.format(StockPromptBuilder.DATETIME_FORMAT);
            else if (to == LocalDateTime.MAX)
                filterName = "Stocked after " + from.format(StockPromptBuilder.DATETIME_FORMAT);
            else
                filterName = "Stocked between " + from.format(StockPromptBuilder.DATETIME_FORMAT) + " and " + to.format(StockPromptBuilder.DATETIME_FORMAT);

            this.clearFilter(s -> s.startsWith("Stocked "));
            this.addFilter(filterName, MedicineFilter.byLatestStocked(from, to));
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
                    new Column("Name", 40),
                    new Column("Type", 40),
                    new Column("Brand", 30),
                    new Column("Unit Cost", 10),
                    new Column("Unit Price", 10),
                    new Column("In stock", 10),
                    new Column("Last stocked", 20)
            }, products, terminal);
        }

        public SelectProductTable(Supplier<ListInterface<Product>> supplier, Terminal terminal) {
            this(supplier.get(), terminal);
            this.supplier = supplier;
        }

        @Override
        protected Cell[] getRow(Product o) {
            return new Cell[]{
                    new Cell(o.getId()),
                    new Cell(StringUtils.trimEarly(o.getName(), 40, "...")),
                    new Cell(StringUtils.trimEarly(o.getMedicine().getName(), 40, "...")),
                    new Cell(StringUtils.trimEarly(o.getBrand(), 30, "...")),
                    new Cell(o.getCost()),
                    new Cell(o.getPrice()),
                    new Cell(MedicineController.getAvailableStocks(o)),
                    new Cell(Objects.requireNonNullElse(MedicineController.getLatestStocked(o), ""))
            };
        }

        @Override
        public String renderExtraControls() {
            return "[n] New Product";
        }

        @Override
        protected KeyEvent handleKey(int ch) throws IOException {
            var resp = super.handleKey(ch);
            if (resp != KeyEvent.NOOP)
                return resp;

            var reader = getLineReader();
            var writer = getWriter();

            //noinspection SwitchStatementWithTooFewBranches
            switch (ch) {
                case 'n':
                    var product = createProduct();
                    if (product == null)
                        break;

                    MedicineController.addProductEntry(product);
                    this.updateData();

                    writer.println();
                    writer.println("Added Product #" + product.getId() + " - [" + product.getBrand() + "] " + product.getName());
                    writer.println("Press to continue");
                    writer.flush();

                    reader.readLine();
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

            var query = queryResult.getResult();

            this.clearFilter(s -> s.startsWith("Searching"));
            if (query.isEmpty() || query.equals("null"))
                return;

            this.addFilter("Searching `" + query + "`", ProductFilter.byNameLike(query));
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
                    .newItem("medicine").text("Medicine Name").add()
                    .newItem("brand").text("Brand").add()
                    .newItem("stock").text("Stock Count").add()
                    .newItem("last_stocked").text("Last Stocked").add()
                    .newItem("cancel").text("Cancel").add()
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var filterResult = (ListResult) result.get("filter");

            switch (filterResult.getResult()) {
                case "medicine":
                    if (this.setMedicineNameFilter())
                        this.updateData();
                    break;
                case "brand":
                    if (this.setBrandFilter())
                        this.updateData();
                    break;
                case "stock":
                    if (this.setStockFilter())
                        this.updateData();
                    break;
                case "last_stocked":
                    if (this.setLastStockedFilter())
                        this.updateData();
                default:
                    break;
            }
        }

        protected boolean setMedicineNameFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("query")
                    .message("Medicine name")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var query = result.get("query").getResult();

            this.clearFilter(s -> s.startsWith("By Medicine name "));
            if (query == null || query.isEmpty())
                return false;

            this.addFilter("By Medicine name of \"" + query + "\"", ProductFilter.byMedicineNameLike(query));
            return true;
        }

        protected boolean setBrandFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("query")
                    .message("Brand name")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var query = result.get("query").getResult();

            this.clearFilter(s -> s.startsWith("By Brand name of "));
            if (query == null || query.isEmpty())
                return false;

            this.addFilter("By Brand name of \"" + query + "\"", ProductFilter.byBrandLike(query));
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
            this.addFilter(filterName, ProductFilter.byStockCount(from, to));
            return true;
        }

        protected boolean setLastStockedFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("from")
                    .message("From (default 0)")
                    .defaultValue("min")
                    .addPrompt()
                    .createInputPrompt()
                    .name("to")
                    .message("To (YYYY-MM-DD HH:SS)")
                    .defaultValue("max")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var fromResult = (InputResult) result.get("from");
            var toResult = (InputResult) result.get("to");

            LocalDateTime from, to;
            try {
                from = fromResult.getResult().equals("min")
                        ? LocalDateTime.MIN
                        : LocalDateTime.parse(fromResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
                to = toResult.getResult().equals("max")
                        ? LocalDateTime.MAX
                        : LocalDateTime.parse(toResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
            } catch (DateTimeParseException _) {
                var writer = terminal.writer();
                writer.write("Invalid date format.");
                writer.flush();

                return false;
            }

            var filterName = "";
            if (from == LocalDateTime.MIN)
                filterName = "Stocked before " + to.format(StockPromptBuilder.DATETIME_FORMAT);
            else if (to == LocalDateTime.MAX)
                filterName = "Stocked after " + from.format(StockPromptBuilder.DATETIME_FORMAT);
            else
                filterName = "Stocked between " + from.format(StockPromptBuilder.DATETIME_FORMAT) + " and " + to.format(StockPromptBuilder.DATETIME_FORMAT);

            this.clearFilter(s -> s.startsWith("Stocked "));
            this.addFilter(filterName, ProductFilter.byLatestStocked(from, to));
            return true;
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
                    .newItem("desc").text("Descending").add()
                    .addPrompt();

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

    public class SelectStockTable extends SelectTable<Stock> {
        public SelectStockTable(ListInterface<Stock> stocks, Terminal terminal) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Product Brand", 30),
                    new Column("Product Medicine", 40),
                    new Column("Product Name", 40),
                    new Column("In quantity", 15),
                    new Column("Stocked at", 20),
                    new Column("Quantity left", 15)
            }, stocks, terminal);
        }

        public SelectStockTable(Supplier<ListInterface<Stock>> supplier, Terminal terminal) {
            this(supplier.get(), terminal);
            this.supplier = supplier;
        }

        @Override
        protected Cell[] getRow(Stock o) {
            return new Cell[] {
                    new Cell(o.getId()),
                    new Cell(StringUtils.trimEarly(o.getProduct().getBrand(), 30, "...")),
                    new Cell(StringUtils.trimEarly(o.getProduct().getMedicine().getName(), 40, "...")),
                    new Cell(StringUtils.trimEarly(o.getProduct().getName(), 40, "...")),
                    new Cell(o.getStockInQuantity()),
                    new Cell(o.getStockInDate().format(StockPromptBuilder.DATETIME_FORMAT)),
                    new Cell(MedicineController.getStockQuantityLeft(o))
            };
        }

        @Override
        public String renderExtraControls() {
            return "[n] New Stock";
        }

        @Override
        protected KeyEvent handleKey(int ch) throws IOException {
            var resp = super.handleKey(ch);
            if (resp != KeyEvent.NOOP)
                return resp;

            var reader = getLineReader();
            var writer = getWriter();

            //noinspection SwitchStatementWithTooFewBranches
            switch (ch) {
                case 'n':
                    var stock = createStock();
                    if (stock == null)
                        break;

                    MedicineController.addStockEntry(stock);
                    this.updateData();

                    writer.println();
                    writer.println("Added Stock #" + stock.getId()
                            + " of " + stock.getStockInQuantity() + " quantity"
                            + " for [" + stock.getProduct().getBrand() + "] " + stock.getProduct().getName());
                    writer.println("Press to continue");
                    writer.flush();

                    reader.readLine();
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

            var query = queryResult.getResult();

            this.clearFilter(s -> s.startsWith("Searching"));
            if (query.isEmpty() || query.equals("null"))
                return;

            this.addFilter(
                    "Searching `" + query + "`",
                    StockFilter.byProductNameLike(query)
                            .or(StockFilter.byProductBrandLike(query))
            );
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
                    .newItem("medicine").text("Medicine").add()
                    .newItem("product").text("Product").add()
                    .newItem("in_quantity").text("In Quantity").add()
                    .newItem("quantity_left").text("Quantity Left").add()
                    .newItem("stocked_at").text("Stocked At").add()
                    .newItem("cancel").text("Cancel").add()
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var filterResult = (ListResult) result.get("filter");

            switch (filterResult.getResult()) {
                case "medicine":
                    if (this.setMedicineFilter())
                        this.updateData();
                    break;
                case "product":
                    if (this.setProductFilter())
                        this.updateData();
                    break;
                case "in_quantity":
                    if (this.setInQuantityFilter())
                        this.updateData();
                    break;
                case "quantity_left":
                    if (this.setQuantityLeftFilter())
                        this.updateData();
                    break;
                case "stocked_at":
                    if (this.setStockedAtFilter())
                        this.updateData();
                    break;
                default:
                    break;
            }
        }

        protected boolean setMedicineFilter() {
            var medicine = selectMedicine();

            this.clearFilter(s -> s.startsWith("Product is "));
            if (medicine == null)
                return false;

            this.addFilter("Product is \"" + medicine.getName() + "\"", StockFilter.byProductMedicine(medicine));
            return true;
        }

        protected boolean setProductFilter() {
            var product = selectProduct();

            this.clearFilter(s -> s.startsWith("Product is "));
            if (product == null)
                return false;

            this.addFilter("Product is \"" + product.getName() + "\"", StockFilter.byProduct(product));
            return true;
        }

        protected boolean setInQuantityFilter() throws IOException {
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
                filterName = "In quantity <=" + to;
            else if (to == Integer.MAX_VALUE)
                filterName = "In quantity >= " + from;
            else
                filterName = "In quantity between " + from + " and " + to;

            this.clearFilter(s -> s.startsWith("In quantity "));
            this.addFilter(filterName, StockFilter.byInQuantityBetween(from, to, true));
            return true;
        }

        protected boolean setQuantityLeftFilter() throws IOException {
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
                filterName = "Quantity left <=" + to;
            else if (to == Integer.MAX_VALUE)
                filterName = "Quantity left >= " + from;
            else
                filterName = "Quantity left between " + from + " and " + to;

            this.clearFilter(s -> s.startsWith("Quantity left "));
            this.addFilter(filterName, StockFilter.byStockLeftBetween(from, to, true));
            return true;
        }

        protected boolean setStockedAtFilter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createInputPrompt()
                    .name("from")
                    .message("From (YYYY-MM-DD hh:mm:ss)")
                    .defaultValue("min")
                    .addPrompt()
                    .createInputPrompt()
                    .name("to")
                    .message("To (YYYY-MM-DD hh:mm:ss)")
                    .defaultValue("max")
                    .addPrompt();

            var result = this.prompt.prompt(builder.build());

            var fromResult = (InputResult) result.get("from");
            var toResult = (InputResult) result.get("to");

            LocalDateTime from, to;
            try {
                from = fromResult.getResult().equals("min")
                        ? LocalDateTime.MIN
                        : LocalDateTime.parse(fromResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
                to = toResult.getResult().equals("max")
                        ? LocalDateTime.MAX
                        : LocalDateTime.parse(toResult.getResult(), StockPromptBuilder.DATETIME_FORMAT);
            } catch (DateTimeParseException _) {
                var writer = terminal.writer();
                writer.write("Invalid date format.");
                writer.flush();

                return false;
            }

            var filterName = "";
            if (from == LocalDateTime.MIN)
                filterName = "Stocked before " + to.format(StockPromptBuilder.DATETIME_FORMAT);
            else if (to == LocalDateTime.MAX)
                filterName = "Stocked after " + from.format(StockPromptBuilder.DATETIME_FORMAT);
            else
                filterName = "Stocked between " + from.format(StockPromptBuilder.DATETIME_FORMAT) + " and " + to.format(StockPromptBuilder.DATETIME_FORMAT);

            this.clearFilter(s -> s.startsWith("Stocked "));
            this.addFilter(filterName, StockFilter.byStockedAtBetween(from, to));
            return true;
        }

        protected void promptAddSorter() throws IOException {
            var builder = this.prompt.getPromptBuilder();
            builder.createListPrompt()
                    .name("type")
                    .message("Order by?")
                    .newItem("product").text("Product Name").add()
                    .newItem("brand").text("Product Brand").add()
                    .newItem("medicine").text("Product Medicine").add()
                    .newItem("in_stock").text("Stock In").add()
                    .newItem("stock_quantity").text("Stock Left").add()
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
            Comparator<Stock> comparator = null;

            switch (type) {
                case "product":
                    this.clearSorter(s -> s.startsWith("By product"));

                    name = "By product (" + order + ")";
                    comparator = StockSorter.byProductName(true);
                    break;
                case "brand":
                    this.clearSorter(s -> s.startsWith("By brand"));

                    name = "By brand (" + order + ")";
                    comparator = StockSorter.byProductBrandName(true);
                    break;
                case "medicine":
                    this.clearSorter(s -> s.startsWith("By medicine"));

                    name = "By medicine (" + order + ")";
                    comparator = StockSorter.byProductMedicineName(true);
                    break;
                case "in_stock":
                    this.clearSorter(s -> s.startsWith("By in quantity"));

                    name = "By in quantity (" + order + ")";
                    comparator = StockSorter.byStockIn();
                    break;
                case "stock_quantity":
                    this.clearSorter(s -> s.startsWith("By quantity left"));

                    name = "By quantity left (" + order + ")";
                    comparator = StockSorter.byStockLeft();
                    break;
                case "last_stock":
                    this.clearSorter(s -> s.startsWith("By stocked at"));

                    name = "By stocked at (" + order + ")";
                    comparator = StockSorter.byStockedAt();
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

    private static class MedicinePromptBuilder extends UIPromptBuilder {
        public final static String NAME = "name";
        public final static String TYPES = "types";

        public TextBuilder createMedicineInfoText(CreateMedicineDTO medicine) {
            return this.createText()
                    .addLine("Medicine Record")
                    .addLine("=".repeat(30))
                    .addLine("Medicine Name: " + medicine.name())
                    .addLine("Types: " + StringUtils.join(", ", medicine.types().map(mt -> mt.type)))
                    .addLine("=".repeat(30));
        }

        public TextBuilder createMedicineInfoText(Medicine medicine) {
            return this.createText()
                    .addLine("Medicine Record #" + medicine.getId())
                    .addLine("=".repeat(30))
                    .addLine("Medicine Name: " + medicine.getName())
                    .addLine("Types: " + StringUtils.join(", ", medicine.getTypes().map(mt -> mt.type)))
                    .addLine("=".repeat(30));
        }

        public InputValueBuilder createMedicineNamePrompt() {
            return this.createInputPrompt()
                    .name(NAME)
                    .message("Medicine Name: ");
        }

        public CheckboxPromptBuilder createMedicineTypePrompt(Filter<MedicineType> check) {
            return this.createEnumCheckboxPrompt(MedicineType.class, e -> e.type, check)
                    .name(TYPES)
                    .message("What is the type of medicine?");
        }

        public CheckboxPromptBuilder createMedicineTypePrompt() {
            return this.createMedicineTypePrompt(_ -> false);
        }

        public CreateMedicineDTO promptCreateMedicine(ConsolePrompt prompt) throws IOException {
            var result = prompt.prompt(this.build());

            var name = result.get(NAME).getResult();
            var types = new DoubleLinkedList<MedicineType>();

            for (var typeInput : ((CheckboxResult) result.get(TYPES)).getSelectedIds())
                types.add(MedicineType.valueOf(typeInput));

            return new CreateMedicineDTO(name, types);
        }
    }

    public static class ProductPromptBuilder extends UIPromptBuilder {
        public static final String NAME = "name";
        public static final String BRAND = "brand";
        public static final String ADMINISTRATION_TYPE = "administration_type";
        public static final String UNIT_COST = "unit_cost";
        public static final String UNIT_PRICE = "unit_price";
        public static final String AUTO_ORDER_THRESHOLD = "auto_order_threshold";

        public TextBuilder createProductInfoText(CreateProductDTO product) {
            return this.createText()
                    .addLine("Product Info")
                    .addLine("=".repeat(30))
                    .addLine("Name: " + product.name())
                    .addLine("Brand: " + product.brand())
                    .addLine("Medicine: " + product.medicine().getName())
                    .addLine("Administration Type: " + product.administrationType().type)
                    .addLine("Unit Cost: " + product.cost())
                    .addLine("Unit Price: " + product.price())
                    .addLine("Order threshold at: " + (product.autoOrderThreshold() == Integer.MAX_VALUE
                            ? "No set"
                            : product.autoOrderThreshold()))
                    .addLine("=".repeat(30));
        }

        public TextBuilder createProductInfoText(Product product) {
            return this.createText()
                    .addLine("Product Info #" + product.getId())
                    .addLine("=".repeat(30))
                    .addLine("Name: " + product.getName())
                    .addLine("Brand: " + product.getBrand())
                    .addLine("Medicine: " + product.getMedicine().getName())
                    .addLine("Administration Type: " + product.getAdministrationType().type)
                    .addLine("Unit Cost: " + product.getCost())
                    .addLine("Unit Price: " + product.getPrice())
                    .addLine("Order threshold at: " + (product.getAutoOrderThreshold() == Integer.MAX_VALUE
                            ? "No set"
                            : product.getAutoOrderThreshold()))
                    .addLine("=".repeat(30));
        }

        public InputValueBuilder createProductNamePrompt() {
            return this.createInputPrompt()
                    .name(NAME)
                    .message("Product Name: ");
        }

        public InputValueBuilder createBrandPrompt() {
            return this.createInputPrompt()
                    .name(BRAND)
                    .message("Brand: ");
        }

        public TextBuilder createChosenMedicineText(Medicine medicine) {
            return this.createText()
                    .addLine("Choose the medicine: " + medicine.getName());
        }

        public ListPromptBuilder createAdministrationTypePrompt() {
            return this.createEnumListPrompt(MedicineAdministrationType.class, e -> e.type)
                    .name(ADMINISTRATION_TYPE)
                    .message("Administration Type: ");
        }

        public InputValueBuilder createUnitCostPrompt() {
            return this.createInputPrompt()
                    .name(UNIT_COST)
                    .message("Unit Cost: ");
        }

        public InputValueBuilder createUnitPricePrompt() {
            return this.createInputPrompt()
                    .name(UNIT_PRICE)
                    .message("Unit Price: ");
        }

        public InputValueBuilder createOrderThresholdPrompt() {
            return this.createInputPrompt()
                    .name(AUTO_ORDER_THRESHOLD)
                    .message("Order Threshold: ");
        }

        public CreateProductDTO promptCreateProduct(ConsolePrompt prompt, Medicine medicine) throws IOException, NumberFormatException {
            var result = prompt.prompt(this.build());

            var name = result.get(NAME).getResult();
            var brand = result.get(BRAND).getResult();
            var administrationType = MedicineAdministrationType.valueOf(result.get(ADMINISTRATION_TYPE).getResult());
            var cost = new BigDecimal(result.get(UNIT_COST).getResult());
            var price = new BigDecimal(result.get(UNIT_PRICE).getResult());
            var autoOrderThreshold = result.get(AUTO_ORDER_THRESHOLD).getResult().equals("No set")
                    ? Integer.MAX_VALUE
                    : Integer.parseInt(result.get(AUTO_ORDER_THRESHOLD).getResult());

            return new CreateProductDTO(
                    name,
                    brand,
                    medicine,
                    administrationType,
                    cost,
                    price,
                    autoOrderThreshold
            );
        }
    }

    public static class StockPromptBuilder extends UIPromptBuilder {
        public static final String QUANTITY = "quantity";
        public static final String STOCKED_AT = "stocked_at";
        public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public TextBuilder createStockInfoText(Stock stock) {
            return this.createText()
                    .addLine("Stock Info #" + stock.getId())
                    .addLine("=".repeat(30))
                    .addLine("Product: [" + stock.getProduct().getBrand() + "] " + stock.getProduct().getName())
                    .addLine("In quantity: " + stock.getStockInQuantity())
                    .addLine("Stocked at: " + stock.getStockInDate())
                    .addLine("=".repeat(30));
        }

        public TextBuilder createStockInfoText(CreateStockDTO stock) {
            return this.createText()
                    .addLine("Stock Info")
                    .addLine("=".repeat(30))
                    .addLine("Product: [" + stock.product().getBrand() + "] " + stock.product().getName())
                    .addLine("In quantity: " + stock.stockInQuantity())
                    .addLine("Stocked at: " + stock.stockInDate())
                    .addLine("=".repeat(30));
        }

        public TextBuilder createChosenProductText(Product product) {
            return this.createText()
                    .addLine("Choose the stock product: [" + product.getBrand() + "] " + product.getName());
        }

        public InputValueBuilder createStockInPrompt() {
            return this.createInputPrompt()
                    .name(QUANTITY)
                    .message("In quantity: ");
        }

        public InputValueBuilder createStockedAtPrompt() {
            return this.createInputPrompt()
                    .name(STOCKED_AT)
                    .message("Stocked at (YYYY-MM-DD hh:mm:ss) : ");
        }

        public CreateStockDTO promptCreateStock(ConsolePrompt prompt, Product product) throws IOException, NumberFormatException, DateTimeParseException {
            var result = prompt.prompt(this.build());

            var stockInQuantity = Integer.parseInt(result.get(QUANTITY).getResult());
            var stockedAt = LocalDateTime.parse(result.get(STOCKED_AT).getResult(), DATETIME_FORMAT);

            return new CreateStockDTO(product, stockInQuantity, stockedAt);
        }
    }

    public static class DispensaryQueuePromptBuilder extends UIPromptBuilder {
        public static final String OPTION = "option";

        public enum Option {
            COMPLETED,
            EXIT
        }

        public TextBuilder createDispensaryText(DispensaryQueue queued) {
            var consultation = queued.consultation();

            var text = this.createText()
                    .addLine("=".repeat(30))
                    .addLine("Dispensing Now")
                    .addLine("For: Consultation #" + consultation.getId())
                    .addLine("Prescriptions needed:")
                    .addLine("-".repeat(30));

            int count = 0;
            for (var diagnosis : consultation.getDiagnoses())
                for (var treatment : diagnosis.getTreatments())
                    for (var prescription : treatment.getPrescriptions()) {
                        text = text
                                .addLine("Product: [" + prescription.getProduct().getBrand() + "] " + prescription.getProduct().getName())
                                .addLine("   Quantity - " + prescription.getQuantity())
                                .addLine("   Notes    - " + prescription.getNotes());
                        count++;
                    }
            text.addLine(count + " prescriptions required");
            text.addLine("=".repeat(30));

            return text;
        }

        public TextBuilder createUpcomingDispensaries(ListInterface<DispensaryQueue> upcoming) {
            var text = this.createText()
                    .addLine("Upcoming Queued (" + upcoming.size() + ")")
                    .addLine("=".repeat(30));
            for (var queued : upcoming) {
                var consultation = queued.consultation();

                for (var diagnosis : consultation.getDiagnoses())
                    for (var treatment : diagnosis.getTreatments())
                        for (var prescription : treatment.getPrescriptions())
                            text = text
                                    .addLine("Product: [" + prescription.getProduct().getBrand() + "] " + prescription.getProduct().getName()
                                            + " (" + prescription.getQuantity() + ") ")
                                    .addLine("-".repeat(30));
            }

            return text;
        }

        public ListPromptBuilder createOptionPrompt() {
            return this.createListPrompt()
                    .name(OPTION)
                    .message("")
                    .newItem(Option.COMPLETED.name()).text("Complete current dispensing").add()
                    .newItem(Option.EXIT.name()).text("Exit").add();
        }

        public Option promptOption(ConsolePrompt prompt) throws IOException {
            var result = prompt.prompt(this.build());

            return Option.valueOf(result.get(OPTION).getResult());
        }
    }

    public static class UIPromptBuilder extends PromptBuilder {
        public static final String CONFIRM = "confirm";

        public ConfirmPromptBuilder createConfirmationPrompt(String message) {
            return this.createConfirmPromp()
                    .name(CONFIRM)
                    .message(message);
        }

        public <T extends Enum<T>> ListPromptBuilder createEnumListPrompt(Class<T> clazz, Mapper<T, String> toText) {
            var list = this.createListPrompt();
            for (var e : clazz.getEnumConstants())
                list = list.newItem(e.name())
                        .text(toText.map(e))
                        .add();
            return list;
        }

        public <T extends Enum<T>> CheckboxPromptBuilder createEnumCheckboxPrompt(Class<T> clazz, Mapper<T, String> toText, Filter<T> checkCondition) {
            var checkbox = this.createCheckboxPrompt();
            for (var e: clazz.getEnumConstants())
                checkbox = checkbox.newItem(e.name())
                        .text(toText.map(e))
                        .checked(checkCondition.filter(e))
                        .add();
            return checkbox;
        }

        public boolean promptConfirmation(ConsolePrompt prompt) throws IOException {
            var result = prompt.prompt(this.build());

            var confirmation = ((ConfirmResult) result.get(CONFIRM)).getConfirmed();
            return confirmation == ConfirmChoice.ConfirmationValue.YES;
        }
    }
}
