package edu.dsa.clinic;

import edu.dsa.clinic.boundary.AppointmentUI;
import edu.dsa.clinic.boundary.DoctorUI;
import edu.dsa.clinic.boundary.MedicalUI;
import edu.dsa.clinic.boundary.MedicineUI;
import edu.dsa.clinic.boundary.PatientUI;
import edu.dsa.clinic.boundary.UI;
import edu.dsa.clinic.utils.StringUtils;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.Scanner;

public class ClinicManagementSystem {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build();
             var scanner = new Scanner(System.in)
        ) {
            boolean running = true;
            while (running) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                String[] menuItems = {
                        "1. Patient Management Module",
                        "2. Doctor Management Module",
                        "3. Appointment and Consultation Management Module",
                        "4. Medical Record Management Module",
                        "5. Medicine Management Module",
                        "0. Exit"
                };

                var width = Math.min(80, terminal.getWidth());

                System.out.println();
                System.out.println("=".repeat(width));
                System.out.println(StringUtils.pad("CLINIC MANAGEMENT SYSTEM", ' ', width));
                System.out.println("=".repeat(width));

                for (String item : menuItems) {
                    System.out.println(StringUtils.padRight(item, ' ', width));
                }

                System.out.println("=".repeat(width));
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine().trim();

                System.out.println("-".repeat(width));
                System.out.println();

                UI ui = switch (choice) {
                    case "1":
                        yield new PatientUI(scanner);
                    case "2":
                        yield new DoctorUI(scanner);
                    case "3":
                    case "4":
                        var appointmentUI = new AppointmentUI(scanner);
                        var medicalUI = new MedicalUI(terminal, scanner);

                        appointmentUI.setMedicalUI(medicalUI);
                        medicalUI.setAppointmentUI(appointmentUI);

                        yield switch (choice) {
                            case "3" -> appointmentUI;
                            case "4" -> medicalUI;
                            default -> throw new IllegalStateException();
                        };
                    case "5":
                        yield new MedicineUI(terminal);
                    case "0":
                        System.out.println();
                        System.out.println("Thank you for using Clinic Management System!");
                        System.out.println();

                        running = false;

                        yield null;
                    default:
                        System.out.println();
                        System.out.println("Invalid choice, please try again!");

                        yield null;
                };

                if (ui == null)
                    continue;

                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                ui.startMenu();
            }
        }
    }

    public static void main2(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build();
             var scanner = new Scanner(System.in)
        ) {
            var writer = terminal.writer();

            var width = Math.min(80, terminal.getWidth());

            var prompt = new ConsolePrompt(terminal);
            var builder = new MainMenuPromptBuilder();
            builder.createText()
                    .addLine("=".repeat(width))
                    .addLine(StringUtils.pad("CLINIC MANAGEMENT SYSTEM", ' ', width))
                    .addLine("=".repeat(width))
                    .addPrompt();
            builder.createMenuOptionPrompt()
                    .addPrompt();

            while (true) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                var option = builder.promptOption(prompt);
                UI ui = switch (option) {
                    case PATIENT:
                        yield new PatientUI(scanner);
                    case DOCTOR:
                        yield new DoctorUI(scanner);
                    case CONSULTATION:
                    case MEDICAL:
                        var appointmentUI = new AppointmentUI(scanner);
                        var medicalUI = new MedicalUI(terminal, scanner);

                        appointmentUI.setMedicalUI(medicalUI);
                        medicalUI.setAppointmentUI(appointmentUI);

                        yield switch (option) {
                            case CONSULTATION -> appointmentUI;
                            case MEDICAL -> medicalUI;
                            default -> throw new IllegalStateException();
                        };
                    case MEDICINE:
                        yield new MedicineUI(terminal);
                    default:
                        writer.println();
                        writer.println("Thank you for using Clinic Management System!");
                        writer.flush();

                        yield null;
                };

                if (ui == null)
                    return;

                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                ui.startMenu();
            }
        }
    }

    public static class MainMenuPromptBuilder extends PromptBuilder {
        public static final String OPTION = "option";

        public enum Option {
            PATIENT,
            DOCTOR,
            CONSULTATION,
            MEDICAL,
            MEDICINE,
            EXIT
        }

        public ListPromptBuilder createMenuOptionPrompt() {
            return this.createListPrompt()
                    .name(OPTION)
                    .message("Choose a menu")
                    .newItem(Option.PATIENT.name()).text("Patient Management Module").add()
                    .newItem(Option.DOCTOR.name()).text("Doctor Management Module").add()
                    .newItem(Option.CONSULTATION.name()).text("Appointment and Consultation Management Module").add()
                    .newItem(Option.MEDICAL.name()).text("Medical Record Management Module").add()
                    .newItem(Option.MEDICINE.name()).text("Medicine Management Module").add()
                    .newItem(Option.EXIT.name()).text("Exit").add();
        }

        public Option promptOption(ConsolePrompt prompt) throws IOException {
            var result = prompt.prompt(this.build());

            return Option.valueOf(result.get(OPTION).getResult());
        }
    }
}
