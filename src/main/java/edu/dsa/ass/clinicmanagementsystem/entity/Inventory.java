package edu.dsa.ass.clinicmanagementsystem.entity;

public class Inventory {
    private int minQuantity;
    private int maxQuantity;
    private int autoOrderThreshold;

    @Override
    public String toString() {
        return "Inventory{" +
                "minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", autoOrderThreshold=" + autoOrderThreshold +
                '}';
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getAutoOrderThreshold() {
        return autoOrderThreshold;
    }

    public void setAutoOrderThreshold(int autoOrderThreshold) {
        this.autoOrderThreshold = autoOrderThreshold;
    }
}
