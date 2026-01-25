package com.gojek.parkinglot.command;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the CommandResult class.
 */
public class CommandResultTest {

    @Test
    public void shouldCreateSuccessResult() {
        CommandResult result = CommandResult.success("Success message");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals("Success message", result.getOutput());
        assertFalse(result.shouldExit());
    }

    @Test
    public void shouldCreateSuccessResultWithoutOutput() {
        CommandResult result = CommandResult.success();

        assertTrue(result.isSuccess());
        assertEquals("", result.getOutput());
        assertFalse(result.getOutputIfPresent().isPresent());
    }

    @Test
    public void shouldCreateFailureResult() {
        CommandResult result = CommandResult.failure("Error message");

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals("Error message", result.getOutput());
        assertFalse(result.shouldExit());
    }

    @Test
    public void shouldCreateExitResult() {
        CommandResult result = CommandResult.exit();

        assertTrue(result.isSuccess());
        assertTrue(result.shouldExit());
        assertEquals("", result.getOutput());
    }

    @Test
    public void getOutputIfPresentShouldReturnOptionalForNonEmpty() {
        CommandResult result = CommandResult.success("message");

        assertTrue(result.getOutputIfPresent().isPresent());
        assertEquals("message", result.getOutputIfPresent().get());
    }

    @Test
    public void getOutputIfPresentShouldReturnEmptyForEmptyOutput() {
        CommandResult result = CommandResult.success();

        assertFalse(result.getOutputIfPresent().isPresent());
    }

    @Test
    public void shouldBeEqualForSameValues() {
        CommandResult r1 = CommandResult.success("test");
        CommandResult r2 = CommandResult.success("test");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void shouldNotBeEqualForDifferentSuccess() {
        CommandResult r1 = CommandResult.success("test");
        CommandResult r2 = CommandResult.failure("test");

        assertNotEquals(r1, r2);
    }

    @Test
    public void shouldNotBeEqualForDifferentOutput() {
        CommandResult r1 = CommandResult.success("test1");
        CommandResult r2 = CommandResult.success("test2");

        assertNotEquals(r1, r2);
    }

    @Test
    public void toStringShouldContainValues() {
        CommandResult result = CommandResult.success("test output");
        String str = result.toString();

        assertTrue(str.contains("success=true"));
        assertTrue(str.contains("test output"));
    }
}
