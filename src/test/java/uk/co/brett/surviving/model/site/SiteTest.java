package uk.co.brett.surviving.model.site;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.Compass;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.ImmutableLandingSiteFlat;
import uk.co.brett.surviving.io.LandingSiteFlat;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SiteTest {

    private Site site;

    private LandingSiteFlat flat;

    private Map<GameVariant, List<Breakthrough>> btrVarMap;

    @BeforeEach
    void setUp() {
        flat = LandingSiteTestUtil.getLandingSite();
        btrVarMap = LandingSiteTestUtil.breakthroughMap();
        site = new Site(flat, btrVarMap);
    }

    @Test
    void setDisasters() {

        Disasters old = site.getDisasters();
        Disasters dis = new Disasters();
        dis.setDustStorms(LandingSiteTestUtil.getInt(1, 4));
        dis.setDustDevils(LandingSiteTestUtil.getInt(1, 4));
        dis.setMeteors(LandingSiteTestUtil.getInt(1, 4));
        dis.setColdWaves(LandingSiteTestUtil.getInt(1, 4));

        site.setDisasters(dis);

        assertThat(site.getDisasters())
                .isEqualToComparingFieldByField(dis)
                .isNotEqualTo(old);

    }

    @Test
    void setResources() {

        Resources old = site.getResources();
        Resources res = new Resources();
        res.setMetal(LandingSiteTestUtil.getInt(1, 4));
        res.setRareMetal(LandingSiteTestUtil.getInt(1, 4));
        res.setConcrete(LandingSiteTestUtil.getInt(1, 4));
        res.setWater(LandingSiteTestUtil.getInt(1, 4));

        site.setResources(res);

        assertThat(site.getResources())
                .isEqualToComparingFieldByField(res)
                .isNotEqualTo(old);

    }

    @Test
    void getMapDetails() {

        MapDetails exp = new MapDetails();
        exp.setAltitude(flat.altitude());
        exp.setTemperature(flat.temperature());
        exp.setChallengeDifficulty(flat.difficulty());
        exp.setTopography(flat.topography());
        exp.setMapName(flat.mapName());
        exp.setNamedLocation(flat.namedLocation());

        assertThat(site.getMapDetails())
                .isEqualToIgnoringGivenFields(exp, "id", "site");

    }

    @Test
    void getLatitude() {
        LandingSiteFlat temp = ImmutableLandingSiteFlat.builder().from(flat)
                .latitude(0)
                .northSouth(Compass.SOUTH)
                .build();


        Site tempSite = new Site(temp, btrVarMap);

        assertThat(tempSite.getLatitude()).isZero();
        assertThat(tempSite.getNorthSouth()).isEqualTo(Compass.NORTH);

    }

    @Test
    void getNorthSouth() {
        if (flat.latitude() != 0) {
            assertThat(site.getNorthSouth()).isEqualTo(flat.northSouth());
        } else {
            assertThat(site.getNorthSouth()).isEqualTo(Compass.NORTH);
        }
    }

    @Test
    void getLongitude() {
        assertThat(site.getLongitude()).isEqualTo(flat.longitude());
    }

    @Test
    void getEastWest() {
        assertThat(site.getEastWest()).isEqualTo(flat.eastWest());
    }

    @Test
    void getNamedLocation() {
        assertThat(site.getNamedLocation()).isEqualTo(flat.namedLocation());
    }

    @Test
    void testToString() {

        assertThat(site.toString())
                .contains("latitude=" + site.getLatitude())
                .contains("northSouth=" + site.getNorthSouth())
                .contains("longitude=" + site.getLongitude())
                .contains("eastWest=" + site.getEastWest())
                .contains("namedLocation=" + site.getNamedLocation());
    }

    @Test
    void toPrettyString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String s = site.toPrettyString();
        JsonNode json = mapper.valueToTree(site);

        assertThat(s)
                .contains(quotedString("latitude") + " : " + json.get("latitude").asText())
                .contains(quotedString("longitude") + " : " + json.get("longitude").asText())
                .contains(quotedString("northSouth") + " : " + quotedString(json.get("northSouth").asText()))
                .contains(quotedString("eastWest") + " : " + quotedString(json.get("eastWest").asText()))
                .contains(quotedString("namedLocation") + " : " + quotedString(json.get("namedLocation").asText()));

    }


    public String quotedString(String s) {
        return "\"" + s + "\"";
    }

    @Test
    void shortFormatted() {
        assertThat(site.shortFormatted())
                .contains(site.getNorthSouth().getAbbrev())
                .contains(site.getEastWest().getAbbrev())
                .contains("" + site.getLatitude())
                .contains("" + site.getLongitude());
    }

    @Test
    void getBreakthroughs() {
        for (GameVariant variant : GameVariant.values()) {
            assertThat(site.getBreakthroughs(variant))
                    .containsExactlyInAnyOrderElementsOf(btrVarMap.get(variant));
        }
    }
}