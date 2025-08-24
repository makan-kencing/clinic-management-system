package edu.dsa.clinic.sorter;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Product;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public interface ProductSorter {
    static Comparator<Product> byId() {
        return Comparator.comparing(Product::getId);
    }

    static Comparator<Product> byName(boolean ignoreCase) {
        if (ignoreCase)
            return (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName());
        return Comparator.comparing(Product::getName);
    }

    static Comparator<Product> byMedicineName(boolean ignoreCase) {
        return (p1, p2) -> MedicineSorter.byName(ignoreCase)
                .compare(p1.getMedicine(), p2.getMedicine());
    }

    static Comparator<Product> byBrandName(boolean ignoreCase) {
        if (ignoreCase)
            return (p1, p2) -> p1.getBrand().compareToIgnoreCase(p2.getBrand());
        return Comparator.comparing(Product::getBrand);
    }

    static Comparator<Product> byUnitCost() {
        return Comparator.comparing(Product::getCost);
    }

    static Comparator<Product> byUnitPrice() {
        return Comparator.comparing(Product::getPrice);
    }

    static Comparator<Product> byStock() {
        return (p1, p2) ->
                Integer.compare(
                        MedicineController.getAvailableStocks(p1),
                        MedicineController.getAvailableStocks(p2)
                );
    }

    static Comparator<Product> byLatestStocked() {
        return (p1, p2) ->
                Objects.requireNonNullElse(
                        MedicineController.getLatestStocked(p1),
                        LocalDateTime.MIN
                ).compareTo(
                        Objects.requireNonNullElse(
                                MedicineController.getLatestStocked(p2),
                                LocalDateTime.MIN
                        )
                );
    }
}
