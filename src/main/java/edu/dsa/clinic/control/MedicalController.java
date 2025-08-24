package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.ConsultationType;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.lambda.Filter;

public class MedicalController {

    public void saveConsultationRecord(Consultation consultation) {
        Database.consultationsList.add(consultation);
    }

//    public Consultation listConsultations(Patient patient) {
//        for (Consultation consultation : Database.consultationsList) {
//            if (consultation.getPatient().equals(patient)) {
//                return consultation;
//            }
//        }
//        return null;
//    }

    public ListInterface<Consultation> getConsultationList() {
        return Database.consultationsList;
    }

    public Consultation selectConsultationById(int id) {
        return Database.consultationsList.findFirst(c -> c.getId() == id);
    }

   public Diagnosis selectDiagnosis(ListInterface<Diagnosis> diagnosis,int id){
        return  diagnosis.findFirst(d -> d.getId() == id);
   }

   public Treatment selectTreatment(ListInterface<Treatment> treatment,int id){
        return  treatment.findFirst(t -> t.getId() == id);
   }

   public Prescription selectPrescription(ListInterface<Prescription> prescription,int id){
        return prescription.findFirst(t -> t.getId() == id);
   }

   public boolean deleteConsultation(int id) {
       var removed=Database.consultationsList.removeFirst(c -> c.getId() == id);
       return removed != null;
   }

   public boolean deleteDiagnosis(ListInterface<Diagnosis> diagnosis,int id) {
        var removed =diagnosis.removeFirst(d -> d.getId() == id);
        return removed != null;
   }



   public boolean deleteTreatment(ListInterface<Treatment> treatment,int id) {
        var removed =treatment.removeFirst(t-> t.getId() == id);
        return removed != null;
   }


   public static Filter<Consultation> getConsultationTypeFilter(ConsultationType type) {
        return c -> c.getType() == type;
   }

   public static Filter<Consultation> getConsultationDoctorFilter(String doctorName) {
        return c -> c.getDoctor().getName().toLowerCase().contains(doctorName.toLowerCase());
   }

   public static Filter<Consultation> getConsultationPatientFilter(String patientName) {
        return c -> c.getPatient().getName().toLowerCase().contains(patientName.toLowerCase());
   }
}
