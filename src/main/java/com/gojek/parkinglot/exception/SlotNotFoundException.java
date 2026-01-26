package com.gojek.parkinglot.exception;

/**
 * Exception thrown when a requested slot cannot be found or is invalid.
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class SlotNotFoundException extends ParkingLotException {

    private static final long serialVersionUID = 1L;

    private final int slotNumber;

    /**
     * Constructs a new SlotNotFoundException for the specified slot.
     *
     * @param slotNumber the slot number that was not found
     */
    public SlotNotFoundException(int slotNumber) {
        super("Slot " + slotNumber + " not found");
        this.slotNumber = slotNumber;
    }

    /**
     * Constructs a new SlotNotFoundException with the specified message.
     *
     * @param slotNumber the slot number that was not found
     * @param message the detail message
     */
    public SlotNotFoundException(int slotNumber, String message) {
        super(message);
        this.slotNumber = slotNumber;
    }

    /**
     * Returns the slot number that was not found.
     *
     * @return the slot number
     */
    public int getSlotNumber() {
        return slotNumber;
    }
}
