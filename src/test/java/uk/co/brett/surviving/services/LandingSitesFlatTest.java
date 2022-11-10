package uk.co.brett.surviving.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.LandingSiteFlat;
import uk.co.brett.surviving.model.repo.BreakthroughMapRepo;
import uk.co.brett.surviving.model.site.BreakthroughMap;
import uk.co.brett.surviving.model.site.Disasters;
import uk.co.brett.surviving.model.site.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.brett.surviving.enums.GameVariant.STANDARD;

@SpringBootTest
class LandingSitesFlatTest {

    LandingSitesFlat ingest;
    @Autowired
    BreakthroughMapRepo btmRepo;
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @BeforeEach
    public void setup() {
        AutowireCapableBeanFactory bf = applicationContext.getAutowireCapableBeanFactory();
        ingest = bf.createBean(LandingSitesFlat.class);
    }

    @Test
    void getDisaster() {

        LandingSiteFlat flat = LandingSiteTestUtil.getLandingSite();

        Disasters d = ingest.getDisaster(flat);

        Disasters exp = new Disasters();
        exp.setColdWaves(flat.coldWaves());
        exp.setMeteors(flat.meteors());
        exp.setDustDevils(flat.dustDevils());
        exp.setDustStorms(flat.dustStorms());

        assertThat(d).isEqualToIgnoringGivenFields(exp, "id", "sum");

        // Add again to check caching
        Disasters d2 = ingest.getDisaster(flat);
        assertThat(d).isEqualTo(d2);

    }

    @Test
    void getResource() {
        LandingSiteFlat flat = LandingSiteTestUtil.getLandingSite();

        Resources r = ingest.getResource(flat);

        Resources exp = new Resources();
        exp.setConcrete(flat.concrete());
        exp.setWater(flat.water());
        exp.setMetal(flat.metals());
        exp.setRareMetal(flat.defaultRareMetals());

        assertThat(r).isEqualToIgnoringGivenFields(exp, "id", "sum");

        Resources r2 = ingest.getResource(flat);
        assertThat(r).isEqualTo(r2);

    }


    @Test
    void populateBreakthroughMap() {
        Map<Breakthrough, Map<GameVariant, List<Long>>> superMap = createMap();
        ReflectionTestUtils.setField(ingest, "superMap", superMap);
        ingest.populateBreakthroughMap();

        List<BreakthroughMap> list = btmRepo.findAll();

        for (BreakthroughMap b : list) {
            Long ord = (long) b.getBreakthrough().ordinal();
            assertThat(b.getIdList(STANDARD).get(0)).isEqualTo(ord);
        }
    }


    private Map<Breakthrough, Map<GameVariant, List<Long>>> createMap() {
        Map<Breakthrough, Map<GameVariant, List<Long>>> map = new HashMap<>();

        for (Breakthrough b : Breakthrough.values()) {
            Map<GameVariant, List<Long>> var = new HashMap<>();
            Long id = (long) b.ordinal();
            var.computeIfAbsent(STANDARD, k -> new ArrayList<>()).add(id);
            map.put(b, var);
        }

        return map;
    }

}