package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.dto.DoctorCounter;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.filter.DoctorFilter;
import edu.dsa.clinic.utils.Ordered;
import edu.dsa.clinic.utils.StringUtils;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static edu.dsa.clinic.control.DoctorController.days;


public class DoctorUI extends UI {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public DoctorUI(Scanner scanner) {
        super(scanner);
    }

    //Doctor Main Menu
    @Override
    public void startMenu() {
        int choice;
        do {
            System.out.println("-".repeat(30));
            System.out.println("Doctor Menu");
            System.out.println("-".repeat(30));
            System.out.println("1. Create new Doctor");
            System.out.println("2. View Available Doctor List");
            System.out.println("3. Delete Doctor Record");
            System.out.println("4. Modify Doctor Information");
            System.out.println("5. Doctor Shifts Menu");
            System.out.println("6. Generate Doctor Summary Report");
            System.out.println("-".repeat(30));
            System.out.println("0. Exit To Main Menu");
            System.out.print("Enter choice: ");

            while (true) {
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 0 || choice > 6) {
                        System.out.println("Invalid choice. Please enter a number between 0 and 5.");
                        System.out.print("Enter choice: ");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    System.out.print("Enter choice: ");
                }
            }
            System.out.println();

            Doctor doctor;
            switch (choice) {
                case 1:
                    createDoctor();
                    break;
                case 2:
                    selectDoctor();
                    break;
                case 3:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;

                    deleteDoctor(doctor);
                    break;
                case 4:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;

                    modifyDoctor(doctor);
                    break;
                case 5:
                    shiftsMenu();
                    break;
                case 6:
                    generateSummaryReport();

                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (true);
    }

    //Select Gender Menu (Reusable)
    public Gender selectAGender() {
        Gender newGender;
        do {
            System.out.print("-".repeat(30));
            System.out.println("\n(1) male");
            System.out.println("(2) female");
            System.out.println("-".repeat(30));
            System.out.print("Enter gender number: ");

            String input = scanner.nextLine().trim();
            int gender;
            try {
                gender = Integer.parseInt(input);
                newGender = switch (gender) {
                    case 1 -> Gender.MALE;
                    case 2 -> Gender.FEMALE;
                    default -> null;
                };
                if (newGender == null) {
                    System.out.println("Invalid option. Please enter 1 for male or 2 for female.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                newGender = null;
            }
        } while (newGender == null);
        return newGender;
    }

    //Create NEW Doctor
    public void createDoctor() {
        Doctor doctor = new Doctor();
        do{
            System.out.print("Enter Doctor Name: ");
            var name = this.scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
                continue;
            }

            if (DoctorController.selectDoctorByName(name) != null) {
                System.out.println("Name taken");
                continue;
            }

            doctor.setName(name);
            break;
        }while(true);


        doctor.setGender(selectAGender());

        String number;
        do {
            System.out.print("\nEnter Contact Number: ");
            number = scanner.nextLine().trim();

            if (number.isEmpty()) {
                System.out.println("Contact number cannot be empty. Try again.");
            } else if (!number.matches("^\\+60\\\\d{9,15}")) {
                System.out.println("Contact number must follow format. E.g +60123456789");
                continue;
            } else {
                    doctor.setContactNumber(number);
                    System.out.println("New contact number updated successfully!");
                    break;
                }


        } while (true);



        int option;
        do {
            System.out.println("\nSpecialization: ");
            System.out.println("1. Neurosurgery");
            System.out.println("2. Pediatrics");
            System.out.println("3. Ophthalmology");
            System.out.println("4. Otorhinolaryngology");
            System.out.println("5. Orthopedics");
            System.out.print("\nSelect the doctor's Specialization: ");
            option = this.scanner.nextInt();  // 4
            this.scanner.nextLine();


            var specialization = switch (option) {
                case 1 -> Specialization.Neurosurgery;
                case 2 -> Specialization.Pediatrics;
                case 3 -> Specialization.Ophthalmology;
                case 4 -> Specialization.Otorhinolaryngology;
                case 5 -> Specialization.Orthopedics;
                default -> null;
            };

            if (specialization == null) {
                System.out.println("Invalid option. Try again.");
                continue;
            }

            doctor.setSpecialization(specialization);
            break;
        } while (true);

        DoctorController.addDoctorRecord(doctor);

        System.out.println("Doctor created successfully ");
    }

    //delete Doctor
    public void deleteDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("No doctor selected to delete.");
            return;
        }

        System.out.print("Are you sure to delete this entry? (Y/N) ");
        String input = this.scanner.nextLine().trim();

        if (!input.equalsIgnoreCase("Y")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        var deletedDoctor = DoctorController.deleteDoctorByID(doctor.getId());

        if (deletedDoctor == null) {
            System.out.println("Doctor ID not found or already deleted.");
        } else {
            System.out.printf("Doctor `%s` deleted.%n", deletedDoctor.getName());
        }
        this.scanner.nextLine();
    }

    //modifyDoctor
    public void modifyDoctor(Doctor doctor) {
        System.out.println("-".repeat(30));
        System.out.println("Doctor Modify Menu:");
        System.out.println("-".repeat(30));
        System.out.println("(1) name");
        System.out.println("(2) gender");
        System.out.println("(3) contact number");
        System.out.println("(4) Specialization");
        System.out.println("(0) exit");
        System.out.println("-".repeat(30));
        System.out.print("Enter the number of the info that you want to modify: ");

        int opt;
        while (true) {
            try {
                opt = Integer.parseInt(scanner.nextLine());
                if (opt < 0 || opt > 4) {
                    System.out.println("Invalid option. Please select a number between 0 and 4.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        switch (opt) {
            case 1: {
                System.out.println("Current name: " + doctor.getName());
                while (true) {
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty. No changes made.");
                        continue;
                    }
                    if (DoctorController.selectDoctorByName(name) != null) {
                        System.out.println("Name taken. Please enter a different name.");
                        continue;
                    }
                    doctor.setName(name);
                    System.out.println("Name updated successfully!");
                    break;
                }
                break;
            }
            case 2: {
                doctor.setGender(selectAGender());
                System.out.println("Gender updated successfully!");
                break;
            }
            case 3: {
                System.out.println("Current contact number: " + doctor.getContactNumber());
                while (true) {
                    System.out.print("Enter new contact number: ");
                    String newNumber = scanner.nextLine().trim();
                    if (newNumber.isEmpty()) {
                        System.out.println("Contact number cannot be empty. Try again.");
                        continue;
                    }
                    if (!newNumber.matches("^\\+60\\\\d{9,15}")) {
                        System.out.println("Contact number must follow format. E.g +60123456789");
                        continue;
                    }
                    doctor.setContactNumber(newNumber);
                    System.out.println("New contact number updated successfully!");
                    break;
                }
                break;
            }
            case 4: {
                System.out.println("Current Doctor Specialization: " + doctor.getSpecialization());
                while (true) {
                    System.out.println("-".repeat(30));
                    System.out.println("Enter new specialization you wish to choose:");
                    System.out.println("1. Neurosurgery");
                    System.out.println("2. Pediatrics");
                    System.out.println("3. Ophthalmology");
                    System.out.println("4. Otorhinolaryngology");
                    System.out.println("5. Orthopedics");
                    System.out.print("Selection: ");
                    String input = scanner.nextLine().trim();
                    int option;
                    try {
                        option = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number (1-5).");
                        continue;
                    }
                    var specialization = switch (option) {
                        case 1 -> Specialization.Neurosurgery;
                        case 2 -> Specialization.Pediatrics;
                        case 3 -> Specialization.Ophthalmology;
                        case 4 -> Specialization.Otorhinolaryngology;
                        case 5 -> Specialization.Orthopedics;
                        default -> null;
                    };
                    if (specialization == null) {
                        System.out.println("Invalid option. Try again.");
                        continue;
                    }
                    doctor.setSpecialization(specialization);
                    System.out.println("Specialization updated successfully!");
                    break;
                }
                break;
            }
            default:
                System.out.println("Exiting modify menu.");
                break;
        }
    }



    //Select Doctor
    public @Nullable Doctor selectDoctor() {
        Doctor selectedDoctor = null;
        var doctors = DoctorController.getDoctors();
        var table = new DoctorTable(doctors);

        int opt;
        do {
            table.display();
            System.out.println("-".repeat(30));
            System.out.println("(1) Select Doctor ID " +
                    "\n(2) Filter Doctor List " +
                    "\n(3) Reset Filters " +
                    "\n(4) Exit");
            System.out.println("-".repeat(30));

            // Validate menu input
            while (true) {
                System.out.print("Selection : ");
                String input = scanner.nextLine().trim();
                try {
                    opt = Integer.parseInt(input);
                    if (opt < 1 || opt > 4) {
                        System.out.println("Invalid option. Please select a number between 1 and 4.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
            System.out.println();

            if (opt != 4) {
                switch (opt) {
                    case 1: {
                        do {
                            System.out.print("\nEnter Doctor ID (0 to exit): ");
                            String idInput = scanner.nextLine().trim();
                            int id;
                            try {
                                id = Integer.parseInt(idInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid Doctor ID.");
                                continue;
                            }
                            System.out.println();

                            if (id == 0) {
                                System.out.println("-".repeat(30));
                                System.out.println();
                                break;
                            }
                            selectedDoctor = DoctorController.selectDoctorByID(id);
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
                        break;
                    }
                }
            } else {
                System.out.println("Exiting doctor selection menu.");
                break;
            }

        } while (opt > 1 && opt < 4);
        return selectedDoctor;
    }

    //filter doctor menu
    public void filterDoctor(InteractiveTable<Doctor> table) {
        System.out.println("-".repeat(30));
        System.out.println("Filters:");
        System.out.println("-".repeat(30));
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
                    filter(table, "gender", Gender.MALE.name());
                } else if (value == 2) {
                    filter(table, "gender", Gender.FEMALE.name());
                }
                break;
            }
            case 3: {
                System.out.print("Search contact number by: ");
                var value = scanner.nextLine();
                System.out.println();
                filter(table, "contact", value);
                break;
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
                filter(table, "specialization", specialization.name());
                break;


            }
            case 5:
                System.out.println("Choose the day to filter (YYYY-MM-DD): ");
                var date = LocalDate.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                System.out.println("Choose the starting time to filter (HH:mm): ");
                var startingTime = LocalTime.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

                System.out.println("Choose the ending time to filter (HH:mm): ");
                var endingTime = LocalTime.parse(this.scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

                var timeRange = new Range<>(startingTime, endingTime);

                table.addFilter("Available", DoctorFilter.isAvailable(date, timeRange));
                break;
            default:
                System.out.println();
                table.display();
                break;
        }
    }

    //filter doctor function
    public void filter(InteractiveTable<Doctor> table, String column, String value) {
        switch (column) {
            case "name":
                table.addFilter(
                        "Search " + column + " \"" + value + "\"",
                        DoctorFilter.byNameLike(value)
                );
                break;
            case "specialization":
                var specialization = Specialization.valueOf(value);

                table.addFilter(
                        "Search " + column + " \"" + value + "\"",
                        DoctorFilter.bySpecialization(specialization)
                );
                break;
            case "contact":
                table.addFilter(
                        "Search " + column + " \"" + value + "\"",
                        DoctorFilter.byContactNumberLike(value)
                );
                break;
            case "gender":
                var gender = Gender.valueOf(value);

                table.addFilter(gender.name() + " only", DoctorFilter.byGender(gender));
                break;
            default:
                break;
        }

    }

    //ShiftsMenu
    public void shiftsMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("-".repeat(30));
            System.out.println("Doctor Shifts Menu");
            System.out.println("-".repeat(30));
            System.out.println("1. Assign Doctor Shift");
            System.out.println("2. Add Doctor Break Time");
            System.out.println("3. View Doctor Schedule");
            System.out.println("4. View Doctor Availability Schedule ");
            System.out.println("5. View Doctor Timetable ");
            System.out.println("-".repeat(30));
            System.out.println("0. Exit To Doctor Menu");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            Doctor doctor;
            switch (choice) {
                case 1:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;
                    assignDoctorShift(doctor);
                    break;
                case 2:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;
                    addDoctorBreak(doctor);
                    break;
                case 3:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;
                    viewDoctorSchedule(doctor);
                    break;
                case 4:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;
                    getDoctorAvailabilitySchedule(doctor);
                    break;
                case 5:
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;
                    viewSchedule(doctor, LocalTime.of(8, 0), LocalTime.of(18, 0), 60);
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            return;
        } while (true);
    }

    //Assign Shift to Doctor
    public void assignDoctorShift(Doctor doctor) {
        System.out.println("Which day do you wish to assign Doctor " + doctor.getName() + "?");
        viewDoctorSchedule(doctor);

        DayOfWeek dayOfWeek = null;
        while (dayOfWeek == null) {
            int index = -1;
            while (index == -1) {
                System.out.print("Enter the day number: ");
                try {
                    index = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number (1-7).");
                    scanner.nextLine();
                }
            }
            try {
                dayOfWeek = DayOfWeek.of(index);
            } catch (DateTimeException e) {
                System.out.println("Please enter a valid day number (1 = Monday, ... 7 = Sunday).");
                dayOfWeek = null;
            }
        }

        if (doctor.getSchedule() == null) {
            doctor.setSchedule(new Schedule());
        }

        var dayShifts = doctor.getSchedule().getShifts(dayOfWeek);

        boolean added = false;
        while (!added) {
            System.out.print("Do you want to add an additional shift to an existing one? (y/n): ");
            String choice = scanner.nextLine().trim();

            Shift shift = null;
            if (choice.equalsIgnoreCase("y")) {
                shift = createAdditionalShiftFromInput();
                if (shift != null) {
                    added = DoctorController.additionalShift(dayShifts, shift);
                    if (!added) {
                        System.out.println("Shift must extend existing shift by at least 30 minutes.");
                    }
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } else if (choice.equalsIgnoreCase("n")) {
                boolean validShift = false;
                while (!validShift) {
                    shift = createShiftFromInput();
                    if (shift != null) {
                        validShift = DoctorController.addShift(dayShifts, shift);
                        if (!validShift) {
                            System.out.println("Shift not added due to overlap. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }
                added = true; // Once a valid shift is added, exit the outer loop
            }
        }

        System.out.println("Assigned to Dr. " + doctor.getName() + " on " + dayOfWeek);
    }



    //For Creating New Shift
    public @Nullable Shift createShiftFromInput() {
        while (true) {
            try {
                System.out.print("Enter start time (hh:mm): ");
                String startInput = scanner.nextLine().trim();
                LocalTime start = LocalTime.parse(startInput);

                System.out.print("Enter end time (hh:mm): ");
                String endInput = scanner.nextLine().trim();
                LocalTime end = LocalTime.parse(endInput);

                if (end.isBefore(start)) {
                    System.out.println("End time must be after start time. Please try again.");
                    continue;
                }
                if (Duration.between(start, end).toHours() < 2) {
                    System.out.println("Shift must be at least 2 hours long. Please try again.");
                    continue;
                }
                return new Shift()
                        .setType(ShiftType.WORK)
                        .setTimeRange(new Range<>(start, end));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use hh:mm (e.g. 07:00).");
            }
        }
    }


    //For Addon Shift
    public @Nullable Shift createAdditionalShiftFromInput() {
        LocalTime start;
        LocalTime end;
        try {
            do{System.out.print("Enter start time (hh:mm): ");
            start = LocalTime.parse(this.scanner.nextLine().trim());

            System.out.print("Enter end time (hh:mm): ");
            end = LocalTime.parse(this.scanner.nextLine().trim());

            if (end.isBefore(start)) {
                System.out.println("End time must be after start time. Please try again.");
            }
            }while(end.isBefore(start));

            return new Shift()
                    .setType(ShiftType.WORK)
                    .setTimeRange(new Range<>(start, end));

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use hh:mm (e.g. 07:00).");
        }

        return null;
    }
    //For Break ONLY
    public @Nullable Shift addBreakFromInput() {
        while (true) {
            try {
                System.out.print("Enter start time (hh:mm): ");
                String startInput = scanner.nextLine().trim();
                LocalTime start = LocalTime.parse(startInput);

                System.out.print("Enter end time (hh:mm): ");
                String endInput = scanner.nextLine().trim();
                LocalTime end = LocalTime.parse(endInput);

                if (end.isBefore(start)) {
                    System.out.println("End time must be after start time. Please try again.");
                    continue;
                }
                return new Shift()
                        .setType(ShiftType.WORK)
                        .setTimeRange(new Range<>(start, end));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use hh:mm (e.g. 07:00).");
            }
        }
    }


    //view Specific Doctor Schedule
    public void viewDoctorSchedule(Doctor doctor) {
        System.out.println("\nWeekly Schedule for Dr." + doctor.getName());
        System.out.println("-".repeat(50));
        this.viewSchedule(doctor.getSchedule());
        System.out.println("-".repeat(50));
    }

    //Get Doctor Availability
    public void viewDoctorAvailabilitySchedule(LocalDate date, Doctor doctor) {
        System.out.println("\nWeekly Availability Schedule for Dr." + doctor.getName());
        System.out.println("-".repeat(50));
        this.viewSchedule(DoctorController.getAvailabilitySchedule(date, doctor));
        System.out.println("-".repeat(50));
    }

    //Doctor Shift Schedule
    public void viewSchedule(Schedule schedule) {
        printDay("1. Monday", schedule.monday());
        printDay("2. Tuesday", schedule.tuesday());
        printDay("3. Wednesday", schedule.wednesday());
        printDay("4. Thursday", schedule.thursday());
        printDay("5. Friday", schedule.friday());
        printDay("6. Saturday", schedule.saturday());
        printDay("7. Sunday", schedule.sunday());
    }

    //Doctor Schedule Formatting
    private void printDay(String day, ListInterface<Shift> shifts) {
        System.out.print(day + ": ");
        if (shifts.size() == 0) {
            System.out.println("No shifts assigned.");
            return;
        }

        for (var shift : shifts)
            System.out.print(shift.getType() + " (" + shift.getTimeRange().from() + "-" + shift.getTimeRange().to() + ") ");

        System.out.println();
    }

    //DoctorTimetable
    public void viewSchedule(Doctor doctor, LocalTime dayStart, LocalTime dayEnd, int slotMinutes) {
        Schedule schedule = doctor.getSchedule();
        int slotCount = (int) (Duration.between(dayStart, dayEnd).toMinutes() / slotMinutes);
        LocalTime[] slots = new LocalTime[slotCount];
        LocalTime current = dayStart;
        for (int i = 0; i < slotCount; i++) {
            slots[i] = current;
            current = current.plusMinutes(slotMinutes);
        }

        // Build columns: Day + time slots
        Column[] columns = new Column[1 + slotCount];
        columns[0] = new Column("Day", Alignment.CENTER, 12);
        for (int i = 0; i < slotCount; i++) {
            LocalTime slotStart = slots[i];
            LocalTime slotEnd = slotStart.plusMinutes(slotMinutes);
            String label = String.format("%02d:%02d-%02d:%02d", slotStart.getHour(), slotStart.getMinute(), slotEnd.getHour(), slotEnd.getMinute());
            columns[i + 1] = new Column(label, Alignment.CENTER, 14);
        }

        // Prepare days data
        DoctorController.getDays();

        InteractiveTable<DayOfWeek> table = new InteractiveTable<>(columns, days) {
            @Override
            protected Cell[] getRow(DayOfWeek day) {
                Cell[] row = new Cell[columns.length];
                row[0] = new Cell(day.toString(), Alignment.CENTER);
                ListInterface<Shift> shifts = schedule.getShifts(day);
                for (int i = 0; i < slots.length; i++) {
                    LocalTime slotStart = slots[i];
                    LocalTime slotEnd = slotStart.plusMinutes(slotMinutes);

                    double coverage = 0.0;
                    String align = "";
                    for (Shift shift : shifts) {
                        LocalTime shiftStart = shift.getTimeRange().from();
                        LocalTime shiftEnd = shift.getTimeRange().to();

                        LocalTime overlapStart = slotStart.isAfter(shiftStart) ? slotStart : shiftStart;
                        LocalTime overlapEnd = slotEnd.isBefore(shiftEnd) ? slotEnd : shiftEnd;
                        long overlapMinutes = Duration.between(overlapStart, overlapEnd).toMinutes();

                        if (overlapMinutes > 0) {
                            double slotCoverage = (double) overlapMinutes / slotMinutes;
                            if (slotCoverage > coverage) coverage = slotCoverage;

                            // Prioritize alignment: start > end > full > center
                            if (shiftStart.compareTo(slotStart) >= 0 && shiftStart.compareTo(slotEnd) < 0) {
                                align = "left";
                                break; // If shift starts in this slot, use left and stop
                            } else if (shiftEnd.compareTo(slotStart) > 0 && shiftEnd.compareTo(slotEnd) <= 0) {
                                align = "right";
                                // don't break, in case a later shift starts in this slot
                            } else if (shiftStart.compareTo(slotStart) <= 0 && shiftEnd.compareTo(slotEnd) >= 0) {
                                align = "full";
                            } else {
                                align = "center";
                            }
                        }
                    }
                    if (coverage > 1.0) coverage = 1.0;

                    int barLength = 13;
                    int fillLength = (int) Math.round(barLength * coverage);
                    String bar = "";

                    if (coverage > 0) {
                        switch (align) {
                            case "left":
                                bar = "█".repeat(fillLength) + " ".repeat(barLength - fillLength);
                                break;
                            case "right":
                                bar = " ".repeat(barLength - fillLength) + "█".repeat(fillLength);
                                break;
                            case "full":
                                bar = "█".repeat(barLength);
                                break;
                            default: // center
                                int leftPad = (barLength - fillLength) / 2;
                                bar = " ".repeat(leftPad) + "█".repeat(fillLength) + " ".repeat(barLength - fillLength - leftPad);
                                break;
                        }
                    }
                    row[i + 1] = new Cell(bar, Alignment.CENTER);
                }for (int i = 0; i < slots.length; i++) {
                    LocalTime slotStart = slots[i];
                    LocalTime slotEnd = slotStart.plusMinutes(slotMinutes);

                    double coverage = 0.0;
                    String align = "";
                    for (Shift shift : shifts) {
                        LocalTime shiftStart = shift.getTimeRange().from();
                        LocalTime shiftEnd = shift.getTimeRange().to();

                        LocalTime overlapStart = slotStart.isAfter(shiftStart) ? slotStart : shiftStart;
                        LocalTime overlapEnd = slotEnd.isBefore(shiftEnd) ? slotEnd : shiftEnd;
                        long overlapMinutes = Duration.between(overlapStart, overlapEnd).toMinutes();

                        if (overlapMinutes > 0) {
                            double slotCoverage = (double) overlapMinutes / slotMinutes;
                            if (slotCoverage > coverage) coverage = slotCoverage;


                            if (shiftStart.compareTo(slotStart) >= 0 && shiftStart.compareTo(slotEnd) < 0) {
                                align = "right";
                                break; // If shift starts in this slot, use right and stop
                            } else if (shiftEnd.compareTo(slotStart) > 0 && shiftEnd.compareTo(slotEnd) <= 0) {
                                align = "left";
                                // don't break, in case a later shift starts in this slot
                            } else if (shiftStart.compareTo(slotStart) <= 0 && shiftEnd.compareTo(slotEnd) >= 0) {
                                align = "full";
                            } else {
                                align = "center";
                            }
                        }
                    }
                    if (coverage > 1.0) coverage = 1.0;

                    int barLength = 13;
                    int fillLength = (int) Math.round(barLength * coverage);
                    String bar = "";

                    if (coverage > 0) {
                        switch (align) {
                            case "left":
                                bar = "█".repeat(fillLength) + " ".repeat(barLength - fillLength);
                                break;
                            case "right":
                                bar = " ".repeat(barLength - fillLength) + "█".repeat(fillLength);
                                break;
                            case "full":
                                bar = "█".repeat(barLength);
                                break;
                            default: // center
                                int leftPad = (barLength - fillLength) / 2;
                                bar = " ".repeat(leftPad) + "█".repeat(fillLength) + " ".repeat(barLength - fillLength - leftPad);
                                break;
                        }
                    }
                    row[i + 1] = new Cell(bar, Alignment.CENTER);
                }
                return row;
            }
        };

        System.out.printf("%nWeekly Shift Table for Dr. %s%n", doctor.getName());
        table.display();
    }

    //Deleting Doctor Shift
    public void addDoctorBreak(Doctor doctor) {
        System.out.println("Which day do you wish to give Doctor " + doctor.getName() + " a break?");
        viewDoctorSchedule(doctor);

        DayOfWeek dayOfWeek = null;

        while (dayOfWeek == null) {
            System.out.print("Enter the day number: ");
            var index = scanner.nextInt();
            this.scanner.nextLine();
            try {
                dayOfWeek = DayOfWeek.of(index);
            } catch (DateTimeException e) {
                // request reprompt for invalid dayOfWeek
                System.out.println("Please enter a valid day number");
            }
        }

        if (doctor.getSchedule() == null) {
            doctor.setSchedule(new Schedule());
        }
        var dayShifts = doctor.getSchedule().getShifts(dayOfWeek);

        var shift = addBreakFromInput();
        if (shift == null) {
            System.out.println("Break not granted due to invalid input");
            return;
        }

        DoctorController.addBreak(dayShifts, shift);

        System.out.println("Granted break" + " to Dr." + doctor.getName() + " on " + dayOfWeek);
    }

    //Get Doctor Availability Date
    public void getDoctorAvailabilitySchedule(Doctor doctor) {
        System.out.println("-".repeat(30));
        System.out.print("Enter date (yyyy-MM-dd): ): ");
        var date = LocalDate.parse(this.scanner.nextLine().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        this.viewDoctorAvailabilitySchedule(date, doctor);
    }

    //SummaryReport
    public void generateSummaryReport() {
        int width = 200;
        int topN = 10;

        while (true) {
            ListInterface<DoctorCounter> counters = DoctorController.getDoctorSummary();
            System.out.println();

            // header
            System.out.println("=".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY", ' ', width));
            System.out.println(StringUtils.pad("DOCTOR MANAGEMENT MODULE", ' ', width));
            System.out.println(StringUtils.pad("SUMMARY OF DOCTOR REPORT", ' ', width));
            System.out.println("=".repeat(width));
            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("TUNKU ABDUL RAHMAN UNIVERSITY OF MANAGEMENT AND TECHNOLOGY - HIGHLY CONFIDENTIAL DOCUMENT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.printf("Generated at: %s%n", DATE_FORMAT.format(java.time.LocalDateTime.now()));
            System.out.println();

            int maxConsultationLength = "Consultations Attended".length();
            int maxPatientLength = "Total Patients Treated".length();

            for (var dc : counters) {
                String consultations = StringUtils.join(", ", DoctorController.getTypeList(dc));
                maxConsultationLength = Math.max(maxConsultationLength, consultations.length());

                String patients = StringUtils.join(", ", DoctorController.getPatientList(dc));
                maxPatientLength = Math.max(maxPatientLength, patients.length());
            }

            maxConsultationLength += 2;
            maxPatientLength += 2;

            InteractiveTable<DoctorCounter> table = new InteractiveTable<>(new Column[]{
                    new Column("Doctor ID", Alignment.CENTER, 20),
                    new Column("Doctor Name", Alignment.CENTER, 30),
                    new Column("Specialization", Alignment.CENTER, 25),
                    new Column("Consultations Attended", Alignment.CENTER, 30),
                    new Column("Total Patients Treated", Alignment.CENTER, 30)
            }, counters) {
                @Override
                protected Cell[] getRow(DoctorCounter dc) {
                    var doctor = dc.key();

                    String specialization = doctor.getSpecialization() != null
                            ? doctor.getSpecialization().name()
                            : "-";
                    int doctorId = doctor.getId();
                    String doctorName = doctor.getName();
                    String consultationCount = StringUtils.join(", ", DoctorController.getTypeList(dc));
                    String patients = String.valueOf(dc.getPatientCounters().size());

                    return new Cell[]{
                            new Cell(doctorId, Alignment.CENTER),
                            new Cell(doctorName),
                            new Cell(specialization, Alignment.CENTER),
                            new Cell(consultationCount),
                            new Cell(patients)
                    };
                }
            };
            table.setPageSize(topN);
            table.display();
            System.out.println();

            ListInterface<Integer> stats = DoctorController.getTotalStats(topN);
            System.out.printf("Total Number of Doctors (Top %d): %d%n", topN, stats.get(0));
            System.out.printf("Total Number of Consultations (Top %d): %d%n", topN, stats.get(1));
            System.out.printf("Total Number of Patients Treated (Top %d): %d%n", topN, stats.get(2));
            System.out.println();

            System.out.println("GRAPHICAL REPRESENTATION OF SUMMARY MODULE");
            System.out.println("------------------------------------------");
            var topConsultations = DoctorController.getTopDoctorCountersByConsultations(topN);
            var topPatients = DoctorController.getTopDoctorCountersByPatients(topN);

            printBarChart("Consultations", topConsultations, topN);
            printBarChart("Patients Treated", topPatients, topN);
            System.out.println();

            System.out.println("Global Highlights:");
            System.out.println("Doctor(s) with fewest consultations: " + StringUtils.join(", ", DoctorController.getExtremeDoctors(false)));
            System.out.println("Doctor(s) with most consultations: " + StringUtils.join(", ", DoctorController.getExtremeDoctors(true)));
            System.out.println();

            // footer
            System.out.println("*".repeat(width));
            System.out.println(StringUtils.pad("END OF THE REPORT", ' ', width));
            System.out.println("*".repeat(width));
            System.out.println();

            System.out.printf("(0) Exit Report | Enter new Top filter (current %d): ", topN);
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println();
                break;
            }

            try {
                int newN = Integer.parseInt(input);
                if (newN > 0) {
                    topN = newN;
                } else {
                    System.out.println("TopN must be greater than 0. Keeping current value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, keeping current TopN.");
            }
        }
    }


    //Doctor Info Table
    public static class DoctorTable extends InteractiveTable<Doctor> {
        public DoctorTable(ListInterface<Doctor> doctors) {
            super(new Column[]{
                    new Column("Doctor Id", Alignment.CENTER, 20),
                    new Column("Name", Alignment.CENTER, 20),
                    new Column("Gender", Alignment.CENTER, 20),
                    new Column("Contact Number", Alignment.CENTER, 20),
                    new Column("Specialization", Alignment.CENTER, 20)
            }, doctors);
        }

        @Override
        protected Cell[] getRow(Doctor o) {
            return new Cell[]{
                    new Cell(String.valueOf(o.getId())),
                    new Cell(o.getName()),
                    new Cell(o.getGender()),
                    new Cell(o.getContactNumber()),
                    new Cell(o.getSpecialization())
            };
        }
    }

    private void printBarChart(String title, ListInterface<DoctorCounter> list, int topN) {
        int maxValue = 0;
        for (var dc : list) {
            int val = 0;
            if (title.equalsIgnoreCase("Consultations")) {
                val = dc.getCount();
            } else if (title.equalsIgnoreCase("Patients Treated")) {
                val = dc.getPatientCounters().size();
            }
            if (val > maxValue) maxValue = val;
        }

        int barWidth = 70; // maximum bar length

        String headerText = "Top " + topN + " Doctors by " + title;
        int borderLength = headerText.length() + 2;

        System.out.println("+" + "-".repeat(borderLength) + "+");
        System.out.println("| " + headerText + " |");
        System.out.println("+" + "-".repeat(borderLength) + "+");

        int shown = 0;
        for (var dc : list) {
            if (shown++ >= topN) break;

            int value = 0;
            if (title.equalsIgnoreCase("Consultations")) {
                value = dc.getCount();
            } else if (title.equalsIgnoreCase("Patients Treated")) {
                value = dc.getPatientCounters().size();
            }

            int scaled = maxValue == 0 ? 0 : (int) Math.round((value / (double) maxValue) * barWidth);

            final String BLUE = "\u001B[34m";
            final String RESET = "\u001B[0m";

            System.out.printf("%-25s | %s%s%s (%d)%n",
                    dc.key().getName(),
                    BLUE,
                    "█".repeat(Math.max(1, scaled)),
                    RESET,
                    value
            );
        }
        System.out.println();
    }


}

