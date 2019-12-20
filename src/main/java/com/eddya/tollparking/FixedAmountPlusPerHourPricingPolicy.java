package com.eddya.tollparking;

/**
 * A pricing policy: bill each hour spent in the parking + fixed amount.
 * An hour is paid if completed (i.e. 55 min cost 0).
 */
public class FixedAmountPlusPerHourPricingPolicy implements PricingPolicy {

    private final int fixedAmountInCts;
    private final int pricePerHourInCts;

    /**
     * @param fixedAmountInCts  the fixed amount to pay
     * @param pricePerHourInCts the amount per hours to pay
     */
    public FixedAmountPlusPerHourPricingPolicy(int fixedAmountInCts, int pricePerHourInCts) {
        this.fixedAmountInCts = fixedAmountInCts;
        this.pricePerHourInCts = pricePerHourInCts;
    }

    int getFixedAmountInCts() {
        return fixedAmountInCts;
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
        return fixedAmountInCts + (int) (nbSeconds / NB_SECOND_PER_HOUR) * pricePerHourInCts;
    }
}
