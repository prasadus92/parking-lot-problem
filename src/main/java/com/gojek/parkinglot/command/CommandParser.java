package com.gojek.parkinglot.command;

import java.util.Arrays;

/**
 * Parses command line input into Command and arguments.
 *
 * <p>This class handles the parsing of user input strings into
 * structured command objects that can be executed.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommandParser {

    /**
     * Represents a parsed command with its arguments.
     */
    public static class ParsedCommand {
        private final Command command;
        private final String[] args;

        /**
         * Constructs a ParsedCommand.
         *
         * @param command the command
         * @param args the arguments
         */
        public ParsedCommand(Command command, String[] args) {
            this.command = command;
            this.args = args != null ? args : new String[0];
        }

        /**
         * Returns the command.
         *
         * @return the command
         */
        public Command getCommand() {
            return command;
        }

        /**
         * Returns the arguments.
         *
         * @return the arguments array
         */
        public String[] getArgs() {
            return args;
        }
    }

    /**
     * Parses an input line into a ParsedCommand.
     *
     * @param input the input line
     * @return the parsed command
     * @throws com.gojek.parkinglot.exception.InvalidCommandException if the command is invalid
     */
    public ParsedCommand parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.trim().split("\\s+");
        if (parts.length == 0) {
            return null;
        }

        Command command = Command.fromString(parts[0]);
        String[] args = parts.length > 1
                ? Arrays.copyOfRange(parts, 1, parts.length)
                : new String[0];

        return new ParsedCommand(command, args);
    }

    /**
     * Checks if the input is a blank or empty line.
     *
     * @param input the input to check
     * @return true if blank or empty
     */
    public boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }
}
