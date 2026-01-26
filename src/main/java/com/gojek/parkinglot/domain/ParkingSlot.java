package com.gojek.parkinglot.domain;

import java.util.Objects;
import java.util.Optional;

/**
 * Domain entity representing a parking slot in the parking lot.
 *
 * <p>A parking slot has a unique slot number and can optionally contain a vehicle.
 * Slots are ordered by their slot number, with lower numbers being closer to the entry.</p>
 *
 * <p>This class is not thread-safe. External synchronization is required
 * for concurrent access.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public final class ParkingSlot implements Comparable<ParkingSlot> {

    private final int slotNumber;
    private Vehicle vehicle;

    /**
     * Constructs a new empty ParkingSlot with the specified slot number.
     *
     * @param slotNumber the unique slot number (must be positive)
     * @throws IllegalArgumentException if slotNumber is not positive
     */
    public ParkingSlot(int slotNumber) {
        if (slotNumber <= 0) {
            throw new IllegalArgumentException("Slot number must be positive: " + slotNumber);
        }
        this.slotNumber = slotNumber;
        this.vehicle = null;
    }

    /**
     * Returns the slot number.
     *
     * @return the slot number (always positive)
     */
    public int getSlotNumber() {
        return slotNumber;
    }

    /**
     * Returns the vehicle parked in this slot, if any.
     *
     * @return an Optional containing the vehicle, or empty if the slot is available
     */
    public Optional<Vehicle> getVehicle() {
        return Optional.ofNullable(vehicle);
    }

    /**
     * Checks if this slot is available (no vehicle parked).
     *
     * @return true if the slot is available, false otherwise
     */
    public boolean isAvailable() {
        return vehicle == null;
    }

    /**
     * Checks if this slot is occupied (has a vehicle parked).
     *
     * @return true if the slot is occupied, false otherwise
     */
    public boolean isOccupied() {
        return vehicle != null;
    }

    /**
     * Parks a vehicle in this slot.
     *
     * @param vehicle the vehicle to park
     * @throws IllegalArgumentException if vehicle is null
     * @throws IllegalStateException if the slot is already occupied
     */
    public void park(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (this.vehicle != null) {
            throw new IllegalStateException("Slot " + slotNumber + " is already occupied");
        }
        this.vehicle = vehicle;
    }

    /**
     * Removes the vehicle from this slot, making it available.
     *
     * @return the vehicle that was parked
     * @throws IllegalStateException if the slot is already empty
     */
    public Vehicle vacate() {
        if (this.vehicle == null) {
            throw new IllegalStateException("Slot " + slotNumber + " is already empty");
        }
        Vehicle departingVehicle = this.vehicle;
        this.vehicle = null;
        return departingVehicle;
    }

    @Override
    public int compareTo(ParkingSlot other) {
        return Integer.compare(this.slotNumber, other.slotNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return slotNumber == that.slotNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotNumber);
    }

    @Override
    public String toString() {
        return String.format("ParkingSlot{slotNumber=%d, vehicle=%s}",
                slotNumber, vehicle != null ? vehicle : "empty");
    }
}
