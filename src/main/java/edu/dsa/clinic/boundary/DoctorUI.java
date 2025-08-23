package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.utils.Utils;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;

import java.util.Scanner;

public class DoctorUI extends UI {
    private final DoctorController doctorController = new DoctorController();
    Doctor doctor = new Doctor();


    public DoctorUI(Scanner scanner) {
        super(scanner);

    }

    public int getDoctorMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nDoctor Menu");
            System.out.println("1. Create new Doctor");
            System.out.println("2. View Doctor List");
            System.out.println("3. Delete Doctor Record");
            System.out.println("4. Modify Doctor Information");
            System.out.println("5. Doctor Shifts Menu");
            System.out.println("0. Exit To Main Menu");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    viewDoctorsList();
                    break;
                case 3:
                    deleteDoctor(doctor);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            return choice;
        } while (choice != 0);
    }

    public int getShiftsMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nDoctor Shifts Menu");
            System.out.println("1. Assign Doctor Shift");
            System.out.println("2. Change Doctor Shift");
            System.out.println("3. View Weekly Schedule");
            System.out.println("0. Exit To Doctor Menu");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();


            return choice;
        } while (choice != 0);
    }

    //Create Doctor
    public void createDoctor() {
        Scanner sc = new Scanner(System.in);
        Doctor doctor = new Doctor();

        System.out.println("Enter Doctor Name: ");
        doctor.setName(sc.nextLine());

        System.out.println("Enter Gender (Male/Female): ");
        doctor.setGender(Gender.valueOf(sc.nextLine().toUpperCase()));

        System.out.print("Enter Contact Number: ");
        doctor.setContactNumber(sc.nextLine());

        int option;
        do {
            System.out.print("Select Specialization: ");
            System.out.println("1. Neurosurgery");
            System.out.println("2. Pediatrics");
            System.out.println("3. Ophthalmology");
            System.out.println("4. Otorhinolaryngology");
            System.out.println("5. Orthopedics");
            option = sc.nextInt();  // 4

            switch (option) {
                case 1:
                    doctor.setSpecialization(Specialization.Neurosurgery);

//                    filter(Column, "specialization", Specialization.Neurosurgery.name());
                    break;
                case 2:
                    doctor.setSpecialization(Specialization.Pediatrics);
                    break;
                case 3:
                    doctor.setSpecialization(Specialization.Ophthalmology);
                    break;
                case 4:
                    doctor.setSpecialization(Specialization.Otorhinolaryngology);
                    break;
                case 5:
                    doctor.setSpecialization(Specialization.Orthopedics);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (option < 1 || option > 5 || !Character.isDigit(option));


        Database.doctorList.add(doctor);

        System.out.println("Doctor created successfully: " + doctor);
    }

    //View Doctor
    public void viewDoctorsList() {
        int count = DoctorController.getDoctorCount();
        if (count == 0) {
            System.out.println("No doctors found.");
            return;
        }

        var doctors = this.doctorController.getDoctors();

        var table = new InteractiveTable<>(new Column[]{
                new Column("Doctor Id", Alignment.CENTER, 20),
                new Column("Name", Alignment.CENTER, 20),
                new Column("Gender", Alignment.CENTER, 20),
                new Column("Contact Number", Alignment.CENTER, 20),
                new Column("Specialization", Alignment.CENTER, 20)
        }, doctors) {
            @Override
            protected Cell[] getRow(Doctor element) {
                return new Cell[]{
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getName()),
                        new Cell(element.getGender().toString()),
                        new Cell(String.valueOf(element.getContactNumber())),
                        new Cell(element.getSpecialization().toString())
                };
            }
        };
        table.display();
    }

    public void modifyDoctor(Doctor doctor) {
        int id;
        viewDoctorsList();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Doctor Id that you want to modify: ");
        id = sc.nextInt();
        var selected = doctorController.selectDoctorByID(id);
        if (selected == null) {

        }

        System.out.println("Current name: " + doctor.getName());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            doctor.setName(newName);
            System.out.println("Name updated successfully!");
        } else {
            System.out.println("Name cannot be empty. No changes made.");
        }


    }

