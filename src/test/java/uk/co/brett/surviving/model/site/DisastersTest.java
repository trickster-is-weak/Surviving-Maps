package uk.co.brett.surviving.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.io.LandingSiteFlat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.co.brett.surviving.enums.DisasterType.*;

class DisastersTest {

    Disasters disasters;
    LandingSiteFlat site;

    @BeforeEach
    void setUp() {
        site = LandingSiteTestUtil.getLandingSite();
        disasters = new Disasters(site);
    }

    @Test
    void getSum() {
        int sum = site.coldWaves() + site.meteors() + site.dustDevils() + site.dustStorms();
        assertThat(disasters.getSum()).isEqualTo(sum);
    }


    @Test
    void getDustDevils() {
        assertThat(disasters.getDustDevils()).isEqualTo(site.dustDevils());
    }


    @Test
    void getDustStorms() {
        assertThat(disasters.getDustStorms()).isEqualTo(site.dustStorms());
    }


    @Test
    void getMeteors() {
        assertThat(disasters.getMeteors()).isEqualTo(site.meteors());
    }

    @Test
    void getColdWaves() {
        assertThat(disasters.getColdWaves()).isEqualTo(site.coldWaves());
    }


    @Test
    void testEquals() {

        assertThat(disasters.equals(disasters)).isTrue();
        assertThat(disasters.equals(null)).isFalse();
        assertThat(disasters.equals(site)).isFalse();
        Disasters disasters1 = new Disasters();
        disasters1.setColdWaves(disasters.getColdWaves());
        disasters1.setMeteors(disasters.getMeteors());
        disasters1.setDustDevils(disasters.getDustDevils());
        disasters1.setDustStorms(disasters.getDustStorms());

        assertThat(disasters.equals(disasters1)).isTrue();
        disasters1.setDustStorms(0);
        assertThat(disasters.equals(disasters1)).isFalse();
    }


    @Test
    void get() {
        assertThat(disasters.get(COLD_WAVES)).isEqualTo(site.coldWaves());
        assertThat(disasters.get(METEORS)).isEqualTo(site.meteors());
        assertThat(disasters.get(DUST_DEVILS)).isEqualTo(site.dustDevils());
        assertThat(disasters.get(DUST_STORMS)).isEqualTo(site.dustStorms());
    }
}