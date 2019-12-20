package com.eddya.tollparking;

/**
 * A pricing policy: bill each hour spent in the parking.
 * An hour is paid if completed (i.e. 55 min cost 0).
 */
public class PerHourPricingPolicy implements PricingPolicy {

    private final int pricePerHourInCts;

    /**
     * @param pricePerHourInCts the amount per hours to pay
     */
    public PerHourPricingPolicy(int pricePerHourInCts) {
        this.pricePerHourInCts = pricePerHourInCts;
    }

    int getPricePerHourInCts() {
        return pricePerHourInCts;
    }

    /**
     * Compute bill according to the booked time in seconds.
     * note: an hour is paid if completed (i.e. 55 min cost 0)
     *
     * @param nbSeconds the number of seconds
     * @return the price in cts
     */
    public int computeBillInCts(long nbSeconds) {
        return (int) (nbSeconds / NB_SECOND_PER_HOUR) * pricePerHourInCts;
    }
}
