package com.gojek.parkinglot.exception;

/**
 * Exception thrown when attempting to park in a full parking lot.
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class ParkingLotFullException extends ParkingLotException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ParkingLotFullException with a default message.
     */
    public ParkingLotFullException() {
        super("Sorry, parking lot is full");
    }

    /**
     * Constructs a new ParkingLotFullException with the specified message.
     *
     * @param message the detail message
     */
    public ParkingLotFullException(String message) {
        super(message);
    }
}
