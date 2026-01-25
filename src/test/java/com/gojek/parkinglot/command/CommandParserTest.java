package com.gojek.parkinglot.command;

import com.gojek.parkinglot.command.CommandParser.ParsedCommand;
import com.gojek.parkinglot.exception.InvalidCommandException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the CommandParser.
 */
public class CommandParserTest {

    private CommandParser parser;

    @Before
    public void setUp() {
        parser = new CommandParser();
    }

    @Test
    public void shouldParseSimpleCommand() {
        ParsedCommand parsed = parser.parse("status");

        assertEquals(Command.STATUS, parsed.getCommand());
        assertEquals(0, parsed.getArgs().length);
    }

    @Test
    public void shouldParseCommandWithOneArg() {
        ParsedCommand parsed = parser.parse("create_parking_lot 6");

        assertEquals(Command.CREATE_PARKING_LOT, parsed.getCommand());
        assertEquals(1, parsed.getArgs().length);
        assertEquals("6", parsed.getArgs()[0]);
    }

    @Test
    public void shouldParseCommandWithMultipleArgs() {
        ParsedCommand parsed = parser.parse("park KA-01-HH-1234 White");

        assertEquals(Command.PARK, parsed.getCommand());
        assertEquals(2, parsed.getArgs().length);
        assertEquals("KA-01-HH-1234", parsed.getArgs()[0]);
        assertEquals("White", parsed.getArgs()[1]);
    }

    @Test
    public void shouldHandleExtraWhitespace() {
        ParsedCommand parsed = parser.parse("  park    KA-01-HH-1234    White  ");

        assertEquals(Command.PARK, parsed.getCommand());
        assertEquals(2, parsed.getArgs().length);
        assertEquals("KA-01-HH-1234", parsed.getArgs()[0]);
        assertEquals("White", parsed.getArgs()[1]);
    }

    @Test
    public void shouldReturnNullForEmptyInput() {
        assertNull(parser.parse(""));
    }

    @Test
    public void shouldReturnNullForNullInput() {
        assertNull(parser.parse(null));
    }

    @Test
    public void shouldReturnNullForBlankInput() {
        assertNull(parser.parse("   "));
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldThrowForInvalidCommand() {
        parser.parse("invalid_command");
    }

    @Test
    public void isBlankShouldReturnTrueForEmpty() {
        assertTrue(parser.isBlank(""));
        assertTrue(parser.isBlank(null));
        assertTrue(parser.isBlank("   "));
    }

    @Test
    public void isBlankShouldReturnFalseForNonEmpty() {
        assertFalse(parser.isBlank("park"));
        assertFalse(parser.isBlank("  status  "));
    }
}
