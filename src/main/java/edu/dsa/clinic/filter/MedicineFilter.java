package edu.dsa.clinic.filter;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Ordered;
import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;

public interface MedicineFilter {


    static Filter<Medicine> byId(int id) {
        return m -> m.getId() == id;
    }

    static Filter<Medicine> byNameLike(String query) {
        return m -> StringFilter.like(query, true).filter(m.getName());
    }

    static Filter<Medicine> hasTypes(ListInterface<MedicineType> types) {
        return m -> {
            for (var t : m.getTypes())
                if (MedicineTypeFilter.isIn(types).filter(t))
                    return true;
            return false;
        };
    }

    static Filter<Medicine> hasType(MedicineType type) {
        return m -> {
            for (var t : m.getTypes())
                if (Filter.is(t).filter(type))
                    return true;
            return false;
        };
    }

    static Filter<Medicine> byStockCount(
            @Range(from = 0, to = Integer.MAX_VALUE) int from,
            @Range(from = 1, to = Integer.MAX_VALUE) int to
    ) {
        return m -> Ordered.isBetween(
                MedicineController.getAvailableStocks(m),
                from,
                to
        );
    }

    static Filter<Medicine> hasStock() {
        return m -> MedicineController.getAvailableStocks(m) > 0;
    }

    static Filter<Medicine> byLatestStocked(LocalDateTime from, LocalDateTime to) {
        return m -> {
            var latest = MedicineController.getLatestStocked(m);
            if (latest == null)
                latest = LocalDateTime.MIN;

            return latest.isAfter(from) && latest.isBefore(to);
        };
    }
}
