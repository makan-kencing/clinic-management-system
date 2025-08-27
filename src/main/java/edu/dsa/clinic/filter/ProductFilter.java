package edu.dsa.clinic.filter;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineAdministrationType;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Ordered;
import org.jetbrains.annotations.Range;

import java.math.BigDecimal;

public interface ProductFilter {
    static Filter<Product> byId(int id) {
        return p -> p.getId() == id;
    }

    static Filter<Product> byNameLike(String name) {
        return p -> StringFilter.like(name, true).filter(p.getName());
    }

    static Filter<Product> byBrandLike(String brand) {
        return p -> StringFilter.like(brand, true).filter(p.getName());
    }

    static Filter<Product> byBrand(String brand) {
        return p -> p.getName().equals(brand);
    }

    static Filter<Product> byMedicine(Medicine medicine) {
        return p -> p.getMedicine() == medicine;
    }

    static Filter<Product> byMedicineId(int id) {
        return p -> MedicineFilter.byId(id).filter(p.getMedicine());
    }

    static Filter<Product> byAdministrationType(MedicineAdministrationType type) {
        return p -> Filter.is(type).filter(p.getAdministrationType());
    }

    static Filter<Product> byUnitCostBetween(BigDecimal from, BigDecimal to) {
        return p -> Ordered.isBetween(p.getCost(), from, to);
    }

    static Filter<Product> byUnitCostMoreThan(BigDecimal cost) {
        return p -> Ordered.isGreaterOrEqualTo(p.getCost(), cost);
    }

    static Filter<Product> byUnitCostLessThan(BigDecimal cost) {
        return p -> Ordered.isLessOrEqualTo(p.getCost(), cost);
    }

    static Filter<Product> byUnitPriceBetween(BigDecimal from, BigDecimal to) {
        return p -> Ordered.isBetween(p.getPrice(), from, to);
    }

    static Filter<Product> byUnitPriceMoreThan(BigDecimal price) {
        return p -> Ordered.isGreaterOrEqualTo(p.getPrice(), price);
    }

    static Filter<Product> byUnitPriceLessThan(BigDecimal price) {
        return p -> Ordered.isLessOrEqualTo(p.getPrice(), price);
    }

    static Filter<Product> canSubstituteFor(Product product) {
        return p -> {
            for (var substituteFor : p.getSubstitutesFor())
                if (Filter.is(product).filter(substituteFor))
                    return true;
            return false;
        };
    }

    static Filter<Product> canBeSubstitutedWith(Product product) {
        return p -> {
            for (var substitute : p.getSubstitutes())
                if (Filter.is(product).filter(substitute))
                    return true;
            return false;
        };
    }

    static Filter<Product> byStockCount(
            @Range(from = 0, to = Integer.MAX_VALUE) int from,
            @Range(from = 1, to = Integer.MAX_VALUE) int to
    ) {
        return p -> Ordered.isBetween(
                MedicineController.getAvailableStocks(p),
                from,
                to
        );
    }

    static Filter<Product> hasStock() {
        return p -> MedicineController.getAvailableStocks(p) > 0;
    }

    static Filter<Product> hasAvailableStock() {
        return p -> {
            var inventory = p .getInventory();
            return byStockCount(
                    inventory.getMinQuantity(),
                    inventory.getMaxQuantity()
            ).filter(p);
        };
    }

    static Filter<Product> requireStockOrder() {
        return p -> byStockCount(
                0,
                p.getInventory().getAutoOrderThreshold()
        ).filter(p);
    }
}
