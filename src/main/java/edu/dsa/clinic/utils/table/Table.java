package edu.dsa.clinic.utils.table;

import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Range;

import java.util.StringJoiner;

public abstract class Table<T> {
    private final Column[] columns;
    private ListInterface<T> data;

    public Table(Column[] columns, ListInterface<T> data) {
        this.columns = columns;
        this.data = data;
    }

    public Table(Column[] columns) {
        this.columns = columns;
    }

    @Range(from = 1, to = Integer.MAX_VALUE)
    private int pageSize = 10;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int page = 1;

    @Range(from = 0, to = Integer.MAX_VALUE)
    private int padding = 1;

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

    public @Range(from = 0, to = Integer.MAX_VALUE) int getWidth() {
        int sum = 0;
        sum += this.columns.length - 1;  // column divider length

        for (var column : this.columns)
            sum += this.padding + column.padLength() + this.padding;

        sum += 2;
        return sum;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getPadding() {
        return padding;
    }

    public void setPadding(@Range(from = 0, to = Integer.MAX_VALUE) int padding) {
        this.padding = padding;
    }

    public ListInterface<T> getData() {
        return data;
    }

    public void setData(ListInterface<T> data) {
        this.data = data;
        this.page = Math.min(this.page, this.getMaxPage());
    }

    protected StringJoiner getRowBuilder(char padding, char border) {
        return new StringJoiner(
                Character.toString(padding).repeat(this.padding) + border + Character.toString(padding).repeat(this.padding),
                border + Character.toString(padding).repeat(this.padding),
                Character.toString(padding).repeat(this.padding) + border
        );
    }

    protected void displayBorder() {
        var joiner = this.getRowBuilder('-', '+');

        for (var column : this.columns)
            joiner.add("-".repeat(column.padLength()));

        System.out.println(joiner);
    }


    protected void displayHeader() {
        var joiner = this.getRowBuilder(' ', '|');

        for (var column : this.columns) {
            joiner.add(column.padded(' '));
        }

        System.out.println(joiner);
    }

    protected void displayBody() {
        if (this.data.size() == 0)
            System.out.println("|" + Alignment.CENTER.pad(
                    "No records",
                    ' ',
                    this.getWidth() - 2
            ) + "|");

        var n = -1;
        for (var row : this.data) {
            n++;

            if (n < (this.page - 1) * this.pageSize)
                continue;
            else if (n >= (this.page) * this.pageSize)
                break;

            var cells = this.getRow(row);
            var joiner = this.getRowBuilder(' ', '|');
            for (int i = 0; i < this.columns.length; i++) {
                var column = this.columns[i];
                var cell = cells[i];

                joiner.add(cell.padded(' ', column.padLength()));
            }

            System.out.println(joiner);
        }
    }

    protected void displayFooter() {
        System.out.println("|" + Alignment.CENTER.pad(
                this.page + " / " + this.getMaxPage(),
                ' ',
                this.getWidth() - 2
        ) + "|");
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

    protected abstract Cell[] getRow(T o);
}
