package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.AppointmentTypeCounter;
import edu.dsa.clinic.dto.ConsultationTypeCounter;
import edu.dsa.clinic.dto.DiagnosisCounter;
import edu.dsa.clinic.dto.DoctorCounter;
import edu.dsa.clinic.dto.MedicalDetail;
import edu.dsa.clinic.dto.PatientCounter;
import edu.dsa.clinic.dto.ProductCounter;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.lambda.Filter;
import org.jetbrains.annotations.Nullable;

public class MedicalController {

    PatientController patientController = new PatientController();

    public static Filter<Consultation> getConsultationTypeFilter(ConsultationType type) {
        return c -> c.getType() == type;
    }

    public static Filter<Consultation> getConsultationDoctorFilter(String doctorName) {
        return c -> c.getDoctor().getName().toLowerCase().contains(doctorName.toLowerCase());
    }

    public static Filter<Consultation> getConsultationPatientFilter(String patientName) {
        return c -> c.getPatient().getName().toLowerCase().contains(patientName.toLowerCase());
    }

    public static Filter<Diagnosis> getDiagnosisFilter(String diagnosisName) {
        return d->d.getDiagnosis().toLowerCase().contains(diagnosisName.trim().toLowerCase());
    }

    public static Filter<Treatment> getTreatmentFilter(String treatmentName) {
        return t->t.getSymptom().toLowerCase().contains(treatmentName.trim().toLowerCase());
    }

    public static Filter<Prescription> getPrescriptionFilter(String prescriptionName) {
        return p->p.getProduct().getName().toLowerCase().contains(prescriptionName.toLowerCase());
    }

    public boolean saveConsultationRecord(Consultation consultation) {
        if (consultation == null) {
            return false;
        }
        Database.consultationsList.add(consultation);
        return true;
    }

    public ListInterface<Consultation> getConsultationList() {
        return Database.consultationsList;
    }

    public Consultation selectConsultationById(int id) {
        return Database.consultationsList.findFirst(c -> c.getId() == id);
    }

    public Consultation selectConsultationByObject(Patient patient) {
        return Database.consultationsList.findFirst(c -> c.getPatient().equals(patient));
    }

    public @Nullable Diagnosis selectDiagnosis(ListInterface<Diagnosis> diagnosis, int id) {
        return diagnosis.findFirst(d -> d.getId() == id);
    }

    public @Nullable Diagnosis selectDiagnosis(Consultation consultation, int id) {
        return this.selectDiagnosis(consultation.getDiagnoses(), id);
    }

    public @Nullable Treatment selectTreatment(ListInterface<Treatment> treatment, int id) {
        return treatment.findFirst(t -> t.getId() == id);
    }

    public @Nullable Treatment selectTreatment(Diagnosis diagnosis, int id) {
        return this.selectTreatment(diagnosis.getTreatments(), id);
    }

    public @Nullable Prescription selectPrescription(ListInterface<Prescription> prescription, int id) {
        return prescription.findFirst(t -> t.getId() == id);
    }

    public @Nullable Prescription selectPrescription(Treatment treatment, int id) {
        return this.selectPrescription(treatment.getPrescriptions(), id);
    }

    public Product selectProduct() {
        var allMedicine = MedicineController.getAllProducts();
        return allMedicine.findFirst(m -> m.getId() == 1);
    }

    public boolean deleteConsultation(int id) {
        var removed = Database.consultationsList.removeFirst(c -> c.getId() == id);
        return removed != null;
    }

    public boolean deleteDiagnosis(ListInterface<Diagnosis> diagnosis, int id) {

        var removed = diagnosis.removeFirst(d -> d.getId() == id);
        return removed != null;
    }

    public boolean deleteDiagnosis(Consultation consultation, int id) {
        return this.deleteDiagnosis(consultation.getDiagnoses(), id);
    }

    public boolean deleteTreatment(ListInterface<Treatment> treatment, int id) {
        var removed = treatment.removeFirst(t -> t.getId() == id);
        return removed != null;
    }

    public boolean deleteTreatment(Diagnosis diagnosis, int id) {
        return this.deleteTreatment(diagnosis.getTreatments(), id);
    }

    public boolean deletePrescription(ListInterface<Prescription> prescriptions, int id) {
        var remove = prescriptions.removeFirst(p -> p.getId() == id);
        return remove != null;
    }

    public boolean deletePrescription(Treatment treatment, int id) {
        return this.deletePrescription(treatment.getPrescriptions(), id);
    }

