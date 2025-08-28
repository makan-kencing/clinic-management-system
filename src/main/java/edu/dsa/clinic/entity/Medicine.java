package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;

/**
 * The definition of a class of drugs or medication.
 */
public class Medicine extends IdentifiableEntity {
    private String name;
    private ListInterface<MedicineType> types = new DoubleLinkedList<>();

    public String getName() {
        return name;
    }

    public Medicine setName(String name) {
        this.name = name;
        return this;
    }

    public ListInterface<MedicineType> getTypes() {
        return types;
    }

    public Medicine setTypes(ListInterface<MedicineType> types) {
        this.types = types;
        return this;
    }

    public Medicine addType(MedicineType type) {
        this.types.add(type);
        return this;
    }
}
