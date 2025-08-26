package edu.dsa.clinic.filter;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.lambda.Filter;

public interface MedicineTypeFilter {
    static Filter<MedicineType> isIn(ListInterface<MedicineType> types) {
        return mt -> types.findFirst(Filter.is(mt)) != null;
    }
}
