package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;

public class DiagnosisCounter extends Counter<String> {
    private final ListInterface<ProductCounter> productCounters = new DoubleLinkedList<>();

    public DiagnosisCounter(String key) {
        super(key);
    }

    public ListInterface<ProductCounter> productCounters() {
        return this.productCounters;
    }
}
