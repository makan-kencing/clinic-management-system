package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Product;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class MedicineController {
    public static void createMedicineEntry(Medicine medicine) {
        Database.medicineList.add(medicine);
    }

    public static void createProductEntry(Product product) {
        Database.productList.add(product);
    }

    public static Product deleteMedicineEntry(int id) {
        var removed = Database.productList.removeFirst(m -> m.getId() == id);
        if (removed == null)
            return null;

        for (var substitute : removed.getSubstitutes())
            substitute.getSubstitutesFor().removeFirst(m -> m.getId() == id);

        for (var substituteFor : removed.getSubstitutesFor())
            substituteFor.getSubstitutes().removeFirst(m -> m.getId() == id);

        return removed;
    }

    public static Product deleteMedicineEntry(Medicine medicine) {
        return deleteMedicineEntry(medicine.getId());
    }

    public static ListInterface<Medicine> getAllMedicines() {
        return Database.medicineList.clone();
    }

    public static ListInterface<Product> getAllProducts() {
        return Database.productList.clone();
    }

    public static int getAvailableStocks(Product product) {
        var sum = 0;
        for (var stock : product.getStocks())
            sum += stock.getQuantityLeft();
        return sum;
    }

    public static int getAvailableStocks(Medicine medicine) {
        var sum = 0;
        for (var product: Database.productList.filtered(p -> p.getMedicine() == medicine))
            for (var stock : product.getStocks())
                sum += stock.getQuantityLeft();
        return sum;
    }

    public static @Nullable LocalDateTime getLatestStocked(Product product) {
        var latest = LocalDateTime.MIN;
        for (var stock : product.getStocks())
            if (stock.getStockInDate().isAfter(latest))
                latest = stock.getStockInDate();

        if (latest.equals(LocalDateTime.MIN))
            return null;
        return latest;
    }

    public static @Nullable LocalDateTime getLatestStocked(Medicine medicine) {
        var latest = LocalDateTime.MIN;
        for (var product : Database.productList.filtered(p -> p.getMedicine() == medicine))
            for (var stock : product.getStocks())
                if (stock.getStockInDate().isAfter(latest))
                    latest = stock.getStockInDate();

        if (latest.equals(LocalDateTime.MIN))
            return null;
        return latest;
    }
}
