package edu.dsa.clinic.utils;

import edu.dsa.clinic.utils.table.InteractiveTable;
import org.jetbrains.annotations.Nullable;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public abstract class SelectTable<T> {
    public final Terminal terminal;
    public final ConsolePrompt prompt;
    public final InteractiveTable<T> table;

    public SelectTable(Terminal terminal, InteractiveTable<T> table) {
        this.terminal = terminal;
        this.prompt = new ConsolePrompt(this.terminal);
        this.table = table;
    }

    public @Nullable T select() {
        var originalAttributes = this.terminal.enterRawMode();

        try (var writer = this.terminal.writer();
             var reader = this.terminal.reader()) {
            while (true) {
                this.terminal.puts(InfoCmp.Capability.clear_screen);
                this.terminal.flush();

                this.table.display(false);

                writer.println("[<] Previous page  [>] Next page");
                writer.println("[n] New record  [s] Search  [f] Filter  [o] Order by");
                writer.println("[Enter] Select");
                writer.println("[q] Quit");
                writer.flush();

                var read = reader.read();
                switch (read) {
                    case '<':
                    case 68:
                        this.table.previousPage();
                        break;
                    case '>':
                    case 67:
                        this.table.nextPage();
                        break;
                    case 65:  // up arrow
                        break;
                    case 66:  // down arrow
                        break;
                    case 'n':
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
                        terminal.setAttributes(originalAttributes);

                        var selected = this.promptSelect();

                        originalAttributes = terminal.enterRawMode();

                        if (selected != null)
                            return selected;

                        break;
                    case 'q':
                        return null;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.terminal.setAttributes(originalAttributes);
        }
    }

    abstract protected void promptSearch() throws IOException;
    abstract protected void promptFilterOption() throws IOException;
    abstract protected void promptSorterOption() throws IOException;
    abstract protected @Nullable T promptSelect() throws IOException;

}
