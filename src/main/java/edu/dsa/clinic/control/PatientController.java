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
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.dto.ConsultationQueue;
import edu.dsa.clinic.dto.PatientCounter;
import edu.dsa.clinic.dto.PatientDetail;
import edu.dsa.clinic.dto.ProductCounter;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

import java.util.Comparator;

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

    public ListInterface<PatientCounter> getPatientSummary() {
        ListInterface<PatientCounter> patientCounters = new DoubleLinkedList<>();

        for (var consult : Database.consultationsList) {
            var patient = consult.getPatient();

            var existing = patientCounters.findFirst(pc -> pc.key().equals(patient));
            if (existing == null) {
                existing = new PatientCounter(patient);
                patientCounters.add(existing);
            }

            existing.incrementConsultationCount();

            for (var diag : consult.getDiagnoses()) {
                for (var treat : diag.getTreatments()) {
                    for (var prescription : treat.getPrescriptions()) {
                        var product = prescription.getProduct();
                        var productCounters = existing.productCounters();

                        var productCounter = productCounters.findFirst(
                                pc -> pc.key().getName().equalsIgnoreCase(product.getName())
                        );
                        if (productCounter == null) {
                            productCounter = new ProductCounter(product);
                            productCounters.add(productCounter);
                        }
                        productCounter.increment();
                    }
                }
            }
        }

        SortedDoubleLinkedList<PatientCounter> sorted =
                new SortedDoubleLinkedList<>((a, b) -> Integer.compare(b.getConsultationCount(), a.getConsultationCount()));

        for (int i = 0; i < patientCounters.size(); i++) {
            sorted.add(patientCounters.get(i));
        }

        return sorted;
    }

    private ListInterface<PatientCounter> getTopPatients(
            int topN,
            Comparator<PatientCounter> comparator
    ) {
        var counters = getPatientSummary();
        var sorted = counters.sorted(comparator);

        var result = new DoubleLinkedList<PatientCounter>();
        for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
            result.add(sorted.get(i));
        }
        return result;
    }

    public ListInterface<PatientCounter> getTopPatientsByConsultations(int topN) {
        return getTopPatients(
                topN,
                (a, b) -> Integer.compare(b.getConsultationCount(), a.getConsultationCount())
        );
    }

    public ListInterface<PatientCounter> getTopPatientsByPrescriptions(int topN) {
        return getTopPatients(
                topN,
                (a, b) -> {
                    int aCount = 0, bCount = 0;
                    for (var prod : a.productCounters()) aCount += prod.count();
                    for (var prod : b.productCounters()) bCount += prod.count();
                    return Integer.compare(bCount, aCount);
                }
        );
    }

    public ListInterface<String> getMedicineList(PatientCounter pc) {
        ListInterface<String> medicineList = new DoubleLinkedList<>();

        for (int j = 0; j < pc.productCounters().size(); j++) {
            var productCounter = pc.productCounters().get(j);
            medicineList.add(productCounter.key().getName() + "(" + productCounter.count() + ")");
        }

        return medicineList;
    }

    public ListInterface<String> getExtremePatients(boolean findMax) {
        ListInterface<String> result = new DoubleLinkedList<>();
        ListInterface<PatientCounter> counters = getPatientSummary();

        if (counters == null) return result;

        int extreme = findMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < counters.size(); i++) {
            int count = counters.get(i).getConsultationCount();
            if (findMax && count > extreme) {
                extreme = count;
            } else if (!findMax && count < extreme) {
                extreme = count;
            }
        }

        for (int i = 0; i < counters.size(); i++) {
            PatientCounter pc = counters.get(i);
            if (pc.getConsultationCount() == extreme) {
                result.add(pc.key().getName() + " (" + pc.getConsultationCount() + ")");
            }
        }

        return result;
    }

    public ListInterface<Integer> getTotalStats(int topN) {
        ListInterface<PatientCounter> counters = getPatientSummary();

        counters.sort((a, b) -> Integer.compare(b.getConsultationCount(), a.getConsultationCount()));

        ListInterface<PatientCounter> topCounters = new DoubleLinkedList<>();
        for (int i = 0; i < Math.min(topN, counters.size()); i++) {
            topCounters.add(counters.get(i));
        }

        int totalConsultations = 0;
        int totalPrescriptions = 0;

        for (int i = 0; i < topCounters.size(); i++) {
            PatientCounter pc = topCounters.get(i);
            totalConsultations += pc.getConsultationCount();

            for (int j = 0; j < pc.productCounters().size(); j++) {
                totalPrescriptions += pc.productCounters().get(j).count();
            }
        }

        ListInterface<Integer> result = new DoubleLinkedList<>();
        result.add(topCounters.size());
        result.add(totalConsultations);
        result.add(totalPrescriptions);
        return result;
    }

    public Object performSelect(int selectedId, String field) {
        return switch (field) {
            case "patient" -> Database.patientsList.findFirst(p -> p.getId() == selectedId);
            case "consultation" -> Database.queueList.findFirst(c -> c.queueNo() == selectedId);
            default -> null;
        };
    }

    public Object validateUnique(String value, String field) {
        return switch (field) {
            case "identification" -> Database.patientsList.findFirst(p -> p.getIdentification().equals(value));
            case "queue" -> Database.queueList.findFirst(c -> String.valueOf(c.patient().getId()).equals(value));
            default -> null;
        };
    }

    public ListInterface<PatientDetail> getPatientDetail(Patient patient) {
        ListInterface<PatientDetail> rows = new DoubleLinkedList<>();

        for (Consultation consult : getPatientConsultations(patient)) {
            if (consult.getDiagnoses() == null) {
                rows.add(new PatientDetail(consult, null, null, null));
            } else {
                for (Diagnosis diag : consult.getDiagnoses()) {
                    if (diag.getTreatments() == null) {
                        rows.add(new PatientDetail(consult, diag, null, null));
                    } else {
                        for (Treatment treat : diag.getTreatments()) {
                            if (treat.getPrescriptions() == null) {
                                rows.add(new PatientDetail(consult, diag, treat, null));
                            } else {
                                for (Prescription pres : treat.getPrescriptions()) {
                                    rows.add(new PatientDetail(consult, diag, treat, pres));
                                }
                            }
                        }
                    }
                }
            }
        }
        return rows;
    }
}
