package edu.dsa.clinic.boundary;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.control.MedicalController;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Dispensing;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineType;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.entity.Treatment;

import javax.print.Doc;
import java.io.IOException;
import java.util.Scanner;


public class MedicalUi {


    public void viewMedical() throws IOException {
        Scanner sc = new Scanner(System.in);
        String [] menu ={
                "Create Consultation Record",
                "List Out Consultation Record",
                "Back"
        };
        while (true) {
            System.out.println("\nMedicine Function");
            System.out.println("Please select the function:");
            System.out.println("-".repeat(30));
            for (int i = 0; i < menu.length; i++) {
                System.out.printf("%d. %s%n", i + 1, menu[i]);
            }
            System.out.println("-".repeat(30));
            System.out.printf("Enter choice [1-%d]: ", menu.length);

            int choice;
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            choice = sc.nextInt();

            if (choice < 1 || choice > menu.length) {
                System.out.println("Out of range. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> insertPatientInform();
                case 2 -> System.out.println(">> Viewing consultation records...");
                case 9 -> {
                    System.out.println(">> Back");
                    return;
                }
            }
        }
    }

    public void insertPatientInform() throws IOException {
        Scanner sc = new Scanner(System.in);
        Patient patients = new Patient();
        Gender genders = null;

        Doctor doctors = new Doctor();

        System.out.println("Enter Patient Name: ");
        String name = sc.nextLine();
        patients.setName(name);
        System.out.println("Select Patient Gender: ");
        System.out.println("-".repeat(30));
        System.out.printf("%s,%s,%s","| [1] Male |"," [2] Female |"," [3] Unknown |");
        System.out.println("-".repeat(30));
        String choice = sc.nextLine();
        switch (choice) {
            case "1"-> genders =Gender.MALE;
            case "2"-> genders =Gender.FEMALE;
            case "3"-> genders=Gender.IDK;
        }
        patients.setGender(genders);
        System.out.println("Enter identification: ");
        String identification = sc.nextLine();
        patients.setIdentification(identification);
        System.out.println("Enter Contact Number: ");
        String contactNumber= sc.nextLine();
        System.out.println("-".repeat(30));
        patients.setContactNumber(contactNumber);
        insertAttendingDoctor(patients);

    }
    public void insertAttendingDoctor(Patient patient) throws IOException {
        Scanner sc = new Scanner(System.in);
        Doctor doctor = new Doctor();
        Gender genders = null;
        Specialization specialization=null;
        System.out.println("Enter Doctor Name: ");
        String name = sc.nextLine();
        System.out.println("Select Doctor Gender: ");
        System.out.println("-".repeat(30));
        System.out.printf("%s,%s,%s","| [1 Male |"," [2] Female |"," [3] Unknown |");
        System.out.println("-".repeat(30));
        String choice = sc.nextLine();
        switch (choice) {
            case "1"-> genders =Gender.MALE;
            case "2"-> genders =Gender.FEMALE;
            case "3"-> genders=Gender.IDK;
        }
        doctor.setGender(genders);
        System.out.println("-".repeat(30));
        System.out.println("Enter Contact Number: ");
        String contactNumber= sc.nextLine();
        System.out.println("-".repeat(30));
        System.out.println("Enter Doctor Specialization: ");
        System.out.printf("%s,%s,%s,%s,%s"
                ,"| [1] Neurosurgery |"
                ," [2] Pediatrics |"
                ," [3] Ophthalmology |",
                " [4] Otorhinolaryngology |"
                ," [5] Orthopedics |");
        String choice2 = sc.nextLine();
        switch (choice2) {
           case "1" -> specialization= Specialization.Neurosurgery;
           case "2" -> specialization=Specialization.Pediatrics;
           case "3" -> specialization=Specialization.Ophthalmology;
           case "4" -> specialization=Specialization.Otorhinolaryngology;
           case "5" -> specialization=Specialization.Orthopedics;
        }
        doctor.setSpecializations(specialization);
        doctor.setContactNumber(contactNumber);

        insertDiagnosisForm(patient,doctor);
    }
    public void insertDiagnosisForm(Patient patient, Doctor doctor) throws IOException {
        Scanner sc = new Scanner(System.in);
        Diagnosis diagnosis = new Diagnosis();
        Consultation consultation =new Consultation();

        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        diagnosis.setConsultation(consultation);

        System.out.println(doctor.toString());
        System.out.println("-".repeat(30));
        System.out.println(patient.toString());
        System.out.println("-".repeat(30));
        System.out.println("| Diagnosis |");
        System.out.println("-".repeat(30));
        while (true) {
            System.out.print("Enter diagnosis description: ");
            String dc = sc.nextLine().trim();

            if (dc.isEmpty()) {
                System.out.println("Description cannot be empty. Please enter again.");

            } else {
                diagnosis.setDescription(dc);
                break;
            }
        }
        System.out.println("Diagnosis notes (optional): ");
        String dn = sc.nextLine();
        diagnosis.setNotes(dn.isBlank() ? null : dn);

        while (true) {
            insertTreatmentForm(diagnosis);
            System.out.print("Add another treatment? (y/n): ");
            String more = sc.nextLine().trim();
            if (!more.equalsIgnoreCase("y")) break;
        }


        MedicalController mc = new MedicalController();
        mc.createConsultationRecord(consultation);

        System.out.println(">> Created Consultation + Diagnosis + Treatments + Prescriptions.");


    }

    public void insertTreatmentForm(Diagnosis diagnosis) throws IOException {
        Scanner sc = new Scanner(System.in);
        Prescription p = new Prescription();
        Treatment treatment = new Treatment();
        treatment.setDiagnosis(diagnosis);
        Medicine medicine = new Medicine();
        Consultation consultation =new Consultation();

        System.out.println("-".repeat(50));
        System.out.println("| Treatment |");
        System.out.println("-".repeat(50));
        System.out.println("Symptom for this treatment (e.g., Fever/Cough/Nasal Congestion): ");
        String symptom = sc.nextLine();
        while (symptom.isEmpty()) {
            System.out.println("Symptom cannot be empty. Please enter again:");
            symptom = sc.nextLine().trim();
        }
        treatment.setSymptom(symptom);
        while(true){
            p.setTreatment(treatment);

            System.out.println("-".repeat(30));
            System.out.println("| Prescription |");
            System.out.println("-".repeat(30));
            System.out.println("Medicine name: ");
            medicine.setName(sc.nextLine());
            p.setMedicine(medicine);

            int qty;
            while (true) {
                System.out.print("Quantity (>=1): ");
                String line = sc.nextLine().trim();
                try {
                    qty = Integer.parseInt(line);
                    if (qty < 1) {
                        System.out.println("Quantity must be >= 1.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. The input must be integer.");
                }
            }
            p.setQuantity(qty);
            System.out.println("Prescription notes (optional): ");
            String pn = sc.nextLine();
            p.setNotes(pn.isBlank() ? null : pn);
            treatment.getPrescriptions().add(p);

            System.out.print("Add another prescription for this treatment? (y/n): ");
            String moreP = sc.nextLine().trim();
            if (!moreP.equalsIgnoreCase("y")) break;

        }

    }


}
