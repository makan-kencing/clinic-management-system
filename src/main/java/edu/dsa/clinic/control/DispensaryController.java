package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.DispensaryQueue;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Dispensing;
import edu.dsa.clinic.entity.Product;
import org.jetbrains.annotations.Nullable;

/**
 * Logics for managing the prescriptions and dispensary queue.
 *
 * @author makan-kencing
 */
public class DispensaryController {
    private DispensaryController() {
    }

    public static void queueConsultation(Consultation consultation) {
        Database.dispensaryQueueList.add(new DispensaryQueue(consultation));

        for (var diagnosis : consultation.getDiagnoses())
            for (var treatment : diagnosis.getTreatments())
                for (var prescription : treatment.getPrescriptions()) {
                    var dispensings = makeDispensing(prescription.getProduct(), prescription.getQuantity());

                    for (var dispense : dispensings)
                        prescription.addDispensing(dispense);
                }
    }

    public static ListInterface<Dispensing> getAllDispensing(Consultation consultation) {
        var dispensings = new DoubleLinkedList<Dispensing>();

        for (var diagnosis : consultation.getDiagnoses())
            for (var treatment : diagnosis.getTreatments())
                for (var prescription : treatment.getPrescriptions())
                    dispensings.extend(prescription.getDispensing());

        return dispensings;
    }

    public static ListInterface<Dispensing> makeDispensing(Product product, int quantity) {
        var dispensings = new DoubleLinkedList<Dispensing>();

        for (var stock : MedicineController.getProductStocks(product)) {
            if (quantity <= 0)
                break;

            var dispensedQuantity = Math.min(quantity, MedicineController.getStockQuantityLeft(stock));
            if (dispensedQuantity <= 0)
                continue;

            var dispensing = new Dispensing()
                    .setStock(stock)
                    .setQuantity(dispensedQuantity);
            stock.addDispensing(dispensing);
            dispensings.add(dispensing);
            quantity -= dispensedQuantity;
        }
        if (quantity > 0)
            throw new IllegalStateException();

        return dispensings;
    }

    public static ListInterface<DispensaryQueue> getDispensaryQueue() {
        return Database.dispensaryQueueList.clone();
    }

    public static @Nullable DispensaryQueue handleNextDispense() {
        try {
            return Database.dispensaryQueueList.popFirst();
        } catch (IndexOutOfBoundsException _) {
            return null;
        }
    }

    public static void addBackDispense(DispensaryQueue queued) {
        Database.dispensaryQueueList.add(queued);
    }
}
