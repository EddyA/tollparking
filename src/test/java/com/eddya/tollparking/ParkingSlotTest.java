package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;

public class ParkingSlotTest implements WithAssertions {

    private static final CurrentTimeSupplier spiedCurrentTimeSupplier = Mockito.spy(new CurrentTimeSupplier());
    private static final String A_PARKING_SLOT_ID = "C2550";

    @Test
    public void constructorShouldSetMembersWithTheExpectedValues() {
        ParkingSlot parkingSlot = new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier);

        // check.
        assertThat(parkingSlot.getId()).isEqualTo(A_PARKING_SLOT_ID);
        assertThat(parkingSlot.getParkingSlotType()).isEqualTo(ParkingSlotType.GASOLINE);
    }

    @Test
    public void initiallyTheSlotShouldBeVacantAndTheStartTimeSetToZero() {
        ParkingSlot parkingSlot = new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier);

        // check.
        assertThat(parkingSlot.isVacant()).isTrue();
        assertThat(parkingSlot.getBookingStartTime()).isEqualTo(0);
    }

    @Test
    public void bookShouldSetTheStartTimeAsExpectedAndSetTheSlotAsNotVacant()
            throws ParkingSlotException {

        ParkingSlot parkingSlot = new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier);
        Mockito.when(spiedCurrentTimeSupplier.get()).thenReturn(Instant.ofEpochSecond(1000));
        parkingSlot.book();

        // check.
        assertThat(parkingSlot.getBookingStartTime()).isEqualTo(1000);
        assertThat(parkingSlot.isVacant()).isFalse();
    }

    @Test
    public void releaseShouldSetTheStartTimeToZeroAndTheSlotAsVacantAndReturnTheExpectedBookingTime()
            throws ParkingSlotException {

        ParkingSlot parkingSlot = new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier);
        Mockito.when(spiedCurrentTimeSupplier.get()).thenReturn(Instant.ofEpochSecond(1000));
        parkingSlot.book();
        Mockito.when(spiedCurrentTimeSupplier.get()).thenReturn(Instant.ofEpochSecond(3000));
        long bookingTime = parkingSlot.release();

        // check.
        assertThat(parkingSlot.getBookingStartTime()).isEqualTo(0);
        assertThat(parkingSlot.isVacant()).isTrue();
        assertThat(bookingTime).isEqualTo(2000);
    }

    @Test
    public void bookANotVacantSlotShouldThrowTheExpectedException() {
        ParkingSlot spiedParkingSlot = Mockito.spy(new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier));
        Mockito.when(spiedParkingSlot.isVacant()).thenReturn(false);

        // check.
        assertThatThrownBy(spiedParkingSlot::book)
                .isInstanceOf(ParkingSlotException.class)
                .hasMessage("cannot book parking slot " + A_PARKING_SLOT_ID + ", it is already booked.");
    }

    @Test
    public void releaseAVacantSlotShouldThrowTheExpectedException() {
        ParkingSlot spyedParkingSlot = Mockito.spy(new ParkingSlot(A_PARKING_SLOT_ID, ParkingSlotType.GASOLINE, spiedCurrentTimeSupplier));
        Mockito.when(spyedParkingSlot.isVacant()).thenReturn(true);

        // check.
        assertThatThrownBy(spyedParkingSlot::release)
                .isInstanceOf(ParkingSlotException.class)
                .hasMessage("cannot release parking slot " + A_PARKING_SLOT_ID + ", it is not booked.");
    }
}