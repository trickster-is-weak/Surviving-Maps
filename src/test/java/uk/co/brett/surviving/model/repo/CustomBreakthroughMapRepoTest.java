package uk.co.brett.surviving.model.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.filters.Difficulties;
import uk.co.brett.surviving.filters.OperatorValue;
import uk.co.brett.surviving.model.site.Disasters;
import uk.co.brett.surviving.model.site.Site;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.co.brett.surviving.enums.Breakthrough.AUTONOMOUS_HUBS;
import static uk.co.brett.surviving.enums.Breakthrough.EXTRACTOR_AI;
import static uk.co.brett.surviving.enums.DisasterType.DUST_STORMS;
import static uk.co.brett.surviving.enums.DisasterType.METEORS;
import static uk.co.brett.surviving.enums.MapName.*;
import static uk.co.brett.surviving.enums.NamedLandingArea.*;
import static uk.co.brett.surviving.enums.ResourceType.METAL;
import static uk.co.brett.surviving.enums.ResourceType.WATER;
import static uk.co.brett.surviving.filters.ComplexPrevalence.*;
import static uk.co.brett.surviving.filters.Operator.*;

@SpringBootTest
class CustomBreakthroughMapRepoTest {

    @Autowired
    CustomBreakthroughMapRepo repo;

    @Autowired
    SiteRepo siteRepo;

