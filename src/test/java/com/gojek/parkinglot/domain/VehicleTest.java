package com.gojek.parkinglot.domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Vehicle domain entity.
 */
public class VehicleTest {

    @Test
    public void shouldCreateVehicleWithValidData() {
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        assertEquals("KA-01-HH-1234", vehicle.getRegistrationNumber());
        assertEquals("White", vehicle.getColor());
    }

    @Test
    public void shouldTrimWhitespaceFromInputs() {
        Vehicle vehicle = new Vehicle("  KA-01-HH-1234  ", "  White  ");

        assertEquals("KA-01-HH-1234", vehicle.getRegistrationNumber());
        assertEquals("White", vehicle.getColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullRegistrationNumber() {
        new Vehicle(null, "White");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyRegistrationNumber() {
        new Vehicle("", "White");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankRegistrationNumber() {
        new Vehicle("   ", "White");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullColor() {
        new Vehicle("KA-01-HH-1234", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectEmptyColor() {
        new Vehicle("KA-01-HH-1234", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectBlankColor() {
        new Vehicle("KA-01-HH-1234", "   ");
    }

    @Test
    public void vehiclesWithSameRegistrationShouldBeEqual() {
        Vehicle v1 = new Vehicle("KA-01-HH-1234", "White");
        Vehicle v2 = new Vehicle("KA-01-HH-1234", "Black");

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void vehiclesWithDifferentRegistrationShouldNotBeEqual() {
        Vehicle v1 = new Vehicle("KA-01-HH-1234", "White");
        Vehicle v2 = new Vehicle("KA-01-HH-5678", "White");

        assertNotEquals(v1, v2);
    }

    @Test
    public void shouldNotEqualNull() {
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        assertNotEquals(null, vehicle);
    }

    @Test
    public void shouldNotEqualDifferentType() {
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");

        assertNotEquals("KA-01-HH-1234", vehicle);
    }

    @Test
    public void toStringShouldContainRegistrationAndColor() {
        Vehicle vehicle = new Vehicle("KA-01-HH-1234", "White");
        String toString = vehicle.toString();

        assertTrue(toString.contains("KA-01-HH-1234"));
        assertTrue(toString.contains("White"));
    }
}
