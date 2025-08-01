package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Patient;

public class Database {
    public final static ListInterface<Patient> patients = new DoubleLinkedList<>();

    private Database() {
    }
}
