package edu.dsa.clinic.control;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.dto.DispensaryQueue;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Prescription;

import java.util.Comparator;

/**
 * Logics for managing the prescriptions and dispensary queue.
 *
 * @author makan-kencing
 */
public class DispensaryController {
    private static final ListInterface<DispensaryQueue> dispensaryQueue = new SortedDoubleLinkedList<>(
            Comparator.comparing(DispensaryQueue::queueTime)
    );

    public void queueConsultation(Consultation consultation) {
        dispensaryQueue.add(new DispensaryQueue(consultation));
    }

    public ListInterface<Prescription> getPrescriptionsToDispense() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean completeDispensing() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
