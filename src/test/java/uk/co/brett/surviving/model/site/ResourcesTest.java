package uk.co.brett.surviving.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.io.LandingSiteFlat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.co.brett.surviving.enums.ResourceType.*;

class ResourcesTest {

    Resources resources;
    LandingSiteFlat site;

    @BeforeEach
    void setUp() {
        site = LandingSiteTestUtil.getLandingSite();
        resources = new Resources(site);
    }

    @Test
    void getSum() {
        int sum = site.concrete() + site.water() + site.metals() + site.defaultRareMetals();
        assertThat(resources.getSum()).isEqualTo(sum);
    }

    @Test
    void getMetal() {
        assertThat(resources.getMetal()).isEqualTo(site.metals());
    }

    @Test
    void getRareMetal() {
        assertThat(resources.getRareMetal()).isEqualTo(site.defaultRareMetals());
    }

    @Test
    void getWater() {
        assertThat(resources.getWater()).isEqualTo(site.water());
    }

    @Test
    void getConcrete() {
        assertThat(resources.getConcrete()).isEqualTo(site.concrete());
    }

    @Test
    void testEquals() {

        assertThat(resources.equals(resources)).isTrue();
        assertThat(resources.equals(null)).isFalse();
        assertThat(resources.equals(site)).isFalse();
        Resources resources1 = new Resources();
        resources1.setConcrete(resources.getConcrete());
        resources1.setWater(resources.getWater());
        resources1.setMetal(resources.getMetal());
        resources1.setRareMetal(resources.getRareMetal());

        assertThat(resources.equals(resources1)).isTrue();
        resources1.setConcrete(0);
        assertThat(resources.equals(resources1)).isFalse();
        
    }

    @Test
    void get() {
        assertThat(resources.get(METAL)).isEqualTo(site.metals());
        assertThat(resources.get(RARE_METAL)).isEqualTo(site.defaultRareMetals());
        assertThat(resources.get(WATER)).isEqualTo(site.water());
        assertThat(resources.get(CONCRETE)).isEqualTo(site.concrete());

    }
}