package com.gojek.parkinglot.command;

import com.gojek.parkinglot.exception.InvalidCommandException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Command enum.
 */
public class CommandTest {

    @Test
    public void shouldParseCreateParkingLot() {
        Command cmd = Command.fromString("create_parking_lot");

        assertEquals(Command.CREATE_PARKING_LOT, cmd);
        assertEquals(1, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParsePark() {
        Command cmd = Command.fromString("park");

        assertEquals(Command.PARK, cmd);
        assertEquals(2, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseLeave() {
        Command cmd = Command.fromString("leave");

        assertEquals(Command.LEAVE, cmd);
        assertEquals(1, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseStatus() {
        Command cmd = Command.fromString("status");

        assertEquals(Command.STATUS, cmd);
        assertEquals(0, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseRegistrationNumbersForCarsWithColour() {
        Command cmd = Command.fromString("registration_numbers_for_cars_with_colour");

        assertEquals(Command.REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR, cmd);
        assertEquals(1, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseSlotNumbersForCarsWithColour() {
        Command cmd = Command.fromString("slot_numbers_for_cars_with_colour");

        assertEquals(Command.SLOT_NUMBERS_FOR_CARS_WITH_COLOUR, cmd);
        assertEquals(1, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseSlotNumberForRegistrationNumber() {
        Command cmd = Command.fromString("slot_number_for_registration_number");

        assertEquals(Command.SLOT_NUMBER_FOR_REGISTRATION_NUMBER, cmd);
        assertEquals(1, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseExit() {
        Command cmd = Command.fromString("exit");

        assertEquals(Command.EXIT, cmd);
        assertEquals(0, cmd.getExpectedArgs());
    }

    @Test
    public void shouldParseHelp() {
        Command cmd = Command.fromString("help");

        assertEquals(Command.HELP, cmd);
        assertEquals(0, cmd.getExpectedArgs());
    }

    @Test
    public void shouldBeCaseInsensitive() {
        assertEquals(Command.CREATE_PARKING_LOT, Command.fromString("CREATE_PARKING_LOT"));
        assertEquals(Command.PARK, Command.fromString("PARK"));
        assertEquals(Command.STATUS, Command.fromString("STATUS"));
    }

    @Test
    public void shouldTrimWhitespace() {
        assertEquals(Command.PARK, Command.fromString("  park  "));
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldRejectEmptyCommand() {
        Command.fromString("");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldRejectNullCommand() {
        Command.fromString(null);
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldRejectBlankCommand() {
        Command.fromString("   ");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldRejectUnknownCommand() {
        Command.fromString("unknown_command");
    }

    @Test
    public void isValidShouldReturnTrueForValidCommands() {
        assertTrue(Command.isValid("park"));
        assertTrue(Command.isValid("leave"));
        assertTrue(Command.isValid("status"));
    }

    @Test
    public void isValidShouldReturnFalseForInvalidCommands() {
        assertFalse(Command.isValid("invalid"));
        assertFalse(Command.isValid(null));
        assertFalse(Command.isValid(""));
        assertFalse(Command.isValid("   "));
    }

    @Test
    public void shouldReturnCorrectCommandString() {
        assertEquals("create_parking_lot", Command.CREATE_PARKING_LOT.getCommandString());
        assertEquals("park", Command.PARK.getCommandString());
        assertEquals("leave", Command.LEAVE.getCommandString());
    }
}
