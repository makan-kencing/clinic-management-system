package edu.dsa.clinic.utils.table;

import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Range;

import java.util.StringJoiner;

public abstract class Table<T> {
    private String title = "";
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
    private int pageSize = 20;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int page = 1;

    @Range(from = 0, to = Integer.MAX_VALUE)
    private int padding = 1;

    public @Range(from = 1, to = Integer.MAX_VALUE) int getPageSize() {
        return pageSize;
    }

    public String getTitle() {
        return title;
    }

    public Table<T> setTitle(String title) {
        this.title = title;
        return this;
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

    protected String renderBorder() {
        var joiner = this.getRowBuilder('-', '+');

        for (var column : this.columns)
            joiner.add("-".repeat(column.padLength()));

        return joiner.toString();
    }


    protected String renderHeader() {
        var joiner = this.getRowBuilder(' ', '|');

        for (var column : this.columns) {
            joiner.add(column.padded(' '));
        }

        return joiner.toString();
    }

    protected String renderBodyRow(T o, int index) {
        var cells = this.getRow(o);
        
        var joiner = this.getRowBuilder(' ', '|');
        for (int i = 0; i < this.columns.length; i++) {
            var column = this.columns[i];
            var cell = cells[i];

            joiner.add(cell.padded(' ', column.padLength()));
        }

        return joiner.toString();
    }

    protected String renderFooter() {
        return "|" + Alignment.CENTER.pad(
                this.page + " / " + this.getMaxPage(),
                ' ',
                this.getWidth() - 2
        ) + "|";
    }

    public void display() {
        System.out.println(this.title);
        System.out.println(this.renderBorder());
        System.out.println(this.renderHeader());
        System.out.println(this.renderBorder());

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

            System.out.println(this.renderBodyRow(row, n));
        }

        System.out.println(this.renderBorder());
        System.out.println(this.renderFooter());
        System.out.println(this.renderBorder());
    }

    protected abstract Cell[] getRow(T o);
}
