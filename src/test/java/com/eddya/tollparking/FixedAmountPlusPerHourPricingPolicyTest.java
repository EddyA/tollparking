package com.eddya.tollparking;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class FixedAmountPlusPerHourPricingPolicyTest implements WithAssertions {

    @Test
    public void constructorShouldSetMembersWithTheExpectedValues() {
        FixedAmountPlusPerHourPricingPolicy fixedAmountPlusPerHourPricingPolicy
                = new FixedAmountPlusPerHourPricingPolicy(500, 250);

        // check.
        assertThat(fixedAmountPlusPerHourPricingPolicy.getFixedAmountInCts()).isEqualTo(500);
        assertThat(fixedAmountPlusPerHourPricingPolicy.getPricePerHourInCts()).isEqualTo(250);
    }

    @Test
    public void computeBillInCtsShouldWorkAsExpected() {
        FixedAmountPlusPerHourPricingPolicy fixedAmountPlusPerHourPricingPolicy
                = new FixedAmountPlusPerHourPricingPolicy(500, 250);

        // check.
        assertThat(fixedAmountPlusPerHourPricingPolicy.computeBillInCts(0)).isEqualTo(500);
        assertThat(fixedAmountPlusPerHourPricingPolicy.computeBillInCts(10000)).isEqualTo(1000);
    }
}