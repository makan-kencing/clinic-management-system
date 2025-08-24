package edu.dsa.clinic.sorter;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Medicine;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public interface MedicineSorter {
    static Comparator<Medicine> byId() {
        return Comparator.comparing(Medicine::getId);
    }

    static Comparator<Medicine> byName(boolean ignoreCase) {
        if (ignoreCase)
            return (m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName());
        return Comparator.comparing(Medicine::getName);
    }

    static Comparator<Medicine> byType() {
        return (m1, m2) -> {
            var typeIterator1 = m1.getTypes().iterator();
            var typeIterator2 = m2.getTypes().iterator();

            while (typeIterator1.hasNext() && typeIterator2.hasNext()) {
                var type1 = typeIterator1.next();
                var type2 = typeIterator2.next();

                var comp = type1.compareTo(type2);
                if (comp != 0)
                    return comp;
            }

            return Boolean.compare(typeIterator1.hasNext(), typeIterator2.hasNext());
        };
    }

    static Comparator<Medicine> byStock() {
        return (m1, m2) ->
                Integer.compare(
                        MedicineController.getAvailableStocks(m1),
                        MedicineController.getAvailableStocks(m2)
                );
    }

    static Comparator<Medicine> byLatestStocked() {
        return (m1, m2) ->
                Objects.requireNonNullElse(
                        MedicineController.getLatestStocked(m1),
                        LocalDateTime.MIN
                ).compareTo(
                        Objects.requireNonNullElse(
                                MedicineController.getLatestStocked(m2),
                                LocalDateTime.MIN
                        )
                );
    }
}
