package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

public class ParkingBuilderTest implements WithAssertions {

    private static final CurrentTimeSupplier spiedCurrentTimeSupplier = Mockito.spy(new CurrentTimeSupplier());

    @Test
    public void initiallyTheParkingAsNoSlot() {
        ParkingBuilder parkingBuilder = new ParkingBuilder();

        // check.
        assertThat(parkingBuilder.getParkingSlots()).isEmpty();
    }

    @Test
    public void addParkingSlotShouldWorkAsExpected() throws ParkingBuilderException {
        ParkingBuilder parkingBuilder = new ParkingBuilder();
        parkingBuilder.addParkingSlot(ParkingSlotType.ELECTRIC_50KW, "E501");

        // check.
        assertThat(parkingBuilder.getParkingSlots()).contains(
                new ParkingSlot("E501", ParkingSlotType.ELECTRIC_50KW, spiedCurrentTimeSupplier));
    }

    @Test
    public void addParkingSlotAddingTwiceASlotShouldThrowTheExpectedException() throws ParkingBuilderException {
        ParkingBuilder parkingBuilder = new ParkingBuilder();
        parkingBuilder.addParkingSlot(ParkingSlotType.ELECTRIC_50KW, "E501");

        // check.
        assertThatThrownBy(() -> parkingBuilder.addParkingSlot(ParkingSlotType.GASOLINE, "E501")).isInstanceOf(
                ParkingBuilderException.class).hasMessage("cannot add parking slot E501, the identifier is already assigned.");
    }

    @Test
    public void removeParkingSlotShouldWorkAsExpected() throws ParkingBuilderException {
        ParkingBuilder parkingBuilder = new ParkingBuilder();
        parkingBuilder.addParkingSlot(ParkingSlotType.ELECTRIC_50KW, "E501"); // add a slot.
        parkingBuilder.removeParkingSlot("E501"); // remove the slot

        // check.
        assertThat(parkingBuilder.getParkingSlots()).isEmpty();
    }

    @Test
    public void removeParkingSlotWithABookedParkingSlotShouldThrowTheExpectedException() {
        ParkingBuilder parkingBuilder = new ParkingBuilder();
        ParkingSlot spiedParkingSlot = Mockito.spy(
                new ParkingSlot("E501", ParkingSlotType.ELECTRIC_50KW, spiedCurrentTimeSupplier));
        Mockito.when(spiedParkingSlot.isVacant()).thenReturn(false);
        parkingBuilder.getParkingSlots().add(spiedParkingSlot);

        // check.
        assertThatThrownBy(() -> parkingBuilder.removeParkingSlot("E501")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot E501, the parking slot is booked.");
    }

    @Test
    public void removeParkingSlotWithANotRegisteredParkingSlotShouldThrowTheExpectedException() {
        ParkingBuilder parkingBuilder = new ParkingBuilder();

        // check.
        assertThatThrownBy(() -> parkingBuilder.removeParkingSlot("E501")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot E501, the parking slot does not exist.");
    }
}