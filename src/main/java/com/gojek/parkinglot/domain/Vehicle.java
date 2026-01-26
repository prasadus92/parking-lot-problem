package com.gojek.parkinglot.domain;

import java.util.Objects;

/**
 * Immutable domain entity representing a vehicle in the parking lot system.
 *
 * <p>A Vehicle is identified by its registration number and has an associated color.
 * This class follows the Value Object pattern - two vehicles with the same
 * registration number are considered equal.</p>
 *
 * <p>Thread-safe: This class is immutable and therefore thread-safe.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public final class Vehicle {

    private final String registrationNumber;
    private final String color;

    /**
     * Constructs a new Vehicle with the specified registration number and color.
     *
     * @param registrationNumber the vehicle's registration number (e.g., "KA-01-HH-1234")
     * @param color the vehicle's color (e.g., "White", "Black")
     * @throws IllegalArgumentException if registrationNumber or color is null or blank
     */
    public Vehicle(String registrationNumber, String color) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be null or blank");
        }
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or blank");
        }
        this.registrationNumber = registrationNumber.trim();
        this.color = color.trim();
    }

    /**
     * Returns the vehicle's registration number.
     *
     * @return the registration number, never null
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Returns the vehicle's color.
     *
     * @return the color, never null
     */
    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return registrationNumber.equals(vehicle.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }

    @Override
    public String toString() {
        return String.format("Vehicle{registrationNumber='%s', color='%s'}",
                registrationNumber, color);
    }
}
