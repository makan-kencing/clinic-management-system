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

    public void filterPatients(Tabulate<Patient> table, Scanner scanner) {
        System.out.print("==============================");
        System.out.println("\nFilters:");
        System.out.println("(1) name");
        System.out.println("(2) identification");
        System.out.println("(3) contact number");
        System.out.println("(4) gender");
        System.out.print("==============================");
        System.out.println();
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                System.out.print("Search name by: ");
                var value = scanner.nextLine();

                System.out.println();

                table.addFilter("Search \"" + value + "\"",
                        p -> p.getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case 2: {
                System.out.print("Search identification by: ");
                var value = scanner.nextLine();

                System.out.println();

                table.addFilter("Search \"" + value + "\"",
                        p -> p.getIdentification().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();

                System.out.println();

                table.addFilter("Search \"" + value + "\"",
                        p -> p.getContactNumber().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case 4: {
                System.out.println();
                System.out.print("==============================");
                System.out.println("\nFilter gender by: ");
                System.out.println("(1) male");
                System.out.println("(2) female");
                System.out.print("==============================");
                System.out.println();
                System.out.print("Selection : ");
                var value = scanner.nextInt();
                scanner.nextLine();

                System.out.println();

                if (value == 1) {
                    table.addFilter("Male only", p -> p.getGender() == Gender.MALE);
                    table.display();
                } else if (value == 2) {
                    table.addFilter("Female only", p -> p.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }
            default:
                break;
        }
    }

    public Patient selectPatientWithFilter(Tabulate<Patient> table, int opt, Scanner scanner) {
        Patient selectedPatient = null;
        switch (opt) {
            case 1: {
                table.display();
                selectedPatient = promptSelect(scanner);
                break;
            }
            case 2: {
                filterPatients(table, scanner);
                break;
            }
            case 3: {
                table.resetFilters();
                table.display();
                break;
            }
        }
        return selectedPatient;
    }

    public Patient promptSelect(Scanner scanner) {
        Patient selectedPatient;
        do {
            System.out.print("\nEnter Patient ID (0 to exit): ");
            int selectedId = scanner.nextInt();
            scanner.nextLine();

            System.out.println();

            if (selectedId == 0) {
                System.out.println();
                System.out.print("==============================");
                System.out.println();
                return null;
            }

            selectedPatient = Database.patientsList.findFirst(p -> p.getId() == selectedId);

            if (selectedPatient == null) {
                System.out.println("Patient with ID (" + selectedId + ") not found. Please re-enter Patient ID...");
            }
        } while (selectedPatient == null);

        System.out.println("Patient (" + selectedPatient.getName() + ") with ID (" + selectedPatient.getId() + ") selected!");
        return selectedPatient;
    }
}
