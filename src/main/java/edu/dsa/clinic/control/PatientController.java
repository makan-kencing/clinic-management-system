/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

/**
 *
 * @author Bincent
 */

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.ConsultationQueue;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.Tabulate;

import java.util.Comparator;
import java.util.Scanner;

public class PatientController {

    private ListInterface<Patient> patientList = new DoubleLinkedList<>();
    private ListInterface<ConsultationQueue> queueList = new SortedDoubleLinkedList<>(Comparator.comparingInt(ConsultationQueue::getQueueNo));

    public boolean createPatientRecord(Patient patient) {
        return true;
    }

    public void editPatientRecord() {

    }

    public void removePatientRecord() {

    }

    public void viewPatientList() {

    }

    public void viewPatientDetail() {

    }

    public void createQueueNumber() {

    }

    public void removeQueueNumber() {

    }

    public void viewConsultationQueue() {

    }

    public void viewSummaryReport() {

    }

    public static DoubleLinkedList<Patient> sortPatients(DoubleLinkedList<Patient> patients, String column, boolean ascending) {
        switch (column.toLowerCase()) {
            case "id":
                return (DoubleLinkedList<Patient>) patients.sorted((a, b) -> ascending
                        ? Integer.compare(a.getId(), b.getId())
                        : Integer.compare(b.getId(), a.getId()));
            case "name":
                return (DoubleLinkedList<Patient>) patients.sorted((a, b) -> ascending
                        ? a.getName().compareToIgnoreCase(b.getName())
                        : b.getName().compareToIgnoreCase(a.getName()));
            case "identification":
                return (DoubleLinkedList<Patient>) patients.sorted((a, b) -> ascending
                        ? a.getIdentification().compareToIgnoreCase(b.getIdentification())
                        : b.getIdentification().compareToIgnoreCase(a.getIdentification()));
            case "contact":
                return (DoubleLinkedList<Patient>) patients.sorted((a, b) -> ascending
                        ? a.getContactNumber().compareTo(b.getContactNumber())
                        : b.getContactNumber().compareTo(a.getContactNumber()));
            case "gender":
                return (DoubleLinkedList<Patient>) patients.sorted((a, b) -> ascending
                        ? a.getGender().compareTo(b.getGender())
                        : b.getGender().compareTo(a.getGender()));
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }
    }

    public void filterPatients(Tabulate<Patient> table, String column, String value) {
        filterPatients(table, column, value, null);
    }

    public void filterPatients(Tabulate<Patient> table, String column, String value, String gender) {
        switch (column) {
            case "name": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "identification": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getIdentification().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "contact": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        p -> p.getContactNumber().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "gender": {
                if (gender.equals("male")) {
                    table.addFilter("Male only", p -> p.getGender() == Gender.MALE);
                    table.display();
                } else if (gender.equals("female")) {
                    table.addFilter("Female only", p -> p.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }
            default:
                break;
        }
    }

    public Patient performSelect(int selectedId) {
        return Database.patientsList.findFirst(p -> p.getId() == selectedId);
    }
}
