package edu.dsa.clinic.boundary;

import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.PrintWriter;
import java.util.Scanner;

public abstract class UI {
    protected final Scanner scanner;
    protected final Terminal terminal;

    public UI(Terminal terminal, Scanner scanner) {
        this.scanner = scanner;
        this.terminal = terminal;
    }

    public UI(Terminal terminal) {
        this.scanner = new Scanner(System.in);
        this.terminal = terminal;
    }

    public UI(Scanner scanner) {
        this.scanner = scanner;
        this.terminal = null;
    }

    public LineReader getLineReader() {
        return LineReaderBuilder.builder()
                .terminal(this.terminal)
                .build();
    }

    public ConsolePrompt getPrompt() {
        return new ConsolePrompt(this.terminal);
    }

    public PrintWriter getWriter() {
        return this.terminal.writer();
    }

    public NonBlockingReader getReader() {
        return this.terminal.reader();
    }
}
