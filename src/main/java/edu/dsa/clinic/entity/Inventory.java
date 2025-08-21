package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import org.jetbrains.annotations.Range;

/**
 * The inventory definition of a {@link Medicine}.
 *
 * @author makan-kencing
 */
public class Inventory {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int minQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int maxQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int autoOrderThreshold;
    private final ListInterface<Stock> stocks = new SortedDoubleLinkedList<>((s1, s2) ->
            s1.getStockInDate().compareTo(s2.getStockInDate())
    );

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMinQuantity() {
        return minQuantity;
    }

    public Inventory setMinQuantity(@Range(from = 0, to = Integer.MAX_VALUE) int minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMaxQuantity() {
        return maxQuantity;
    }

    public Inventory setMaxQuantity(@Range(from = 0, to = Integer.MAX_VALUE) int maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getAutoOrderThreshold() {
        return autoOrderThreshold;
    }

    public Inventory setAutoOrderThreshold(@Range(from = 0, to = Integer.MAX_VALUE) int autoOrderThreshold) {
        this.autoOrderThreshold = autoOrderThreshold;
        return this;
    }

    public ListInterface<Stock> getStocks() {
        return stocks;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", autoOrderThreshold=" + autoOrderThreshold +
                ", stocks=" + stocks +
                '}';
    }
}
