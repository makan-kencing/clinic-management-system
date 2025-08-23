package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Product;

import java.time.LocalDateTime;

public class MedicineController {
    public void createMedicineEntry(Medicine medicine) {
        Database.medicineList.add(medicine);
    }

    public void createProductEntry(Product product) {
        Database.productList.add(product);
    }

    public Product deleteMedicineEntry(int id) {
        var removed = Database.productList.removeFirst(m -> m.getId() == id);
        if (removed == null)
            return null;

        for (var substitute : removed.getSubstitutes())
            substitute.getSubstitutesFor().removeFirst(m -> m.getId() == id);

        for (var substituteFor : removed.getSubstitutesFor())
            substituteFor.getSubstitutes().removeFirst(m -> m.getId() == id);

        return removed;
    }

    public Product deleteMedicineEntry(Medicine medicine) {
        return deleteMedicineEntry(medicine.getId());
    }

    public ListInterface<Medicine> getAllMedicines() {
        return Database.medicineList.clone();
    }

    public ListInterface<Product> getAllProducts() {
        return Database.productList.clone();
    }

    public static int getAvailableStocks(Product product) {
        var sum = 0;
        for (var stock : product.getStocks())
            sum += stock.getQuantityLeft();
        return sum;
    }

    public static LocalDateTime getLatestStocked(Product product) {
        var latest = LocalDateTime.MIN;
        for (var stock : product.getStocks())
            if (stock.getStockInDate().isAfter(latest))
                latest = stock.getStockInDate();
        return latest;
    }
}
