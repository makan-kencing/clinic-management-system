package edu.dsa.ass.clinicmanagementsystem.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentifiableEntity that = (IdentifiableEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
