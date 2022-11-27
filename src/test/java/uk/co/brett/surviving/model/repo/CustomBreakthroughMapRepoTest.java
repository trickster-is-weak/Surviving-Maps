package uk.co.brett.surviving.model.repo;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.filters.*;
import uk.co.brett.surviving.io.IngestChecker;
import uk.co.brett.surviving.model.site.*;
import uk.co.brett.surviving.services.CommonFilterService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.co.brett.surviving.LandingSiteTestUtil.getAssortedEnums;
import static uk.co.brett.surviving.LandingSiteTestUtil.getRandomEnum;
import static uk.co.brett.surviving.enums.Breakthrough.AUTONOMOUS_HUBS;
import static uk.co.brett.surviving.enums.Breakthrough.EXTRACTOR_AI;
import static uk.co.brett.surviving.enums.DisasterType.COLD_WAVES;
import static uk.co.brett.surviving.enums.DisasterType.DUST_STORMS;
import static uk.co.brett.surviving.enums.GameVariant.STANDARD;
import static uk.co.brett.surviving.enums.ResourceType.CONCRETE;
import static uk.co.brett.surviving.enums.ResourceType.METAL;
import static uk.co.brett.surviving.filters.ComplexPrevalence.HIGH;
import static uk.co.brett.surviving.filters.ComplexPrevalence.MED;
import static uk.co.brett.surviving.filters.Operator.*;


@SpringBootTest
class CustomBreakthroughMapRepoTest {

    private final QSite qSite = QSite.site;
    private final QBreakthroughMap breakthroughMap = QBreakthroughMap.breakthroughMap;
    private final QMapDetails mapDetails = QMapDetails.mapDetails;
    private final QDisasters qDisasters = QDisasters.disasters;
    private final QResources qResources = QResources.resources;
    @Autowired
    IngestChecker checker;

    @Autowired
    CustomBreakthroughMapRepo repo;
    @Autowired
    CommonFilterService commonFilterService;
    @Autowired
    SiteRepo siteRepo;
    CriteriaBuilder criteriaBuilder;
    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;


