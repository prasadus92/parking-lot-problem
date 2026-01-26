package com.gojek.parkinglot.app;

import com.gojek.parkinglot.command.Command;
import com.gojek.parkinglot.command.CommandExecutor;
import com.gojek.parkinglot.command.CommandParser;
import com.gojek.parkinglot.command.CommandParser.ParsedCommand;
import com.gojek.parkinglot.command.CommandResult;
import com.gojek.parkinglot.exception.InvalidCommandException;
import com.gojek.parkinglot.io.InputReader;
import com.gojek.parkinglot.io.OutputWriter;
import com.gojek.parkinglot.service.ParkingLotService;

import java.io.IOException;
import java.util.List;

/**
 * Main application class for the Parking Lot System.
 *
 * <p>This class orchestrates the application flow, supporting both
 * interactive command-line mode and file-based batch processing.</p>
 *
 * <p>Usage:</p>
 * <ul>
 *   <li>Interactive mode: {@code java -jar parking-lot.jar}</li>
 *   <li>File mode: {@code java -jar parking-lot.jar <input_file>}</li>
 * </ul>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class ParkingLotApplication {

    private final ParkingLotService service;
    private final CommandExecutor executor;
    private final CommandParser parser;
    private final OutputWriter output;

    /**
     * Constructs the application with default dependencies.
     */
    public ParkingLotApplication() {
        this(new ParkingLotService(), new OutputWriter());
    }

    /**
     * Constructs the application with custom dependencies.
     *
     * @param service the parking lot service
     * @param output the output writer
     */
    public ParkingLotApplication(ParkingLotService service, OutputWriter output) {
        this.service = service;
        this.executor = new CommandExecutor(service);
        this.parser = new CommandParser();
        this.output = output;
    }

    /**
     * Runs the application in interactive mode.
     *
     * <p>Commands are read from standard input until 'exit' is entered
     * or end of input is reached.</p>
     */
    public void runInteractive() {
        try (InputReader input = new InputReader()) {
            String line;
            while ((line = input.readLine()) != null) {
                CommandResult result = processCommand(line);
                if (result != null) {
                    result.getOutputIfPresent().ifPresent(output::writeLine);
                    if (result.shouldExit()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            output.writeLine("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Runs the application with commands from a file.
     *
     * @param filePath the path to the input file
     */
    public void runFromFile(String filePath) {
        try {
            List<String> lines = InputReader.readLinesFromFile(filePath);
            for (String line : lines) {
                CommandResult result = processCommand(line);
                if (result != null) {
                    result.getOutputIfPresent().ifPresent(output::writeLine);
                    if (result.shouldExit()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            output.writeLine("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Processes a single command line.
     *
     * @param line the command line
     * @return the command result, or null if the line is blank
     */
    public CommandResult processCommand(String line) {
        if (parser.isBlank(line)) {
            return null;
        }

        try {
            ParsedCommand parsed = parser.parse(line);
            if (parsed == null) {
                return null;
            }
            return executor.execute(parsed.getCommand(), parsed.getArgs());
        } catch (InvalidCommandException e) {
            return CommandResult.failure(e.getMessage());
        }
    }

    /**
     * Returns the parking lot service.
     *
     * @return the service instance
     */
    public ParkingLotService getService() {
        return service;
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments (optional: input file path)
     */
    public static void main(String[] args) {
        ParkingLotApplication app = new ParkingLotApplication();

        if (args.length == 0) {
            app.runInteractive();
        } else {
            app.runFromFile(args[0]);
        }
    }
}
