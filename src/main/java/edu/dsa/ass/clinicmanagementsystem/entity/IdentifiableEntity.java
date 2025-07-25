package edu.dsa.ass.clinicmanagementsystem.entity;

public abstract class IdentifiableEntity {
    private static int lastId = 0;
    private int id;

    public IdentifiableEntity(int id) {
        this.id = id;
    }

    public IdentifiableEntity() {
        this(++lastId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
