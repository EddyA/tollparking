package com.eddya.tollparking;

public interface PricingPolicy {

    int NB_SECOND_PER_HOUR = 3600; // number of seconds per hour.

    /**
     * Compute the bill according to the number of seconds
     *
     * @param nbSeconds the number of seconds
     * @return the related bill in cts
     */
    int computeBillInCts(long nbSeconds);
}
