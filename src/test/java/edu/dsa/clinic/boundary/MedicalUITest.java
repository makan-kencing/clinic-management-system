package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;

import java.util.Scanner;

public class MedicalUITest {
    public static void main(String[] args) {
        Initializer.initialize();

        var scanner = new Scanner(System.in);

        var medicalUI = new MedicalUI(scanner);
        medicalUI.viewMenu();
    }
}
