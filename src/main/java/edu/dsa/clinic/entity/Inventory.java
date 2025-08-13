package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import org.jetbrains.annotations.Range;

public class Inventory {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int minQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int maxQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int autoOrderThreshold;
    private ListInterface<Stock> stocks = new SortedDoubleLinkedList<>((s1, s2) ->
            s1.getStockInDate().compareTo(s2.getStockInDate())
    );

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(@Range(from = 0, to = Integer.MAX_VALUE) int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(@Range(from = 0, to = Integer.MAX_VALUE) int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getAutoOrderThreshold() {
        return autoOrderThreshold;
    }

    public void setAutoOrderThreshold(@Range(from = 0, to = Integer.MAX_VALUE) int autoOrderThreshold) {
        this.autoOrderThreshold = autoOrderThreshold;
    }

    public ListInterface<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ListInterface<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", autoOrderThreshold=" + autoOrderThreshold +
                '}';
    }
}
