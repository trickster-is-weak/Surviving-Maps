//package uk.co.brett.surviving.filters;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.co.brett.surviving.LandingSite;
//import uk.co.brett.surviving.LandingSiteTestUtil;
//import uk.co.brett.surviving.SimpleSiteStatistics;
//import uk.co.brett.surviving.enums.Breakthrough;
//import uk.co.brett.surviving.types.Disasters;
//import uk.co.brett.surviving.types.MapDetails;
//import uk.co.brett.surviving.types.Resources;
//
//import java.util.*;
//import java.util.function.Predicate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static uk.co.brett.surviving.filters.Prevalence.*;
//
//class SimpleSiteFilterTest {
//    private final Comparator<LandingSite> resourcesComparator = Comparator.<LandingSite, Integer>
//            comparing(landingSite -> landingSite.resources().difficulty())
//            .thenComparing(LandingSite::id);
//    private final Comparator<LandingSite> mapComparator = Comparator.<LandingSite, Integer>comparing(landingSite -> landingSite.mapDetails().difficulty()).thenComparing(LandingSite::id);
//    private final Comparator<LandingSite> disasterComparator = Comparator.<LandingSite, Integer>comparing(landingSite -> landingSite.disasters().difficulty()).thenComparing(LandingSite::id);
//    private List<LandingSite> sites;
//    private SimpleSiteStatistics stats;
//    private SimpleSiteFilter filter;
//
//
//    @BeforeEach
//    void setUp() {
//        sites = LandingSiteTestUtil.getLandingSites(13);
//        stats = new SimpleSiteStatistics(sites);
//        filter = new SimpleSiteFilter(sites, stats);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void filterResources() {
//        Predicate<Prevalence> p = atLeast(Prevalence.MED);
//        List<LandingSite> filtered = filter.filterResources(p);
//
//        List<LandingSite> low = sites.stream().sorted(resourcesComparator).limit(4).toList();
//        assertThat(filtered).isNotEmpty().doesNotContainAnyElementsOf(low);
//
//    }
//
//    @Test
//    void filterDisasters() {
//        Predicate<Prevalence> p = atLeast(Prevalence.MED);
//        List<LandingSite> filtered = filter.filterDisasters(p);
//
//        List<LandingSite> low = sites.stream().sorted(disasterComparator).limit(4).toList();
//        assertThat(filtered).isNotEmpty().doesNotContainAnyElementsOf(low);
//    }
//
//    @Test
//    void filterMaps() {
//        Predicate<Prevalence> p = atLeast(Prevalence.MED);
//        List<LandingSite> filtered = filter.filterMaps(p);
//
//        List<LandingSite> low = sites.stream().sorted(mapComparator).limit(4).toList();
//        assertThat(filtered).isNotEmpty().doesNotContainAnyElementsOf(low);
//    }
//
//    @Test
//    void filterBreakthroughs() {
//        sites = LandingSiteTestUtil.getLandingSites(80);
//        stats = new SimpleSiteStatistics(sites);
//        filter = new SimpleSiteFilter(sites, stats);
//        List<Breakthrough> breakthroughs = LandingSiteTestUtil.getBreakthroughs(2);
//        List<LandingSite> list = filter.filterBreakthroughs(breakthroughs);
//
//        for (LandingSite site : list) {
//            assertThat(site.breakthroughs()).containsAll(breakthroughs);
//        }
//
//
//    }
//
//    @Test
//    void filter() {
//        int num = 80;
//        int third = num / 3;
//
//        final List<LandingSite> sites = LandingSiteTestUtil.getLandingSites(num);
//        final List<LandingSite> test1 = new ArrayList<>(sites);
//        final List<LandingSite> test2 = new ArrayList<>(sites);
//        final List<LandingSite> test3 = new ArrayList<>(sites);
//        final List<LandingSite> test4 = new ArrayList<>(sites);
//
//        stats = new SimpleSiteStatistics(sites);
//        filter = new SimpleSiteFilter(new ArrayList<>(sites), stats);
//
//        List<Breakthrough> list = LandingSiteTestUtil.getBreakthroughs(1);
//        Map<Class<?>, Predicate<Prevalence>> predicates = new HashMap<>();
//        predicates.put(Disasters.class, atLeast(MED));
//        predicates.put(MapDetails.class, atMost(MED));
//        predicates.put(Resources.class, atLeast(MED));
//
//        List<LandingSite> filtered = filter.filter(predicates, list);
//
//        List<LandingSite> dis = test1.stream().sorted(disasterComparator).limit(third).toList();
//        List<LandingSite> map = test2.stream().sorted(mapComparator.reversed()).limit(third).toList();
//        List<LandingSite> btr = test3.stream().filter(i -> !i.breakthroughs().containsAll(list)).toList();
//        List<LandingSite> res = test4.stream().sorted(resourcesComparator).limit(third).toList();
//
//        List<LandingSite> filteredLocal = new ArrayList<>(sites);
//
//        filteredLocal.removeAll(dis);
//        filteredLocal.removeAll(map);
//        filteredLocal.removeAll(btr);
//        filteredLocal.removeAll(res);
//
//
//        assertThat(filtered.stream().map(LandingSite::id).toList())
//                .doesNotContainAnyElementsOf(dis.stream().map(LandingSite::id).toList());
//
//        assertThat(filtered.stream().map(LandingSite::id).toList())
//                .doesNotContainAnyElementsOf(map.stream().map(LandingSite::id).toList());
//
//        assertThat(filtered.stream().map(LandingSite::id).toList())
//                .doesNotContainAnyElementsOf(btr.stream().map(LandingSite::id).toList());
//
//        assertThat(filtered.size()).isEqualTo(filteredLocal.size());
//        assertThat(filtered).containsExactlyInAnyOrderElementsOf(filteredLocal);
//
//    }
//
//
//}