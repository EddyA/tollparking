package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParkingSlotBookerTest implements WithAssertions {

    private static final CurrentTimeSupplier spiedCurrentTimeSupplier = Mockito.spy(new CurrentTimeSupplier());

    @Test
    public void getParkingSlotWithAnEmptyListShouldReturnNull() throws ParkingSlotException {
        ParkingSlotBooker parkingSlotBooker = new ParkingSlotBooker(new ArrayList<>());

        // check.
        assertNull(parkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE));
        assertNull(parkingSlotBooker.getParkingSlot(ParkingSlotType.ELECTRIC_20KW));
        assertNull(parkingSlotBooker.getParkingSlot(ParkingSlotType.ELECTRIC_50KW));
    }

    @Test
    public void getParkingSlotShouldWorkAsExpected() throws ParkingSlotException {
        ParkingSlot parkingSlot = Mockito.spy(new ParkingSlot("G1", ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier));
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        parkingSlots.add(parkingSlot);
        ParkingSlotBooker parkingSlotBooker = new ParkingSlotBooker(parkingSlots);

        // -- 1st call: check the parking slot has been booked and then returned.
        ParkingSlot retParkingSlot = parkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE);
        Mockito.verify(parkingSlot, Mockito.times(1)).book();
        assertThat(parkingSlot).isEqualTo(retParkingSlot);

        // -- 2nd call: check the second call returns null.
        retParkingSlot = parkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE);
        assertNull(retParkingSlot);
    }

    @Test
    public void releaseParkingSlotShouldWorkAsExpected() throws ParkingSlotException, ParkingSlotBookerException {
        ParkingSlot parkingSlot = Mockito.spy(new ParkingSlot("E201", ParkingSlotType.ELECTRIC_20KW, spiedCurrentTimeSupplier));
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        parkingSlots.add(parkingSlot);
        ParkingSlotBooker parkingSlotBooker = new ParkingSlotBooker(parkingSlots);
        Mockito.when(spiedCurrentTimeSupplier.get()).thenReturn(Instant.ofEpochSecond(10000));
        parkingSlotBooker.getParkingSlot(ParkingSlotType.ELECTRIC_20KW);
        Mockito.when(spiedCurrentTimeSupplier.get()).thenReturn(Instant.ofEpochSecond(15000));

        // -- 1st call: check the parking slot has been release and the booking time well returned.
        long bookingTime = parkingSlotBooker.releaseParkingSlot("E201");
        Mockito.verify(parkingSlot, Mockito.times(1)).release();
        assertThat(bookingTime).isEqualTo(5000L);

        // -- 2nd call: check the second call throws an exception.
        assertThatThrownBy(() -> parkingSlotBooker.releaseParkingSlot("E201"))
                .isInstanceOf(ParkingSlotBookerException.class)
                .hasMessage("cannot release parking slot E201, it is not booked or does not exist.");
    }

    @Test
    public void getNbVacantParkingSlotShouldWorkAsExpected() throws ParkingSlotException {
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        parkingSlots.add(new ParkingSlot("GAZ1", ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier));
        parkingSlots.add(new ParkingSlot("GAZ2", ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier));
        ParkingSlotBooker parkingSlotBooker = new ParkingSlotBooker(parkingSlots);

        // check.
        assertThat(parkingSlotBooker.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(2);
        parkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE);
        assertThat(parkingSlotBooker.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(1);
        parkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE);
        assertThat(parkingSlotBooker.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(0);
    }
}