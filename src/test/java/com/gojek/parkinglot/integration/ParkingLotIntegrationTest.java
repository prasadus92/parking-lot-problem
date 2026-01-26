package com.gojek.parkinglot.integration;

import com.gojek.parkinglot.app.ParkingLotApplication;
import com.gojek.parkinglot.command.CommandResult;
import com.gojek.parkinglot.io.OutputWriter;
import com.gojek.parkinglot.service.ParkingLotService;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests for the complete parking lot workflow.
 *
 * These tests verify the end-to-end behavior of the system
 * as described in the problem statement.
 */
public class ParkingLotIntegrationTest {

    private ParkingLotApplication app;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        ParkingLotService service = new ParkingLotService();
        OutputWriter output = new OutputWriter(new PrintStream(outputStream));
        app = new ParkingLotApplication(service, output);
    }

    @Test
    public void shouldExecuteCompleteWorkflow() {
        // Create parking lot
        CommandResult result = app.processCommand("create_parking_lot 6");
        assertTrue(result.isSuccess());
        assertEquals("Created a parking lot with 6 slots", result.getOutput());

        // Park vehicles
        result = app.processCommand("park KA-01-HH-1234 White");
        assertEquals("Allocated slot number: 1", result.getOutput());

        result = app.processCommand("park KA-01-HH-9999 White");
        assertEquals("Allocated slot number: 2", result.getOutput());

        result = app.processCommand("park KA-01-BB-0001 Black");
        assertEquals("Allocated slot number: 3", result.getOutput());

        result = app.processCommand("park KA-01-HH-7777 Red");
        assertEquals("Allocated slot number: 4", result.getOutput());

        result = app.processCommand("park KA-01-HH-2701 Blue");
        assertEquals("Allocated slot number: 5", result.getOutput());

        result = app.processCommand("park KA-01-HH-3141 Black");
        assertEquals("Allocated slot number: 6", result.getOutput());

        // Leave slot 4
        result = app.processCommand("leave 4");
        assertEquals("Slot number 4 is free", result.getOutput());

        // Check status
        result = app.processCommand("status");
        assertTrue(result.getOutput().contains("KA-01-HH-1234"));
        assertTrue(result.getOutput().contains("KA-01-HH-9999"));
        assertTrue(result.getOutput().contains("KA-01-BB-0001"));
        assertTrue(result.getOutput().contains("KA-01-HH-2701"));
        assertTrue(result.getOutput().contains("KA-01-HH-3141"));
        assertFalse(result.getOutput().contains("KA-01-HH-7777")); // Left

        // Park in freed slot
        result = app.processCommand("park KA-01-P-333 White");
        assertEquals("Allocated slot number: 4", result.getOutput());

        // Try to park when full
        result = app.processCommand("park DL-12-AA-9999 White");
        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is full", result.getOutput());

        // Query by color
        result = app.processCommand("registration_numbers_for_cars_with_colour White");
        assertTrue(result.getOutput().contains("KA-01-HH-1234"));
        assertTrue(result.getOutput().contains("KA-01-HH-9999"));
        assertTrue(result.getOutput().contains("KA-01-P-333"));

        result = app.processCommand("slot_numbers_for_cars_with_colour White");
        assertEquals("1, 2, 4", result.getOutput());

        // Query by registration
        result = app.processCommand("slot_number_for_registration_number KA-01-HH-3141");
        assertEquals("6", result.getOutput());

        result = app.processCommand("slot_number_for_registration_number MH-04-AY-1111");
        assertTrue(result.isFailure());
        assertEquals("Not found", result.getOutput());
    }

    @Test
    public void shouldHandleSlotReallocation() {
        app.processCommand("create_parking_lot 3");

        app.processCommand("park V1 White");
        app.processCommand("park V2 Black");
        app.processCommand("park V3 Red");

        // Free slot 2
        app.processCommand("leave 2");

        // New vehicle should get slot 2 (nearest available)
        CommandResult result = app.processCommand("park V4 Blue");
        assertEquals("Allocated slot number: 2", result.getOutput());
    }

    @Test
    public void shouldHandleMultipleLeavesAndParks() {
        app.processCommand("create_parking_lot 5");

        // Fill lot
        for (int i = 1; i <= 5; i++) {
            app.processCommand("park V" + i + " Color" + i);
        }

        // Leave slots 2 and 4
        app.processCommand("leave 2");
        app.processCommand("leave 4");

        // New parks should use 2 first, then 4
        CommandResult r1 = app.processCommand("park V6 Blue");
        assertEquals("Allocated slot number: 2", r1.getOutput());

        CommandResult r2 = app.processCommand("park V7 Green");
        assertEquals("Allocated slot number: 4", r2.getOutput());
    }

    @Test
    public void shouldHandleColorQueriesWithCaseInsensitivity() {
        app.processCommand("create_parking_lot 3");
        app.processCommand("park V1 White");
        app.processCommand("park V2 white");
        app.processCommand("park V3 WHITE");

        CommandResult result = app.processCommand("registration_numbers_for_cars_with_colour white");
        assertTrue(result.isSuccess());
        // All three should be found regardless of case
        String output = result.getOutput();
        assertTrue(output.contains("V1"));
        assertTrue(output.contains("V2"));
        assertTrue(output.contains("V3"));
    }

    @Test
    public void shouldProcessBlankLinesGracefully() {
        assertNull(app.processCommand(""));
        assertNull(app.processCommand("   "));
        assertNull(app.processCommand(null));
    }

    @Test
    public void shouldRejectOperationsWithoutParkingLot() {
        CommandResult result = app.processCommand("park V1 White");
        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is not created", result.getOutput());

        result = app.processCommand("leave 1");
        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is not created", result.getOutput());

        result = app.processCommand("status");
        assertTrue(result.isFailure());
        assertEquals("Sorry, parking lot is not created", result.getOutput());
    }

    @Test
    public void shouldProcessFileInput() throws IOException {
        // Create a temp file with commands
        Path tempFile = Files.createTempFile("parking_test", ".txt");
        List<String> commands = Arrays.asList(
                "create_parking_lot 3",
                "park KA-01-HH-1234 White",
                "park KA-01-BB-5678 Black",
                "status"
        );
        Files.write(tempFile, commands);

        try {
            app.runFromFile(tempFile.toString());

            String output = outputStream.toString();
            assertTrue(output.contains("Created a parking lot with 3 slots"));
            assertTrue(output.contains("Allocated slot number: 1"));
            assertTrue(output.contains("Allocated slot number: 2"));
            assertTrue(output.contains("KA-01-HH-1234"));
            assertTrue(output.contains("KA-01-BB-5678"));
        } finally {
            Files.delete(tempFile);
        }
    }

    @Test
    public void shouldHandleExitCommand() {
        CommandResult result = app.processCommand("exit");

        assertTrue(result.isSuccess());
        assertTrue(result.shouldExit());
    }

    @Test
    public void shouldHandleHelpCommand() {
        CommandResult result = app.processCommand("help");

        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("Available commands"));
    }

    @Test
    public void shouldHandleInvalidCommands() {
        CommandResult result = app.processCommand("invalid_command");

        assertTrue(result.isFailure());
        assertTrue(result.getOutput().contains("Invalid command"));
    }
}
