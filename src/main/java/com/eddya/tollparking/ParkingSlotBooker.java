package com.eddya.tollparking;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class handles parking slot booking.
 *
 * @author Eddy Albert
 */
class ParkingSlotBooker {

    private final List<ParkingSlot> parkingSlots; // the whole set of parking slot.

    ParkingSlotBooker(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    /**
     * Book and return a {@link ParkingSlot} is available.
     *
     * @param parkingSlotType the related {@link ParkingSlotType} to book
     * @return the booked {@link ParkingSlot} if available, null otherwise
     */
    @Nullable
    ParkingSlot getParkingSlot(@NotNull ParkingSlotType parkingSlotType) throws ParkingSlotException {

        synchronized (parkingSlots) {
            ParkingSlot parkingSlot = parkingSlots.stream()
                    .filter(p -> p.getParkingSlotType().equals(parkingSlotType) && p.isVacant())
                    .findAny()
                    .orElse(null);

            if (parkingSlot != null) {
                parkingSlot.book();
            }
            return parkingSlot;
        }
    }

    /**
     * Release a {@link ParkingSlot} based on its identifier
     *
     * @param parkingSlotId the parking slot identifier
     * @return the time the parking slot was booked in seconds
     * @throws ParkingSlotException       if the system failed releasing a booked parking slot (technical issue)
     * @throws ParkingSlotBookerException if the parking slot identifier is not booked or does not exist
     */
    long releaseParkingSlot(@NotNull String parkingSlotId) throws ParkingSlotBookerException, ParkingSlotException {

        synchronized (parkingSlots) {
            ParkingSlot parkingSlot = parkingSlots.stream()
                    .filter(p -> p.getId().equals(parkingSlotId) && !p.isVacant())
                    .findFirst()
                    .orElse(null);

            if (parkingSlot != null) {
                return parkingSlot.release();
            } else {
                throw new ParkingSlotBookerException(
                        "cannot release parking slot " + parkingSlotId + ", it is not booked or does not exist.");
            }
        }
    }

    /**
     * Compute the number of vacant slots for a given {@link ParkingSlotType}.
     *
     * @param parkingSlotType the {@link ParkingSlotType}
     * @return the number of vacant slots having type {@link ParkingSlotType}
     */
    long getNbVacantParkingSlot(@NotNull ParkingSlotType parkingSlotType) {
        synchronized (parkingSlots) {
            return parkingSlots.stream()
                    .filter(p -> p.getParkingSlotType().equals(parkingSlotType) && p.isVacant())
                    .count();
        }
    }
}
