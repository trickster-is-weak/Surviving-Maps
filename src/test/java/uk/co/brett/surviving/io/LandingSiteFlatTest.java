package uk.co.brett.surviving.io;

import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.enums.GameVariant;

import static org.assertj.core.api.Assertions.assertThat;

class LandingSiteFlatTest {


    @Test
    void getBreakthroughs() {

        LandingSiteFlat std = LandingSiteTestUtil.getLandingSite(GameVariant.STANDARD);
        LandingSiteFlat evn = LandingSiteTestUtil.getLandingSite(GameVariant.EVANS_GREEN_PLANET);

        assertThat(std.getBreakthroughs()).hasSize(12);
        assertThat(evn.getBreakthroughs()).hasSize(17);

    }

}