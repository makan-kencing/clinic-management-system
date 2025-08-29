package edu.dsa.clinic.sorter;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Stock;

import java.util.Comparator;

public interface StockSorter {
    static Comparator<Stock> byProductName(boolean ignoreCase) {
        return (s1, s2) -> ProductSorter.byName(ignoreCase).compare(s1.getProduct(), s2.getProduct());
    }

    static Comparator<Stock> byProductBrandName(boolean ignoreCase) {
        return (s1, s2) -> ProductSorter.byBrandName(ignoreCase).compare(s1.getProduct(), s2.getProduct());
    }

    static Comparator<Stock> byProductMedicineName(boolean ignoreCase) {
        return (s1, s2) -> ProductSorter.byMedicineName(ignoreCase).compare(s1.getProduct(), s2.getProduct());
    }

    static Comparator<Stock> byStockIn() {
        return Comparator.comparingInt(Stock::getStockInQuantity);
    }

    static Comparator<Stock> byStockLeft() {
        return Comparator.comparingInt(MedicineController::getStockQuantityLeft);
    }

    static Comparator<Stock> byStockedAt() {
        return Comparator.comparing(Stock::getStockInDate);
    }
}
