package com.gojek.parkinglot.command;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the result of executing a command.
 *
 * <p>A command result can be successful or failed, and may contain
 * an output message to be displayed to the user.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandResult {

    private final boolean success;
    private final String output;
    private final boolean shouldExit;

    private CommandResult(boolean success, String output, boolean shouldExit) {
        this.success = success;
        this.output = output;
        this.shouldExit = shouldExit;
    }

    /**
     * Creates a successful result with the specified output.
     *
     * @param output the output message
     * @return a successful CommandResult
     */
    public static CommandResult success(String output) {
        return new CommandResult(true, output, false);
    }

    /**
     * Creates a successful result with no output.
     *
     * @return a successful CommandResult with empty output
     */
    public static CommandResult success() {
        return new CommandResult(true, "", false);
    }

    /**
     * Creates a failed result with the specified error message.
     *
     * @param errorMessage the error message
     * @return a failed CommandResult
     */
    public static CommandResult failure(String errorMessage) {
        return new CommandResult(false, errorMessage, false);
    }

    /**
     * Creates a result that signals the application should exit.
     *
     * @return an exit CommandResult
     */
    public static CommandResult exit() {
        return new CommandResult(true, "", true);
    }

    /**
     * Returns whether the command was successful.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns whether the command failed.
     *
     * @return true if failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Returns the output message.
     *
     * @return the output message, may be empty
     */
    public String getOutput() {
        return output;
    }

    /**
     * Returns whether the application should exit.
     *
     * @return true if the application should exit
     */
    public boolean shouldExit() {
        return shouldExit;
    }

    /**
     * Returns the output if present.
     *
     * @return an Optional containing the output if not empty
     */
    public Optional<String> getOutputIfPresent() {
        return output.isEmpty() ? Optional.empty() : Optional.of(output);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandResult that = (CommandResult) o;
        return success == that.success &&
                shouldExit == that.shouldExit &&
                Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, output, shouldExit);
    }

    @Override
    public String toString() {
        return String.format("CommandResult{success=%s, output='%s', shouldExit=%s}",
                success, output, shouldExit);
    }
}
