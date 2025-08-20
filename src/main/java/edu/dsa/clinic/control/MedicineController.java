package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.utils.Filter;
import edu.dsa.clinic.utils.MultiComparator;
import edu.dsa.clinic.utils.MultiFilter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class MedicineController {
    public void createMedicineEntry(Medicine medicine) {
        Database.medicineList.add(medicine);
    }

    public boolean deleteMedicineEntry(int id) {
        var removed = Database.medicineList.removeFirst(m -> m.getId() == id);
        if (removed == null)
            return false;

        for (var substitute : removed.getSubstitutes())
            substitute.getSubstitutesFor().removeFirst(m -> m.getId() == id);

        for (var substituteFor : removed.getSubstitutesFor())
            substituteFor.getSubstitutes().removeFirst(m -> m.getId() == id);

        return true;
    }

    public boolean deleteMedicineEntry(Medicine medicine) {
        return deleteMedicineEntry(medicine.getId());
    }

    public ListInterface<Medicine> getAllMedicines(
            @Nullable ListInterface<Filter<Medicine>> filters,
            @Nullable ListInterface<Comparator<Medicine>> sorters
    ) {
        var medicines = Database.medicineList.clone();

        if (filters != null && filters.size() != 0)
            medicines.filter(new MultiFilter<>(filters.toArray()));

        if (sorters != null && sorters.size() != 0)
            medicines.sort(new MultiComparator<>(sorters.toArray()));

        return medicines;
    }

}
