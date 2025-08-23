package edu.dsa.clinic.control;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.lambda.Filter;

import java.util.Comparator;

public class InventoryController {
    public record SimilarStock(Stock stock, float similarity) {
    }

    public void createStock(Stock stock) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public ListInterface<Stock> getAllStocks(Filter<Stock> filter, Comparator<Stock> sorter) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public ListInterface<SimilarStock> findSuitableStocks(Product medicine) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deductStock(Stock stock, int quantity) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
