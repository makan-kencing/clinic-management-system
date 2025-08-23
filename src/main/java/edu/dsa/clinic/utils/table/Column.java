package edu.dsa.clinic.utils.table;

import org.jetbrains.annotations.Range;

public class Column extends Cell {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private final int padLength;

    public Column(String text, Alignment alignment, @Range(from = 0, to = Integer.MAX_VALUE) int padLength) {
        super(text, alignment);
        this.padLength = padLength;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int padLength() {
        return padLength;
    }

    public String padded(char pad) {
        return super.padded(pad, this.padLength);
    }
}
