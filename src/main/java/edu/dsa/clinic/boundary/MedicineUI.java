package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.dto.CreateMedicineDTO;
import edu.dsa.clinic.dto.CreateProductDTO;
import edu.dsa.clinic.dto.CreateStockDTO;
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
import java.util.Scanner;
import java.util.StringJoiner;

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

    public MedicineUI(Scanner scanner) {
        super(scanner);
    }

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
                    this.manageReportsMenu();
                    break;
                case "back":
                    return;
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
                    this.manageProductStockMenu(product);
                    break;
                case "cancel":
                    break;
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

    public void manageProductStockMenu(Product product) throws IOException {
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
            var stock = this.selectProductStock(product);
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
        // TODO
    }

    public void manageReportsMenu() throws IOException {
        // TODO
    }

    public @Nullable Medicine selectMedicine(
            String title,
            ListInterface<InteractiveTable.NamedFilter<Medicine>> defaultFilters,
            ListInterface<InteractiveTable.NamedSorter<Medicine>> defaultSorters
    ) {
        var table = new SelectMedicineTable(MedicineController::getAllMedicines, this.terminal);
        if (title != null)
            table.setTitle(title);
        if (defaultFilters != null && defaultFilters.size() > 0)
            table.setDefaultFilters(defaultFilters);
        if (defaultSorters != null && defaultSorters.size() > 0)
            table.setDefaultSorters(defaultSorters);
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
        if (defaultFilters != null && defaultFilters.size() > 0)
            table.setDefaultFilters(defaultFilters);
        if (defaultSorters != null && defaultSorters.size() > 0)
            table.setDefaultSorters(defaultSorters);
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
        if (defaultFilters != null && defaultFilters.size() > 0)
            table.setDefaultFilters(defaultFilters);
        if (defaultSorters != null && defaultSorters.size() > 0)
            table.setDefaultSorters(defaultSorters);
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

    public @Nullable Stock selectProductStock(Product product) {
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
            ui.startMenu();
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

        public SelectMedicineTable(Supplier<ListInterface<Medicine>> supplier, Terminal terminal) {
            this(supplier.get(), terminal);
            this.supplier = supplier;
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

        public SelectProductTable(Supplier<ListInterface<Product>> supplier, Terminal terminal) {
            this(supplier.get(), terminal);
            this.supplier = supplier;
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

    public class SelectStockTable extends SelectTable<Stock> {
        public SelectStockTable(ListInterface<Stock> stocks, Terminal terminal) {
            super(new Column[]{
                    new Column("Id", 4),
                    new Column("Product Brand", 15),
                    new Column("Product Type", 30),
                    new Column("Product Name", 30),
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
                    new Cell(o.getProduct().getBrand()),
                    new Cell(o.getProduct().getMedicine().getName()),
                    new Cell(o.getProduct().getName()),
                    new Cell(o.getStockInQuantity()),
                    new Cell(o.getStockInDate().format(StockPromptBuilder.DATETIME_FORMAT)),
                    new Cell(o.getQuantityLeft())
            };
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

        }

        @Override
        protected void promptFilterOption() throws IOException {

        }

        @Override
        protected void promptSorterOption() throws IOException {

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