    public ListInterface<MedicalDetail> getMedicalDetails(Patient patient) {
       ListInterface<MedicalDetail> row =new DoubleLinkedList<>();
       for (Consultation consultation :patientController.getPatientConsultations(patient)) {
           if (consultation.getDiagnoses()==null){
               row.add(new MedicalDetail(consultation,null,null,null));
           }else {
                for (Diagnosis diagnosis : consultation.getDiagnoses()) {
                    if (diagnosis.getTreatments()==null){
                        row.add(new MedicalDetail(consultation,diagnosis,null,null));
                    }
                    else {
                        for (Treatment treatment : diagnosis.getTreatments()) {
                            if (treatment.getPrescriptions()==null){
                                row.add(new MedicalDetail(consultation,diagnosis,treatment,null));
                            }
                            else {
                               for (Prescription prescription : treatment.getPrescriptions()) {
                                   row.add(new MedicalDetail(consultation,diagnosis,treatment,prescription));
                               }

                            }
                        }
                    }
                }
           }
       }
        return row;
    }

    public static ListInterface<DiagnosisCounter> countDiagnosesOccurrence() {
        var counters = new DoubleLinkedList<DiagnosisCounter>();

        for (var consultation : Database.consultationsList) {
            for (var diagnosis : consultation.getDiagnoses()) {
                var diagnosedCondition = diagnosis.getDiagnosis();  // String

                var counter = counters.findFirst(dc -> dc.key().equals(diagnosedCondition));
                if (counter == null) {
                    counter = new DiagnosisCounter(diagnosedCondition);
                    counters.add(counter);
                }

                counter.increment();

                for (var treatment : diagnosis.getTreatments())
                    for (var prescription : treatment.getPrescriptions()) {
                        var product = prescription.getProduct();
                        var productCounters = counter.productCounters();

                        var productCounter = productCounters.findFirst(pc -> pc.key().equals(product));
                        if (productCounter == null) {
                            productCounter = new ProductCounter(product);
                            productCounters.add(productCounter);
                        }

                        productCounter.increment();
                    }
            }
        }
        return counters;
    }

   public static int getTotalProductUsage(ListInterface<DiagnosisCounter> diagnosisCounters) {
    int total = 0;
    for (int i = 0; i < diagnosisCounters.size(); i++) {
        DiagnosisCounter dc = diagnosisCounters.get(i);
        ListInterface<ProductCounter> productCounters = dc.productCounters();

        for (int j = 0; j < productCounters.size(); j++) {
            total += productCounters.get(j).count();
        }
    }
    return total;
}

    public static String getProductUsageString(ListInterface<ProductCounter> productCounters) {
        StringBuilder productInfo = new StringBuilder();

        for (int i = 0; i < productCounters.size(); i++) {
            ProductCounter pc = productCounters.get(i);
            productInfo.append(pc.key().getName())
                    .append(" (")
                    .append(pc.count())
                    .append("), ");
        }

        if (productInfo.length() > 0) {
            productInfo.setLength(productInfo.length() - 2);
        }

        return productInfo.toString();
    }

    public ListInterface<String> getDoctorList(ConsultationTypeCounter ctc) {
        return ctc.getDoctorCounters().map(p -> p.key().getName() + "(" + p.count() + ")");
    }

    public ListInterface<String> getPatientList(ConsultationTypeCounter ctc) {
        return ctc.getPatientCounters().map(c -> c.key().getName() + "(" + c.count() + ")");
    }

    public ListInterface<ConsultationTypeCounter> getConsultationSummary() {
        ListInterface<ConsultationTypeCounter> typeCounters = new DoubleLinkedList<>();
        for (ConsultationType type : ConsultationType.values()) {
            typeCounters.add(new ConsultationTypeCounter(type));
        }

        for (Consultation consult : Database.consultationsList) {
            ConsultationType type = consult.getType();
            Patient patient = consult.getPatient();
            Doctor doctor = consult.getDoctor();

            ConsultationTypeCounter ctc = typeCounters.findFirst(tc -> tc.getType() == type);

            ctc.incrementConsultationCount();

            DoctorCounter existingDoctor = ctc.getDoctorCounters().findFirst(dc -> dc.key().equals(doctor));
            if (existingDoctor == null) {
                existingDoctor = new DoctorCounter(doctor);
                ctc.getDoctorCounters().add(existingDoctor);
            }
            existingDoctor.increment();

            PatientCounter existingPatient = ctc.getPatientCounters().findFirst(pc -> pc.key().equals(patient));
            if (existingPatient == null) {
                existingPatient = new PatientCounter(patient);
                ctc.getPatientCounters().add(existingPatient);
            }
            existingPatient.increment();
        }

        for (ConsultationTypeCounter atc : typeCounters) {
            atc.getDoctorCounters().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            atc.getPatientCounters().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        }

        return typeCounters;
    }


}
