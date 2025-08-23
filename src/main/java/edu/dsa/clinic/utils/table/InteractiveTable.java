package edu.dsa.clinic.utils.table;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.lambda.MultiComparator;
import edu.dsa.clinic.lambda.MultiFilter;

import java.util.Comparator;
import java.util.StringJoiner;

abstract public class InteractiveTable<T> extends Table<T> {
    public record NamedFilter<T>(String name, Filter<T> filter) {
    }

    public record NamedSorter<T>(String name, Comparator<T> sorter) {
    }

    protected final ListInterface<NamedFilter<T>> filters = new DoubleLinkedList<>();
    protected final ListInterface<NamedSorter<T>> sorters = new DoubleLinkedList<>();
    protected final ListInterface<T> unfilteredData;

    public InteractiveTable(Column[] columns, ListInterface<T> data) {
        super(columns, data);
        this.unfilteredData = data;
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

    public void updateData() {
        var data = this.unfilteredData.clone();

        if (this.filters.size() != 0)
            data.filter(new MultiFilter<>(this.filters.map(NamedFilter::filter)));

        if (this.sorters.size() != 0)
            data.sort(new MultiComparator<>(this.sorters.map(NamedSorter::sorter)));

        this.setData(data);
    }

    @Override
    protected void displayFooter() {
        super.displayFooter();

        var rowJoiner = this.getRowBuilder(' ', '|');
        if (this.filters.size() > 0) {
            var joiner = new StringJoiner(", ");
            for (var filter : this.filters)
                joiner.add(filter.name);

            rowJoiner.add(Alignment.LEFT.pad(
                    "Filters: " + joiner,
                    ' ',
                    this.getWidth() - 2 - 2 * this.getPadding()
            ));
            System.out.println(rowJoiner);
        }

        rowJoiner = this.getRowBuilder(' ', '|');
        if (this.sorters.size() > 0) {
            var joiner = new StringJoiner(", ");
            for (var sorter : this.sorters)
                joiner.add(sorter.name);

            rowJoiner.add(Alignment.LEFT.pad(
                    "Sorters: " + joiner,
                    ' ',
                    this.getWidth() - 2 - 2 * this.getPadding()
            ));
            System.out.println(rowJoiner);
        }
    }

    public void display(boolean update) {
        if (update)
            this.updateData();

        super.display();
    }

    public void display() {
        this.display(true);
    }
}
