package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;

import java.time.LocalDateTime;

public record CreateStockDTO (
        Product product,
        int stockInQuantity,
        LocalDateTime stockInDate
) {
    public Stock create() {
        return new Stock()
                .setProduct(this.product)
                .setStockInQuantity(this.stockInQuantity)
                .setStockInDate(this.stockInDate);
    }

    public void update(Stock stock) {
        if (this.product != null)
            stock.setProduct(this.product);

        if (this.stockInQuantity > 0)
            stock.setStockInQuantity(this.stockInQuantity);

        if (this.stockInDate != null)
            stock.setStockInDate(this.stockInDate);
    }
}
