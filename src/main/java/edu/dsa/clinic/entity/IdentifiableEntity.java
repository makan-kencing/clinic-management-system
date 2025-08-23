package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * An entity that can be uniquely identified with an id.
 */
public abstract class IdentifiableEntity {
    @Range(from = 0, to = Integer.MAX_VALUE)
    private static int lastId = 0;

    @Range(from = 0, to = Integer.MAX_VALUE)
    protected int id;

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

    public IdentifiableEntity setId(@Range(from = 1, to = Integer.MAX_VALUE) int id) {
        this.id = id;
        lastId = Math.max(lastId, id);
        return this;
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
