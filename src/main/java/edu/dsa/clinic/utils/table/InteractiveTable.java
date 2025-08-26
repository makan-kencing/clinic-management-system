package edu.dsa.clinic.utils.table;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.lambda.Filter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.StringJoiner;

abstract public class InteractiveTable<T> extends Table<T> {
    public record NamedFilter<T>(String name, Filter<T> filter) {
    }

    public record NamedSorter<T>(String name, Comparator<T> sorter) {
    }

    protected ListInterface<NamedFilter<T>> defaultFilters = new DoubleLinkedList<>();
    protected ListInterface<NamedSorter<T>> defaultSorters = new DoubleLinkedList<>();
    protected ListInterface<NamedFilter<T>> filters;
    protected ListInterface<NamedSorter<T>> sorters;
    protected final ListInterface<T> unfilteredData;

    public InteractiveTable(
            Column[] columns,
            ListInterface<T> data,
            ListInterface<NamedFilter<T>> defaultFilters,
            ListInterface<NamedSorter<T>> defaultSorters
    ) {
        this(columns, data);
        this.defaultFilters.extend(defaultFilters);
        this.defaultSorters.extend(defaultSorters);
        this.resetFilters();
        this.resetSorters();
    }

    public InteractiveTable(Column[] columns, ListInterface<T> data) {
        super(columns, data);
        this.filters = this.defaultFilters.clone();
        this.sorters = this.defaultSorters.clone();
        this.unfilteredData = data;
    }

    public @Nullable Filter<T> getCombinedFilter() {
        if (this.filters.size() == 0)
            return null;

        var iterator = this.filters.iterator();
        var f = iterator.next().filter;

        while (iterator.hasNext())
            f = f.and(iterator.next().filter);

        return f;
    }

    public void addDefaultFilter(String name, Filter<T> filter) {
        this.defaultFilters.add(new NamedFilter<>(name, filter));
    }

    public void clearDefaultFilter(Filter<String> exclude) {
        this.defaultFilters.filter(nf -> exclude.not().filter(nf.name));
    }

    public void addFilter(String name, Filter<T> filter) {
        this.filters.add(new NamedFilter<>(name, filter));
    }

    public void clearFilter(Filter<String> exclude) {
        this.filters.filter(nf -> exclude.not().filter(nf.name));
    }

    public void resetFilters() {
        this.filters = this.defaultFilters.clone();
    }

    public Comparator<T> getCombinedSorter() {
        if (this.sorters.size() == 0)
            return null;

        var iterator = this.sorters.iterator();
        var s = iterator.next().sorter;

        while (iterator.hasNext())
            s = s.thenComparing(iterator.next().sorter);

        return s;
    }

    public void addDefaultSorter(String name, Comparator<T> sorter) {
        this.defaultSorters.add(new NamedSorter<>(name, sorter));
    }

    public void clearDefaultSorter(Filter<String> exclude) {
        this.defaultSorters.filter(ns -> exclude.not().filter(ns.name));
    }

    public void addSorter(String name, Comparator<T> sorter) {
        this.sorters.add(new NamedSorter<>(name, sorter));
    }

    public void clearSorter(Filter<String> exclude) {
        this.sorters.filter(ns -> exclude.not().filter(ns.name));
    }

    public void resetSorters() {
        this.sorters = this.defaultSorters.clone();
    }

    public ListInterface<NamedFilter<T>> getDefaultFilters() {
        return defaultFilters;
    }

    public ListInterface<NamedSorter<T>> getDefaultSorters() {
        return defaultSorters;
    }

    public InteractiveTable<T> setDefaultFilters(ListInterface<NamedFilter<T>> defaultFilters) {
        this.defaultFilters = defaultFilters;
        return this;
    }

    public InteractiveTable<T> setDefaultSorters(ListInterface<NamedSorter<T>> defaultSorters) {
        this.defaultSorters = defaultSorters;
        return this;
    }

    public void updateData() {
        var data = this.unfilteredData.clone();

        if (this.filters.size() != 0)
            data.filter(this.getCombinedFilter());

        if (this.sorters.size() != 0)
            data.sort(this.getCombinedSorter());

        this.setData(data);
    }

    @Override
    protected String renderFooter() {
        var footer = super.renderFooter();

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
            footer += "\n" + rowJoiner;
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
            footer += "\n" + rowJoiner;
        }
        return footer;
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
