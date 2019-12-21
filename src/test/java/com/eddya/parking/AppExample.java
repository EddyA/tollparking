package com.eddya.parking;

import com.eddya.tollparking.*;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class AppExample implements WithAssertions {

    @Test
    public void allCasesInOne() throws ParkingBuilderException, ParkingSlotException, ParkingSlotBookerException {
        TollParking tollParking = new TollParking(new FixedAmountPlusPerHourPricingPolicy(500, 250));

        // build.
        final int NB_SLOT_BY_EACH = 10;
        for (int slotIdx = 1; slotIdx <= NB_SLOT_BY_EACH; slotIdx++) {
            tollParking.addParkingSlot(ParkingSlotType.GASOLINE, "GAZ" + slotIdx);
            tollParking.addParkingSlot(ParkingSlotType.ELECTRIC_20KW, "E20" + slotIdx);
            tollParking.addParkingSlot(ParkingSlotType.ELECTRIC_50KW, "E50" + slotIdx);
        }
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(NB_SLOT_BY_EACH);
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isEqualTo(NB_SLOT_BY_EACH);
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isEqualTo(NB_SLOT_BY_EACH);

        // book all slots.
        for (int slotIdx = 1; slotIdx <= NB_SLOT_BY_EACH; slotIdx++) {
            assertThat(tollParking.getParkingSlot(ParkingSlotType.GASOLINE)).isNotNull();
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(NB_SLOT_BY_EACH - slotIdx);
            assertThat(tollParking.getParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isNotNull();
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isEqualTo(NB_SLOT_BY_EACH - slotIdx);
            assertThat(tollParking.getParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isNotNull();
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isEqualTo(NB_SLOT_BY_EACH - slotIdx);
        }

        // book (but no room).
        assertThat(tollParking.getParkingSlot(ParkingSlotType.GASOLINE)).isNull();
        assertThat(tollParking.getParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isNull();
        assertThat(tollParking.getParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isNull();

        // remove (but booked)
        assertThatThrownBy(() -> tollParking.removeParkingSlot("GAZ1")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot GAZ1, the parking slot is booked.");
        assertThatThrownBy(() -> tollParking.removeParkingSlot("E201")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot E201, the parking slot is booked.");
        assertThatThrownBy(() -> tollParking.removeParkingSlot("E501")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot E501, the parking slot is booked.");

        // release all slots.
        for (int slotIdx = 1; slotIdx <= NB_SLOT_BY_EACH; slotIdx++) {
            assertThat(tollParking.releaseParkingSlot("GAZ" + slotIdx)).isEqualTo(500);
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(slotIdx);
            assertThat(tollParking.releaseParkingSlot("E20" + slotIdx)).isEqualTo(500);
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isEqualTo(slotIdx);
            assertThat(tollParking.releaseParkingSlot("E50" + slotIdx)).isEqualTo(500);
            assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isEqualTo(slotIdx);
        }

        // release (but not booked)
        assertThatThrownBy(() -> tollParking.releaseParkingSlot("GAZ1")).isInstanceOf(ParkingSlotBookerException.class)
                .hasMessage("cannot release parking slot GAZ1, it is not booked or does not exist.");

        // release (but not does not exist)
        assertThatThrownBy(() -> tollParking.releaseParkingSlot("GAZ0")).isInstanceOf(ParkingSlotBookerException.class)
                .hasMessage("cannot release parking slot GAZ0, it is not booked or does not exist.");

        // remove.
        for (int slotIdx = 1; slotIdx <= NB_SLOT_BY_EACH; slotIdx++) {
            tollParking.removeParkingSlot("GAZ" + slotIdx);
            tollParking.removeParkingSlot("E20" + slotIdx);
            tollParking.removeParkingSlot("E50" + slotIdx);
        }
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.GASOLINE)).isEqualTo(0);
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_20KW)).isEqualTo(0);
        assertThat(tollParking.getNbVacantParkingSlot(ParkingSlotType.ELECTRIC_50KW)).isEqualTo(0);

        // remove (but does not exist)
        assertThatThrownBy(() -> tollParking.removeParkingSlot("GAZ1")).isInstanceOf(ParkingBuilderException.class)
                .hasMessage("cannot remove parking slot GAZ1, the parking slot does not exist.");
    }
}
