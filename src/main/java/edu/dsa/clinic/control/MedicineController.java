package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.filter.MedicineFilter;
import edu.dsa.clinic.filter.ProductFilter;
import edu.dsa.clinic.filter.StockFilter;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * Logics for managing everything medicinal related. (e.g. {@link Medicine}, {@link Product},
 * {@link Stock})
 *
 * @author makan-kencing
 */
public class MedicineController {
    public static void addMedicineEntry(Medicine medicine) {
        Database.medicineList.add(medicine);
    }

    public static void addProductEntry(Product product) {
        Database.productList.add(product);
    }

    public static void addStockEntry(Stock stock) {
        Database.stockList.add(stock);
    }

    public static Medicine deleteMedicineEntry(int id) {
        var removed = Database.medicineList.removeFirst(MedicineFilter.byId(id));
        if (removed == null)
            return null;

        return removed;
    }

    public static Medicine deleteMedicineEntry(Medicine medicine) {
        return deleteMedicineEntry(medicine.getId());
    }

    public static Product deleteProductEntry(int id) {
        var removed = Database.productList.removeFirst(ProductFilter.byId(id));
        if (removed == null)
            return null;

        for (var substitute : removed.getSubstitutes())
            substitute.getSubstitutesFor().removeFirst(m -> m.getId() == id);

        for (var substituteFor : removed.getSubstitutesFor())
            substituteFor.getSubstitutes().removeFirst(m -> m.getId() == id);

        return removed;
    }

    public static Product deleteProductEntry(Product product) {
        return deleteProductEntry(product.getId());
    }

    public static ListInterface<Medicine> getAllMedicines() {
        return Database.medicineList.clone();
    }

    public static ListInterface<Product> getAllProducts() {
        return Database.productList.clone();
    }

    public static ListInterface<Stock> getAllStocks() {
        return Database.stockList.clone();
    }

    public static ListInterface<Stock> getProductStocks(Product product) {
        return Database.stockList.filtered(StockFilter.byProduct(product));
    }

    public static int getAvailableStocks(Product product) {
        var sum = 0;
        for (var stock : getProductStocks(product))
            sum += stock.getQuantityLeft();
        return sum;
    }

    public static int getAvailableStocks(Medicine medicine) {
        var sum = 0;
        for (var product : Database.productList.filtered(ProductFilter.byMedicine(medicine)))
            sum += getAvailableStocks(product);
        return sum;
    }

    public static @Nullable LocalDateTime getLatestStocked(Product product) {
        var latest = LocalDateTime.MIN;
        for (var stock : getProductStocks(product))
            if (stock.getStockInDate().isAfter(latest))
                latest = stock.getStockInDate();

        if (latest.equals(LocalDateTime.MIN))
            return null;
        return latest;
    }

    public static @Nullable LocalDateTime getLatestStocked(Medicine medicine) {
        var latest = LocalDateTime.MIN;
        for (var product : Database.productList.filtered(ProductFilter.byMedicine(medicine))) {
            var productLatest = getLatestStocked(product);
            if (productLatest == null)
                continue;

            if (productLatest.isAfter(latest))
                latest = productLatest;
        }

        if (latest.equals(LocalDateTime.MIN))
            return null;
        return latest;
    }
}
