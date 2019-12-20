package com.eddya.tollparking;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages parking building.
 *
 * @author Eddy Albert
 */
class ParkingBuilder {

    private static CurrentTimeSupplier currentTimeSupplier = new CurrentTimeSupplier();
    private final List<ParkingSlot> parkingSlots;

    ParkingBuilder() {
        this.parkingSlots = new ArrayList<>();
    }

    List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }

    /**
     * Add a parking slot.
     * note: this method can be used to alter the parking during its used (e.g. new slot)
     *
     * @param parkingSlotType the the {@link ParkingSlotType}
     * @param id              the parking slot identifier
     * @throws ParkingBuilderException if the parking slot identifier has already been assigned
     */
    void addParkingSlot(@NotNull ParkingSlotType parkingSlotType, @NotNull String id) throws ParkingBuilderException {

        synchronized (parkingSlots) {
            ParkingSlot parkingSlot = parkingSlots.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
            if (parkingSlot == null) {
                parkingSlots.add(new ParkingSlot(id, parkingSlotType, currentTimeSupplier));
            } else {
                throw new ParkingBuilderException("cannot add parking slot " + id + ", the identifier is already assigned.");
            }
        }
    }

    /**
     * Remove a parking slot.
     * note: this method can be used to alter the parking during its used (e.g. temporarily unavailable slot)
     *
     * @param id the parking slot identifier
     * @throws ParkingBuilderException if the parking slot is booked or does not exist
     */
    void removeParkingSlot(@NotNull String id) throws ParkingBuilderException {

        synchronized (parkingSlots) {
            ParkingSlot parkingSlot = parkingSlots.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
            if (parkingSlot != null) {
                if (!parkingSlot.isVacant()) {
                    throw new ParkingBuilderException("cannot remove parking slot " + id + ", the parking slot is booked.");
                }
                parkingSlots.remove(parkingSlot);
            } else {
                throw new ParkingBuilderException("cannot remove parking slot " + id + ", the parking slot does not exist.");
            }
        }
    }
}
