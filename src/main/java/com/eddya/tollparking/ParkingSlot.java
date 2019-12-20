package com.eddya.tollparking;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class represents a parking slot.
 * It handles booking time computation.
 *
 * @author Eddy Albert
 */
public class ParkingSlot {

    private final String id;
    private final ParkingSlotType parkingSlotType;
    private final CurrentTimeSupplier currentTimeSupplier;

    private long bookingStartTime;

    /**
     * A park slot.
     *
     * @param id              the parking slot identifier (e.g. "C20")
     * @param parkingSlotType the related {@link ParkingSlotType}
     */
    ParkingSlot(@NotNull String id, @NotNull ParkingSlotType parkingSlotType,
                @NotNull CurrentTimeSupplier currentTimeSupplier) {
        this.id = id;
        this.parkingSlotType = parkingSlotType;
        this.currentTimeSupplier = currentTimeSupplier;

        this.bookingStartTime = 0;
    }

    String getId() {
        return id;
    }

    ParkingSlotType getParkingSlotType() {
        return parkingSlotType;
    }

    long getBookingStartTime() {
        return bookingStartTime;
    }

    /**
     * book a {@link ParkingSlot} setting its start time.
     *
     * @throws ParkingSlotException if the {@link ParkingSlot} is already booked
     */
    void book() throws ParkingSlotException {
        if (!isVacant()) {
            throw new ParkingSlotException("cannot book parking slot " + id + ", it is already booked.");
        }
        bookingStartTime = currentTimeSupplier.get().getEpochSecond();
    }

    /**
     * Release the {@link ParkingSlot} and return the booking time.
     *
     * @return the booked time in seconds
     * @throws ParkingSlotException if the {@link ParkingSlot} is not booked
     */
    long release() throws ParkingSlotException {
        if (isVacant()) {
            throw new ParkingSlotException("cannot release parking slot " + id + ", it is not booked.");
        }
        long bookedTime = currentTimeSupplier.get().getEpochSecond() - bookingStartTime;
        bookingStartTime = 0;
        return bookedTime;
    }

    boolean isVacant() {
        return bookingStartTime == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return id.equals(that.id) &&
                parkingSlotType == that.parkingSlotType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parkingSlotType);
    }
}
