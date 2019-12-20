package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class PerHourPricingPolicyTest implements WithAssertions {

    @Test
    public void constructorShouldSetMembersWithTheExpectedValues() {
        PerHourPricingPolicy perHourPricingPolicy = new PerHourPricingPolicy(350);

        // check.
        assertThat(perHourPricingPolicy.getPricePerHourInCts()).isEqualTo(350);
    }

    @Test
    public void computeBillInCtsShouldWorkAsExpected() {
        PerHourPricingPolicy perHourPricingPolicy = new PerHourPricingPolicy(350);

        // check.
        assertThat(perHourPricingPolicy.computeBillInCts(0)).isEqualTo(0);
        assertThat(perHourPricingPolicy.computeBillInCts(20000)).isEqualTo(1750);
    }
}