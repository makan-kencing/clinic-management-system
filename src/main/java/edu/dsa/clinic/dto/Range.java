package edu.dsa.clinic.dto;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.utils.Ordered;

import java.util.Comparator;

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
        return Ordered.isLessOrEqualTo(this.from, other.from)
                && Ordered.isGreaterOrEqualTo(this.to, other.to);
    }

    /**
     * Check if the other {@link Range} is exclusively contained by this {@link Range}
     *
     * @param other The other {@link Range} to check contains.
     * @return If this {@link Range} contains the given {@link Range}
     */
    public boolean containsExclusively(Range<T> other) {
        return Ordered.isBetweenExclusively(this.from, other.from, other.to)
                && Ordered.isBetweenExclusively(this.to, other.from, other.to);
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

    /**
     * Subtract this {@link Range} with other {@link Range} exclusively.
     *
     * @param other The other {@link Range} to subtract with.
     * @return The split {@link Range}(s).
     */
    // https://stackoverflow.com/questions/6462272/subtract-overlaps-between-two-ranges-without-sets
    public ListInterface<Range<T>> subtract(Range<T> other) {
        var ranges = new DoubleLinkedList<Range<T>>();

        var coords = new SortedDoubleLinkedList<T>(Comparator.naturalOrder());
        coords.add(this.from);
        coords.add(this.to);
        coords.add(other.from);
        coords.add(other.to);

        var iterator = coords.iterator();

        var start = iterator.next();
        var end = iterator.next();
        if (start == this.from && end != this.from)
            ranges.add(new Range<>(start, end));

        start = iterator.next();
        end = iterator.next();
        if (end == this.to && start != this.to)
            ranges.add(new Range<>(start, end));

        return ranges;
    }
}
