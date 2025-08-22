package edu.dsa.clinic.utils.table;

public class Cell {
    private final String text;
    private final Alignment alignment;

    public Cell(String text, Alignment alignment) {
        this.text = text;
        this.alignment = alignment;
    }

    public Cell(String text) {
        this(text, Alignment.LEFT);
    }

    public Cell(Object o, Alignment alignment) {
        this(o.toString(), alignment);
    }

    public Cell(Object o) {
        this(o.toString());
    }

    public String text() {
        return text;
    }

    public Alignment alignment() {
        return alignment;
    }

    public String padded(char pad, int n) {
        return this.alignment.pad(this.text, pad, n);
    }
}
