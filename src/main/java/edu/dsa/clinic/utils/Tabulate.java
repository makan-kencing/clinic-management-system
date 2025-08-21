package edu.dsa.clinic.utils;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.StringJoiner;

public abstract class Tabulate<T> {
    public record NamedFilter<T>(String name, Filter<T> filter) {
    }

    public record NamedSorter<T>(String name, Comparator<T> sorter) {
    }

    public record Header(String text, int padLength, Alignment alignment) {
        public Header(String text, int padLength) {
            this(text, padLength, Alignment.LEFT);
        }

        public String pad(char pad, int n) {
            return this.alignment.pad(this.text, pad, n);
        }
    }

    public record Cell(String text, Alignment alignment) {
        public Cell(String text) {
            this(text, Alignment.LEFT);
        }

        public String pad(char pad, int n) {
            return this.alignment.pad(this.text, pad, n);
        }
    }

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT;

        public String pad(String s, char pad, int n) {
            return switch (this) {
                case Alignment.LEFT -> StringUtils.padRight(s, pad, n);
                case Alignment.CENTER -> StringUtils.pad(s, pad, n);
                case Alignment.RIGHT -> StringUtils.padLeft(s, pad, n);
            };
        }
    }

    protected final ListInterface<NamedFilter<T>> filters = new DoubleLinkedList<>();
    protected final ListInterface<NamedSorter<T>> sorters = new DoubleLinkedList<>();
    protected final Header[] headers;
    protected final ListInterface<T> data;

    @Range(from = 1, to = Integer.MAX_VALUE)
    protected int pageSize = 10;
    @Range(from = 1, to = Integer.MAX_VALUE)
    protected int page = 1;

    @Range(from = 0, to = Integer.MAX_VALUE)
    protected int padding = 1;

    public Tabulate(Header[] headers, ListInterface<T> data) {
        this.headers = headers;
        this.data = data;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getPageSize() {
        return pageSize;
    }

    public void setPageSize(@Range(from = 1, to = Integer.MAX_VALUE) int pageSize) {
        this.pageSize = pageSize;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getPage() {
        return page;
    }

    public void setPage(@Range(from = 1, to = Integer.MAX_VALUE) int page) {
        this.page = page;
    }

    public void nextPage() {
        this.page++;
        this.page = Math.min(this.page, this.getMaxPage());
    }

    public void previousPage() {
        this.page--;
        this.page = Math.max(this.page, 1);
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getNumberOfElements() {
        return this.data.size();
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getMaxPage() {
        return (this.getNumberOfElements() - 1) / this.pageSize + 1;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getPadding() {
        return padding;
    }

    public void setPadding(@Range(from = 0, to = Integer.MAX_VALUE) int padding) {
        this.padding = padding;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getWidth() {
        int sum = 0;
        sum += this.headers.length - 1;  // column divider length

        for (var header : this.headers)
            sum += this.padding + header.padLength + this.padding;

        sum += 2;
        return sum;
    }

    protected StringJoiner getRowBuilder(char padding, char border) {
        return new StringJoiner(
                Character.toString(padding).repeat(this.padding) + border + Character.toString(padding).repeat(this.padding),
                border + Character.toString(padding).repeat(this.padding),
                Character.toString(padding).repeat(this.padding) + border
        );
    }

    public void addFilter(String name, Filter<T> filter) {
        this.filters.add(new NamedFilter<>(name, filter));
    }

    public void resetFilters() {
        this.filters.clear();
    }

    public void addSorter(String name, Comparator<T> sorter) {
        this.sorters.add(new NamedSorter<>(name, sorter));
    }

    public void resetSorters() {
        this.sorters.clear();
    }

    protected abstract Cell[] getRow(T element);

    protected void displayBorder() {
        var joiner = this.getRowBuilder('-', '+');

        for (var header : headers)
            joiner.add("-".repeat(header.padLength));

        System.out.println(joiner);
    }

    protected void displayHeader() {
        var joiner = this.getRowBuilder(' ', '|');

        for (var header : this.headers)
            joiner.add(header.pad(' ', header.padLength));

        System.out.println(joiner);
    }

    protected void displayBody() {
        var rows = this.data.clone();

        if (this.filters.size() != 0)
            rows.filter(new MultiFilter<>(this.filters.map(nf -> nf.filter)));

        if (this.sorters.size() != 0)
            rows.sort(new MultiComparator<>(this.sorters.map(ns -> ns.sorter)));

        var n = -1;
        for (var row : rows) {
            n++;
            if (n < (this.page - 1) * this.pageSize)
                continue;
            else if (n >= (this.page) * this.pageSize)
                break;

            var cells = this.getRow(row);

            var joiner = this.getRowBuilder(' ', '|');
            for (int i = 0; i < this.headers.length; i++) {
                var header = this.headers[i];
                var cell = cells[i];

                joiner.add(cell.pad(' ', header.padLength));
            }

            System.out.println(joiner);
        }
    }

    protected void displayFooter() {
        var page = String.format("%d / %d", this.page, this.getMaxPage());
        System.out.println("|" + Alignment.CENTER.pad(page, ' ', this.getWidth() - 2) + "|");

        if (this.filters.size() > 0) {
            var joiner = new StringJoiner(", ");
            for (var filter : this.filters)
                joiner.add(filter.name);
            System.out.println("Filters: " + joiner);
        }

        if (this.sorters.size() > 0) {
            var joiner = new StringJoiner(", ");
            for (var sorter : this.sorters)
                joiner.add(sorter.name);
            System.out.println("Sorters: " + joiner);
        }
    }

    public void display() {
        this.displayBorder();
        this.displayHeader();

        this.displayBorder();
        this.displayBody();
        this.displayBorder();

        this.displayFooter();
        this.displayBorder();
    }
}
