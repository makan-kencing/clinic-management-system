package edu.dsa.clinic.boundary;

import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class AppointmentUI {

    public int getMenuChoice() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("\nAppointment MENU");
            System.out.println("1. Schedule An Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. Edit An Appointment");
            System.out.println("4. Cancel An Appointment");
            System.out.println("0. Quit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            return choice;
        } while (choice != 0);
    }
}