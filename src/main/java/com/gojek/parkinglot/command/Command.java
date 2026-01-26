package com.gojek.parkinglot.command;

import com.gojek.parkinglot.exception.InvalidCommandException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum representing all available parking lot commands.
 *
 * <p>This enum implements the Command Pattern using a functional approach.
 * Each command is associated with its command string and expected argument count.</p>
 *
 * <p>Commands are case-insensitive and can be looked up using {@link #fromString(String)}.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public enum Command {

    /**
     * Creates a new parking lot with specified capacity.
     * Usage: create_parking_lot [capacity]
     */
    CREATE_PARKING_LOT("create_parking_lot", 1),

    /**
     * Parks a vehicle in the nearest available slot.
     * Usage: park [registration_number] [color]
     */
    PARK("park", 2),

    /**
     * Removes a vehicle from a slot.
     * Usage: leave [slot_number]
     */
    LEAVE("leave", 1),

    /**
     * Displays the current parking lot status.
     * Usage: status
     */
    STATUS("status", 0),

    /**
     * Finds registration numbers by vehicle color.
     * Usage: registration_numbers_for_cars_with_colour [color]
     */
    REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR("registration_numbers_for_cars_with_colour", 1),

    /**
     * Finds slot numbers by vehicle color.
     * Usage: slot_numbers_for_cars_with_colour [color]
     */
    SLOT_NUMBERS_FOR_CARS_WITH_COLOUR("slot_numbers_for_cars_with_colour", 1),

    /**
     * Finds slot number by registration number.
     * Usage: slot_number_for_registration_number [registration_number]
     */
    SLOT_NUMBER_FOR_REGISTRATION_NUMBER("slot_number_for_registration_number", 1),

    /**
     * Exits the application.
     * Usage: exit
     */
    EXIT("exit", 0),

    /**
     * Displays help information.
     * Usage: help
     */
    HELP("help", 0);

    private final String commandString;
    private final int expectedArgs;

    private static final Map<String, Command> COMMAND_MAP;

    static {
        COMMAND_MAP = Arrays.stream(values())
                .collect(Collectors.toMap(
                        cmd -> cmd.commandString.toLowerCase(),
                        Function.identity()
                ));
    }

    Command(String commandString, int expectedArgs) {
        this.commandString = commandString;
        this.expectedArgs = expectedArgs;
    }

    /**
     * Returns the command string.
     *
     * @return the command string
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Returns the expected number of arguments.
     *
     * @return the expected argument count
     */
    public int getExpectedArgs() {
        return expectedArgs;
    }

    /**
     * Parses a string to find the corresponding Command.
     *
     * @param commandStr the command string to parse
     * @return the matching Command
     * @throws InvalidCommandException if the command is not recognized
     */
    public static Command fromString(String commandStr) {
        if (commandStr == null || commandStr.trim().isEmpty()) {
            throw new InvalidCommandException("");
        }

        Command command = COMMAND_MAP.get(commandStr.toLowerCase().trim());
        if (command == null) {
            throw new InvalidCommandException(commandStr);
        }
        return command;
    }

    /**
     * Checks if a command string is valid.
     *
     * @param commandStr the command string to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String commandStr) {
        if (commandStr == null || commandStr.trim().isEmpty()) {
            return false;
        }
        return COMMAND_MAP.containsKey(commandStr.toLowerCase().trim());
    }
}
