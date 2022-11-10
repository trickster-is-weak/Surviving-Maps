//package uk.co.brett.surviving.filters;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import uk.co.brett.surviving.LandingSiteTestUtil;
//import uk.co.brett.surviving.io.LandingSiteFlat;
//
//import java.util.List;
//
//class ComplexSiteFilterTest {
//
//    private List<LandingSiteFlat> sites;
//    private ComplexSiteFilter complexSiteFilter;
//
//    @BeforeEach
//    void setUp() {
//        sites = LandingSiteTestUtil.getLandingSites(250);
//        complexSiteFilter = new ComplexSiteFilter(sites);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
////    @Test
////    void filter() {
////
////        List<NamedLandingArea> areaList = LandingSiteTestUtil.getAssortedEnums(NamedLandingArea.class, 8);
////
////        Map<ResourceType, Predicate<Rating>> resourceMap = new EnumMap<>(ResourceType.class);
////        resourceMap.put(ResourceType.METAL, Rating.atLeast(MEDIUM));
////        Map<DisasterType, Predicate<Rating>> disasterMap = new EnumMap<>(DisasterType.class);
////        disasterMap.put(DisasterType.METEORS, Rating.atLeast(MEDIUM));
////        Predicate<Topography> topography = Topography.atMost(Topography.ROUGH);
////        Predicate<NamedLandingArea> areaPredicate = areaList::contains;
////        List<Breakthrough> breakthroughs = LandingSiteTestUtil.getBreakthroughs(1);
////        List<LandingSite> filtered = complexSiteFilter.filter(resourceMap, disasterMap, topography, areaPredicate, breakthroughs);
////
////        for (LandingSite site : filtered) {
////            System.out.println("site....topography() = " + site.mapDetails().topography());
////            System.out.println("site....namedLocation() = " + site.mapDetails().namedLocation());
////            System.out.println("site....get(DisasterType.METEORS) = " + site.disasters().map().get(DisasterType.METEORS));
////            System.out.println("site....get(DisasterType.METEORS) = " + site.resources().map().get(ResourceType.METAL));
////
////
////            assertThat(site.mapDetails().topography()).isLessThanOrEqualTo(Topography.ROUGH);
////            assertThat(site.mapDetails().namedLocation()).isIn(areaList);
////            assertThat(site.breakthroughs()).containsAll(breakthroughs);
////            assertThat(site.disasters().map().get(DisasterType.METEORS)).isGreaterThanOrEqualTo(MEDIUM);
////            assertThat(site.resources().map().get(ResourceType.METAL)).isGreaterThanOrEqualTo(MEDIUM);
////
////            System.out.println();
////        }
////
////    }
////
////    @Test
////    void create() {
////
////        Map<ResourceType, Predicate<Rating>> map = new EnumMap<>(ResourceType.class);
////        map.put(ResourceType.METAL, Rating.atLeast(MEDIUM));
////        map.put(ResourceType.CONCRETE, Rating.atMost(MEDIUM));
////
////        Predicate<LandingSite> out = complexSiteFilter.reduceResourcesPredicate(map);
////
////        List<LandingSite> filtered = sites.stream().filter(out).toList();
////
////        for (LandingSite site : filtered) {
////            assertThat(site.resources().map().get(ResourceType.METAL)).isGreaterThanOrEqualTo(MEDIUM);
////            assertThat(site.resources().map().get(ResourceType.CONCRETE)).isLessThanOrEqualTo(MEDIUM);
////        }
////
////
////    }
////
////    @Test
////    void disasterFilter() {
////
////        Map<DisasterType, Predicate<Rating>> predicate = new HashMap<>();
////        predicate.put(DisasterType.DUST_DEVILS, Rating.equalTo(MEDIUM));
////        predicate.put(DisasterType.METEORS, Rating.atLeast(MEDIUM));
////
////
////        List<LandingSite> out = complexSiteFilter.disasterFilter(predicate);
////
////        for (LandingSite l : out) {
////            assertThat(l.disasters().map().get(DisasterType.DUST_DEVILS)).isEqualTo(MEDIUM);
////            assertThat(l.disasters().map().get(DisasterType.METEORS)).isGreaterThanOrEqualTo(MEDIUM);
////        }
////
////    }
////
////    @Test
////    void topographyFilter() {
////        Topography topography = LandingSiteTestUtil.getRandomEnum(Topography.class);
////
////        Predicate<Topography> predicate = p -> p.equals(topography);
////        List<LandingSite> out = complexSiteFilter.topographyFilter(predicate);
////
////        for (LandingSite l : out) {
////            assertThat(l.mapDetails().topography()).isEqualTo(topography);
////        }
////
////    }
////
////    @Test
////    void namedLandingArea() {
////        NamedLandingArea nla = LandingSiteTestUtil.getRandomEnum(NamedLandingArea.class);
////
////        Predicate<NamedLandingArea> predicate = p -> p.equals(nla);
////        List<LandingSite> out = complexSiteFilter.namedLandingArea(predicate);
////
////        for (LandingSite l : out) {
////            assertThat(l.mapDetails().namedLocation()).isEqualTo(nla);
////        }
////
////    }
//}