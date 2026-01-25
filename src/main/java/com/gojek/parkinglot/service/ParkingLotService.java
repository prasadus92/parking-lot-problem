package com.gojek.parkinglot.service;

import com.gojek.parkinglot.domain.ParkingLot;
import com.gojek.parkinglot.domain.ParkingSlot;
import com.gojek.parkinglot.domain.Vehicle;
import com.gojek.parkinglot.exception.*;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for parking lot operations.
 *
 * <p>This service provides the business logic for managing parking operations.
 * It encapsulates the domain model and provides a clean API for the application layer.</p>
 *
 * <p>All operations that require a parking lot to exist will throw
 * {@link ParkingLotNotCreatedException} if called before {@link #createParkingLot(int)}.</p>
 *
 * <p>This class is not thread-safe. External synchronization is required
 * for concurrent access.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public class ParkingLotService {

    private ParkingLot parkingLot;

    /**
     * Creates a new parking lot with the specified capacity.
     *
     * <p>If a parking lot already exists, it will be replaced.</p>
     *
     * @param capacity the number of slots in the parking lot
     * @return the created capacity
     * @throws IllegalArgumentException if capacity is not positive
     */
    public int createParkingLot(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Invalid lot count: capacity must be positive");
        }
        this.parkingLot = new ParkingLot(capacity);
        return capacity;
    }

    /**
     * Parks a vehicle in the nearest available slot.
     *
     * @param registrationNumber the vehicle's registration number
     * @param color the vehicle's color
     * @return the allocated slot number
     * @throws ParkingLotNotCreatedException if parking lot is not created
     * @throws ParkingLotFullException if parking lot is full
     * @throws IllegalArgumentException if registration number or color is invalid
     */
    public int park(String registrationNumber, String color) {
        ensureParkingLotCreated();

        if (parkingLot.isFull()) {
            throw new ParkingLotFullException();
        }

        Vehicle vehicle = new Vehicle(registrationNumber, color);
        return parkingLot.park(vehicle);
    }

    /**
     * Removes a vehicle from the specified slot.
     *
     * @param slotNumber the slot to vacate
     * @return the slot number that was freed
     * @throws ParkingLotNotCreatedException if parking lot is not created
     * @throws SlotNotFoundException if slot number is invalid
     * @throws IllegalStateException if slot is already empty
     */
    public int leave(int slotNumber) {
        ensureParkingLotCreated();

        if (slotNumber <= 0 || slotNumber > parkingLot.getCapacity()) {
            throw new SlotNotFoundException(slotNumber,
                    "Invalid slot number: " + slotNumber);
        }

        Optional<ParkingSlot> slot = parkingLot.getSlot(slotNumber);
        if (!slot.isPresent() || slot.get().isAvailable()) {
            throw new SlotNotFoundException(slotNumber,
                    "Slot number " + slotNumber + " is already empty");
        }

        parkingLot.leave(slotNumber);
        return slotNumber;
    }

    /**
     * Returns the current status of all occupied slots.
     *
     * @return list of occupied parking slots, sorted by slot number
     * @throws ParkingLotNotCreatedException if parking lot is not created
     */
    public List<ParkingSlot> getStatus() {
        ensureParkingLotCreated();
        return parkingLot.getOccupiedSlots();
    }

    /**
     * Finds the slot number for a given registration number.
     *
     * @param registrationNumber the registration number to search for
     * @return the slot number
     * @throws ParkingLotNotCreatedException if parking lot is not created
     * @throws VehicleNotFoundException if vehicle is not found
     */
    public int getSlotByRegistration(String registrationNumber) {
        ensureParkingLotCreated();

        return parkingLot.getSlotByRegistration(registrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException(registrationNumber));
    }

    /**
     * Returns all registration numbers of vehicles with the specified color.
     *
     * @param color the color to search for
     * @return list of registration numbers
     * @throws ParkingLotNotCreatedException if parking lot is not created
     * @throws VehicleNotFoundException if no vehicles with that color are found
     */
    public List<String> getRegistrationsByColor(String color) {
        ensureParkingLotCreated();

        List<String> registrations = parkingLot.getRegistrationsByColor(color);
        if (registrations.isEmpty()) {
            throw new VehicleNotFoundException(null, "Not found");
        }
        return registrations;
    }

    /**
     * Returns all slot numbers where vehicles of the specified color are parked.
     *
     * @param color the color to search for
     * @return sorted list of slot numbers
     * @throws ParkingLotNotCreatedException if parking lot is not created
     * @throws VehicleNotFoundException if no vehicles with that color are found
     */
    public List<Integer> getSlotsByColor(String color) {
        ensureParkingLotCreated();

        List<Integer> slots = parkingLot.getSlotsByColor(color);
        if (slots.isEmpty()) {
            throw new VehicleNotFoundException(null, "Not found");
        }
        return slots;
    }

    /**
     * Checks if a parking lot has been created.
     *
     * @return true if parking lot exists, false otherwise
     */
    public boolean isParkingLotCreated() {
        return parkingLot != null;
    }

    /**
     * Returns the parking lot capacity.
     *
     * @return the capacity, or 0 if not created
     */
    public int getCapacity() {
        return parkingLot != null ? parkingLot.getCapacity() : 0;
    }

    /**
     * Returns whether the parking lot is empty.
     *
     * @return true if empty or not created
     */
    public boolean isEmpty() {
        return parkingLot == null || parkingLot.isEmpty();
    }

    /**
     * Ensures that a parking lot has been created.
     *
     * @throws ParkingLotNotCreatedException if parking lot is not created
     */
    private void ensureParkingLotCreated() {
        if (parkingLot == null) {
            throw new ParkingLotNotCreatedException();
        }
    }

    /**
     * Resets the service by removing the parking lot.
     * Useful for testing.
     */
    public void reset() {
        this.parkingLot = null;
    }
}
