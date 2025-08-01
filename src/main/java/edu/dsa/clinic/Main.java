package edu.dsa.clinic;

import edu.dsa.clinic.entity.Gender;

public class Main {
    public static void main(String[] args) {
        Initializer.initialize();;

        System.out.println(Database.patients.size());
        for (var patient : Database.patients)
            System.out.println(patient);

        Database.patients.filter((p) -> p.getGender() == Gender.MALE);

        System.out.println(Database.patients.size());
        for (var patient : Database.patients)
            System.out.println(patient);
    }
}
