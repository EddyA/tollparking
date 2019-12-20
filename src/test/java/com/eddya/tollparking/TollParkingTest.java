package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

public class TollParkingTest implements WithAssertions {

  private final PricingPolicy pricingPolicy = new PerHourPricingPolicy(350);

  @Test public void addParkingSlotShouldCallTheRelatedParkingBuilderMethods() throws ParkingBuilderException {
    ParkingBuilder mockedParkingBuilder = Mockito.mock(ParkingBuilder.class);
    ParkingSlotBooker mockedParkingSlotBooker = Mockito.mock(ParkingSlotBooker.class);
    TollParking tollParking = new TollParking(pricingPolicy, mockedParkingBuilder, mockedParkingSlotBooker);

    // check.
    tollParking.addParkingSlot(ParkingSlotType.GASOLINE, "C20");
    Mockito.verify(mockedParkingBuilder, Mockito.times(1)).addParkingSlot(ParkingSlotType.GASOLINE, "C20");
  }

  @Test public void addParkingSlotWithIllegalArgumentShouldThrowTheExpectedException() {
    TollParking tollParking = new TollParking(pricingPolicy);

    // check.
    assertThatThrownBy(() -> tollParking.addParkingSlot(null, "C20")).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("cannot add the parking slot, parkingSlotType field is null.");

    assertThatThrownBy(() -> tollParking.addParkingSlot(ParkingSlotType.GASOLINE, null)).isInstanceOf(
        IllegalArgumentException.class)
        .hasMessage("cannot add the parking slot, parkingSlotId field is null or empty.");
  }

  @Test public void removeParkingSlotShouldCallTheRelatedParkingBuilderMethods() throws ParkingBuilderException {
    ParkingBuilder mockedParkingBuilder = Mockito.mock(ParkingBuilder.class);
    ParkingSlotBooker mockedParkingSlotBooker = Mockito.mock(ParkingSlotBooker.class);
    TollParking tollParking = new TollParking(pricingPolicy, mockedParkingBuilder, mockedParkingSlotBooker);

    // check.
    tollParking.removeParkingSlot("C20");
    Mockito.verify(mockedParkingBuilder, Mockito.times(1)).removeParkingSlot("C20");
  }

  @Test public void removeParkingSlotWithIllegalArgumentShouldThrowTheExpectedException() {
    TollParking tollParking = new TollParking(pricingPolicy);

    // check.
    assertThatThrownBy(() -> tollParking.removeParkingSlot(null)).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("cannot remove the parking slot, parkingSlotId field is null or empty.");
  }

  @Test public void getParkingSlotShouldCallTheRelatedParkingBuilderMethodsAndReturnTheIdOfTheAssignedParkingSlot()
      throws ParkingSlotException {
    ParkingBuilder spiedParkingBuilder = Mockito.spy(new ParkingBuilder());
    ParkingSlotBooker spiedParkingSlotBooker = Mockito.spy(
        new ParkingSlotBooker(spiedParkingBuilder.getParkingSlots()));
    ParkingSlot mockedParkingSlot = Mockito.mock(ParkingSlot.class);
    Mockito.when(mockedParkingSlot.getId()).thenReturn("H2500");
    Mockito.when(spiedParkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE)).thenReturn(mockedParkingSlot);
    TollParking tollParking = new TollParking(pricingPolicy, spiedParkingBuilder, spiedParkingSlotBooker);

    // check.
    String retId = tollParking.getParkingSlot(ParkingSlotType.GASOLINE);
    Mockito.verify(spiedParkingSlotBooker, Mockito.times(1)).getParkingSlot(ParkingSlotType.GASOLINE);
    assertThat(retId).isEqualTo("H2500");
  }

  @Test public void getParkingSlotShouldCallTheRelatedParkingBuilderMethodsAndReturnNullAsNoParkingSlotHasBeenFound()
      throws ParkingSlotException {
    ParkingBuilder spiedParkingBuilder = Mockito.spy(new ParkingBuilder());
    ParkingSlotBooker spiedParkingSlotBooker = Mockito.spy(
        new ParkingSlotBooker(spiedParkingBuilder.getParkingSlots()));
    Mockito.when(spiedParkingSlotBooker.getParkingSlot(ParkingSlotType.GASOLINE)).thenReturn(null);
    TollParking tollParking = new TollParking(pricingPolicy, spiedParkingBuilder, spiedParkingSlotBooker);

    // check.
    String retId = tollParking.getParkingSlot(ParkingSlotType.GASOLINE);
    Mockito.verify(spiedParkingSlotBooker, Mockito.times(1)).getParkingSlot(ParkingSlotType.GASOLINE);
    assertThat(retId).isNull();
  }

  @Test public void getParkingSlotWithIllegalArgumentShouldThrowTheExpectedException() {
    TollParking tollParking = new TollParking(pricingPolicy);

    // check.
    assertThatThrownBy(() -> tollParking.getParkingSlot(null)).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("cannot get the parking slot, parkingSlotType field is null.");
  }

  @Test public void releaseParkingSlotShouldCallTheRelatedParkingBuilderMethodsAndReturnTheExpectedBookingTime()
      throws ParkingSlotException, ParkingSlotBookerException {
    ParkingBuilder mockedParkingBuilder = Mockito.mock(ParkingBuilder.class);
    ParkingSlotBooker mockedParkingSlotBooker = Mockito.mock(ParkingSlotBooker.class);
    Mockito.when(mockedParkingSlotBooker.releaseParkingSlot("C20")).thenReturn(7800L); // 7800s booking time.
    TollParking tollParking = new TollParking(pricingPolicy, mockedParkingBuilder, mockedParkingSlotBooker);

    // check.
    long retBookingTime = tollParking.releaseParkingSlot("C20");
    Mockito.verify(mockedParkingSlotBooker, Mockito.times(1)).releaseParkingSlot("C20");
    assertThat(retBookingTime).isEqualTo(pricingPolicy.computeBillInCts(7800L));
  }

  @Test public void releaseParkingSlotWithIllegalArgumentShouldThrowTheExpectedException() {
    TollParking tollParking = new TollParking(pricingPolicy);

    // check.
    assertThatThrownBy(() -> tollParking.releaseParkingSlot(null)).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("cannot release the parking slot, parkingSlotId field is null or empty.");
  }
}