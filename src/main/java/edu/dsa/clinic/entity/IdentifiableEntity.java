package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Range;

import java.util.Objects;

public abstract class IdentifiableEntity {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private static int lastId = 0;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int id;

    public IdentifiableEntity(@Range(from = 0, to = Integer.MAX_VALUE) int id) {
        this.id = id;
        lastId = Math.max(lastId, id);
    }

    public IdentifiableEntity() {
        this(++lastId);
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getId() {
        return id;
    }

    public void setId(@Range(from = 1, to = Integer.MAX_VALUE) int id) {
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
