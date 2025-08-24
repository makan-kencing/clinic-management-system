package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.AppointmentController;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.lambda.MultiFilter;
import edu.dsa.clinic.utils.Ordered;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Arc2D;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DoctorUI extends UI {
    private final DoctorController doctorController;


    public DoctorUI(Scanner scanner) {
        super(scanner);
        this.doctorController = new DoctorController();
    }

    //Doctor Menu
    public int getDoctorMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
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

            System.out.println();

            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    viewDoctorsList();
                    break;
                case 3:
                    deleteDoctor();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

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


    //delete Doctor
    public void deleteDoctor() {
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
    public @Nullable Doctor selectDoctor() {
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


    //filter Doctor Menu
    public void filterDoctor(InteractiveTable<Doctor> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("(1) name");
        System.out.println("(2) gender");
        System.out.println("(3) contact number");
        System.out.println("(4) Specialization");
        System.out.println("(5) Availability");
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
                for (int i = 0; i < availableSpecialization.length; i++) {
                    System.out.printf("%d. %s%n", i + 1, availableSpecialization[i]);
                }
                System.out.print("Enter the Specialization number of the doctor that you want to find: ");
                var index = scanner.nextInt();

                // get the selected specialization
                if (!Ordered.isBetween(index, 0, availableSpecialization.length)) {  // prevent indexoutofbounds
                    System.out.println("Invalid specialization number");
                    break;
                }
                var specialization = availableSpecialization[index - 1];

                System.out.println();
                filter(table, "Specialization", specialization.name());
                break;
            }
            case 5: {

                System.out.println("Choose the day to filter (YYYY-MM-DD): ");
                var date = LocalDate.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                System.out.println("Choose the starting time to filter (HH:mm): ");
                var startingTime = LocalTime.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

                System.out.println("Choose the ending time to filter (HH:mm): ");
                var endingTime = LocalTime.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

                var timeRange = new Range<>(startingTime, endingTime);

                table.addFilter("Available", DoctorController.getIsAvailableFilter(date, timeRange));
            }

                break;
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    //filter  function
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
                if (value.equals("male")) {
                    table.addFilter("Male only", d -> d.getGender() == Gender.MALE);
                    table.display();
                } else if (value.equals("female")) {
                    table.addFilter("Female only", d -> d.getGender() == Gender.FEMALE);
                    table.display();
                }
                break;
            }

            case "Availability": {

            }
            default:
                break;
        }

    }

    //ShiftsMenu
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

    public void assignDoctorShift(InteractiveTable<Doctor> table, Doctor doctor) {
        int id;
        Doctor selectedDoctor;

        table.display();
        System.out.print("\nEnter Doctor ID (0 to exit): ");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        do {
            table.display();
            System.out.print("\nEnter Doctor ID (0 to exit): ");
            id = scanner.nextInt();
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
                System.out.println("Which day do you wish to assign Doctor " + selectedDoctor.getName() + "?");
                String[] days = {
                        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
                };

                for (int i = 0; i < days.length; i++) {
                    System.out.printf("%d. %s%n", i + 1, days[i]);
                }

                System.out.print("Enter the day number: ");
                var index = scanner.nextInt();

                Schedule schedule = selectedDoctor.getSchedule();

                ListInterface<Shift> dayShifts;
                try {
                    dayShifts = switch (index) {
                        case 1 -> schedule.monday();
                        case 2 -> schedule.tuesday();
                        case 3 -> schedule.wednesday();
                        case 4 -> schedule.thursday();
                        case 5 -> schedule.friday();
                        case 6 -> schedule.saturday();
                        case 7 -> schedule.sunday();
                        default -> throw new IllegalStateException("Invalid day number");
                    };
                } catch (IllegalArgumentException e) {
                    return;
                }

                var shift = createShiftFromInput();

                this.doctorController.addShift(dayShifts, shift);
                System.out.println("Assigned " + shift + " to Dr. " + doctor.getName() + " on " + index);
            }
        } while (selectedDoctor == null);

        if (doctor.getSchedule() == null) {
            doctor.setSchedule(new Schedule());
        }







    }

    //Sub Method For Assign Doctor Shift
    public @Nullable Shift createShiftFromInput() {
        try {
            System.out.print("Enter start time (hh:mm): ");
            LocalTime start = LocalTime.parse(this.scanner.nextLine().trim());

            System.out.print("Enter end time (hh:mm): ");
            LocalTime end = LocalTime.parse(this.scanner.nextLine().trim());

            // 7 - 12
            // 9 - 15

            // ideal
            // 7 - 15

            // bad
            // 7 - 12, 9 - 15

            // work, break
            // 7 - 15 (work)
            // 12 - 13 (break)

            // ideal
            // 7 - 12 (work), 12 - 13 (break), 13 - 15 (work)
            return new Shift().setTimeRange(new Range<>(start, end));

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use hh:mm (e.g. 07:00).");
        }
        return null;
    }

    public void changeDoctorShift() {

    }
    public void viewWeeklySchedule(Doctor doctor) {

            Schedule schedule = doctor.getSchedule();
            System.out.println("Weekly Schedule for Dr. " + doctor.getName());

            printDay("Monday", schedule.monday());
            printDay("Tuesday", schedule.tuesday());
            printDay("Wednesday", schedule.wednesday());
            printDay("Thursday", schedule.thursday());
            printDay("Friday", schedule.friday());
            printDay("Saturday", schedule.saturday());
            printDay("Sunday", schedule.sunday());
        }

        private void printDay(String day, ListInterface<Shift> shifts) {
            System.out.print(day + ": ");
            if (shifts == null) {
                System.out.println("No shifts assigned.");
                return;
            }
            for (int i = 0; i < shifts.size(); i++) {
                Shift shift = shifts.get(i);
                System.out.print(shift.getType() + " (" + shift.getTimeRange().from() + "-" + shift.getTimeRange().to() + ") ");
            }
            System.out.println();
        }
}

