package edu.dsa.clinic;

import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineAdministrationType;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.entity.Treatment;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Initializer {
    private Initializer() {
    }

    public static void initialize() {
        // Patients data
        Database.patientsList.add(((Patient) new Patient().setId(1)).setName("Ahmad Faiz").setGender(Gender.MALE).setIdentification("900101-14-1234").setContactNumber("+60123450001"));
        Database.patientsList.add(((Patient) new Patient().setId(2)).setName("Nurul Aisyah").setGender(Gender.FEMALE).setIdentification("880305-08-5678").setContactNumber("+60123450002"));
        Database.patientsList.add(((Patient) new Patient().setId(3)).setName("Muhammad Hafiz").setGender(Gender.MALE).setIdentification("950712-10-4321").setContactNumber("+60123450003"));
        Database.patientsList.add(((Patient) new Patient().setId(4)).setName("Siti Mariam").setGender(Gender.FEMALE).setIdentification("920918-12-8765").setContactNumber("+60123450004"));
        Database.patientsList.add(((Patient) new Patient().setId(5)).setName("Ahmad Zulkifli").setGender(Gender.MALE).setIdentification("970424-05-1122").setContactNumber("+60123450005"));
        Database.patientsList.add(((Patient) new Patient().setId(6)).setName("Farah Hani").setGender(Gender.FEMALE).setIdentification("890623-09-3344").setContactNumber("+60123450006"));
        Database.patientsList.add(((Patient) new Patient().setId(7)).setName("Daniel Lim").setGender(Gender.MALE).setIdentification("930802-11-5566").setContactNumber("+60123450007"));
        Database.patientsList.add(((Patient) new Patient().setId(8)).setName("Chong Wei").setGender(Gender.MALE).setIdentification("910707-03-7788").setContactNumber("+60123450008"));
        Database.patientsList.add(((Patient) new Patient().setId(9)).setName("Aina Sofea").setGender(Gender.FEMALE).setIdentification("960115-06-9900").setContactNumber("+60123450009"));
        Database.patientsList.add(((Patient) new Patient().setId(10)).setName("Hafizah Rahman").setGender(Gender.FEMALE).setIdentification("940321-01-2233").setContactNumber("+60123450010"));
        Database.patientsList.add(((Patient) new Patient().setId(11)).setName("Syafiq Amir").setGender(Gender.MALE).setIdentification("901231-07-3344").setContactNumber("+60123450011"));
        Database.patientsList.add(((Patient) new Patient().setId(12)).setName("Noraini Hassan").setGender(Gender.FEMALE).setIdentification("881215-02-4455").setContactNumber("+60123450012"));
        Database.patientsList.add(((Patient) new Patient().setId(13)).setName("Irfan Azman").setGender(Gender.MALE).setIdentification("950606-09-5566").setContactNumber("+60123450013"));
        Database.patientsList.add(((Patient) new Patient().setId(14)).setName("Fatin Nadira").setGender(Gender.FEMALE).setIdentification("921101-05-6677").setContactNumber("+60123450014"));
        Database.patientsList.add(((Patient) new Patient().setId(15)).setName("Adib Syahmi").setGender(Gender.MALE).setIdentification("970530-03-7788").setContactNumber("+60123450015"));
        Database.patientsList.add(((Patient) new Patient().setId(16)).setName("Sabrina Khairun").setGender(Gender.FEMALE).setIdentification("890809-11-8899").setContactNumber("+60123450016"));
        Database.patientsList.add(((Patient) new Patient().setId(17)).setName("Wei Ling").setGender(Gender.FEMALE).setIdentification("930202-06-9900").setContactNumber("+60123450017"));
        Database.patientsList.add(((Patient) new Patient().setId(18)).setName("Jonathan Tan").setGender(Gender.MALE).setIdentification("910415-04-1122").setContactNumber("+60123450018"));
        Database.patientsList.add(((Patient) new Patient().setId(19)).setName("Nur Farah").setGender(Gender.FEMALE).setIdentification("960730-08-2233").setContactNumber("+60123450019"));
        Database.patientsList.add(((Patient) new Patient().setId(20)).setName("Ahmad Irfan").setGender(Gender.MALE).setIdentification("940909-12-3344").setContactNumber("+60123450020"));
        Database.patientsList.add(((Patient) new Patient().setId(21)).setName("Siti Zulaikha").setGender(Gender.FEMALE).setIdentification("900424-05-4455").setContactNumber("+60123450021"));
        Database.patientsList.add(((Patient) new Patient().setId(22)).setName("Daniel Ong").setGender(Gender.MALE).setIdentification("880610-09-5566").setContactNumber("+60123450022"));
        Database.patientsList.add(((Patient) new Patient().setId(23)).setName("Aina Najwa").setGender(Gender.FEMALE).setIdentification("950101-11-6677").setContactNumber("+60123450023"));
        Database.patientsList.add(((Patient) new Patient().setId(24)).setName("Hafiz Adnan").setGender(Gender.MALE).setIdentification("921212-03-7788").setContactNumber("+60123450024"));
        Database.patientsList.add(((Patient) new Patient().setId(25)).setName("Farah Iman").setGender(Gender.FEMALE).setIdentification("970303-07-8899").setContactNumber("+60123450025"));
        Database.patientsList.add(((Patient) new Patient().setId(26)).setName("Lim Wei Jie").setGender(Gender.MALE).setIdentification("890808-02-9900").setContactNumber("+60123450026"));
        Database.patientsList.add(((Patient) new Patient().setId(27)).setName("Chong Mei Ling").setGender(Gender.FEMALE).setIdentification("930515-06-1122").setContactNumber("+60123450027"));
        Database.patientsList.add(((Patient) new Patient().setId(28)).setName("Ahmad Danish").setGender(Gender.MALE).setIdentification("910707-10-2233").setContactNumber("+60123450028"));
        Database.patientsList.add(((Patient) new Patient().setId(29)).setName("Siti Hajar").setGender(Gender.FEMALE).setIdentification("960909-04-3344").setContactNumber("+60123450029"));
        Database.patientsList.add(((Patient) new Patient().setId(30)).setName("Muhammad Rizal").setGender(Gender.MALE).setIdentification("940101-08-4455").setContactNumber("+60123450030"));
        Database.patientsList.add(((Patient) new Patient().setId(31)).setName("Nurul Izzah").setGender(Gender.FEMALE).setIdentification("900515-12-5566").setContactNumber("+60123450031"));
        Database.patientsList.add(((Patient) new Patient().setId(32)).setName("Adib Hakim").setGender(Gender.MALE).setIdentification("880303-03-6677").setContactNumber("+60123450032"));
        Database.patientsList.add(((Patient) new Patient().setId(33)).setName("Sabrina Syafiqah").setGender(Gender.FEMALE).setIdentification("950909-07-7788").setContactNumber("+60123450033"));
        Database.patientsList.add(((Patient) new Patient().setId(34)).setName("Wei Han").setGender(Gender.MALE).setIdentification("921212-01-8899").setContactNumber("+60123450034"));
        Database.patientsList.add(((Patient) new Patient().setId(35)).setName("Jonathan Lee").setGender(Gender.MALE).setIdentification("970101-05-9900").setContactNumber("+60123450035"));
        Database.patientsList.add(((Patient) new Patient().setId(36)).setName("Nurul Huda").setGender(Gender.FEMALE).setIdentification("890909-09-1122").setContactNumber("+60123450036"));
        Database.patientsList.add(((Patient) new Patient().setId(37)).setName("Ahmad Nabil").setGender(Gender.MALE).setIdentification("930303-04-2233").setContactNumber("+60123450037"));
        Database.patientsList.add(((Patient) new Patient().setId(38)).setName("Siti Sarah").setGender(Gender.FEMALE).setIdentification("910808-11-3344").setContactNumber("+60123450038"));
        Database.patientsList.add(((Patient) new Patient().setId(39)).setName("Hafiz Firdaus").setGender(Gender.MALE).setIdentification("960101-06-4455").setContactNumber("+60123450039"));
        Database.patientsList.add(((Patient) new Patient().setId(40)).setName("Farah Aina").setGender(Gender.FEMALE).setIdentification("940404-08-5566").setContactNumber("+60123450040"));
        Database.patientsList.add(((Patient) new Patient().setId(41)).setName("Ahmad Saif").setGender(Gender.MALE).setIdentification("900305-01-1122").setContactNumber("+60123450041"));
        Database.patientsList.add(((Patient) new Patient().setId(42)).setName("Siti Aishah").setGender(Gender.FEMALE).setIdentification("880712-02-2233").setContactNumber("+60123450042"));
        Database.patientsList.add(((Patient) new Patient().setId(43)).setName("Muhammad Danish").setGender(Gender.MALE).setIdentification("950901-03-3344").setContactNumber("+60123450043"));
        Database.patientsList.add(((Patient) new Patient().setId(44)).setName("Nur Farzana").setGender(Gender.FEMALE).setIdentification("921001-04-4455").setContactNumber("+60123450044"));
        Database.patientsList.add(((Patient) new Patient().setId(45)).setName("Adib Zulkarnain").setGender(Gender.MALE).setIdentification("970212-05-5566").setContactNumber("+60123450045"));
        Database.patientsList.add(((Patient) new Patient().setId(46)).setName("Fatin Amani").setGender(Gender.FEMALE).setIdentification("890109-06-6677").setContactNumber("+60123450046"));
        Database.patientsList.add(((Patient) new Patient().setId(47)).setName("Lim Shi Wen").setGender(Gender.MALE).setIdentification("930601-07-7788").setContactNumber("+60123450047"));
        Database.patientsList.add(((Patient) new Patient().setId(48)).setName("Chong Mei Fang").setGender(Gender.FEMALE).setIdentification("910803-08-8899").setContactNumber("+60123450048"));
        Database.patientsList.add(((Patient) new Patient().setId(49)).setName("Ahmad Hakim").setGender(Gender.MALE).setIdentification("960506-09-9900").setContactNumber("+60123450049"));
        Database.patientsList.add(((Patient) new Patient().setId(50)).setName("Siti Nurul").setGender(Gender.FEMALE).setIdentification("940202-10-1122").setContactNumber("+60123450050"));
        Database.patientsList.add(((Patient) new Patient().setId(51)).setName("Muhammad Haziq").setGender(Gender.MALE).setIdentification("900707-11-2233").setContactNumber("+60123450051"));
        Database.patientsList.add(((Patient) new Patient().setId(52)).setName("Nurul Hidayah").setGender(Gender.FEMALE).setIdentification("880808-12-3344").setContactNumber("+60123450052"));
        Database.patientsList.add(((Patient) new Patient().setId(53)).setName("Ahmad Firdaus").setGender(Gender.MALE).setIdentification("950505-01-4455").setContactNumber("+60123450053"));
        Database.patientsList.add(((Patient) new Patient().setId(54)).setName("Siti Khadijah").setGender(Gender.FEMALE).setIdentification("921212-02-5566").setContactNumber("+60123450054"));
        Database.patientsList.add(((Patient) new Patient().setId(55)).setName("Adib Farhan").setGender(Gender.MALE).setIdentification("970303-03-6677").setContactNumber("+60123450055"));
        Database.patientsList.add(((Patient) new Patient().setId(56)).setName("Fatin Najwa").setGender(Gender.FEMALE).setIdentification("890909-04-7788").setContactNumber("+60123450056"));
        Database.patientsList.add(((Patient) new Patient().setId(57)).setName("Wei Jie").setGender(Gender.MALE).setIdentification("930101-05-8899").setContactNumber("+60123450057"));
        Database.patientsList.add(((Patient) new Patient().setId(58)).setName("Jonathan Chia").setGender(Gender.MALE).setIdentification("910202-06-9900").setContactNumber("+60123450058"));
        Database.patientsList.add(((Patient) new Patient().setId(59)).setName("Nurul Syahirah").setGender(Gender.FEMALE).setIdentification("960606-07-1122").setContactNumber("+60123450059"));
        Database.patientsList.add(((Patient) new Patient().setId(60)).setName("Ahmad Faizal").setGender(Gender.MALE).setIdentification("940707-08-2233").setContactNumber("+60123450060"));
        Database.patientsList.add(((Patient) new Patient().setId(61)).setName("Siti Hawa").setGender(Gender.FEMALE).setIdentification("900808-09-3344").setContactNumber("+60123450061"));
        Database.patientsList.add(((Patient) new Patient().setId(62)).setName("Muhammad Aiman").setGender(Gender.MALE).setIdentification("880909-10-4455").setContactNumber("+60123450062"));
        Database.patientsList.add(((Patient) new Patient().setId(63)).setName("Nurul Iman").setGender(Gender.FEMALE).setIdentification("950101-11-5566").setContactNumber("+60123450063"));
        Database.patientsList.add(((Patient) new Patient().setId(64)).setName("Adib Syafiq").setGender(Gender.MALE).setIdentification("921212-12-6677").setContactNumber("+60123450064"));
        Database.patientsList.add(((Patient) new Patient().setId(65)).setName("Fatin Husna").setGender(Gender.FEMALE).setIdentification("970303-01-7788").setContactNumber("+60123450065"));
        Database.patientsList.add(((Patient) new Patient().setId(66)).setName("Lim Wei Han").setGender(Gender.MALE).setIdentification("890909-02-8899").setContactNumber("+60123450066"));
        Database.patientsList.add(((Patient) new Patient().setId(67)).setName("Chong Mei Hui").setGender(Gender.FEMALE).setIdentification("930101-03-9900").setContactNumber("+60123450067"));
        Database.patientsList.add(((Patient) new Patient().setId(68)).setName("Ahmad Hafiz").setGender(Gender.MALE).setIdentification("910202-04-1122").setContactNumber("+60123450068"));
        Database.patientsList.add(((Patient) new Patient().setId(69)).setName("Siti Aina").setGender(Gender.FEMALE).setIdentification("960606-05-2233").setContactNumber("+60123450069"));
        Database.patientsList.add(((Patient) new Patient().setId(70)).setName("Muhammad Farid").setGender(Gender.MALE).setIdentification("940707-06-3344").setContactNumber("+60123450070"));
        Database.patientsList.add(((Patient) new Patient().setId(71)).setName("Nurul Shafiqah").setGender(Gender.FEMALE).setIdentification("900808-07-4455").setContactNumber("+60123450071"));
        Database.patientsList.add(((Patient) new Patient().setId(72)).setName("Ahmad Zain").setGender(Gender.MALE).setIdentification("880909-08-5566").setContactNumber("+60123450072"));
        Database.patientsList.add(((Patient) new Patient().setId(73)).setName("Siti Mariam").setGender(Gender.FEMALE).setIdentification("950101-09-6677").setContactNumber("+60123450073"));
        Database.patientsList.add(((Patient) new Patient().setId(74)).setName("Adib Hakim").setGender(Gender.MALE).setIdentification("921212-10-7788").setContactNumber("+60123450074"));
        Database.patientsList.add(((Patient) new Patient().setId(75)).setName("Fatin Aisyah").setGender(Gender.FEMALE).setIdentification("970303-11-8899").setContactNumber("+60123450075"));
        Database.patientsList.add(((Patient) new Patient().setId(76)).setName("Lim Shi Han").setGender(Gender.MALE).setIdentification("890909-12-9900").setContactNumber("+60123450076"));
        Database.patientsList.add(((Patient) new Patient().setId(77)).setName("Chong Mei Yan").setGender(Gender.FEMALE).setIdentification("930101-01-1122").setContactNumber("+60123450077"));
        Database.patientsList.add(((Patient) new Patient().setId(78)).setName("Ahmad Haziq").setGender(Gender.MALE).setIdentification("910202-02-2233").setContactNumber("+60123450078"));
        Database.patientsList.add(((Patient) new Patient().setId(79)).setName("Siti Hajar").setGender(Gender.FEMALE).setIdentification("960606-03-3344").setContactNumber("+60123450079"));
        Database.patientsList.add(((Patient) new Patient().setId(80)).setName("Muhammad Danish").setGender(Gender.MALE).setIdentification("940707-04-4455").setContactNumber("+60123450080"));
        Database.patientsList.add(((Patient) new Patient().setId(81)).setName("Nurul Izzah").setGender(Gender.FEMALE).setIdentification("900808-05-5566").setContactNumber("+60123450081"));
        Database.patientsList.add(((Patient) new Patient().setId(82)).setName("Ahmad Faizal").setGender(Gender.MALE).setIdentification("880909-06-6677").setContactNumber("+60123450082"));
        Database.patientsList.add(((Patient) new Patient().setId(83)).setName("Siti Nadira").setGender(Gender.FEMALE).setIdentification("950101-07-7788").setContactNumber("+60123450083"));
        Database.patientsList.add(((Patient) new Patient().setId(84)).setName("Adib Syahmi").setGender(Gender.MALE).setIdentification("921212-08-8899").setContactNumber("+60123450084"));
        Database.patientsList.add(((Patient) new Patient().setId(85)).setName("Fatin Husna").setGender(Gender.FEMALE).setIdentification("970303-09-9900").setContactNumber("+60123450085"));
        Database.patientsList.add(((Patient) new Patient().setId(86)).setName("Lim Wei Han").setGender(Gender.MALE).setIdentification("890909-10-1122").setContactNumber("+60123450086"));
        Database.patientsList.add(((Patient) new Patient().setId(87)).setName("Chong Mei Hui").setGender(Gender.FEMALE).setIdentification("930101-11-2233").setContactNumber("+60123450087"));
        Database.patientsList.add(((Patient) new Patient().setId(88)).setName("Ahmad Hafiz").setGender(Gender.MALE).setIdentification("910202-12-3344").setContactNumber("+60123450088"));
        Database.patientsList.add(((Patient) new Patient().setId(89)).setName("Siti Aina").setGender(Gender.FEMALE).setIdentification("960606-01-4455").setContactNumber("+60123450089"));
        Database.patientsList.add(((Patient) new Patient().setId(90)).setName("Muhammad Farid").setGender(Gender.MALE).setIdentification("940707-02-5566").setContactNumber("+60123450090"));
        Database.patientsList.add(((Patient) new Patient().setId(91)).setName("Nurul Shafiqah").setGender(Gender.FEMALE).setIdentification("900808-03-6677").setContactNumber("+60123450091"));
        Database.patientsList.add(((Patient) new Patient().setId(92)).setName("Ahmad Zain").setGender(Gender.MALE).setIdentification("880909-04-7788").setContactNumber("+60123450092"));
        Database.patientsList.add(((Patient) new Patient().setId(93)).setName("Siti Mariam").setGender(Gender.FEMALE).setIdentification("950101-05-8899").setContactNumber("+60123450093"));
        Database.patientsList.add(((Patient) new Patient().setId(94)).setName("Adib Hakim").setGender(Gender.MALE).setIdentification("921212-06-9900").setContactNumber("+60123450094"));
        Database.patientsList.add(((Patient) new Patient().setId(95)).setName("Fatin Aisyah").setGender(Gender.FEMALE).setIdentification("970303-07-1122").setContactNumber("+60123450095"));
        Database.patientsList.add(((Patient) new Patient().setId(96)).setName("Lim Shi Han").setGender(Gender.MALE).setIdentification("890909-08-2233").setContactNumber("+60123450096"));
        Database.patientsList.add(((Patient) new Patient().setId(97)).setName("Chong Mei Yan").setGender(Gender.FEMALE).setIdentification("930101-09-3344").setContactNumber("+60123450097"));
        Database.patientsList.add(((Patient) new Patient().setId(98)).setName("Ahmad Haziq").setGender(Gender.MALE).setIdentification("910202-10-4455").setContactNumber("+60123450098"));
        Database.patientsList.add(((Patient) new Patient().setId(99)).setName("Siti Hajar").setGender(Gender.FEMALE).setIdentification("960606-11-5566").setContactNumber("+60123450099"));
        Database.patientsList.add(((Patient) new Patient().setId(100)).setName("Muhammad Danish").setGender(Gender.MALE).setIdentification("940707-12-6677").setContactNumber("+60123450100"));

        // Doctors data
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(1))
                        .setName("daren1")
                        .setGender(Gender.MALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Neurosurgery)
                        .setSchedule(new Schedule()
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(13, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)))
                                                .setType(ShiftType.WORK)))
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(2))
                        .setName("daren2")
                        .setGender(Gender.IDK)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Ophthalmology)
                        .setSchedule(new Schedule()
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(13, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)))
                                                .setType(ShiftType.WORK)))
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(3))
                        .setName("daren3")
                        .setGender(Gender.FEMALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Orthopedics)
                        .setSchedule(new Schedule()
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(13, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)))
                                                .setType(ShiftType.WORK)))
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(4))
                        .setName("daren4")
                        .setGender(Gender.FEMALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Pediatrics)
                        .setSchedule(new Schedule()
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(13, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)))
                                                .setType(ShiftType.WORK)))
        );
        Database.doctorList.add(
                ((Doctor) new Doctor()
                        .setId(5))
                        .setName("daren5")
                        .setGender(Gender.MALE)
                        .setContactNumber("+123456789")
                        .setSpecialization(Specialization.Otorhinolaryngology)
                        .setSchedule(new Schedule()
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(13, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.MONDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(14, 0), LocalTime.of(20, 0)))
                                                .setType(ShiftType.WORK))
                                .addShift(DayOfWeek.THURSDAY,
                                        new Shift()
                                                .setTimeRange(new Range<>(LocalTime.of(10, 0), LocalTime.of(16, 0)))
                                                .setType(ShiftType.WORK)))
        );

        // Medicines data
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(1))
                        .setName("Acetylsalicylic acid")
                        .addType(MedicineType.ANALGESICS)
                        .addType(MedicineType.ANTIPYRETICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(2))
                        .setName("Paracetamol")
                        .addType(MedicineType.ANALGESICS)
                        .addType(MedicineType.ANTIPYRETICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(3))
                        .setName("Ibuprofen")
                        .addType(MedicineType.ANALGESICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(4))
                        .setName("Penicillin")
                        .addType(MedicineType.ANTIBIOTICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(5))
                        .setName("Diphenhydramine")
                        .addType(MedicineType.ANTIHISTAMINES)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(6))
                        .setName("Centrizine")
                        .addType(MedicineType.ANTIHISTAMINES)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(7))
                        .setName("Loratadine")
                        .addType(MedicineType.ANTIHISTAMINES)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(8))
                        .setName("Warfarin")
                        .addType(MedicineType.ANTICOAGULANTS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(9))
                        .setName("Heparin")
                        .addType(MedicineType.ANTICOAGULANTS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(10))
                        .setName("Clozapine")
                        .addType(MedicineType.ANTIPSYCHOTICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(11))
                        .setName("Dexamethasone")
                        .addType(MedicineType.CORTICOSTEROIDS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(12))
                        .setName("Rosuvastatin")
                        .addType(MedicineType.STATINS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(13))
                        .setName("Calcium Carbonate")
                        .addType(MedicineType.ANTACIDS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(13))
                        .setName("Magnesium Hydroxide")
                        .addType(MedicineType.ANTACIDS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(14))
                        .setName("Ondansetron")
                        .addType(MedicineType.ANTIEMETICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(15))
                        .setName("Metaclopramide")
                        .addType(MedicineType.ANTIEMETICS)
        );
        Database.medicineList.add(
                ((Medicine) new Medicine()
                        .setId(16))
                        .setName("Insulin")
                        .addType(MedicineType.ANTIDIABETICS)
        );

        // Products data
        // https://ndclist.com/ndc/0536-1054/package/0536-1054-29/price
        Database.productList.add(
                ((Product) new Product()
                        .setId(1))
                        .setName("Aspirin")
                        .setBrand("Recadin")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0.01529"))
                        .setPrice(new BigDecimal("1"))
                        .addStock(new Stock()
                                .setStockInQuantity(32)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(15))
                        .addStock(new Stock()
                                .setStockInQuantity(32)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(32))
        );
        Database.productList.add(
                ((Product) new Product()
                        .setId(2))
                        .setName("Aspirin")
                        .setBrand("Viquprin")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 1))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0.01529"))
                        .setPrice(new BigDecimal("1"))
                        .addStock(new Stock()
                                .setStockInQuantity(60)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(14))
                        .addStock(new Stock()
                                .setStockInQuantity(40)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(40))
        );
        Database.productList.add(  // https://ndclist.com/ndc/0135-7021
                ((Product) new Product()
                        .setId(3))
                        .setName("Panadol PM")
                        .setBrand("Panadol")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(62))
        );
        Database.productList.add(  // https://ndclist.com/ndc/0135-0620
                ((Product) new Product()
                        .setId(4))
                        .setName("Panadol Extra")
                        .setBrand("Panadol")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(62))
        );
        Database.productList.add(  // https://ndclist.com/ndc/0135-0609
                ((Product) new Product()
                        .setId(5))
                        .setName("Panadol Extra Strength")
                        .setBrand("Panadol")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setQuantityLeft(62))
        );
        Database.productList.add(
                ((Product) new Product()
                        .setId(6))
                        .setName("Paracetamol")
                        .setBrand("Tylenol")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 2))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(30)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(27))
        );
        Database.productList.add(
                ((Product) new Product()
                        .setId(7))
                        .setName("Ibuprofen")
                        .setBrand("Advil")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 3))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(30)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(13))
        );
        Database.productList.add(
                ((Product) new Product()
                        .setId(8))
                        .setName("Centrizine")
                        .setBrand("")
                        .setMedicine(Database.medicineList.findFirst(m -> m.getId() == 6))
                        .setAdministrationType(MedicineAdministrationType.ORAL)
                        .setCost(new BigDecimal("0"))
                        .setPrice(new BigDecimal("0"))
                        .addStock(new Stock()
                                .setStockInQuantity(30)
                                .setStockInDate(LocalDateTime.of(2025, 8, 15, 10, 13))
                                .setQuantityLeft(13))
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
                        .setAppointmentType(ConsultationType.GENERAL)
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(2))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 2))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 2))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 24, 9, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 24, 10, 00))
                        .setAppointmentType(ConsultationType.FOLLOW_UP)
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(3))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 3))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 3))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 25, 9, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 25, 10, 00))
                        .setAppointmentType(ConsultationType.SPECIALIST)
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(4))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 4))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 4))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 26, 14, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 26, 15, 00))
                        .setAppointmentType(ConsultationType.GENERAL)
        );
        Database.appointmentList.add(
                ((Appointment) new Appointment()
                        .setId(5))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 5))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 5))
                        .setCreatedAt(LocalDateTime.of(2025, 8, 16, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 8, 28, 11, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 8, 28, 12, 00))
                        .setAppointmentType(ConsultationType.EMERGENCY)
        );

        // Consultations data
        Database.consultationsList.add(
                ((Consultation) new Consultation()
                        .setId(1))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 1))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 1))
                        .setType(ConsultationType.GENERAL)
                        .setNotes("Patient reports fever and sore throat.")
                        .setConsultedAt(LocalDateTime.of(2025, 9, 26, 11, 00))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fever")
                                .setDescription("High fever, patient feels weak, needs rest and hydration.")
                                .setNotes("Initial treatment with antipyretics.")
                                .addTreatment(new Treatment()
                                        .setSymptom("High fever")
                                        .setNotes("Administer paracetamol 500mg every 6 hours.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 1)) // Paracetamol
                                                .setQuantity(10)
                                                .setNotes("500mg, take 1 every 6 hours"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Sore Throat")
                                .setDescription("Throat redness, mild pain, treated with lozenges.")
                                .setNotes("Recommend throat lozenges.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Sore Throat")
                                        .setNotes("Recommend warm saline gargle and throat lozenges.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 2)) // Lozenges
                                                .setQuantity(20)
                                                .setNotes("Take one lozenge every 4 hours"))))
        );


        Database.consultationsList.add(
                ((Consultation) new Consultation()
                        .setId(2))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 2))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 2))
                        .setType(ConsultationType.SPECIALIST)
                        .setNotes("Patient reports a persistent cough, moderate difficulty breathing.")
                        .setConsultedAt(LocalDateTime.of(2025, 8, 26, 14, 00))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Cough")
                                .setDescription("Persistent cough, no significant mucus production, possible viral infection.")
                                .setNotes("Initial treatment with expectorants.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Persistent Cough")
                                        .setNotes("Administer expectorants to relieve cough.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 3)) // Cough syrup
                                                .setQuantity(1)
                                                .setNotes("Take 5ml three times a day"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Breathing Difficulty")
                                .setDescription("Mild shortness of breath, no wheezing, monitor closely.")
                                .setNotes("May require bronchodilators if symptoms worsen.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Shortness of Breath")
                                        .setNotes("Monitor symptoms, use inhaler if necessary.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 4)) // Inhaler
                                                .setQuantity(1)
                                                .setNotes("Use as needed for shortness of breath."))))
        );


        Database.consultationsList.add(
                ((Consultation) new Consultation()
                        .setId(3))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 3))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 3))
                        .setType(ConsultationType.EMERGENCY)
                        .setNotes("Emergency consultation for severe abdominal pain and vomiting.")
                        .setConsultedAt(LocalDateTime.of(2025, 7, 15, 9, 00))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Abdominal Pain")
                                .setDescription("Severe abdominal pain, nausea, vomiting. Possible gastrointestinal infection.")
                                .setNotes("Treatment with antispasmodics and hydration.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Severe Abdominal Pain")
                                        .setNotes("Administer Buscopan and fluids for hydration.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 5)) // Buscopan
                                                .setQuantity(10)
                                                .setNotes("Take 1 tablet every 8 hours"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Vomiting")
                                .setDescription("Frequent vomiting, cannot keep food down.")
                                .setNotes("Administer antiemetics and monitor hydration.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Vomiting")
                                        .setNotes("Administer Ondansetron for nausea.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 6)) // Ondansetron
                                                .setQuantity(10)
                                                .setNotes("Take 1 tablet every 12 hours")
                                        )
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 7))
                                                .setQuantity(10)
                                                .setNotes("Take 1 tablet every 12 hours")
                                        )
                                )
                        )

        );

        Database.consultationsList.add(
                ((Consultation) new Consultation()
                        .setId(4))
                        .setPatient(Database.patientsList.findFirst(p -> p.getId() == 4))
                        .setDoctor(Database.doctorList.findFirst(d -> d.getId() == 4))
                        .setType(ConsultationType.FOLLOW_UP)
                        .setNotes("Follow-up consultation for hypertension management.")
                        .setConsultedAt(LocalDateTime.of(2025, 6, 10, 10, 00))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Hypertension")
                                .setDescription("High blood pressure, ongoing treatment with antihypertensive medications.")
                                .setNotes("Continue current medications, monitor blood pressure.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Elevated Blood Pressure")
                                        .setNotes("Continue medication regimen.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 7)) // Lisinopril
                                                .setQuantity(30)
                                                .setNotes("Take 1 tablet daily"))))
                        .addDiagnosis(new Diagnosis()
                                .setDiagnosis("Fatigue")
                                .setDescription("General fatigue, stress-related.")
                                .setNotes("Recommend lifestyle changes, stress management techniques.")
                                .addTreatment(new Treatment()
                                        .setSymptom("Fatigue")
                                        .setNotes("Advise rest, proper diet, and exercise.")
                                        .addPrescription(new Prescription()
                                                .setProduct(Database.productList.findFirst(m -> m.getId() == 8)) // Vitamin B12
                                                .setQuantity(30)
                                                .setNotes("Take 1 tablet daily for 30 days"))))
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
