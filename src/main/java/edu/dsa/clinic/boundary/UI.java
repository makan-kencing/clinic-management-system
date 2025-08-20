package edu.dsa.clinic.boundary;

import java.util.Scanner;

public abstract class UI {
    protected final Scanner scanner;

    public UI(Scanner scanner) {
        this.scanner = scanner;
    }
}
