package com.gojek.parkinglot.exception;

/**
 * Base exception class for all parking lot related exceptions.
 *
 * <p>This is an unchecked exception (extends RuntimeException) following
 * the modern Java best practice of using unchecked exceptions for
 * programming errors and business rule violations.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class ParkingLotException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ParkingLotException with the specified message.
     *
     * @param message the detail message
     */
    public ParkingLotException(String message) {
        super(message);
    }

    /**
     * Constructs a new ParkingLotException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ParkingLotException(String message, Throwable cause) {
        super(message, cause);
    }
}
