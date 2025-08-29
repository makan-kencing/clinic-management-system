package edu.dsa.clinic.filter;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Ordered;
import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;

public interface StockFilter {
    static Filter<Stock> byProduct(Product product) {
        return s -> s.getProduct().equals(product);
    }

    static Filter<Stock> byProductNameLike(String name) {
        return s -> ProductFilter.byNameLike(name).filter(s.getProduct());
    }

    static Filter<Stock> byProductBrandLike(String brand) {
        return s -> ProductFilter.byBrandLike(brand).filter(s.getProduct());
    }

    static Filter<Stock> byProductMedicine(Medicine medicine) {
        return s -> ProductFilter.byMedicine(medicine).filter(s.getProduct());
    }

    static Filter<Stock> byProductMedicineTypes(ListInterface<MedicineType> types) {
        return s -> ProductFilter.hasMedicineTypes(types).filter(s.getProduct());
    }

    static Filter<Stock> byInQuantityBetween(
            @Range(from = 0, to = Integer.MAX_VALUE) int from,
            @Range(from = 1, to = Integer.MAX_VALUE) int to,
            boolean inclusive
    ) {
        return s -> Ordered.isBetween(s.getStockInQuantity(), from ,to);
    }

    static Filter<Stock> byStockLeftBetween(
            @Range(from = 0, to = Integer.MAX_VALUE) int from,
            @Range(from = 1, to = Integer.MAX_VALUE) int to,
            boolean inclusive
    ) {
        return s -> Ordered.isBetween(MedicineController.getStockQuantityLeft(s), from, to);
    }

    static Filter<Stock> hasStock() {
        return s -> MedicineController.getStockQuantityLeft(s) > 0;
    }

    static Filter<Stock> byStockedAtBetween(LocalDateTime from, LocalDateTime to) {
        return s -> s.getStockInDate().isAfter(from) && s.getStockInDate().isBefore(to);
    }
}
