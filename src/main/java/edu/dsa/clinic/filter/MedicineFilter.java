package edu.dsa.clinic.filter;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Ordered;

import java.time.LocalDateTime;

public interface MedicineFilter {
    static Filter<Medicine> byId(int id) {
        return m -> m.getId() == id;
    }

    static Filter<Medicine> byNameLike(String query) {
        return m -> m.getName().toLowerCase()
                .contains(query.toLowerCase());
    }

    static Filter<Medicine> hasTypes(ListInterface<MedicineType> types) {
        return m -> {
            for (var type : m.getTypes())
                if (types.findFirst(t -> t == type) != null)
                    return true;
            return false;
        };
    }

    static Filter<Medicine> byStockCount(int from, int to) {
        return m -> Ordered.isBetween(
                MedicineController.getAvailableStocks(m),
                from,
                to
        );
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
