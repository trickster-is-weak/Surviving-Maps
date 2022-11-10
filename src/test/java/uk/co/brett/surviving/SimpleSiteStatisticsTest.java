//package uk.co.brett.surviving;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.co.brett.surviving.filters.Prevalence;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Predicate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static uk.co.brett.surviving.filters.Prevalence.*;
//
//class SimpleSiteStatisticsTest {
//    List<LandingSite> sites;
//    SimpleSiteStatistics stats;
//    private final Comparator<LandingSite> resourcesComparator = Comparator.<LandingSite, Integer>
//            comparing(landingSite -> landingSite.resources().difficulty())
//            .thenComparing(LandingSite::id);
//    private final Comparator<LandingSite> mapComparator = Comparator.<LandingSite, Integer>
//            comparing(landingSite -> landingSite.mapDetails().difficulty())
//            .thenComparing(LandingSite::id);
//    private final Comparator<LandingSite> disasterComparator = Comparator.<LandingSite, Integer>
//            comparing(landingSite -> landingSite.disasters().difficulty())
//            .thenComparing(LandingSite::id);;
//
//    @BeforeEach
//    void setUp() {
//        sites = LandingSiteTestUtil.getLandingSites(13);
//        stats = new SimpleSiteStatistics(sites);
//    }
//
//    @AfterEach
//    void tearDown() {
//        sites = null;
//    }
//
//    @Test
//    void getResources() {
//
//        Predicate<Prevalence> pred = Prevalence.atLeast(MED);
//        List<LandingSite> out = stats.getResources(pred);
//        int third = sites.size() / 3;
//        List<LandingSite> list = sites.stream().sorted(resourcesComparator.reversed()).limit(sites.size() - third).toList();
//
//
//        assertThat(out).containsExactlyInAnyOrderElementsOf(list);
//
//    }
//
//    @Test
//    void binResources() {
//
//        Map<Prevalence, List<LandingSite>> out = stats.binSites(resourcesComparator);
//
//        List<LandingSite> low = sites.stream()
//                .sorted(resourcesComparator)
//                .limit(4).toList();
//        List<LandingSite> high = sites.stream()
//                .sorted(resourcesComparator.reversed())
//                .limit(4).toList();
//        List<LandingSite> med = sites.stream()
//                .filter(i -> !low.contains(i))
//                .filter(i -> !high.contains(i))
//                .toList();
//
//        assertThat(out.get(LOW)).containsExactlyInAnyOrderElementsOf(low);
//        assertThat(out.get(MED)).containsExactlyInAnyOrderElementsOf(med);
//        assertThat(out.get(HIGH)).containsExactlyInAnyOrderElementsOf(high);
//
//    }
//
//    @Test
//    void getDisasters() {
//        Predicate<Prevalence> pred = Prevalence.atLeast(MED);
//        List<LandingSite> out = stats.getDisasters(pred);
//        int third = sites.size() / 3;
//        List<LandingSite> list = sites.stream().sorted(disasterComparator.reversed()).limit(sites.size() - third).toList();
//
//
//        assertThat(out).containsExactlyInAnyOrderElementsOf(list);
//    }
//
//    @Test
//    void getMaps() {
//        Predicate<Prevalence> pred = Prevalence.atLeast(MED);
//        List<LandingSite> out = stats.getMaps(pred);
//        int third = sites.size() / 3;
//        List<LandingSite> list = sites.stream().sorted(mapComparator.reversed()).limit(sites.size() - third).toList();
//
//
//        assertThat(out).containsExactlyInAnyOrderElementsOf(list);
//    }
//}