package com.eddya.tollparking;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * Provide a way to control time.
 */
public class CurrentTimeSupplier implements Supplier<Instant> {

    @Override
    public Instant get() {
        return Instant.now();
    }
}
