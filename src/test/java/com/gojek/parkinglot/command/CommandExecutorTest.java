package com.gojek.parkinglot.command;

import com.gojek.parkinglot.service.ParkingLotService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the CommandExecutor.
 */
public class CommandExecutorTest {

    private ParkingLotService service;
    private CommandExecutor executor;

    @Before
    public void setUp() {
        service = new ParkingLotService();
        executor = new CommandExecutor(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullService() {
        new CommandExecutor(null);
    }

    // Create parking lot tests

    @Test
    public void shouldExecuteCreateParkingLot() {
        CommandResult result = executor.execute(Command.CREATE_PARKING_LOT, new String[]{"6"});

        assertTrue(result.isSuccess());
        assertEquals("Created a parking lot with 6 slots", result.getOutput());
        assertTrue(service.isParkingLotCreated());
    }

    @Test
    public void shouldFailCreateWithInvalidCapacity() {
        CommandResult result = executor.execute(Command.CREATE_PARKING_LOT, new String[]{"abc"});

        assertTrue(result.isFailure());
        assertTrue(result.getOutput().contains("Invalid lot count"));
    }

    @Test
    public void shouldFailCreateWithMissingArgs() {
        CommandResult result = executor.execute(Command.CREATE_PARKING_LOT, new String[]{});

        assertTrue(result.isFailure());
        assertTrue(result.getOutput().contains("requires"));
    }

    // Park tests

    @Test
    public void shouldExecutePark() {
        service.createParkingLot(6);

        CommandResult result = executor.execute(Command.PARK,
                new String[]{"KA-01-HH-1234", "White"});

        assertTrue(result.isSuccess());
        assertEquals("Allocated slot number: 1", result.getOutput());
    }

    @Test
    public void shouldFailParkWhenFull() {
        service.createParkingLot(1);
        service.park("V1", "White");

        CommandResult result = executor.execute(Command.PARK,
                new String[]{"V2", "Black"});

        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is full", result.getOutput());
    }

    @Test
    public void shouldFailParkWithoutCreation() {
        CommandResult result = executor.execute(Command.PARK,
                new String[]{"V1", "White"});

        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is not created", result.getOutput());
    }

    // Leave tests

    @Test
    public void shouldExecuteLeave() {
        service.createParkingLot(6);
        service.park("V1", "White");

        CommandResult result = executor.execute(Command.LEAVE, new String[]{"1"});

        assertTrue(result.isSuccess());
        assertEquals("Slot number 1 is free", result.getOutput());
    }

    @Test
    public void shouldFailLeaveWithInvalidSlot() {
        service.createParkingLot(6);

        CommandResult result = executor.execute(Command.LEAVE, new String[]{"abc"});

        assertTrue(result.isFailure());
        assertTrue(result.getOutput().contains("Invalid slot number"));
    }

    // Status tests

    @Test
    public void shouldExecuteStatus() {
        service.createParkingLot(6);
        service.park("KA-01-HH-1234", "White");
        service.park("KA-01-HH-5678", "Black");

        CommandResult result = executor.execute(Command.STATUS, new String[]{});

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("Slot No."));
        assertTrue(result.getOutput().contains("KA-01-HH-1234"));
        assertTrue(result.getOutput().contains("KA-01-HH-5678"));
        assertTrue(result.getOutput().contains("White"));
        assertTrue(result.getOutput().contains("Black"));
    }

    @Test
    public void shouldShowEmptyStatusMessage() {
        service.createParkingLot(6);

        CommandResult result = executor.execute(Command.STATUS, new String[]{});

        assertTrue(result.isSuccess());
        assertEquals("Parking lot is empty", result.getOutput());
    }

    // Query tests

    @Test
    public void shouldFindRegistrationsByColor() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.park("V2", "Black");
        service.park("V3", "White");

        CommandResult result = executor.execute(
                Command.REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR,
                new String[]{"White"});

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("V1"));
        assertTrue(result.getOutput().contains("V3"));
    }

    @Test
    public void shouldReturnNotFoundForUnknownColor() {
        service.createParkingLot(6);
        service.park("V1", "White");

        CommandResult result = executor.execute(
                Command.REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR,
                new String[]{"Purple"});

        assertTrue(result.isFailure());
        assertEquals("Not found", result.getOutput());
    }

    @Test
    public void shouldFindSlotsByColor() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.park("V2", "Black");
        service.park("V3", "White");

        CommandResult result = executor.execute(
                Command.SLOT_NUMBERS_FOR_CARS_WITH_COLOUR,
                new String[]{"White"});

        assertTrue(result.isSuccess());
        assertEquals("1, 3", result.getOutput());
    }

    @Test
    public void shouldFindSlotByRegistration() {
        service.createParkingLot(6);
        service.park("KA-01-HH-1234", "White");
        service.park("KA-01-HH-5678", "Black");

        CommandResult result = executor.execute(
                Command.SLOT_NUMBER_FOR_REGISTRATION_NUMBER,
                new String[]{"KA-01-HH-5678"});

        assertTrue(result.isSuccess());
        assertEquals("2", result.getOutput());
    }

    @Test
    public void shouldReturnNotFoundForUnknownRegistration() {
        service.createParkingLot(6);

        CommandResult result = executor.execute(
                Command.SLOT_NUMBER_FOR_REGISTRATION_NUMBER,
                new String[]{"UNKNOWN"});

        assertTrue(result.isFailure());
        assertEquals("Not found", result.getOutput());
    }

    // Exit tests

    @Test
    public void shouldExecuteExit() {
        CommandResult result = executor.execute(Command.EXIT, new String[]{});

        assertTrue(result.isSuccess());
        assertTrue(result.shouldExit());
    }

    // Help tests

    @Test
    public void shouldExecuteHelp() {
        CommandResult result = executor.execute(Command.HELP, new String[]{});

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("Available commands"));
        assertTrue(result.getOutput().contains("create_parking_lot"));
        assertTrue(result.getOutput().contains("park"));
    }
}