    CriteriaBuilder criteriaBuilder;
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void post() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }


    @Test
    void getIds() {
        Map<ResourceType, OperatorValue> resourceMap = new EnumMap<>(ResourceType.class);
        resourceMap.put(METAL, equalTo(HIGH));
        resourceMap.put(WATER, atLeast(MED));

        Map<DisasterType, OperatorValue> disasterMap = new EnumMap<>(DisasterType.class);
        disasterMap.put(METEORS, atLeast(HIGH));
        disasterMap.put(DUST_STORMS, atMost(MED));

        ComplexFilterRequest request = new ComplexFilterRequest();
        request.setVariant(GameVariant.STANDARD);
        request.setBreakthroughs(List.of(EXTRACTOR_AI));
        request.setDisasters(disasterMap);
        request.setResources(resourceMap);

//        List<Long> list = repo.getIds(request);

        GameVariant variant = GameVariant.STANDARD;
        List<Breakthrough> btr = List.of(EXTRACTOR_AI);
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

    }


    @Test
    void getBreakthroughIds() {

        GameVariant variant = GameVariant.STANDARD;
        Breakthrough btr = EXTRACTOR_AI;
        List<Long> out = repo.getBreakthroughIds(variant, btr);

        List<Site> sites = siteRepo.findAllById(out);
        sites.stream().map(site -> site.getBreakthroughs(variant)).forEach(btrs -> assertThat(btrs).contains(btr));

    }

    @Test
    void getNamedLandingArea() {
        List<NamedLandingArea> nlas = LandingSiteTestUtil.getAssortedEnums(NamedLandingArea.class, 5);
        List<Long> ids = repo.getNamedLandingArea(nlas);
        List<Site> sites = siteRepo.findAllById(ids);
        sites.stream().map(Site::getNamedLocation).forEach(nla -> assertThat(nla).isIn(nlas));
    }

    @Test
    void getDisasters() {
        Map<DisasterType, OperatorValue> map = new EnumMap<>(DisasterType.class);
        map.put(DUST_STORMS, new OperatorValue(AT_LEAST, MED));
        map.put(METEORS, new OperatorValue(EQUAL_TO, HIGH));
        List<Long> out = repo.getDisasters(map);
        List<Site> sites = siteRepo.findAllById(out);
        sites.stream().map(Site::getDisasters).forEach(d -> {
            for (Map.Entry<DisasterType, OperatorValue> p : map.entrySet()) {
                assertThat(d.get(p.getKey())).matches(p.getValue().getIntPredicate());
            }
        });
    }


    @Test
    void getResources() {
        Map<ResourceType, OperatorValue> map = new EnumMap<>(ResourceType.class);
        map.put(METAL, new OperatorValue(AT_LEAST, MED));
        map.put(WATER, new OperatorValue(EQUAL_TO, HIGH));
        List<Long> out = repo.getResources(map);
        List<Site> sites = siteRepo.findAllById(out);
        sites.stream().map(Site::getResources).forEach(d -> {
            for (Map.Entry<ResourceType, OperatorValue> p : map.entrySet()) {
                assertThat(d.get(p.getKey())).matches(p.getValue().getIntPredicate());
            }
        });
    }

    @Test
    void getPredicate() {

        CriteriaQuery<Disasters> query = criteriaBuilder.createQuery(Disasters.class);
        Root<Disasters> site = query.from(Disasters.class);
        Predicate predMT = repo.getPredicate("meteors", AT_LEAST, 2, site);

        List<Disasters> unfiltered = entityManager.createQuery(query).getResultList();
        query.where(predMT);

        List<Disasters> res = entityManager.createQuery(query).getResultList();

        assertThat(unfiltered).hasSizeGreaterThanOrEqualTo(res.size());
        res.stream().map(Disasters::getMeteors)
                .forEach(d -> assertThat(d).isGreaterThanOrEqualTo(2));


    }


    @Test
    void getTopography() {
    }

    @Test
    void getChallengeDifficulty() {
    }

    @Test
    void runJoin() {
        CriteriaQuery<Disasters> query = criteriaBuilder.createQuery(Disasters.class);
        Root<Disasters> site = query.from(Disasters.class);
        Predicate predMT = repo.getPredicate("meteors", AT_LEAST, 2, site);
        Predicate predCW = repo.getPredicate("coldWaves", AT_MOST, 3, site);
        Predicate predDD = repo.getPredicate("dustDevils", EQUAL_TO, 4, site);
        Predicate predDS = repo.getPredicate("dustStorms", AT_LEAST, 0, site);

    }

    @Test
    public void blazetest() {
        List<Breakthrough> list = List.of(EXTRACTOR_AI, AUTONOMOUS_HUBS);
//        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
//        Root<Site> root = query.from(Site.class);
        ComplexFilterRequest request = new ComplexFilterRequest();
//        request.setBreakthroughs(list);
        request.setTopographies(List.of(Topography.RELATIVELY_FLAT));
        request.setMapDifficultiesInt(Difficulties.D140);
        request.setMapDifficultiesOp(AT_MOST);
        request.setNamedLandingAreas(List.of(TERRA_SABAEA, COLONY_SITE, UNNAMED));
        request.setMapNames(List.of(
                CLIFFS_2,
                TERRACE_MIX_11,
                TERRACE_MIX_14,
                TERRACE_MIX_15,
                TERRACE_MIX_16
        ));
        repo.getBreakthroughPredicates(request);
    }

    @Test
    public void newtest() {
        List<Breakthrough> list = List.of(EXTRACTOR_AI, AUTONOMOUS_HUBS);
//        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
//        Root<Site> root = query.from(Site.class);
        ComplexFilterRequest request = new ComplexFilterRequest();
        request.setBreakthroughs(list);
        request.setTopographies(List.of(Topography.RELATIVELY_FLAT));
        request.setMapDifficultiesInt(Difficulties.D140);
        request.setMapDifficultiesOp(AT_MOST);
        request.setNamedLandingAreas(List.of(TERRA_SABAEA, COLONY_SITE, UNNAMED));
        request.setMapNames(List.of(CLIFFS_2, TERRACE_MIX_11, TERRACE_MIX_14, TERRACE_MIX_15, TERRACE_MIX_16));
        request.setWater(new OperatorValue(AT_LEAST, MED));
        request.setConcrete(new OperatorValue(AT_LEAST, HIGH));
        request.setMeteors(new OperatorValue(AT_LEAST, MED));
        request.setColdWaves(new OperatorValue(AT_MOST, MED));
        repo.getIds(request);
    }

}