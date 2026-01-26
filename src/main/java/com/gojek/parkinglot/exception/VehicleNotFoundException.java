package com.gojek.parkinglot.exception;

/**
 * Exception thrown when a vehicle cannot be found by registration number.
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class VehicleNotFoundException extends ParkingLotException {

    private static final long serialVersionUID = 1L;

    private final String registrationNumber;

    /**
     * Constructs a new VehicleNotFoundException with a default message.
     *
     * @param registrationNumber the registration number that was not found
     */
    public VehicleNotFoundException(String registrationNumber) {
        super("Not found");
        this.registrationNumber = registrationNumber;
    }

    /**
     * Constructs a new VehicleNotFoundException with the specified message.
     *
     * @param registrationNumber the registration number that was not found
     * @param message the detail message
     */
    public VehicleNotFoundException(String registrationNumber, String message) {
        super(message);
        this.registrationNumber = registrationNumber;
    }

    /**
     * Returns the registration number that was not found.
     *
     * @return the registration number
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }
}
