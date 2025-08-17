package edu.dsa.clinic;

import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;

public class Initializer {
    private Initializer() {
    }

    public static void initialize() {
        var patient = new Patient();
        patient.setName("hi");
        patient.setGender(Gender.MALE);
        patient.setIdentification("01234");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        patient = new Patient();
        patient.setName("hello");
        patient.setGender(Gender.IDK);
        patient.setIdentification("31324");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        patient = new Patient();
        patient.setName("john doe");
        patient.setGender(Gender.IDK);
        patient.setIdentification("54821");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        patient = new Patient();
        patient.setName("Patient Zero");
        patient.setGender(Gender.FEMALE);
        patient.setIdentification("94756");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        patient = new Patient();
        patient.setName("PAX-19");
        patient.setGender(Gender.MALE);
        patient.setIdentification("09865");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);
    }
}
