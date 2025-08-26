package edu.dsa.clinic.boundary;

import edu.dsa.clinic.Initializer;
import edu.dsa.clinic.entity.Doctor;

import java.util.Scanner;

public class DoctorUITest {
    public static void main(String[] args) {
        Initializer.initialize();
        Doctor doctor = new Doctor();
        var scanner = new Scanner(System.in);

        var ui = new DoctorUI(scanner);
        ui.startMenu();
    }
}
