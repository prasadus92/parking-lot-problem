package com.gojek.parkinglot.command;

import com.gojek.parkinglot.domain.ParkingSlot;
import com.gojek.parkinglot.domain.Vehicle;
import com.gojek.parkinglot.exception.*;
import com.gojek.parkinglot.service.ParkingLotService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes parking lot commands and returns results.
 *
 * <p>This class serves as the controller layer, translating commands into
 * service calls and formatting the results for output.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommandExecutor {

    private final ParkingLotService service;

    /**
     * Constructs a CommandExecutor with the specified service.
     *
     * @param service the parking lot service
     */
    public CommandExecutor(ParkingLotService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    /**
     * Executes a command with the given arguments.
     *
     * @param command the command to execute
     * @param args the command arguments
     * @return the result of the command execution
     */
    public CommandResult execute(Command command, String[] args) {
        try {
            validateArgs(command, args);

            switch (command) {
                case CREATE_PARKING_LOT:
                    return executeCreateParkingLot(args);

                case PARK:
                    return executePark(args);

                case LEAVE:
                    return executeLeave(args);

                case STATUS:
                    return executeStatus();

                case REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR:
                    return executeGetRegistrationsByColor(args);

                case SLOT_NUMBERS_FOR_CARS_WITH_COLOUR:
                    return executeGetSlotsByColor(args);

                case SLOT_NUMBER_FOR_REGISTRATION_NUMBER:
                    return executeGetSlotByRegistration(args);

                case EXIT:
                    return CommandResult.exit();

                case HELP:
                    return executeHelp();

                default:
                    return CommandResult.failure("Unknown command");
            }
        } catch (ParkingLotException e) {
            return CommandResult.failure(e.getMessage());
        } catch (IllegalArgumentException e) {
            return CommandResult.failure(e.getMessage());
        }
    }

    private void validateArgs(Command command, String[] args) {
        int expectedArgs = command.getExpectedArgs();
        int actualArgs = args != null ? args.length : 0;

        if (actualArgs < expectedArgs) {
            throw new InvalidCommandException(command.getCommandString(),
                    String.format("Command '%s' requires %d argument(s), but got %d",
                            command.getCommandString(), expectedArgs, actualArgs));
        }
    }

    private CommandResult executeCreateParkingLot(String[] args) {
        try {
            int capacity = Integer.parseInt(args[0]);
            service.createParkingLot(capacity);
            return CommandResult.success("Created a parking lot with " + capacity + " slots");
        } catch (NumberFormatException e) {
            return CommandResult.failure("Invalid lot count: " + args[0]);
        }
    }

    private CommandResult executePark(String[] args) {
        String registrationNumber = args[0];
        String color = args[1];
        int slot = service.park(registrationNumber, color);
        return CommandResult.success("Allocated slot number: " + slot);
    }

    private CommandResult executeLeave(String[] args) {
        try {
            int slotNumber = Integer.parseInt(args[0]);
            service.leave(slotNumber);
            return CommandResult.success("Slot number " + slotNumber + " is free");
        } catch (NumberFormatException e) {
            return CommandResult.failure("Invalid slot number: " + args[0]);
        }
    }

    private CommandResult executeStatus() {
        List<ParkingSlot> occupiedSlots = service.getStatus();

        if (occupiedSlots.isEmpty()) {
            return CommandResult.success("Parking lot is empty");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Slot No.    Registration No    Colour\n");

        for (ParkingSlot slot : occupiedSlots) {
            Vehicle vehicle = slot.getVehicle().orElse(null);
            if (vehicle != null) {
                sb.append(String.format("%-12d%-19s%s\n",
                        slot.getSlotNumber(),
                        vehicle.getRegistrationNumber(),
                        vehicle.getColor()));
            }
        }

        // Remove trailing newline
        String output = sb.toString();
        if (output.endsWith("\n")) {
            output = output.substring(0, output.length() - 1);
        }

        return CommandResult.success(output);
    }

    private CommandResult executeGetRegistrationsByColor(String[] args) {
        String color = args[0];
        List<String> registrations = service.getRegistrationsByColor(color);
        String output = String.join(", ", registrations);
        return CommandResult.success(output);
    }

    private CommandResult executeGetSlotsByColor(String[] args) {
        String color = args[0];
        List<Integer> slots = service.getSlotsByColor(color);
        String output = slots.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        return CommandResult.success(output);
    }

    private CommandResult executeGetSlotByRegistration(String[] args) {
        String registrationNumber = args[0];
        int slot = service.getSlotByRegistration(registrationNumber);
        return CommandResult.success(String.valueOf(slot));
    }

    private CommandResult executeHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        sb.append("  create_parking_lot <capacity>           - Create a parking lot\n");
        sb.append("  park <registration_number> <color>      - Park a vehicle\n");
        sb.append("  leave <slot_number>                     - Remove vehicle from slot\n");
        sb.append("  status                                  - Show parking lot status\n");
        sb.append("  registration_numbers_for_cars_with_colour <color>\n");
        sb.append("                                          - Find registrations by color\n");
        sb.append("  slot_numbers_for_cars_with_colour <color>\n");
        sb.append("                                          - Find slots by color\n");
        sb.append("  slot_number_for_registration_number <reg_no>\n");
        sb.append("                                          - Find slot by registration\n");
        sb.append("  help                                    - Show this help\n");
        sb.append("  exit                                    - Exit the program");
        return CommandResult.success(sb.toString());
    }
}
