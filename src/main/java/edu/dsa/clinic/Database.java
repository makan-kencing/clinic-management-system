package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.utils.ConsultationQueue;

import java.util.Comparator;

public class Database {
    public final static ListInterface<Patient> patientsList = new DoubleLinkedList<>();
    public final static ListInterface<Consultation> consultationsList = new DoubleLinkedList<>();
    public final static ListInterface<Medicine> medicineList = new DoubleLinkedList<>();
    public final static ListInterface<Doctor> doctorList = new DoubleLinkedList<>();
    public final static ListInterface<Appointment> appointmentList = new DoubleLinkedList<>();
    public final static ListInterface<ConsultationQueue> queueList = new SortedDoubleLinkedList<>(Comparator.comparingInt(ConsultationQueue::queueNo));

    private Database() {
    }
}