//    public void modifyName(Doctor doctor, Scanner sc) {
//        int id;
//        viewDoctorsList();
//        System.out.println("Enter the Doctor Id that you want to modify: ");
//        id = sc.nextInt();
//
//        if (selectDoctor == null) {
//            System.out.println("Doctor Id not found.");
//        }
//
//
//    }

    public void findDoctor(Doctor doctor) {

        Scanner sc = new Scanner(System.in);
        int id;
        viewDoctorsList();
        System.out.println("Enter the Doctor Id that you want to modify: ");
        id = sc.nextInt();
        var selectDoctor = Database.doctorList.findFirst(d -> d.getId() == id);
        if (selectDoctor == null) {
            System.out.println("Doctor Id not found.");
            return;
        }

        System.out.println("\n--- Modifying Doctor: " + selectDoctor.getName() + " ---");
        System.out.println("(Leave field empty to keep current value)");
        System.out.print("Current Name: " + doctor.getName());
        System.out.print("Enter new Name: ");
        String newName = sc.nextLine();
        if (!newName.trim().isEmpty()) {
            doctor.setName(newName);
        }
    }

    //delete Doctor
    public void deleteDoctor(Doctor doctor) {
        Scanner sc = new Scanner(System.in);
        int id;
        viewDoctorsList();
        System.out.println("Enter the Doctor Id that you want to Delete: ");
        id = sc.nextInt();

        System.out.print("Are you sure to delete this entry? (Y/N) ");

        if (this.scanner.next().equalsIgnoreCase("Y")) {

            var deletedDoctor = doctorController.deleteDoctorbyID(id);

            if (deletedDoctor == null) {
                System.out.println("Doctor Id not found.");
            } else {
                System.out.printf("Doctor `%s` deleted", deletedDoctor.getName());
            }
        }
        this.scanner.next();
    }

    //Select Doctor
    public Doctor selectDoctor() {
        Doctor selectedDoctor = null;

        var doctors = this.doctorController.getDoctors();

        var table = new InteractiveTable<>(new Column[]{
                new Column("Doctor Id", Alignment.CENTER, 20),
                new Column("Name", Alignment.CENTER, 20),
                new Column("Gender", Alignment.CENTER, 20),
                new Column("Contact Number", Alignment.CENTER, 20),
                new Column("Specialization", Alignment.CENTER, 20)
        }, doctors) {
            @Override
            protected Cell[] getRow(Doctor element) {
                return new Cell[]{
                        new Cell(String.valueOf(element.getId())),
                        new Cell(element.getName()),
                        new Cell(element.getGender().toString()),
                        new Cell(String.valueOf(element.getContactNumber())),
                        new Cell(element.getSpecialization().toString())
                };
            }
        };
        table.display();

        int opt;
        do {
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Doctor ID " +
                    "\n(2) Filter Doctor List " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("-".repeat(30));
            System.out.print("Selection : ");
            opt = this.scanner.nextInt();
            this.scanner.nextLine();

            System.out.println();

            if (opt != 4) {
                switch (opt) {
                    case 1: {
                        do {
                            table.display();
                            System.out.print("\nEnter Doctor ID (0 to exit): ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (id == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                break;
                            }
                            selectedDoctor = doctorController.selectDoctorByID(id);
                            if (selectedDoctor == null) {
                                System.out.println("Doctor with ID (" + id + ") not found. Please re-enter Doctor ID...");
                            } else {
                                System.out.println("Doctor (" + selectedDoctor.getName() + ") with ID (" + selectedDoctor.getId() + ") selected!");
                            }
                        } while (selectedDoctor == null);
                        break;
                    }
                    case 2: {
                        filterDoctor(table);
                        break;
                    }
                    case 3: {
                        table.resetFilters();
                        table.display();
                        break;
                    }
                }
            } else {
                System.out.println();
                table.display();
                break;
            }
        } while (opt > 1 && opt < 4);

        return selectedDoctor;
    }

    public void filterDoctor(InteractiveTable<Doctor> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("(1) name");
        System.out.println("(2) gender");
        System.out.println("(3) contact number");
        System.out.println("(4) Specialization");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Filter by: ");
        var opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: {
                System.out.print("Search name by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "name", value);
                break;
            }
            case 2: {
                System.out.println();
                System.out.println("-".repeat(30));
                System.out.println("Filter gender by: ");
                System.out.println("(1) male");
                System.out.println("(2) female");
                System.out.println("-".repeat(30));
                System.out.print("Selection : ");
                var value = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (value == 1) {
                    filter(table, "gender", "male");
                } else if (value == 2) {
                    filter(table, "gender", "female");
                }
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "contact", value);
            }
            case 4: {
                // show all specializations
                var availableSpecialization = Specialization.values();
                for (int i = 0; i < availableSpecialization.length; i++)
                    System.out.printf("%d. %s%n", i + 1, availableSpecialization[i]);

                System.out.print("Enter the Specialization number of the doctor that you want to find: ");
                var index = scanner.nextInt();

                // get the selected specialization
                if (!Utils.isBetween(index, 0, availableSpecialization.length)) {  // prevent indexoutofbounds
                    System.out.println("Invalid specialization number");
                    break;
                }
                var specialization = availableSpecialization[index - 1];

                System.out.println();
                filter(table, "Specialization", specialization.name());
                break;
            }
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    public void filter(InteractiveTable<Doctor> table, String column, String value) {
        switch (column) {
            case "name": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        d -> d.getName().toLowerCase().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "specialization": {
                var specialization = Specialization.valueOf(value);
                table.addFilter("Search " + column + " \"" + value + "\"",
                        d -> d.getSpecialization() == specialization);
                table.display();
                break;
            }
            case "contact": {
                table.addFilter("Search " + column + " \"" + value + "\"",
                        d -> d.getContactNumber().contains(value.toLowerCase()));
                table.display();
                break;
            }
            case "gender": {
//                var gender = Gender.valueOf(value.toUpperCase());
//                table.addFilter(StringUtils.toTitlecase(gender.name()) + " only", d -> d.getGender() == gender);
                if (value.equals("male")) {
                    table.addFilter("Male only", d -> d.getGender() == Gender.MALE);
                    table.display();
                } else if (value.equals("female")) {
                    table.addFilter("Female only", d -> d.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }
            default:
                break;
        }

    }

}

