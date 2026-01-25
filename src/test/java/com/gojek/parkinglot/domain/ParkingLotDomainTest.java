package com.gojek.parkinglot.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for the ParkingLot domain entity.
 */
public class ParkingLotDomainTest {

    private ParkingLot parkingLot;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(6);
    }

    @Test
    public void shouldCreateParkingLotWithValidCapacity() {
        assertEquals(6, parkingLot.getCapacity());
        assertEquals(6, parkingLot.getAvailableCount());
        assertEquals(0, parkingLot.getOccupiedCount());
        assertTrue(parkingLot.isEmpty());
        assertFalse(parkingLot.isFull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectZeroCapacity() {
        new ParkingLot(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeCapacity() {
        new ParkingLot(-1);
    }

    @Test
    public void shouldParkVehicleInNearestSlot() {
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        int slot = parkingLot.park(vehicle);

        assertEquals(1, slot);
        assertEquals(5, parkingLot.getAvailableCount());
        assertEquals(1, parkingLot.getOccupiedCount());
    }

    @Test
    public void shouldAllocateSlotsSequentially() {
        assertEquals(1, parkingLot.park(new Vehicle("KA-01-HH-1234", "White")));
        assertEquals(2, parkingLot.park(new Vehicle("KA-01-HH-5678", "Black")));
        assertEquals(3, parkingLot.park(new Vehicle("KA-01-HH-9999", "Red")));
    }

    @Test
    public void shouldReuseFreedSlots() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.park(new Vehicle("KA-01-HH-5678", "Black"));
        parkingLot.park(new Vehicle("KA-01-HH-9999", "Red"));

        parkingLot.leave(2);

        assertEquals(2, parkingLot.park(new Vehicle("KA-01-HH-0000", "Blue")));
    }

    @Test
    public void shouldAllocateNearestAvailableSlot() {
        parkingLot.park(new Vehicle("V1", "White"));
        parkingLot.park(new Vehicle("V2", "White"));
        parkingLot.park(new Vehicle("V3", "White"));
        parkingLot.park(new Vehicle("V4", "White"));

        parkingLot.leave(2);
        parkingLot.leave(4);

        // Should allocate slot 2 (nearest)
        assertEquals(2, parkingLot.park(new Vehicle("V5", "White")));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectParkingWhenFull() {
        for (int i = 1; i <= 6; i++) {
            parkingLot.park(new Vehicle("V" + i, "White"));
        }
        parkingLot.park(new Vehicle("V7", "White"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullVehicle() {
        parkingLot.park(null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectDuplicateVehicle() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.park(new Vehicle("KA-01-HH-1234", "Black"));
    }

    @Test
    public void shouldLeaveSlotSuccessfully() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));

        Vehicle departed = parkingLot.leave(1);

        assertEquals("KA-01-HH-1234", departed.getRegistrationNumber());
        assertTrue(parkingLot.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidSlotNumber() {
        parkingLot.leave(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectSlotNumberBeyondCapacity() {
        parkingLot.leave(7);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectLeavingEmptySlot() {
        parkingLot.leave(1);
    }

    @Test
    public void shouldFindSlotByRegistration() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.park(new Vehicle("KA-01-HH-5678", "Black"));

        Optional<Integer> slot = parkingLot.getSlotByRegistration("KA-01-HH-5678");

        assertTrue(slot.isPresent());
        assertEquals(Integer.valueOf(2), slot.get());
    }

    @Test
    public void shouldReturnEmptyForUnknownRegistration() {
        Optional<Integer> slot = parkingLot.getSlotByRegistration("UNKNOWN");

        assertFalse(slot.isPresent());
    }

    @Test
    public void shouldFindRegistrationsByColor() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.park(new Vehicle("KA-01-HH-5678", "Black"));
        parkingLot.park(new Vehicle("KA-01-HH-9999", "White"));

        List<String> registrations = parkingLot.getRegistrationsByColor("White");

        assertEquals(2, registrations.size());
        assertTrue(registrations.contains("KA-01-HH-1234"));
        assertTrue(registrations.contains("KA-01-HH-9999"));
    }

    @Test
    public void shouldFindRegistrationsByColorCaseInsensitive() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));

        List<String> registrations = parkingLot.getRegistrationsByColor("white");

        assertEquals(1, registrations.size());
        assertTrue(registrations.contains("KA-01-HH-1234"));
    }

    @Test
    public void shouldReturnEmptyListForUnknownColor() {
        List<String> registrations = parkingLot.getRegistrationsByColor("Purple");

        assertTrue(registrations.isEmpty());
    }

    @Test
    public void shouldFindSlotsByColor() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.park(new Vehicle("KA-01-HH-5678", "Black"));
        parkingLot.park(new Vehicle("KA-01-HH-9999", "White"));

        List<Integer> slots = parkingLot.getSlotsByColor("White");

        assertEquals(2, slots.size());
        assertEquals(Integer.valueOf(1), slots.get(0));
        assertEquals(Integer.valueOf(3), slots.get(1));
    }

    @Test
    public void shouldReturnSortedSlotsByColor() {
        parkingLot.park(new Vehicle("V1", "White"));
        parkingLot.park(new Vehicle("V2", "Black"));
        parkingLot.park(new Vehicle("V3", "White"));
        parkingLot.park(new Vehicle("V4", "White"));
        parkingLot.leave(1);
        parkingLot.park(new Vehicle("V5", "White"));

        List<Integer> slots = parkingLot.getSlotsByColor("White");

        assertEquals(4, slots.size());
        // Should be sorted: 1, 3, 4
        for (int i = 1; i < slots.size(); i++) {
            assertTrue(slots.get(i - 1) <= slots.get(i));
        }
    }

    @Test
    public void shouldGetOccupiedSlots() {
        parkingLot.park(new Vehicle("V1", "White"));
        parkingLot.park(new Vehicle("V2", "Black"));
        parkingLot.park(new Vehicle("V3", "Red"));
        parkingLot.leave(2);

        List<ParkingSlot> occupied = parkingLot.getOccupiedSlots();

        assertEquals(2, occupied.size());
        assertEquals(1, occupied.get(0).getSlotNumber());
        assertEquals(3, occupied.get(1).getSlotNumber());
    }

    @Test
    public void shouldUpdateIndexesOnLeave() {
        parkingLot.park(new Vehicle("KA-01-HH-1234", "White"));
        parkingLot.leave(1);

        assertFalse(parkingLot.getSlotByRegistration("KA-01-HH-1234").isPresent());
        assertTrue(parkingLot.getRegistrationsByColor("White").isEmpty());
    }

    @Test
    public void toStringShouldContainStats() {
        parkingLot.park(new Vehicle("V1", "White"));

        String toString = parkingLot.toString();

        assertTrue(toString.contains("capacity=6"));
        assertTrue(toString.contains("occupied=1"));
        assertTrue(toString.contains("available=5"));
    }
}
