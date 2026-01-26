package com.gojek.parkinglot.domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core domain entity representing a parking lot with multiple slots.
 *
 * <p>The parking lot manages a collection of parking slots and provides
 * efficient operations for parking, leaving, and querying vehicles.
 * Slot allocation follows the "nearest to entry" strategy - lower slot
 * numbers are allocated first.</p>
 *
 * <p>This implementation uses a min-heap (PriorityQueue) for O(log n)
 * slot allocation and maintains indexed lookups for O(1) query performance.</p>
 *
 * <p>This class is not thread-safe. External synchronization is required
 * for concurrent access.</p>
 *
 * @author Enterprise Parking Solutions
 * @version 2.0.0
 * @since 2.0.0
 */
public final class ParkingLot {

    private final int capacity;
    private final Map<Integer, ParkingSlot> slots;
    private final PriorityQueue<Integer> availableSlots;
    private final Map<String, Integer> registrationToSlot;
    private final Map<String, Set<String>> colorToRegistrations;

    /**
     * Constructs a new ParkingLot with the specified capacity.
     *
     * @param capacity the maximum number of vehicles that can be parked
     * @throws IllegalArgumentException if capacity is not positive
     */
    public ParkingLot(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive: " + capacity);
        }

        this.capacity = capacity;
        this.slots = new HashMap<>(capacity);
        this.availableSlots = new PriorityQueue<>(capacity);
        this.registrationToSlot = new HashMap<>();
        this.colorToRegistrations = new HashMap<>();

        // Initialize all slots
        for (int i = 1; i <= capacity; i++) {
            slots.put(i, new ParkingSlot(i));
            availableSlots.offer(i);
        }
    }

    /**
     * Returns the capacity of the parking lot.
     *
     * @return the total number of slots
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of occupied slots.
     *
     * @return the count of parked vehicles
     */
    public int getOccupiedCount() {
        return capacity - availableSlots.size();
    }

    /**
     * Returns the number of available slots.
     *
     * @return the count of empty slots
     */
    public int getAvailableCount() {
        return availableSlots.size();
    }

    /**
     * Checks if the parking lot is full.
     *
     * @return true if no slots are available, false otherwise
     */
    public boolean isFull() {
        return availableSlots.isEmpty();
    }

    /**
     * Checks if the parking lot is empty.
     *
     * @return true if all slots are available, false otherwise
     */
    public boolean isEmpty() {
        return availableSlots.size() == capacity;
    }

    /**
     * Parks a vehicle in the nearest available slot.
     *
     * @param vehicle the vehicle to park
     * @return the allocated slot number
     * @throws IllegalArgumentException if vehicle is null
     * @throws IllegalStateException if the parking lot is full
     * @throws IllegalStateException if the vehicle is already parked
     */
    public int park(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (isFull()) {
            throw new IllegalStateException("Parking lot is full");
        }
        if (registrationToSlot.containsKey(vehicle.getRegistrationNumber())) {
            throw new IllegalStateException("Vehicle " + vehicle.getRegistrationNumber()
                    + " is already parked");
        }

        // Get the nearest available slot (min-heap provides O(log n) extraction)
        int slotNumber = availableSlots.poll();
        ParkingSlot slot = slots.get(slotNumber);
        slot.park(vehicle);

        // Update indexes
        registrationToSlot.put(vehicle.getRegistrationNumber(), slotNumber);
        colorToRegistrations
                .computeIfAbsent(vehicle.getColor().toLowerCase(), k -> new LinkedHashSet<>())
                .add(vehicle.getRegistrationNumber());

        return slotNumber;
    }

    /**
     * Removes a vehicle from the specified slot.
     *
     * @param slotNumber the slot to vacate
     * @return the vehicle that was parked
     * @throws IllegalArgumentException if slot number is invalid
     * @throws IllegalStateException if the slot is already empty
     */
    public Vehicle leave(int slotNumber) {
        validateSlotNumber(slotNumber);

        ParkingSlot slot = slots.get(slotNumber);
        if (slot.isAvailable()) {
            throw new IllegalStateException("Slot " + slotNumber + " is already empty");
        }

        Vehicle vehicle = slot.vacate();

        // Update indexes
        registrationToSlot.remove(vehicle.getRegistrationNumber());
        Set<String> registrations = colorToRegistrations.get(vehicle.getColor().toLowerCase());
        if (registrations != null) {
            registrations.remove(vehicle.getRegistrationNumber());
            if (registrations.isEmpty()) {
                colorToRegistrations.remove(vehicle.getColor().toLowerCase());
            }
        }

        // Return slot to available pool
        availableSlots.offer(slotNumber);

        return vehicle;
    }

    /**
     * Returns the slot number for a given registration number.
     *
     * @param registrationNumber the vehicle's registration number
     * @return an Optional containing the slot number, or empty if not found
     */
    public Optional<Integer> getSlotByRegistration(String registrationNumber) {
        if (registrationNumber == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(registrationToSlot.get(registrationNumber));
    }

    /**
     * Returns all registration numbers of vehicles with the specified color.
     *
     * @param color the color to search for
     * @return a list of registration numbers (may be empty, never null)
     */
    public List<String> getRegistrationsByColor(String color) {
        if (color == null) {
            return Collections.emptyList();
        }
        Set<String> registrations = colorToRegistrations.get(color.toLowerCase());
        return registrations != null
                ? new ArrayList<>(registrations)
                : Collections.emptyList();
    }

    /**
     * Returns all slot numbers where vehicles of the specified color are parked.
     *
     * @param color the color to search for
     * @return a sorted list of slot numbers (may be empty, never null)
     */
    public List<Integer> getSlotsByColor(String color) {
        if (color == null) {
            return Collections.emptyList();
        }
        Set<String> registrations = colorToRegistrations.get(color.toLowerCase());
        if (registrations == null || registrations.isEmpty()) {
            return Collections.emptyList();
        }
        return registrations.stream()
                .map(registrationToSlot::get)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns the parking slot at the specified slot number.
     *
     * @param slotNumber the slot number
     * @return an Optional containing the slot, or empty if not found
     */
    public Optional<ParkingSlot> getSlot(int slotNumber) {
        return Optional.ofNullable(slots.get(slotNumber));
    }

    /**
     * Returns all occupied slots in order.
     *
     * @return a sorted list of occupied slots
     */
    public List<ParkingSlot> getOccupiedSlots() {
        return slots.values().stream()
                .filter(ParkingSlot::isOccupied)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns all slots (both occupied and available) in order.
     *
     * @return a sorted list of all slots
     */
    public List<ParkingSlot> getAllSlots() {
        return slots.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private void validateSlotNumber(int slotNumber) {
        if (slotNumber <= 0 || slotNumber > capacity) {
            throw new IllegalArgumentException(
                    "Invalid slot number: " + slotNumber + ". Must be between 1 and " + capacity);
        }
    }

    @Override
    public String toString() {
        return String.format("ParkingLot{capacity=%d, occupied=%d, available=%d}",
                capacity, getOccupiedCount(), getAvailableCount());
    }
}
