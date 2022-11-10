package uk.co.brett.surviving.model.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BreakthroughMapTest {

    private BreakthroughMap map;

//    private String exp;

    private Breakthrough bt;

    private List<Long> list;

    @BeforeEach
    public void setup() {
        bt = Breakthrough.ADVANCED_DRONE_DRIVE;
        list = List.of(1L, 2L, 3L);
//        exp = "1,2,3";
        Map<GameVariant, List<Long>> varMap = new HashMap<>();
        for (GameVariant v : GameVariant.values()) {
            varMap.put(v, list);
        }
        map = new BreakthroughMap(bt, varMap);
    }


    @Test
    void getTitoGreenPlanet() {
        assertThat(map.getTitoGreenPlanet()).isEqualTo(list);
    }

    @Test
    void getBelowBeyond() {
        assertThat(map.getBelowBeyond()).isEqualTo(list);
    }


    @Test
    void getBelowBeyondGreenPlanet() {
        assertThat(map.getBelowBeyondGreenPlanet()).isEqualTo(list);
    }

    @Test
    void getGreenPlanet() {
        assertThat(map.getGreenPlanet()).isEqualTo(list);
    }

    @Test
    void getBreakthrough() {
        assertThat(map.getBreakthrough()).isEqualTo(bt);
    }

    @Test
    void getStandard() {
        assertThat(map.getStandard()).isEqualTo(list);
    }

    @Test
    void getIdList() {
        for (GameVariant var : GameVariant.values()) {
            assertThat(map.getIdList(var))
                    .isEqualTo(list);
        }
    }

    @Test
    void getEvansGreenPlanet() {
        assertThat(map.getEvansGreenPlanet()).isEqualTo(list);
    }

    @Test
    void testToString() {
        assertThat(map.toString())
                .isEqualToNormalizingPunctuationAndWhitespace(
                        "BreakthroughMap" +
                                "{breakthrough=ADVANCED_DRONE_DRIVE, " +
                                "standard='[1, 2, 3]', " +
                                "greenPlanet='[1, 2, 3]', " +
                                "belowBeyond='[1, 2, 3]', " +
                                "belowBeyondGreenPlanet='[1, 2, 3]', " +
                                "titoGreenPlanet='[1, 2, 3]', " +
                                "evansGreenPlanet='[1, 2, 3]'" +
                                "}");
    }
}