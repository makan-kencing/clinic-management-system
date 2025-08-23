package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class MedicineUITest {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        try (var terminal = TerminalBuilder.builder()
                .system(true)
                .build()
        ) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            var ui = new MedicineUI(null, terminal);
            ui.startMenu();
            // Entrypoint to the main UI.
        }
    }
}
