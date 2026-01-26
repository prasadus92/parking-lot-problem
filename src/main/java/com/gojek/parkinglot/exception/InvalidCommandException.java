package com.gojek.parkinglot.exception;

/**
 * Exception thrown when an invalid or unknown command is received.
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class InvalidCommandException extends ParkingLotException {

    private static final long serialVersionUID = 1L;

    private final String command;

    /**
     * Constructs a new InvalidCommandException for the specified command.
     *
     * @param command the invalid command
     */
    public InvalidCommandException(String command) {
        super("Invalid command: " + command);
        this.command = command;
    }

    /**
     * Constructs a new InvalidCommandException with the specified message.
     *
     * @param command the invalid command
     * @param message the detail message
     */
    public InvalidCommandException(String command, String message) {
        super(message);
        this.command = command;
    }

    /**
     * Returns the invalid command.
     *
     * @return the command string
     */
    public String getCommand() {
        return command;
    }
}
