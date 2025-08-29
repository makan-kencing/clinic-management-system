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

            UI ui;
            while (true) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                var option = builder.promptOption(prompt);
                switch (option) {
                    case PATIENT:
                        ui = new PatientUI(scanner);
                        break;
                    case DOCTOR:
                        ui = new DoctorUI(scanner);
                        break;
                    case CONSULTATION:
                    case MEDICAL:
                        var appointmentUI = new AppointmentUI(scanner);
                        var medicalUI = new MedicalUI(terminal, scanner);

                        appointmentUI.setMedicalUI(medicalUI);
                        medicalUI.setAppointmentUI(appointmentUI);

                        ui = switch (option) {
                            case CONSULTATION -> appointmentUI;
                            case MEDICAL -> medicalUI;
                            default -> throw new IllegalStateException();
                        };
                        break;
                    case MEDICINE:
                        ui = new MedicineUI(terminal);
                        break;
                    case EXIT:
                        writer.println();
                        writer.println("Thank you for using Clinic Management System!");
                        writer.flush();
                        return;
                    default:
                        continue;
                }

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
