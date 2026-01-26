package com.gojek.parkinglot.io;

import java.io.PrintStream;

/**
 * Handles output formatting and writing for the parking lot system.
 *
 * <p>This class abstracts the output mechanism, making it easy to
 * redirect output for testing or logging purposes.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class OutputWriter {

    private final PrintStream out;

    /**
     * Constructs an OutputWriter that writes to the specified PrintStream.
     *
     * @param out the output stream
     */
    public OutputWriter(PrintStream out) {
        this.out = out != null ? out : System.out;
    }

    /**
     * Constructs an OutputWriter that writes to System.out.
     */
    public OutputWriter() {
        this(System.out);
    }

    /**
     * Writes a line of output.
     *
     * @param message the message to write
     */
    public void writeLine(String message) {
        if (message != null && !message.isEmpty()) {
            out.println(message);
        }
    }

    /**
     * Writes multiple lines of output.
     *
     * @param messages the messages to write
     */
    public void writeLines(String... messages) {
        for (String message : messages) {
            writeLine(message);
        }
    }

    /**
     * Writes an empty line.
     */
    public void writeEmptyLine() {
        out.println();
    }

    /**
     * Writes a message without a newline.
     *
     * @param message the message to write
     */
    public void write(String message) {
        if (message != null) {
            out.print(message);
        }
    }

    /**
     * Flushes the output stream.
     */
    public void flush() {
        out.flush();
    }
}
