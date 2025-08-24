/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

/**
 *
 * @author Bincent
 */

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

public class PatientController {

    public void createPatientRecord(Patient patient) {
        Database.patientsList.add(patient);
    }

    public Patient editPatientRecord(Patient newPatient) {
        Patient oldPatient = Database.patientsList.findFirst(p -> p.getId() == newPatient.getId());
        if (oldPatient != null) {
            oldPatient.setName(newPatient.getName())
                    .setGender(newPatient.getGender())
                    .setIdentification(newPatient.getIdentification())
                    .setContactNumber(newPatient.getContactNumber());
        }
        return oldPatient;
    }

    public void removePatientRecord(Patient patient) {
        Database.patientsList.removeFirst(p -> p.getId() == patient.getId());
    }

    public ListInterface<Consultation> getPatientConsultations(Patient patient) {
        return Database.consultationsList.filtered(c -> c.getPatient().equals(patient));
    }

    public ListInterface<Diagnosis> getPatientDiagnoses(Patient patient) {
        ListInterface<Diagnosis> patientDiagnoses = new DoubleLinkedList<>();
        for (Consultation c : getPatientConsultations(patient)) {
            patientDiagnoses.extend(c.getDiagnoses());
        }
        return patientDiagnoses;
    }

    public ListInterface<Treatment> getPatientTreatments(Patient patient) {
        ListInterface<Treatment> patientTreatments = new DoubleLinkedList<>();
        for (Diagnosis d : getPatientDiagnoses(patient)) {
            patientTreatments.extend(d.getTreatments());
        }
        return patientTreatments;
    }

    public ListInterface<Prescription> getPatientPrescriptions(Patient patient) {
        ListInterface<Prescription> patientPrescriptions = new DoubleLinkedList<>();
        for (Treatment t : getPatientTreatments(patient)) {
            patientPrescriptions.extend(t.getPrescriptions());
        }
        return patientPrescriptions;
    }

    public void createQueue(ConsultationQueue queue) {
        Database.queueList.add(queue);
    }

    public void removeQueue(ConsultationQueue queue) {
        Database.queueList.removeFirst(c -> c.queueNo() == queue.queueNo());
    }

    public ConsultationQueue getFirstQueue() {
        return Database.queueList.popFirst();
    }

    public void viewSummaryReport() {

    }

    public Object performSelect(int selectedId, String field) {
        return switch (field) {
            case "patient" -> Database.patientsList.findFirst(p -> p.getId() == selectedId);
            case "consultation" -> Database.queueList.findFirst(c -> c.queueNo() == selectedId);
            default -> null;
        };
    }

    public ConsultationQueue validateUnique(Patient patient) {
        return Database.queueList.findFirst(c -> c.patient().getId() == patient.getId());
    }
}
