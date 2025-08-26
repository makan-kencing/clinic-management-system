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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
                                .setLocation("Storage")
                                .setQuantityLeft(15))
                        .addStock(new Stock()
                                .setStockInQuantity(32)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
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
                                .setLocation("Storage")
                                .setQuantityLeft(14))
                        .addStock(new Stock()
                                .setStockInQuantity(40)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
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
                                .setLocation("Storage")
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
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
                                .setLocation("Storage")
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
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
                                .setLocation("Storage")
                                .setQuantityLeft(3))
                        .addStock(new Stock()
                                .setStockInQuantity(100)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
                                .setQuantityLeft(100))
                        .addStock(new Stock()
                                .setStockInQuantity(62)
                                .setStockInDate(LocalDateTime.of(2025, 8, 23, 12, 34))
                                .setLocation("Storage")
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
                                .setLocation("Storage")
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
                                .setLocation("Storage")
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
                                .setLocation("Storage")
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
                        .setCreatedAt(LocalDateTime.of(2025, 9, 21, 16, 45))
                        .setExpectedStartAt(LocalDateTime.of(2025, 9, 26, 11, 00))
                        .setExpectedEndAt(LocalDateTime.of(2025, 10, 1, 12, 00))
                        .setAppointmentType(ConsultationType.EMERGENCY)
        );

        // Consultations data
        // 第1条咨询记录
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
                                                .setNotes("Take 1 tablet every 12 hours"))))
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

// **上面是生成的4条模拟咨询记录数据**


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
