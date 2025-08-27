package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;

public record CreateMedicineDTO(
        String name,
        ListInterface<MedicineType> types
) {
    public Medicine create() {
        var medicine = new Medicine();

        medicine.setName(this.name);
        medicine.getTypes().extend(this.types);

        return medicine;
    }

    public void update(Medicine medicine) {
        if (this.name != null)
            medicine.setName(this.name);

        if (this.types != null && this.types.size() > 0) {
            var types = medicine.getTypes();
            if (this.types != types) {
                types.clear();
                types.extend(this.types);
            }
        }
    }
}
