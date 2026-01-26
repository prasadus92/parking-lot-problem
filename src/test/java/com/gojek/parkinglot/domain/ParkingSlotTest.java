package com.gojek.parkinglot.domain;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for the ParkingSlot domain entity.
 */
public class ParkingSlotTest {

    @Test
    public void shouldCreateEmptySlotWithValidNumber() {
        ParkingSlot slot = new ParkingSlot(1);

        assertEquals(1, slot.getSlotNumber());
        assertTrue(slot.isAvailable());
        assertFalse(slot.isOccupied());
        assertEquals(Optional.empty(), slot.getVehicle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectZeroSlotNumber() {
        new ParkingSlot(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeSlotNumber() {
        new ParkingSlot(-1);
    }

    @Test
    public void shouldParkVehicleSuccessfully() {
        ParkingSlot slot = new ParkingSlot(1);
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        slot.park(vehicle);

        assertTrue(slot.isOccupied());
        assertFalse(slot.isAvailable());
        assertTrue(slot.getVehicle().isPresent());
        assertEquals(vehicle, slot.getVehicle().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullVehicle() {
        ParkingSlot slot = new ParkingSlot(1);
        slot.park(null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectParkingWhenOccupied() {
        ParkingSlot slot = new ParkingSlot(1);
        Vehicle v1 = new Vehicle("KA-01-HH-1234", "White");
        Vehicle v2 = new Vehicle("KA-01-HH-5678", "Black");

        slot.park(v1);
        slot.park(v2);
    }

    @Test
    public void shouldVacateSlotSuccessfully() {
        ParkingSlot slot = new ParkingSlot(1);
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        slot.park(vehicle);
        Vehicle departed = slot.vacate();

        assertEquals(vehicle, departed);
        assertTrue(slot.isAvailable());
        assertFalse(slot.isOccupied());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectVacatingEmptySlot() {
        ParkingSlot slot = new ParkingSlot(1);
        slot.vacate();
    }

    @Test
    public void slotsShouldBeComparableByNumber() {
        ParkingSlot slot1 = new ParkingSlot(1);
        ParkingSlot slot2 = new ParkingSlot(2);
        ParkingSlot slot3 = new ParkingSlot(1);

        assertTrue(slot1.compareTo(slot2) < 0);
        assertTrue(slot2.compareTo(slot1) > 0);
        assertEquals(0, slot1.compareTo(slot3));
    }

    @Test
    public void slotsShouldBeEqualByNumber() {
        ParkingSlot slot1 = new ParkingSlot(1);
        ParkingSlot slot2 = new ParkingSlot(1);

        assertEquals(slot1, slot2);
        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    public void slotsShouldNotBeEqualByDifferentNumber() {
        ParkingSlot slot1 = new ParkingSlot(1);
        ParkingSlot slot2 = new ParkingSlot(2);

        assertNotEquals(slot1, slot2);
    }

    @Test
    public void toStringShouldContainSlotInfo() {
        ParkingSlot slot = new ParkingSlot(5);
        String toString = slot.toString();

        assertTrue(toString.contains("5"));
    }
}
