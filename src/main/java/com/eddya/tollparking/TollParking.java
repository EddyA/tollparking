package com.eddya.tollparking;

/**
 * This library handles toll parking.
 * 1. choose a pricing policy,
 * 2. build your parking,
 * 3. manage your parking.
 */
public class TollParking {

    private final PricingPolicy pricingPolicy;
    private final ParkingBuilder parkingBuilder;
    private final ParkingSlotBooker parkingSlotBooker;

    public TollParking(PricingPolicy pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
        this.parkingBuilder = new ParkingBuilder();
        this.parkingSlotBooker = new ParkingSlotBooker(parkingBuilder.getParkingSlots());
    }

    TollParking(PricingPolicy pricingPolicy,
                ParkingBuilder parkingBuilder,
                ParkingSlotBooker parkingSlotBooker) {  /* test purpose */
        this.pricingPolicy = pricingPolicy;
        this.parkingBuilder = parkingBuilder;
        this.parkingSlotBooker = parkingSlotBooker;
    }

    // --- build your parking.

    /**
     * This methods allows adding a parking slot.
     *
     * @param parkingSlotType the {@link ParkingSlotType}
     * @param parkingSlotId   the parking slot identifier (e.g. C20)
     * @throws ParkingBuilderException if the parking slot identifier has already been assigned
     */
    public void addParkingSlot(ParkingSlotType parkingSlotType, String parkingSlotId) throws ParkingBuilderException {
        if (parkingSlotType == null) {
            throw new IllegalArgumentException("cannot add the parking slot, parkingSlotType field is null.");
        }
        if (parkingSlotId == null || parkingSlotId.isEmpty()) {
            throw new IllegalArgumentException("cannot add the parking slot, parkingSlotId field is null or empty.");
        }
        parkingBuilder.addParkingSlot(parkingSlotType, parkingSlotId);
    }

    /**
     * This method allows removing a parking slot.
     * note: this method can be used to alter the parking during its used (e.g. temporarily unavailable slot)
     *
     * @param parkingSlotId the parking slot identifier (e.g. C20)
     * @throws ParkingBuilderException if the parking slot identifier has already been assigned
     */
    public void removeParkingSlot(String parkingSlotId) throws ParkingBuilderException {
        if (parkingSlotId == null || parkingSlotId.isEmpty()) {
            throw new IllegalArgumentException("cannot remove the parking slot, parkingSlotId field is null or empty.");
        }
        parkingBuilder.removeParkingSlot(parkingSlotId);
    }

    // -- manage your parking.

    /**
     * This methods asks the system for a vacant parking slot of a given type.
     *
     * @param parkingSlotType the {@link ParkingSlotType}
     * @return a parking slot identifier if available, null otherwise (i.e. no slot found for that type)
     * @throws ParkingSlotException if the system failed looking for a vacant parking slot (technical issue)
     */
    public String getParkingSlot(ParkingSlotType parkingSlotType) throws ParkingSlotException {
        if (parkingSlotType == null) {
            throw new IllegalArgumentException("cannot get the parking slot, parkingSlotType field is null.");
        }
        ParkingSlot parkingSlot = parkingSlotBooker.getParkingSlot(parkingSlotType);
        if (parkingSlot != null) {
            return parkingSlot.getId();
        } else {
            return null;
        }
    }

    /**
     * This methods asks the system to release a parking slot.
     *
     * @param parkingSlotId the parking slot identifier (e.g. C20)
     * @return the related bill
     * @throws ParkingSlotException       if the system failed releasing a booked parking slot (technical issue)
     * @throws ParkingSlotBookerException if the parking slot identifier is not booked or does not exist
     */
    public int releaseParkingSlot(String parkingSlotId) throws ParkingSlotException, ParkingSlotBookerException {
        if (parkingSlotId == null || parkingSlotId.isEmpty()) {
            throw new IllegalArgumentException("cannot release the parking slot, parkingSlotId field is null or empty.");
        }
        return pricingPolicy.computeBillInCts(parkingSlotBooker.releaseParkingSlot(parkingSlotId));
    }

    /**
     * Compute the number of vacant slots for a given {@link ParkingSlotType}.
     *
     * @param parkingSlotType the {@link ParkingSlotType}
     * @return the number of vacant slots having type {@link ParkingSlotType}
     */
    public long getNbVacantParkingSlot(ParkingSlotType parkingSlotType) {
        if (parkingSlotType == null) {
            throw new IllegalArgumentException("cannot get the number of vacant slots, parkingSlotType field is null.");
        }
        return parkingSlotBooker.getNbVacantParkingSlot(parkingSlotType);
    }
}
