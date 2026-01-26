package com.gojek.parkinglot.service;

import com.gojek.parkinglot.domain.ParkingSlot;
import com.gojek.parkinglot.exception.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the ParkingLotService.
 */
public class ParkingLotServiceTest {

    private ParkingLotService service;

    @Before
    public void setUp() {
        service = new ParkingLotService();
    }

    // Create parking lot tests

    @Test
    public void shouldCreateParkingLot() {
        int capacity = service.createParkingLot(6);

        assertEquals(6, capacity);
        assertTrue(service.isParkingLotCreated());
        assertEquals(6, service.getCapacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectZeroCapacity() {
        service.createParkingLot(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeCapacity() {
        service.createParkingLot(-5);
    }

    @Test
    public void shouldReplaceExistingParkingLot() {
        service.createParkingLot(3);
        service.park("V1", "White");

        service.createParkingLot(6);

        assertEquals(6, service.getCapacity());
        assertTrue(service.isEmpty());
    }

    // Park tests

    @Test
    public void shouldParkVehicle() {
        service.createParkingLot(6);

        int slot = service.park("KA-01-HH-1234", "White");

        assertEquals(1, slot);
    }

    @Test
    public void shouldParkMultipleVehicles() {
        service.createParkingLot(6);

        assertEquals(1, service.park("V1", "White"));
        assertEquals(2, service.park("V2", "Black"));
        assertEquals(3, service.park("V3", "Red"));
    }

    @Test(expected = ParkingLotNotCreatedException.class)
    public void shouldRejectParkingWithoutCreation() {
        service.park("V1", "White");
    }

    @Test(expected = ParkingLotFullException.class)
    public void shouldRejectParkingWhenFull() {
        service.createParkingLot(2);
        service.park("V1", "White");
        service.park("V2", "Black");
        service.park("V3", "Red");
    }

    // Leave tests

    @Test
    public void shouldLeaveSlot() {
        service.createParkingLot(6);
        service.park("V1", "White");

        int slot = service.leave(1);

        assertEquals(1, slot);
        assertTrue(service.isEmpty());
    }

    @Test(expected = ParkingLotNotCreatedException.class)
    public void shouldRejectLeaveWithoutCreation() {
        service.leave(1);
    }

    @Test(expected = SlotNotFoundException.class)
    public void shouldRejectInvalidSlotNumber() {
        service.createParkingLot(6);
        service.leave(0);
    }

    @Test(expected = SlotNotFoundException.class)
    public void shouldRejectSlotBeyondCapacity() {
        service.createParkingLot(6);
        service.leave(7);
    }

    @Test(expected = SlotNotFoundException.class)
    public void shouldRejectLeaveOnEmptySlot() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.leave(2);
    }

    // Status tests

    @Test
    public void shouldReturnStatus() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.park("V2", "Black");

        List<ParkingSlot> status = service.getStatus();

        assertEquals(2, status.size());
        assertEquals(1, status.get(0).getSlotNumber());
        assertEquals(2, status.get(1).getSlotNumber());
    }

    @Test
    public void shouldReturnEmptyStatusForEmptyLot() {
        service.createParkingLot(6);

        List<ParkingSlot> status = service.getStatus();

        assertTrue(status.isEmpty());
    }

    @Test(expected = ParkingLotNotCreatedException.class)
    public void shouldRejectStatusWithoutCreation() {
        service.getStatus();
    }

    // Query by registration tests

    @Test
    public void shouldFindSlotByRegistration() {
        service.createParkingLot(6);
        service.park("KA-01-HH-1234", "White");
        service.park("KA-01-HH-5678", "Black");

        int slot = service.getSlotByRegistration("KA-01-HH-5678");

        assertEquals(2, slot);
    }

    @Test(expected = VehicleNotFoundException.class)
    public void shouldThrowWhenRegistrationNotFound() {
        service.createParkingLot(6);
        service.park("V1", "White");

        service.getSlotByRegistration("UNKNOWN");
    }

    @Test(expected = ParkingLotNotCreatedException.class)
    public void shouldRejectRegistrationQueryWithoutCreation() {
        service.getSlotByRegistration("V1");
    }

    // Query by color tests

    @Test
    public void shouldFindRegistrationsByColor() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.park("V2", "Black");
        service.park("V3", "White");

        List<String> registrations = service.getRegistrationsByColor("White");

        assertEquals(2, registrations.size());
        assertTrue(registrations.contains("V1"));
        assertTrue(registrations.contains("V3"));
    }

    @Test(expected = VehicleNotFoundException.class)
    public void shouldThrowWhenColorNotFound() {
        service.createParkingLot(6);
        service.park("V1", "White");

        service.getRegistrationsByColor("Purple");
    }

    @Test
    public void shouldFindSlotsByColor() {
        service.createParkingLot(6);
        service.park("V1", "White");
        service.park("V2", "Black");
        service.park("V3", "White");

        List<Integer> slots = service.getSlotsByColor("White");

        assertEquals(2, slots.size());
        assertEquals(Integer.valueOf(1), slots.get(0));
        assertEquals(Integer.valueOf(3), slots.get(1));
    }

    @Test(expected = VehicleNotFoundException.class)
    public void shouldThrowWhenNoSlotsForColor() {
        service.createParkingLot(6);
        service.park("V1", "White");

        service.getSlotsByColor("Blue");
    }

    // Reset test

    @Test
    public void shouldResetService() {
        service.createParkingLot(6);
        service.park("V1", "White");

        service.reset();

        assertFalse(service.isParkingLotCreated());
        assertEquals(0, service.getCapacity());
    }
}
