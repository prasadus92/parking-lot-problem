package com.gojek.parkinglot.exception;

/**
 * Exception thrown when attempting operations on a parking lot that hasn't been created.
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class ParkingLotNotCreatedException extends ParkingLotException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ParkingLotNotCreatedException with a default message.
     */
    public ParkingLotNotCreatedException() {
        super("Sorry, parking lot is not created");
    }

    /**
     * Constructs a new ParkingLotNotCreatedException with the specified message.
     *
     * @param message the detail message
     */
    public ParkingLotNotCreatedException(String message) {
        super(message);
    }
}
