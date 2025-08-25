package edu.dsa.clinic.utils;

import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.utils.table.Column;
import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public abstract class SelectTable<T> extends InteractiveTable<T> {
    protected enum KeyEvent {
        NOOP,
        RETURN,
        EXIT
    }

    public final Terminal terminal;
    public final ConsolePrompt prompt;
    @Range(from = 1, to = Integer.MAX_VALUE)
    public int selectedRow = 1;

    public SelectTable(Column[] columns, ListInterface<T> data, Terminal terminal) {
        super(columns, data);
        this.terminal = terminal;
        this.prompt = new ConsolePrompt(this.terminal);
    }

    public @Nullable T select() throws IOException {
        var originalAttributes = this.terminal.enterRawMode();
        var writer = this.terminal.writer();
        var reader = this.terminal.reader();

        try {
            while (true) {
                this.terminal.puts(InfoCmp.Capability.clear_screen);
                this.terminal.flush();

                this.display(false);

                writer.println("[<] Previous page        [>] Next page");
                writer.println("[\\/] Move selection down        [/\\] Move selection up");
                writer.println("[s] Search        [f] Filter        [o] Order by");
                writer.println("[Enter] Select");

                writer.println("[q] Quit");
                writer.flush();

                var read = reader.read();
                switch (this.handleKey(read)) {
                    case EXIT:
                        return null;
                    case RETURN:
                        return this.getSelected();
                }
            }
        } finally {
            this.terminal.setAttributes(originalAttributes);
        }
    }

    public T getSelected() {
        return this.getData().get(this.getSelectedIndex());
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getSelectedIndex() {
        return (this.getPage() - 1) * this.getPageSize() + this.selectedRow - 1;
    }

    protected KeyEvent handleKey(int ch) throws IOException {
        switch (ch) {
            case '<':
            case 68:
                this.previousPage();
                break;
            case '>':
            case 67:
                this.nextPage();
                break;
            case 65:  // up arrow
                this.selectedRow--;
                this.selectedRow = Math.max(this.selectedRow, 1);
                break;
            case 66:  // down arrow
                this.selectedRow++;
                this.selectedRow = Math.min(this.selectedRow, this.getPageSize());
                break;
            case 's':
                this.promptSearch();
                break;
            case 'f':
                this.promptFilterOption();
                break;
            case 'o':
                this.promptSorterOption();
                break;
            case '\r':
            case '\n':
                return KeyEvent.RETURN;
            case 'q':
                return KeyEvent.EXIT;
        }
        return KeyEvent.NOOP;
    }

    @Override
    protected String renderBodyRow(T o, int index) {
        if (index == this.getSelectedIndex())
            return "> " + super.renderBodyRow(o, index);
        return super.renderBodyRow(o, index);
    }

    abstract protected void promptSearch() throws IOException;
    abstract protected void promptFilterOption() throws IOException;
    abstract protected void promptSorterOption() throws IOException;
}