    @BeforeEach
    void setUp() {
        checker.checkIntegrity();
        criteriaBuilder = entityManager.getCriteriaBuilder();
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getIds() {
    }

    @Test
    void getMapPredicate() {
    }

    @Test
    void getBreakthroughPredicate() {

        ComplexFilterRequest request = new ComplexFilterRequest();
        List<Breakthrough> expected = List.of(AUTONOMOUS_HUBS, EXTRACTOR_AI);
        request.setBreakthroughs(List.of(AUTONOMOUS_HUBS, EXTRACTOR_AI));
        Predicate res = repo.getBreakthroughPredicate(request);

        List<Site> sites = queryFactory.selectFrom(qSite).where(res).fetch();
        List<List<Breakthrough>> btrs = sites.stream().map(s -> s.getBreakthroughs(STANDARD)).toList();

        btrs.forEach(btr -> assertThat(btr).containsAll(expected));

    }

    @Test
    void getBreakthroughPredicateEmpty() {

        ComplexFilterRequest request = new ComplexFilterRequest();
        Predicate res = repo.getBreakthroughPredicate(request);

        List<Site> sites = queryFactory.selectFrom(qSite).where(res).fetch();
        List<Site> expected = queryFactory.selectFrom(qSite).fetch();

        assertThat(sites.size()).isEqualTo(expected.size());
        assertThat(sites).containsExactlyInAnyOrderElementsOf(expected);

    }

    @Test
    void getResourceDisasterPredicate() {

        List<ResourceType> resourceTypes = List.of(METAL, CONCRETE);
        List<DisasterType> disasterTypes = List.of(COLD_WAVES, DUST_STORMS);

        Map<DisasterType, OperatorValue> disastersMap = new EnumMap<>(DisasterType.class);
        Map<ResourceType, OperatorValue> resourcesMap = new EnumMap<>(ResourceType.class);

        resourceTypes.forEach(r -> resourcesMap.put(r, new OperatorValue(AT_LEAST, MED)));
        disasterTypes.forEach(d -> disastersMap.put(d, new OperatorValue(AT_MOST, HIGH)));

        ComplexFilterRequest request = new ComplexFilterRequest();
        request.setResources(resourcesMap);
        request.setDisasters(disastersMap);
        Predicate pred = repo.getResourceDisasterPredicate(request);

        List<Site> results = queryFactory.selectFrom(qSite).where(pred).fetch();

        List<Resources> resources = results.stream().map(Site::getResources).distinct().toList();
        List<Disasters> disasters = results.stream().map(Site::getDisasters).distinct().toList();

        assertThat(disasters).isNotEmpty().allMatch(p -> p.getColdWaves() <= 3 && p.getDustStorms() <= 3);
        assertThat(resources).isNotEmpty().allMatch(p -> p.getMetal() >= 2 && p.getConcrete() >= 2);


    }

    private Predicate createDisasterPredicate(DisasterType disasterType) {
        OperatorValue opVal = getOperatorValue();
        Map.Entry<DisasterType, OperatorValue> vars = Map.entry(disasterType, opVal);
        return repo.getDisasterPredicate(vars);
    }

    private Predicate createResourcePredicate(ResourceType resourceType) {
        OperatorValue opVal = getOperatorValue();
        Map.Entry<ResourceType, OperatorValue> vars = Map.entry(resourceType, opVal);
        return repo.getResourcePredicate(vars);
    }

    private OperatorValue getOperatorValue() {
        Operator operator = getRandomEnum(Operator.class);
        ComplexPrevalence value = getRandomEnum(ComplexPrevalence.class);
        return new OperatorValue(operator, value);
    }

    @Test
    void getDisasterPredicate() {
        Map<DisasterType, OperatorValue> map = new HashMap<>();

        int i = 0;
        for (DisasterType disasterType : DisasterType.values()) {
            Operator operator = Operator.values()[i++];
            ComplexPrevalence value = getRandomEnum(ComplexPrevalence.class);
            OperatorValue opVal = new OperatorValue(operator, value);
            map.put(disasterType, opVal);
        }

        Map<DisasterType, Predicate> predicateMap = new HashMap<>();
        for (Map.Entry<DisasterType, OperatorValue> entry : map.entrySet()) {
            predicateMap.put(entry.getKey(), repo.getDisasterPredicate(entry));
        }

        for (DisasterType disasterType : DisasterType.values()) {
            List<Disasters> result = queryFactory.selectFrom(qDisasters).where(predicateMap.get(disasterType)).fetch();
            List<Integer> test = result.stream().map(r -> r.get(disasterType)).toList();
            assertThat(test).isNotEmpty().allMatch(map.get(disasterType).getIntPredicate());
        }
    }

    @Test
    void getResourcePredicate() {

        Map<ResourceType, OperatorValue> map = new HashMap<>();

        int i = 0;
        for (ResourceType resourceType : ResourceType.values()) {
            Operator operator = Operator.values()[i++];
            ComplexPrevalence value = getRandomEnum(ComplexPrevalence.class);
            OperatorValue opVal = new OperatorValue(operator, value);
            map.put(resourceType, opVal);
        }

        Map<ResourceType, Predicate> predicateMap = new HashMap<>();
        for (Map.Entry<ResourceType, OperatorValue> entry : map.entrySet()) {
            predicateMap.put(entry.getKey(), repo.getResourcePredicate(entry));
        }

        for (ResourceType resourceType : ResourceType.values()) {
            List<Resources> result = queryFactory.selectFrom(qResources).where(predicateMap.get(resourceType)).fetch();
            List<Integer> test = result.stream().map(r -> r.get(resourceType)).toList();
            assertThat(test).isNotEmpty().allMatch(map.get(resourceType).getIntPredicate());
        }

    }

    @Test
    void getNamedLandingArea() {
        int all = queryFactory.selectFrom(mapDetails).fetch().size();

        Map<NamedLandingArea, Predicate> predicateMap = new HashMap<>();
        Map<NamedLandingArea, List<MapDetails>> detailsMap = new HashMap<>();
        Map<NamedLandingArea, Integer> countMap = new HashMap<>();

        for (NamedLandingArea t : NamedLandingArea.values()) {
            predicateMap.put(t, repo.getNamedLandingArea(List.of(t)));
            detailsMap.put(t, queryFactory.selectFrom(mapDetails).where(predicateMap.get(t)).fetch());
            countMap.put(t, detailsMap.get(t).size());
        }

        // Test single requests
        int sum = countMap.values().stream().mapToInt(i -> i).sum();
        assertThat(sum).isEqualTo(all);

        for (NamedLandingArea t : NamedLandingArea.values()) {
            if (countMap.get(t) > 0)
                assertThat(detailsMap.get(t).stream().map(MapDetails::getNamedLocation).toList()).containsOnly(t);
        }

        // Test random request
        List<NamedLandingArea> random = getAssortedEnums(NamedLandingArea.class, 2);
        List<MapDetails> expected = detailsMap.entrySet().stream()
                .filter(e -> random.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream).toList();

        Predicate pred = repo.getNamedLandingArea(random);
        List<MapDetails> list = queryFactory.selectFrom(mapDetails).where(pred).fetch();

        assertThat(list).containsExactlyInAnyOrderElementsOf(expected);

        // Test empty request
        Predicate empty = repo.getNamedLandingArea(new ArrayList<>());
        List<MapDetails> fullList = queryFactory.selectFrom(mapDetails).where(empty).fetch();
        expected = detailsMap.values().stream().flatMap(Collection::stream).toList();

        assertThat(fullList).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void getTopography() {

        int all = queryFactory.selectFrom(mapDetails).fetch().size();

        Map<Topography, Predicate> predicateMap = new HashMap<>();
        Map<Topography, List<MapDetails>> detailsMap = new HashMap<>();
        Map<Topography, Integer> countMap = new HashMap<>();

        for (Topography t : Topography.values()) {
            predicateMap.put(t, repo.getTopography(List.of(t)));
            detailsMap.put(t, queryFactory.selectFrom(mapDetails).where(predicateMap.get(t)).fetch());
            countMap.put(t, detailsMap.get(t).size());
        }

        // Test single requests
        int sum = countMap.values().stream().mapToInt(i -> i).sum();
        assertThat(sum).isEqualTo(all);

        for (Topography t : Topography.values()) {
            if (countMap.get(t) > 0)
                assertThat(detailsMap.get(t).stream().map(MapDetails::getTopography).toList()).containsOnly(t);
        }

        // Test random request
        List<Topography> random = getAssortedEnums(Topography.class, 2);
        List<MapDetails> expected = detailsMap.entrySet().stream()
                .filter(e -> random.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream).toList();

        Predicate pred = repo.getTopography(random);
        List<MapDetails> list = queryFactory.selectFrom(mapDetails).where(pred).fetch();

        assertThat(list).containsExactlyInAnyOrderElementsOf(expected);

        // Test empty request
        Predicate empty = repo.getTopography(new ArrayList<>());
        List<MapDetails> fullList = queryFactory.selectFrom(mapDetails).where(empty).fetch();
        expected = detailsMap.values().stream().flatMap(Collection::stream).toList();

        assertThat(fullList).containsExactlyInAnyOrderElementsOf(expected);

    }

    @Test
    void getMapName() {

        int all = queryFactory.selectFrom(mapDetails).fetch().size();

        Map<MapName, Predicate> predicateMap = new HashMap<>();
        Map<MapName, List<MapDetails>> detailsMap = new HashMap<>();
        Map<MapName, Integer> countMap = new HashMap<>();
        for (MapName t : MapName.values()) {
            predicateMap.put(t, repo.getMapName(List.of(t)));
            detailsMap.put(t, queryFactory.selectFrom(mapDetails).where(predicateMap.get(t)).fetch());
            countMap.put(t, detailsMap.get(t).size());
        }

        // Test single requests
        int sum = countMap.values().stream().mapToInt(i -> i).sum();
        assertThat(sum).isEqualTo(all);

        for (MapName t : MapName.values()) {
            if (countMap.get(t) > 0)
                assertThat(detailsMap.get(t).stream().map(MapDetails::getMapName).toList()).containsOnly(t);
        }

        // Test random request
        List<MapName> random = getAssortedEnums(MapName.class, 10);
        List<MapDetails> expected = detailsMap.entrySet().stream()
                .filter(e -> random.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream).toList();

        Predicate pred = repo.getMapName(random);
        List<MapDetails> list = queryFactory.selectFrom(mapDetails).where(pred).fetch();

        assertThat(list).containsExactlyInAnyOrderElementsOf(expected);

        // Test empty request
        Predicate empty = repo.getMapName(new ArrayList<>());
        List<MapDetails> fullList = queryFactory.selectFrom(mapDetails).where(empty).fetch();
        expected = detailsMap.values().stream().flatMap(Collection::stream).toList();

        assertThat(fullList).containsExactlyInAnyOrderElementsOf(expected);
    }


    @Test
    void getChallengeDifficulty() {

        int all = queryFactory.selectFrom(mapDetails).fetch().size();

        Map<Difficulties, Predicate> predicateMap = new HashMap<>();
        Map<Difficulties, List<MapDetails>> detailsMap = new HashMap<>();
        Map<Difficulties, Integer> countMap = new HashMap<>();
        for (Difficulties t : Difficulties.values()) {
            predicateMap.put(t, repo.getChallengeDifficulty(EQUAL_TO, t));
            detailsMap.put(t, queryFactory.selectFrom(mapDetails).where(predicateMap.get(t)).fetch());
            countMap.put(t, detailsMap.get(t).size());
        }

        // Test single requests
        int sum = countMap.values().stream().mapToInt(i -> i).sum();
        assertThat(sum).isEqualTo(all);

        for (Difficulties t : Difficulties.values()) {
            if (countMap.get(t) > 0)
                assertThat(detailsMap.get(t).stream().map(MapDetails::getChallengeDifficulty).toList()).containsOnly(t.getValue());
        }

        // Test atMost request
        Predicate atMost = repo.getChallengeDifficulty(AT_MOST, Difficulties.D140);
        List<MapDetails> expected = detailsMap.entrySet().stream()
                .filter(e -> e.getKey().getValue() <= Difficulties.D140.getValue())
                .map(Map.Entry::getValue).flatMap(Collection::stream).toList();
        List<MapDetails> atMostList = queryFactory.selectFrom(mapDetails).where(atMost).fetch();
        assertThat(atMostList).containsExactlyInAnyOrderElementsOf(expected);

        // Test atLeast request
        Predicate atLeast = repo.getChallengeDifficulty(AT_LEAST, Difficulties.D140);
        expected = detailsMap.entrySet().stream()
                .filter(e -> e.getKey().getValue() >= Difficulties.D140.getValue())
                .map(Map.Entry::getValue).flatMap(Collection::stream).toList();
        List<MapDetails> atLeastList = queryFactory.selectFrom(mapDetails).where(atLeast).fetch();
        assertThat(atLeastList).containsExactlyInAnyOrderElementsOf(expected);

        // Test empty request
        Predicate empty = repo.getChallengeDifficulty(NO_PREFERENCE, Difficulties.D100);
        List<MapDetails> fullList = queryFactory.selectFrom(mapDetails).where(empty).fetch();
        expected = detailsMap.values().stream().flatMap(Collection::stream).toList();

        assertThat(fullList).containsExactlyInAnyOrderElementsOf(expected);

    }


//    @Test
//    void getIds() {
//        Map<ResourceType, OperatorValue> resourceMap = new EnumMap<>(ResourceType.class);
//        resourceMap.put(METAL, equalTo(HIGH));
//        resourceMap.put(WATER, atLeast(MED));
//
//        Map<DisasterType, OperatorValue> disasterMap = new EnumMap<>(DisasterType.class);
//        disasterMap.put(METEORS, atLeast(HIGH));
//        disasterMap.put(DUST_STORMS, atMost(MED));
//
//        ComplexFilterRequest request = new ComplexFilterRequest();
//        request.setVariant(GameVariant.STANDARD);
//        request.setBreakthroughs(List.of(EXTRACTOR_AI));
//        request.setDisasters(disasterMap);
//        request.setResources(resourceMap);
//
////        List<Long> list = repo.getIds(request);
//
//        GameVariant variant = GameVariant.STANDARD;
//        List<Breakthrough> btr = List.of(EXTRACTOR_AI);
//
//
//        List<NamedLandingArea> nla = List.of(NamedLandingArea.OPPORTUNITY_LANDING_AREA, NamedLandingArea.COLONY_SITE, NamedLandingArea.TERRA_SABAEA);
//
//        List<Long> list = repo.getIds(variant, btr, resourceMap, disasterMap, nla);
//
//        List<Site> sites = siteRepo.findAllById(list);
//        for (Site s : sites) {
//            Resources r = s.getResources();
//            Disasters d = s.getDisasters();
//            List<Breakthrough> b = s.getBreakthroughs(variant);
//
//            resourceMap.forEach((key, value) -> assertThat(r.get(key)).matches(value.getIntPredicate()));
//            disasterMap.forEach((key, value) -> assertThat(d.get(key)).matches(value.getIntPredicate()));
//            btr.forEach(bt -> assertThat(b).contains(bt));
//            assertThat(s.getNamedLocation()).isIn(nla);

//        }

//    }


//    @Test
//    void testNewJun() {
//        GameVariant a = GameVariant.STANDARD;
//        QBreakthroughMap breakthroughMap = QBreakthroughMap.breakthroughMap;
//        List<Breakthrough> c = List.of(EXTRACTOR_AI, ADVANCED_DRONE_DRIVE);
////        List<Breakthrough> c = List.of();
//
//        com.querydsl.core.types.Predicate preddd =   repo.getBreakthroughPredicate(a, breakthroughMap, c);
//
//        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//        List<Long> results = queryFactory.selectFrom(QSite.site).select(QSite.site.id).where(preddd).fetch();
//
//        System.out.println(results);
//
//        //      repo.getBreakthroughPredicate(a, b, c);
//    }


//    @Test
//    void getNamedLandingArea() {
//        List<NamedLandingArea> nlas = LandingSiteTestUtil.getAssortedEnums(NamedLandingArea.class, 5);
//        List<Long> ids = repo.getNamedLandingArea(nlas);
//        List<Site> sites = siteRepo.findAllById(ids);
//        sites.stream().map(Site::getNamedLocation).forEach(nla -> assertThat(nla).isIn(nlas));
//    }

//    @Test
//    void getDisasters() {
//        Map<DisasterType, OperatorValue> map = new EnumMap<>(DisasterType.class);
//        map.put(DUST_STORMS, new OperatorValue(AT_LEAST, MED));
//        map.put(METEORS, new OperatorValue(EQUAL_TO, HIGH));
//        List<Long> out = repo.getDisasters(map);
//        List<Site> sites = siteRepo.findAllById(out);
//        sites.stream().map(Site::getDisasters).forEach(d -> {
//            for (Map.Entry<DisasterType, OperatorValue> p : map.entrySet()) {
//                assertThat(d.get(p.getKey())).matches(p.getValue().getIntPredicate());
//            }
//        });
//    }

//
//    @Test
//    void getResources() {
//        Map<ResourceType, OperatorValue> map = new EnumMap<>(ResourceType.class);
//        map.put(METAL, new OperatorValue(AT_LEAST, MED));
//        map.put(WATER, new OperatorValue(EQUAL_TO, HIGH));
//        List<Long> out = repo.getResources(map);
//        List<Site> sites = siteRepo.findAllById(out);
//        sites.stream().map(Site::getResources).forEach(d -> {
//            for (Map.Entry<ResourceType, OperatorValue> p : map.entrySet()) {
//                assertThat(d.get(p.getKey())).matches(p.getValue().getIntPredicate());
//            }
//        });
//    }

//    @Test
//    void getPredicate() {
//
//        CriteriaQuery<Disasters> query = criteriaBuilder.createQuery(Disasters.class);
//        Root<Disasters> site = query.from(Disasters.class);
//        Predicate predMT = repo.getPredicate("meteors", AT_LEAST, 2, site);
//
//        List<Disasters> unfiltered = entityManager.createQuery(query).getResultList();
//        query.where(predMT);
//
//        List<Disasters> res = entityManager.createQuery(query).getResultList();
//
//        assertThat(unfiltered).hasSizeGreaterThanOrEqualTo(res.size());
//        res.stream().map(Disasters::getMeteors)
//                .forEach(d -> assertThat(d).isGreaterThanOrEqualTo(2));
//
//
//    }


//    @Test
//    void runJoin() {
//        CriteriaQuery<Disasters> query = criteriaBuilder.createQuery(Disasters.class);
//        Root<Disasters> site = query.from(Disasters.class);
//        Predicate predMT = repo.getPredicate("meteors", AT_LEAST, 2, site);
//        Predicate predCW = repo.getPredicate("coldWaves", AT_MOST, 3, site);
//        Predicate predDD = repo.getPredicate("dustDevils", EQUAL_TO, 4, site);
//        Predicate predDS = repo.getPredicate("dustStorms", AT_LEAST, 0, site);
//
//    }


//    @Test
//    public void newtest() {
//        List<Breakthrough> list = List.of(EXTRACTOR_AI, AUTONOMOUS_HUBS);
////        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
////        Root<Site> root = query.from(Site.class);
//        ComplexFilterRequest request = new ComplexFilterRequest();
//        request.setBreakthroughs(list);
//        request.setTopographies(List.of(Topography.RELATIVELY_FLAT));
//        request.setMapDifficultiesInt(Difficulties.D140);
//        request.setMapDifficultiesOp(AT_MOST);
//        request.setNamedLandingAreas(List.of(TERRA_SABAEA, COLONY_SITE, UNNAMED));
//        request.setMapNames(List.of(CLIFFS_2, TERRACE_MIX_11, TERRACE_MIX_14, TERRACE_MIX_15, TERRACE_MIX_16));
//        request.setWater(new OperatorValue(AT_LEAST, MED));
//        request.setConcrete(new OperatorValue(AT_LEAST, HIGH));
//        request.setMeteors(new OperatorValue(AT_LEAST, MED));
//        request.setColdWaves(new OperatorValue(AT_MOST, MED));
//        repo.getIds(request);
//    }

}