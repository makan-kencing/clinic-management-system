package edu.dsa.clinic;

import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.dto.ConsultationQueue;

import java.time.Instant;
import java.time.LocalDateTime;

public class Initializer {
    private Initializer() {
    }

    public static void initialize() {
        // Patients data
        Database.patientsList.add(
                ((Patient) new Patient()
                        .setId(1))
                        .setName("hi")
                        .setGender(Gender.MALE)
                        .setIdentification("01234")
                        .setContactNumber("+123456789")
        );
        Database.patientsList.add(
                ((Patient) new Patient()
                        .setId(2))
                        .setName("hello")
                        .setGender(Gender.IDK)
                        .setIdentification("31324")
                        .setContactNumber("+123456789")
        );
        Database.patientsList.add(
                ((Patient) new Patient()
                        .setId(3))
                        .setName("john doe")
                        .setGender(Gender.IDK)
                        .setIdentification("54821")
                        .setContactNumber("+123456789")
        );
        Database.patientsList.add(
                ((Patient) new Patient()
                        .setId(4))
                        .setName("Patient Zero")
                        .setGender(Gender.FEMALE)
                        .setIdentification("94756")
                        .setContactNumber("+123456789")
        );
        Database.patientsList.add(
                ((Patient) new Patient()
                        .setId(5))
                        .setName("PAX-19")
                        .setGender(Gender.MALE)
                        .setIdentification("09865")
                        .setContactNumber("+123456789")
        );

        // Doctors data
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(1))
                        .setName("daren1")
                        .setGender(Gender.MALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Neurosurgery)
                        .setSchedule(null)
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(2))
                        .setName("daren2")
                        .setGender(Gender.IDK)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Ophthalmology)
                        .setSchedule(null)
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(3))
                        .setName("daren3")
                        .setGender(Gender.FEMALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Orthopedics)
                        .setSchedule(null)
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(4))
                        .setName("daren4")
                        .setGender(Gender.FEMALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Pediatrics)
                        .setSchedule(null)
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(5))
                        .setName("daren5")
                        .setGender(Gender.MALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Otorhinolaryngology)
                        .setSchedule(null)
        );

        // Appointments data
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(1))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 1))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 1))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 11, 30))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 22, 8, 30))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 22, 9, 30))
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(2))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 2))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 2))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 24, 9, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 24, 10, 00))
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(3))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 3))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 3))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 25, 9, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 25, 10, 00))
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(4))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 4))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 4))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 26, 14, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 26, 15, 00))
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(5))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 5))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 5))
                        .setCreatedAt(LocalDateTime.of(2025, 9, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 9, 26, 11, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 10, 1, 12, 00))
        );

        // Consultations data
        Database.consultationsList.add(
                new Consultation()
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 1))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 1))
                        .setType(ConsultationType.GENERAL)
                        .setNotes("This is a consultation")
                        .setConsultedAt(Instant.parse("2025-06-05T10:15:30Z"))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fever")
                                .setDescription("Let patient become ok ")
                                .setNotes("good")
                                .addTreatment(new Treatment()
                                        .setSymptom("hhhhhhhhhhhhhhhh")
                                        .setNotes("uuuuuuuuuuuuuuuuuuuu")
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                                                .setQuantity(1)
                                                .setNotes("llllll"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fever")
                                .setDescription("Let patient become ok ")
                                .setNotes("good")
                                .addTreatment(new Treatment()
                                        .setSymptom("hhhhhhhhhhhhhhhh")
                                        .setNotes("uuuuuuuuuuuuuuuuuuuu")
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                                                .setQuantity(1)
                                                .setNotes("llllll"))
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                                                .setQuantity(1)
                                                .setNotes("llllll"))))


        );

        Database.consultationsList.add(
                new Consultation()
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 2))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 2))
                        .setType(ConsultationType.GENERAL)
                        .setNotes("This is a consultation")
                        .setConsultedAt(Instant.parse("2025-06-05T10:15:30Z"))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fever")
                                .setDescription("Let patient become ok ")
                                .setNotes("good")
                                .addTreatment(new Treatment()
                                        .setSymptom("hhhhhhhhhhhhhhhh")
                                        .setNotes("uuuuuuuuuuuuuuuuuuuu")
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                                                .setQuantity(1)
                                                .setNotes("llllll"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fever")
                                .setDescription("Let patient become ok ")
                                .setNotes("good")
                                .addTreatment(new Treatment()
                                        .setSymptom("hhhhhhhhhhhhhhhh")
                                        .setNotes("uuuuuuuuuuuuuuuuuuuu")
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                                                .setQuantity(1)
                                                .setNotes("llllll"))
                                        .addPrescription(new Prescription()
                                                .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                                                .setQuantity(1)
                                                .setNotes("llllll"))))


        );

        // Consultation queues data
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(1), ConsultationType.GENERAL));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(1), ConsultationType.SPECIALIST));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(2), ConsultationType.EMERGENCY));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(3), ConsultationType.FOLLOW_UP));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(4), ConsultationType.GENERAL));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(1), ConsultationType.GENERAL));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(4), ConsultationType.SPECIALIST));
        Database.queueList.add(new ConsultationQueue(Database.patientsList.get(2), ConsultationType.FOLLOW_UP));
    }
}
