package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Range;

public class Inventory {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int minQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int maxQuantity;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int autoOrderThreshold;

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

    @Override
    public String toString() {
        return "Inventory{" +
                "minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", autoOrderThreshold=" + autoOrderThreshold +
                '}';
    }
}
