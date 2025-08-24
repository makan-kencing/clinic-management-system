package edu.dsa.clinic.dto;

import edu.dsa.clinic.utils.Ordered;

/**
 * A pair of two values usually relating to upper bound and lower bound.
 *
 * @param from The lower value of the {@link Range}.
 * @param to The upper value of the {@link Range}.
 * @param <T> The datatype of the values.
 *
 * @author makan-kencing
 */
public record Range<T extends Comparable<T>>(
        T from,
        T to
) {
    // https://stackoverflow.com/questions/36035074/how-can-i-find-an-overlap-between-two-given-ranges

    /**
     * Check if the other {@link Range} is inclusively overlapped by this {@link Range}
     *
     * @param other The other {@link Range} to check overlaps.
     * @return If this {@link Range} overlaps the given {@link Range}
     */
    public boolean overlaps(Range<T> other) {
        // a <= d && b >= c
        return Ordered.isLessOrEqualTo(this.from, other.to)
                && Ordered.isGreaterOrEqualTo(this.to, other.from);
    }

    /**
     * Check if the other {@link Range} is exclusively overlapped by this {@link Range}
     *
     * @param other The other {@link Range} to check overlaps.
     * @return If this {@link Range} overlaps the given {@link Range}
     */
    public boolean overlapsExclusively(Range<T> other) {
        // a <= d && b >= c
        return Ordered.isLessThan(this.from, other.to)
                && Ordered.isGreaterThan(this.to, other.from);
    }

    /**
     * Check if the other {@link Range} is inclusively contained by this {@link Range}
     *
     * @param other The other {@link Range} to check contains.
     * @return If this {@link Range} contains the given {@link Range}
     */
    public boolean contains(Range<T> other) {
        return Ordered.isBetween(this.from, other.from, other.to)
                && Ordered.isBetween(this.to, other.from, other.to);
    }

    /**
     * Get the overlapped {@link Range} between two {@link Range}s.
     *
     * @param other The other {@link Range} to overlap.
     * @return The overlapping {@link Range}.
     */
    public Range<T> getOverlap(Range<T> other) {
        return new Range<>(
                Ordered.max(this.from, other.from),
                Ordered.min(this.to, other.to)
        );
    }

    /**
     * Combine two continuous {@link Range}s into one range.
     *
     * @param other The other {@link Range} to combine with.
     * @return The combined {@link Range}
     */
    public Range<T> combine(Range<T> other) {
        return new Range<>(
                Ordered.min(this.from, other.from),
                Ordered.max(this.to, other.to)
        );
    }
}
