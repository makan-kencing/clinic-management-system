package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.DoctorController;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.filter.DoctorFilter;
import edu.dsa.clinic.utils.Ordered;
import edu.dsa.clinic.utils.table.Alignment;
import edu.dsa.clinic.utils.table.Cell;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DoctorUI extends UI {
    public DoctorUI(Scanner scanner) {
        super(scanner);
    }

    public void startMenu() {
        int choice;
        do {
            System.out.println("\nDoctor Menu");
            System.out.println("1. Create new Doctor");
            System.out.println("2. View Doctor List");
            System.out.println("3. Delete Doctor Record");
            System.out.println("4. Modify Doctor Information");
            System.out.println("5. Doctor Shifts Menu");
            System.out.println("6. Generate Summary Report Menu");
            System.out.println("0. Exit To Main Menu");
            System.out.print("Enter choice: ");

            while (true) {
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 0 || choice > 5) {
                        System.out.println("Invalid choice. Please enter a number between 0 and 5.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
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
                    // editDoctorInformation
                    doctor = selectDoctor();
                    if (doctor == null)
                        break;

                    modifyDoctor(doctor);
                    break;
                case 5:
                    // doctorShiftMenu
                    shiftsMenu();
                    break;
                case 6:

                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (true);
    }

    public Gender selectAGender(){
        Gender newGender;
        int gender;
        do{

            System.out.print("-".repeat(30));
            System.out.println("\n(1) male");
            System.out.println("(2) female");
            System.out.print("Enter gender number: ");
            gender = scanner.nextInt();
            this.scanner.nextLine();

            while (true) {
                try {
                    gender = Integer.parseInt(scanner.nextLine());
                    if (gender < 1 || gender > 2) {
                        System.out.println("Invalid option. Please enter 1 for male or 2 for female.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number (1 or 2).");
                }
            }

            newGender = switch (gender){
                case 1 -> Gender.MALE;
                case 2 -> Gender.FEMALE;
                default -> null;
            };
            if (newGender == null) {
                System.out.println("Invalid option. Try again.");
                continue;
            }
            break;
        }while(newGender == null);
      return newGender;
    }

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

        System.out.print("\nEnter Contact Number: ");
        var number =this.scanner.nextLine();
        if (!number.trim().isEmpty()) {
            doctor.setContactNumber(number);
            System.out.println("New contact number updated successfully!");
        } else  {
            System.out.println("New contact number cannot be empty. Try again.");
        }


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

        System.out.println("Doctor created successfully: ");
    }

    //delete Doctor
    public void deleteDoctor(Doctor doctor) {
        System.out.print("Are you sure to delete this entry? (Y/N) ");

        if (this.scanner.nextLine().equalsIgnoreCase("Y")) {

            var deletedDoctor = DoctorController.deleteDoctorByID(doctor.getId());

            if (deletedDoctor == null) {
                System.out.println("Doctor Id not found.");
            } else {
                System.out.printf("Doctor `%s` deleted", deletedDoctor.getName());
            }
            this.scanner.nextLine();
        }
    }

    //modifyDoctor
    public void modifyDoctor(Doctor doctor) {
        System.out.println("-".repeat(30));
        System.out.println("Doctor Modify Menu:");
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
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                do{
                    System.out.print("Enter Doctor Name: ");
                    var name = this.scanner.nextLine().trim();

                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty. No changes made.");
                        continue;
                    }

                    if (DoctorController.selectDoctorByName(name) != null) {
                        System.out.println("Name taken");
                        continue;
                    }

                    doctor.setName(newName);
                    break;
                }while(true);

                break;
            }
            case 2: {
                doctor.setGender(selectAGender());
                break;
            }
            case 3: {
                System.out.print("Current contact number: " + doctor.getContactNumber());
                System.out.print("Enter new contact number: ");
                var newNumber = scanner.nextLine();
                if (!newNumber.trim().isEmpty()) {
                    doctor.setContactNumber(newNumber);
                    System.out.println("New contact number updated successfully!");
                } else  {
                    System.out.println("New contact number cannot be empty. Try again.");
                }
                break;
            }
            case 4: {
                System.out.println("Current Doctor Specialization: " + doctor.getSpecialization());
                int option;
                do {
                    System.out.print("Enter new specialization you wish to choose: ");
                    System.out.println("1. Neurosurgery");
                    System.out.println("2. Pediatrics");
                    System.out.println("3. Ophthalmology");
                    System.out.println("4. Otorhinolaryngology");
                    System.out.println("5. Orthopedics");
                    option = this.scanner.nextInt();  // 4

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
            }
            default:
                break;
        }
    }
    
    public @Nullable Doctor selectAvailableDoctor(LocalDate date, Range<LocalTime> timeRange) {

        Doctor selectedDoctor = null;

        var doctors = DoctorController.getDoctors();

        var table = new DoctorTable(doctors);
        table.addDefaultFilter("Available at " + date + "from"+ timeRange.from() + " to " + timeRange.to(), DoctorFilter.isAvailable(date, timeRange));

        // TODO: do ur menu loop thingy here
        return selectedDoctor;
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
                        table.display();
                        break;
                    }
                }
            } else {
                System.out.println();
                break;
            }
        } while (opt > 1 && opt < 4);
        return selectedDoctor;
    }

    //filter doctor menu
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
            System.out.println("\nDoctor Shifts Menu");
            System.out.println("1. Assign Doctor Shift");
            System.out.println("2. Add Doctor Break Time");
            System.out.println("3. View Doctor Schedule");
            System.out.println("4. View Doctor Availability Schedule ");
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

        var shift = createShiftFromInput();
        if (shift == null) {
            System.out.println("Shift not assigned due to invalid input");
            return;
        }

        DoctorController.addShift(dayShifts, shift);

        System.out.println("Assigned" + " to Dr." + doctor.getName() + " on " + dayOfWeek);
    }

    //Get User start & end time
    public @Nullable Shift createShiftFromInput() {
        try {
            System.out.print("Enter start time (hh:mm): ");
            LocalTime start = LocalTime.parse(this.scanner.nextLine().trim());

            System.out.print("Enter end time (hh:mm): ");
            LocalTime end = LocalTime.parse(this.scanner.nextLine().trim());

            if(end.isBefore(start)){
                System.out.println("End time must be after start time. Please try again.");
            }
            return new Shift()
                    .setType(ShiftType.WORK)
                    .setTimeRange(new Range<>(start, end));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use hh:mm (e.g. 07:00).");
        }
        return null;
    }

    //view Specific Doctor Schedule
    public void viewDoctorSchedule(Doctor doctor) {
        System.out.println("\nWeekly Schedule for Dr." + doctor.getName());
        System.out.println("-".repeat(50));
        this.viewSchedule(doctor.getSchedule());
        System.out.println("-".repeat(50));
    }

    public void viewDoctorAvailabilitySchedule(LocalDate date, Doctor doctor) {
        System.out.println("\nWeekly Availability Schedule for Dr." + doctor.getName());
        System.out.println("-".repeat(50));
        this.viewSchedule(DoctorController.getAvailabilitySchedule(date, doctor));
        System.out.println("-".repeat(50));
    }

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

        var shift = createShiftFromInput();
        if (shift == null) {
            System.out.println("Break not granted due to invalid input");
            return;
        }

        DoctorController.addBreak(dayShifts, shift);

        System.out.println("Granted break" + " to Dr." + doctor.getName() + " on " + dayOfWeek);
    }

    public void getDoctorAvailabilitySchedule(Doctor doctor) {
        System.out.println("-".repeat(30));
        System.out.print("Enter date (yyyy-MM-dd): ): ");
        var date = LocalDate.parse(this.scanner.nextLine().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        this.viewDoctorAvailabilitySchedule(date, doctor);
    }


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

}

