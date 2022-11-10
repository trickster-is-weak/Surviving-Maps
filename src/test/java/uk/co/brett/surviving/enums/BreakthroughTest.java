package uk.co.brett.surviving.enums;

import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.brett.surviving.enums.GameVariant.*;

class BreakthroughTest {

    @Test
    void getBreakthrough() {
        Breakthrough bt = LandingSiteTestUtil.getRandomEnum(Breakthrough.class);
        Breakthrough out = Breakthrough.getBreakthrough(bt.getFormatted());
        Breakthrough out2 = Breakthrough.getBreakthrough(bt.name());

        assertThat(out).isEqualTo(bt);
        assertThat(out2).isEqualTo(bt);

    }


    @Test
    void testFilter() {
        Map<GameVariant, List<Breakthrough>> map = new HashMap<>();
        for (Breakthrough b : Breakthrough.values()) {
            map.computeIfAbsent(b.getVariant(), k -> new ArrayList<>()).add(b);
        }

        assertThat(Breakthrough.filterVariant(STANDARD))
                .containsAll(map.get(STANDARD))
                .doesNotContainAnyElementsOf(map.get(BELOW_BEYOND))
                .doesNotContainAnyElementsOf(map.get(GREEN_PLANET));

        assertThat(Breakthrough.filterVariant(GREEN_PLANET))
                .containsAll(map.get(GREEN_PLANET))
                .containsAll(map.get(STANDARD))
                .doesNotContainAnyElementsOf(map.get(BELOW_BEYOND));

        assertThat(Breakthrough.filterVariant(BELOW_BEYOND))
                .containsAll(map.get(STANDARD))
                .containsAll(map.get(BELOW_BEYOND))
                .doesNotContainAnyElementsOf(map.get(GREEN_PLANET));

        assertThat(Breakthrough.filterVariant(BEYOND_GREEN))
                .containsAll(map.get(STANDARD))
                .containsAll(map.get(GREEN_PLANET))
                .containsAll(map.get(BELOW_BEYOND));
    }


//    @Test
//    void predicate() {
//
//        List<Breakthrough> list = LandingSiteTestUtil.getBreakthroughs(2);
//        Predicate<LandingSite> p = Breakthrough.predicate(list);
//
//        List<LandingSite> sites = LandingSiteTestUtil.getLandingSites(200);
//        List<LandingSite> filtered = sites.stream().filter(p).toList();
//
//        for (LandingSite site : filtered){
//            assertThat(site.breakthroughs()).containsAll(list);
//        }
//    }
}