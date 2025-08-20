package edu.dsa.clinic.utils;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Scanner;

public abstract class Pager<T> {
    protected final ListInterface<Filter<T>> filters = new DoubleLinkedList<>();
    protected final ListInterface<Comparator<T>> sorters = new DoubleLinkedList<>();
    protected final Scanner scanner;

    @Range(from = 1, to = Integer.MAX_VALUE)
    protected int numberOfRows = 10;

    public Pager(Scanner scanner) {
        this.scanner = scanner;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(@Range(from = 1, to = Integer.MAX_VALUE) int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void resetFilters() {
        this.filters.clear();
    }

    public void resetSorters() {
        this.sorters.clear();
    }

    protected abstract ListInterface<T> fetch(ListInterface<Filter<T>> filters, ListInterface<Comparator<T>> sorters);

    protected abstract void displayAll(ListInterface<T> data);

    protected abstract @Nullable Filter<T> promptFilter();

    protected abstract @Nullable Comparator<T> promptSorter();

    public @Nullable T promptSelection() {
        while (true) {
            var items = this.fetch(this.filters, this.sorters);

            this.displayAll(items);

            System.out.println("(1) Select");
            System.out.println("(2) Filter");
            System.out.println("(3) Sort");
            System.out.println("(4) Clear filters");
            System.out.println("(5) Clear sorters");
            System.out.println("(6) Exit");

            var opt = this.scanner.nextInt();

            switch (opt) {
                case 1:
                    System.out.print("Item no. : ");

                    var i = this.scanner.nextInt();
                    return items.get(i - 1);
                case 2:
                    var filter = this.promptFilter();
                    if (filter != null)
                        this.filters.add(filter);

                    break;
                case 3:
                    var sorter = this.promptSorter();
                    if (sorter != null)
                        this.sorters.add(sorter);

                    break;
                case 4:
                    this.resetFilters();
                    break;
                case 5:
                    this.resetSorters();
                    break;
                case 6:
                    return null;
                default:
                    break;
            }
        }
    }
}
