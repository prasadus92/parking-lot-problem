package com.gojek.parkinglot.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles input reading from various sources.
 *
 * <p>This class provides methods for reading commands from both
 * interactive console input and file-based input.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class InputReader implements Closeable {

    private final BufferedReader reader;
    private final boolean interactive;

    /**
     * Constructs an InputReader for the specified input stream.
     *
     * @param in the input stream
     * @param interactive whether this is interactive mode
     */
    public InputReader(InputStream in, boolean interactive) {
        this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.interactive = interactive;
    }

    /**
     * Constructs an interactive InputReader for System.in.
     */
    public InputReader() {
        this(System.in, true);
    }

    /**
     * Creates an InputReader for the specified file.
     *
     * @param filePath the path to the input file
     * @return an InputReader for the file
     * @throws IOException if the file cannot be read
     */
    public static InputReader fromFile(String filePath) throws IOException {
        return new InputReader(
                new FileInputStream(filePath),
                false
        );
    }

    /**
     * Reads a single line of input.
     *
     * @return the line read, or null if end of input
     * @throws IOException if an I/O error occurs
     */
    public String readLine() throws IOException {
        return reader.readLine();
    }

    /**
     * Reads all lines from the input.
     *
     * @return a list of all lines
     * @throws IOException if an I/O error occurs
     */
    public List<String> readAllLines() throws IOException {
        return reader.lines().collect(Collectors.toList());
    }

    /**
     * Reads all non-empty lines from the input.
     *
     * @return a list of non-empty lines
     * @throws IOException if an I/O error occurs
     */
    public List<String> readAllNonEmptyLines() throws IOException {
        return reader.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Checks if this is interactive mode.
     *
     * @return true if interactive
     */
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * Reads all lines from a file.
     *
     * @param filePath the path to the file
     * @return a list of lines
     * @throws IOException if the file cannot be read
     */
    public static List<String> readLinesFromFile(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
