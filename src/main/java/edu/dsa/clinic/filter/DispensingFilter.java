package edu.dsa.clinic.filter;

import edu.dsa.clinic.entity.Dispensing;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.lambda.Filter;

public interface DispensingFilter {
    static Filter<Dispensing> byProduct(Product product) {
        return d -> StockFilter.byProduct(product).filter(d.getStock());
    }
}
