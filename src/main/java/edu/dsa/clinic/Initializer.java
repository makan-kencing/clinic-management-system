package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.ConsultationQueue;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Specialization;

import java.time.LocalDateTime;

public class Initializer {
    private Initializer() {
    }

    public static void initialize() {
        //first
        var patient = new Patient();
        patient.setName("hi");
        patient.setGender(Gender.MALE);
        patient.setIdentification("01234");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        var doctor = new Doctor();
        doctor.setName("daren1");
        doctor.setGender(Gender.MALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Neurosurgery);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        var appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedAt(LocalDateTime.of(2025, 8, 21, 11, 30));
        appointment.setExpectedStartAt(LocalDateTime.of(2025, 8, 22, 8, 30));
        appointment.setExpectedEndAt(LocalDateTime.of(2025, 8, 22, 9, 30));
        Database.appointmentList.add(appointment);

        //second
        patient = new Patient();
        patient.setName("hello");
        patient.setGender(Gender.IDK);
        patient.setIdentification("31324");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        doctor = new Doctor();
        doctor.setName("daren2");
        doctor.setGender(Gender.IDK);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Ophthalmology);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45));
        appointment.setExpectedStartAt(LocalDateTime.of(2025, 8, 24, 9, 00));
        appointment.setExpectedEndAt(LocalDateTime.of(2025, 8, 24, 10, 00));
        Database.appointmentList.add(appointment);

        //third
        patient = new Patient();
        patient.setName("john doe");
        patient.setGender(Gender.IDK);
        patient.setIdentification("54821");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        doctor = new Doctor();
        doctor.setName("daren3");
        doctor.setGender(Gender.FEMALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Orthopedics);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45));
        appointment.setExpectedStartAt(LocalDateTime.of(2025, 8, 25, 9, 00));
        appointment.setExpectedEndAt(LocalDateTime.of(2025, 8, 25, 10, 00));
        Database.appointmentList.add(appointment);

        //Fourth
        patient = new Patient();
        patient.setName("Patient Zero");
        patient.setGender(Gender.FEMALE);
        patient.setIdentification("94756");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        doctor = new Doctor();
        doctor.setName("daren4");
        doctor.setGender(Gender.FEMALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Pediatrics);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45));
        appointment.setExpectedStartAt(LocalDateTime.of(2025, 8, 26, 14, 00));
        appointment.setExpectedEndAt(LocalDateTime.of(2025, 8, 26, 15, 00));
        Database.appointmentList.add(appointment);

        //fifth
        patient = new Patient();
        patient.setName("PAX-19");
        patient.setGender(Gender.MALE);
        patient.setIdentification("09865");
        patient.setContactNumber("+123456789");
        Database.patientsList.add(patient);

        doctor = new Doctor();
        doctor.setName("daren5");
        doctor.setGender(Gender.MALE);
        doctor.setContactNumber("+123456789");
        doctor.setSpecialization(Specialization.Otorhinolaryngology);
        doctor.setSchedule(null);
        Database.doctorList.add(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setCreatedAt(LocalDateTime.of(2025, 9, 21, 16, 45));
        appointment.setExpectedStartAt(LocalDateTime.of(2025, 9, 26, 11, 00));
        appointment.setExpectedEndAt(LocalDateTime.of(2025, 10, 1, 12, 00));
        Database.appointmentList.add(appointment);

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(0))
                .setConsultationType(ConsultationType.GENERAL));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(1))
                .setConsultationType(ConsultationType.SPECIALIST));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(2))
                .setConsultationType(ConsultationType.EMERGENCY));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(3))
                .setConsultationType(ConsultationType.FOLLOW_UP));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(4))
                .setConsultationType(ConsultationType.GENERAL));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(1))
                .setConsultationType(ConsultationType.GENERAL));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(3))
                .setConsultationType(ConsultationType.SPECIALIST));

        Database.queueList.add(new ConsultationQueue()
                .setPatient(Database.patientsList.get(2))
                .setConsultationType(ConsultationType.FOLLOW_UP));
    }
}
