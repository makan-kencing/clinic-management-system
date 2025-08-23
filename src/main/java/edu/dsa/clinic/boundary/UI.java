package edu.dsa.clinic.boundary;

import org.jline.terminal.Terminal;

import java.util.Scanner;

public abstract class UI {
    protected final Scanner scanner;
    protected final Terminal terminal;

    public UI(Scanner scanner,  Terminal terminal) {
        this.scanner = scanner;
        this.terminal = terminal;
    }

    public UI(Scanner scanner) {
        this(scanner, null);
    }


}
