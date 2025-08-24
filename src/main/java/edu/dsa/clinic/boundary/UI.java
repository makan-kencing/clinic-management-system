package edu.dsa.clinic.boundary;

import org.jline.terminal.Terminal;

import java.util.Scanner;

public abstract class UI {
    protected final Scanner scanner;
    protected final Terminal terminal;

    public UI(Terminal terminal) {
        this.scanner = new Scanner(System.in);
        this.terminal = terminal;
    }

    public UI(Scanner scanner) {
        this.scanner = scanner;
        this.terminal = null;
    }


}
