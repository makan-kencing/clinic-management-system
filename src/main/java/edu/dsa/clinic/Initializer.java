package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.entity.ConsultationQueue;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Specialization;

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

        var queue = new ConsultationQueue()
                .setPatient(patient)
                .setConsultationType(ConsultationType.GENERAL);

        var consultationQueues = new DoubleLinkedList<ConsultationQueue>();

        consultationQueues.add(queue);

        var doctor = new Doctor();
        doctor.setName("daren1");
        doctor.setGender(Gender.MALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Neurosurgery);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        doctor = new Doctor();
        doctor.setName("daren2");
        doctor.setGender(Gender.IDK);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Ophthalmology);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        doctor = new Doctor();
        doctor.setName("daren3");
        doctor.setGender(Gender.FEMALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Orthopedics);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        doctor = new Doctor();
        doctor.setName("daren4");
        doctor.setGender(Gender.FEMALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Pediatrics);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        doctor = new Doctor();
        doctor.setName("daren5");
        doctor.setGender(Gender.MALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Otorhinolaryngology);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);
    }
}
