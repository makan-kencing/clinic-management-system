package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Product;

public record ProductTreatedUsage(
        Product product,
        Integer appearedCount,
        Integer totalUsage,
        ListInterface<ProductDoctorUsage> doctors,
        String treatedSymptom,
        Integer nUniqueTreatments,
        Integer treatmentUsage
) {
    public record ProductDoctorUsage(
            Doctor doctor,
            Integer doctorCount,
            Integer doctorUsage
    ) {
    }
}
