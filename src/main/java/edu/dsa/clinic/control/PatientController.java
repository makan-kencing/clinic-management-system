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
import edu.dsa.clinic.boundary.PatientUI;

import java.util.Comparator;
import java.util.Scanner;

public class PatientController {
    private ListInterface<Patient> patientList = new DoubleLinkedList<>();
    private ListInterface<ConsultationQueue> queueList = new SortedDoubleLinkedList<>(Comparator.comparingInt(ConsultationQueue::getQueueNo));
    private final PatientUI patientUI;
    private final Scanner scanner;

    public PatientController(PatientUI patientUI, Scanner scanner) {
        this.patientUI = patientUI;
        this.scanner = scanner;
    }
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

    public Patient handlePatientMenuOption(Tabulate<Patient> table, int opt, Scanner scanner) {
        Patient selectedPatient = null;

        switch (opt) {
            case 1:
                patientUI.displayTable(table);
                selectedPatient = promptSelect(scanner);
                break;
            case 2:
                filterPatients(table, scanner);
                break;
            case 3:
                table.resetFilters();
                patientUI.displayTable(table);
                break;
        }
        return selectedPatient;
    }

    public void filterPatients(Tabulate<Patient> table, Scanner scanner) {
        patientUI.showFilterMenu();
        int opt = patientUI.getFilterOption(scanner);

        switch (opt) {
            case 1: {
                String value = patientUI.getSearchValue("name", scanner);
                table.addFilter("Search \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                break;
            }
            case 2: {
                String value = patientUI.getSearchValue("identification", scanner);
                table.addFilter("Search \"" + value + "\"",
                        p -> p.getIdentification().toLowerCase().contains(value.toLowerCase()));
                break;
            }
            case 3: {
                String value = patientUI.getSearchValue("contact number", scanner);
                table.addFilter("Search \"" + value + "\"",
                        p -> p.getContactNumber().contains(value));
                break;
            }
            case 4: {
                patientUI.showGenderFilterMenu();
                int genderOpt = patientUI.getGenderOption(scanner);
                if (genderOpt == 1) {
                    table.addFilter("Male only", p -> p.getGender() == Gender.MALE);
                } else if (genderOpt == 2) {
                    table.addFilter("Female only", p -> p.getGender() == Gender.FEMALE);
                }
                break;
            }
        }
        patientUI.displayTable(table);
    }

    public Patient promptSelect(Scanner scanner) {
        Patient selectedPatient;
        do {
            int selectedId = patientUI.promptInt("Select Patient ID (0 to exit): ", scanner);

            if (selectedId == 0) {
                return null;
            }

            selectedPatient = Database.patientsList.findFirst(p -> p.getId() == selectedId);

            if (selectedPatient == null) {
                patientUI.showPatientNotFound(selectedId);
            }
        } while (selectedPatient == null);

        patientUI.showPatientSelected(selectedPatient);
        return selectedPatient;
    }
}
