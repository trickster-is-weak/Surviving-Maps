package uk.co.brett.surviving.model.repo;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.text.CaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.filters.Operator;
import uk.co.brett.surviving.filters.OperatorValue;
import uk.co.brett.surviving.model.site.*;
import uk.co.brett.surviving.services.CommonFilterService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomBreakthroughMapRepo {

    private static final Logger LOGGER = LogManager.getLogger(CustomBreakthroughMapRepo.class);
    private final CommonFilterService commonFilterService;
    private final CriteriaBuilder criteriaBuilder;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CustomBreakthroughMapRepo(CommonFilterService commonFilterService, EntityManager entityManager
    ) {
        this.commonFilterService = commonFilterService;
        this.entityManager = entityManager;
        queryFactory = new JPAQueryFactory(entityManager);
        criteriaBuilder = entityManager.getCriteriaBuilder();


    }


    List<Long> getNamedLandingArea(List<NamedLandingArea> nla) {

        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
        Root<Site> site = query.from(Site.class);
        List<Predicate> ps = new ArrayList<>();
        nla.forEach(n -> ps.add(criteriaBuilder.equal(site.get("namedLocation"), n)));
        Optional<Predicate> optPredicate = ps.stream().reduce(criteriaBuilder::or);

        if (optPredicate.isEmpty()) {
            query.select(site);
        } else {
            Predicate predicate = optPredicate.get();
            query.select(site).where(predicate);
        }

        TypedQuery<Site> typedQuery = entityManager.createQuery(query);
        List<Site> sites = typedQuery.getResultList();

        return sites.stream().map(Site::getId).toList();
    }


    List<Long> getDisasters(Map<DisasterType, OperatorValue> disasterMap) {

        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
        Root<Site> site = query.from(Site.class);
        Join<Site, Disasters> disastersJoin = site.join("disasters");

        List<Predicate> ps = disasterMap.entrySet().stream().map(entry -> getPredicate(entry.getKey().getCamel(), entry.getValue(), disastersJoin)).toList();
        return runJoin(query, site, ps);

    }

    Predicate collatePredicates(List<Predicate> ps) {
        Predicate predicate;
        if (ps.size() == 1) {
            predicate = ps.get(0);
        } else {
            predicate = ps.stream().reduce(criteriaBuilder::and).orElseThrow();
        }
        return predicate;
    }

    List<Long> runJoin(CriteriaQuery<Site> query, Root<Site> site, List<Predicate> ps) {
        Predicate predicate = collatePredicates(ps);

        query.select(site).where(predicate);
        TypedQuery<Site> typedQuery = entityManager.createQuery(query);
        List<Site> sites = typedQuery.setMaxResults(4000).getResultList();

        return sites.stream().map(Site::getId).toList();
    }

    List<Long> getResources(Map<ResourceType, OperatorValue> resourceMap) {

        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
        Root<Site> site = query.from(Site.class);
        Join<Site, Resources> resourcesJoin = site.join("resources");

        List<Predicate> ps = resourceMap.entrySet().stream().map(entry -> getPredicate(CaseUtils.toCamelCase(entry.getKey().getFormatted(), false, ' '), entry.getValue(), resourcesJoin)).toList();
        return runJoin(query, site, ps);

    }

    Predicate getPredicate(String key, OperatorValue value, Join<Site, ?> itemRoot) {

        return switch (value.getOp()) {
            case AT_LEAST -> criteriaBuilder.ge(itemRoot.get(key), value.getValueInt());
            case AT_MOST -> criteriaBuilder.le(itemRoot.get(key), value.getValueInt());
            case EQUAL_TO -> criteriaBuilder.equal(itemRoot.get(key), value.getValueInt());
            case NO_PREFERENCE -> criteriaBuilder.ge(itemRoot.get(key), 0);
        };
    }

    Predicate getPredicate(String key, Operator operator, Integer value, Root<?> itemRoot) {

        return switch (operator) {
            case AT_LEAST -> criteriaBuilder.ge(itemRoot.get(key), value);
            case AT_MOST -> criteriaBuilder.le(itemRoot.get(key), value);
            case EQUAL_TO -> criteriaBuilder.equal(itemRoot.get(key), value);
            case NO_PREFERENCE -> criteriaBuilder.ge(itemRoot.get(key), 0);
        };
    }


    public List<Long> getBreakthroughIds(GameVariant variant, Breakthrough btr) {

        CriteriaQuery<BreakthroughMap> criteriaQuery = criteriaBuilder.createQuery(BreakthroughMap.class);
        Root<BreakthroughMap> itemRoot = criteriaQuery.from(BreakthroughMap.class);
        Predicate p = criteriaBuilder.equal(itemRoot.get("breakthrough"), btr);
        criteriaQuery.where(p);
        BreakthroughMap item = entityManager.createQuery(criteriaQuery).getSingleResult();

        return item.getIdList(variant);

    }

    public List<Long> getIdsOld(ComplexFilterRequest request) {
        List<List<Long>> ids = new ArrayList<>();
        request.getBreakthroughs().forEach(bt -> ids.add(getBreakthroughIds(request.getVariant(), bt)));

        ids.add(getResources(request.getResourceMap()));
        ids.add(getDisasters(request.getDisasterMap()));
        ids.add(getNamedLandingArea(request.getNamedLandingAreas()));
        ids.add(getTopography(request.getTopographies()));
        ids.add(getChallengeDifficulty(request.getMapDifficultiesOp(), request.getMapDifficultiesInt().getValue()));
        ids.add(getMapNames(request.getMapNames()));

        return commonFilterService.intersect(ids);
    }

    public List<Site> getIds(ComplexFilterRequest request) {
        CriteriaQuery<Site> query = criteriaBuilder.createQuery(Site.class);
        Root<Site> site = query.from(Site.class);
        QSite qSite = QSite.site;

        List<Predicate> ps = new ArrayList<>();
        ps.addAll(resourcePredicates(request.getResourceMap(), site));
        ps.addAll(disasterPredicates(request.getDisasterMap(), site));

        com.querydsl.core.types.Predicate predicate = collatePredicatesNew(request);
        List<Long> psbtr = getBreakthroughPredicates(request);

        List<Site> abc = queryFactory.selectFrom(qSite).where(qSite.id.in(psbtr)).where(predicate).fetch();
//        List<Long> def = runJoin(query, site, ps);

        LOGGER.info(abc.size());
//        LOGGER.info(def.size());

        return abc;
    }

    private com.querydsl.core.types.Predicate collatePredicatesNew(ComplexFilterRequest request) {
        QResources qResources = QResources.resources;
        QDisasters qDisasters = QDisasters.disasters;

        com.querydsl.core.types.Predicate resPred = null;
        Map<ResourceType, OperatorValue> resMap = request.getResourceMap();
        for (Map.Entry<ResourceType, OperatorValue> entry : resMap.entrySet()) {
            if (entry.getValue().getOp().equals(Operator.NO_PREFERENCE)) continue;
            com.querydsl.core.types.Predicate val = getResourcePredicate(entry, qResources);
            resPred = Objects.isNull(resPred) ? val : ExpressionUtils.and(resPred, val);
        }
        for (Map.Entry<DisasterType, OperatorValue> entry : request.getDisasterMap().entrySet()) {
            if (entry.getValue().getOp().equals(Operator.NO_PREFERENCE)) continue;
            com.querydsl.core.types.Predicate val = getDisasterPredicate(entry, qDisasters);
            resPred = Objects.isNull(resPred) ? val : ExpressionUtils.and(resPred, val);
        }

        resPred = Objects.isNull(resPred) ? ExpressionUtils.allOf(qResources.metal.gt(0)) : resPred;

        return resPred;
    }

    private com.querydsl.core.types.Predicate getDisasterPredicate(Map.Entry<DisasterType, OperatorValue> entry, QDisasters qDisasters) {
        return switch (entry.getKey()) {
            case DUST_DEVILS -> qDisasters.dustDevils.in(entry.getValue().getRange());
            case DUST_STORMS -> qDisasters.dustStorms.in(entry.getValue().getRange());
            case METEORS -> qDisasters.meteors.in(entry.getValue().getRange());
            case COLD_WAVES -> qDisasters.coldWaves.in(entry.getValue().getRange());
        };
    }

    private com.querydsl.core.types.Predicate getResourcePredicate(Map.Entry<ResourceType, OperatorValue> entry, QResources qResources) {
        return switch (entry.getKey()) {
            case METAL -> qResources.metal.in(entry.getValue().getRange());
            case RARE_METAL -> qResources.rareMetal.in(entry.getValue().getRange());
            case WATER -> qResources.water.in(entry.getValue().getRange());
            case CONCRETE -> qResources.concrete.in(entry.getValue().getRange());
        };
    }

    public List<Long> getBreakthroughPredicates(ComplexFilterRequest request) {

        QBreakthroughMap breakthroughMap = QBreakthroughMap.breakthroughMap;
        QMapDetails mapDetails = QMapDetails.mapDetails;

        boolean run = false;
        List<BreakthroughMap> resv = new ArrayList<>();
        if (!request.getBreakthroughs().isEmpty()) {
            run = true;
            JPAQuery<BreakthroughMap> asd = queryFactory.select(breakthroughMap).from(breakthroughMap).where(breakthroughMap.breakthrough.in(request.getBreakthroughs()));
            resv = asd.fetch();
        }


        JPAQuery<MapDetails> md = queryFactory.selectFrom(mapDetails);

        if (!request.getTopographies().isEmpty()) {
            run = true;
            md.where(mapDetails.topography.in(request.getTopographies()));
        }
        if (!request.getMapNames().isEmpty()) {
            run = true;
            md.where(mapDetails.mapName.in(request.getMapNames()));
        }
        if (!request.getNamedLandingAreas().isEmpty()) {
            run = true;
            md.where(mapDetails.namedLocation.in(request.getNamedLandingAreas()));
        }
        if (!request.getMapDifficultiesOp().equals(Operator.NO_PREFERENCE)) {
            switch (request.getMapDifficultiesOp()) {
                case AT_LEAST ->
                        md.where(mapDetails.challengeDifficulty.goe(request.getMapDifficultiesInt().getValue()));
                case AT_MOST ->
                        md.where(mapDetails.challengeDifficulty.loe(request.getMapDifficultiesInt().getValue()));
                case EQUAL_TO ->
                        md.where(mapDetails.challengeDifficulty.eq(request.getMapDifficultiesInt().getValue()));
            }
        }
        List<List<Long>> wer = (resv.stream().map(a -> a.getIdList(request.getVariant())).collect(Collectors.toList()));

        if (run) {
            List<Long> maps = md.select(mapDetails.site.id).fetch();
            wer.add(maps);


            List<Long> intersect = commonFilterService.intersect(wer);
            System.out.println(intersect.size());
            return intersect;
        } else return new ArrayList<>();
    }

    private ListPath<Long, NumberPath<Long>> getTable(QBreakthroughMap q, GameVariant variant) {
        return switch (variant) {
            case STANDARD -> q.standard;
            case GREEN_PLANET -> q.greenPlanet;
            case BELOW_BEYOND -> q.belowBeyond;
            case BEYOND_GREEN -> q.belowBeyondGreenPlanet;
            case TITO_GREEN_PLANET -> q.titoGreenPlanet;
            case EVANS_GREEN_PLANET -> q.evansGreenPlanet;
        };
    }


    private List<Predicate> resourcePredicates(Map<ResourceType, OperatorValue> resourceMap, Root<Site> site) {
        Join<Site, Resources> resourcesJoin = site.join("resources");
        return resourceMap.entrySet().stream().map(entry -> getPredicate(CaseUtils.toCamelCase(entry.getKey().getFormatted(), false, ' '), entry.getValue(), resourcesJoin)).toList();
    }

    private List<Predicate> disasterPredicates(Map<DisasterType, OperatorValue> disasterMap, Root<Site> site) {
        Join<Site, Disasters> disasterJoin = site.join("disasters");
        return disasterMap.entrySet().stream().map(entry -> getPredicate(CaseUtils.toCamelCase(entry.getKey().getFormatted(), false, ' '), entry.getValue(), disasterJoin)).toList();
    }


    private List<Long> getMapNames(List<MapName> mapNames) {
        CriteriaQuery<MapDetails> query = criteriaBuilder.createQuery(MapDetails.class);
        Root<MapDetails> mapDetails = query.from(MapDetails.class);
        List<Predicate> ps = new ArrayList<>();
        mapNames.forEach(n -> ps.add(criteriaBuilder.equal(mapDetails.get("mapName"), n)));
        Optional<Predicate> optPredicate = ps.stream().reduce(criteriaBuilder::or);

        if (optPredicate.isEmpty()) {
            query.select(mapDetails);
        } else {
            Predicate predicate = optPredicate.get();
            query.select(mapDetails).where(predicate);
        }

        TypedQuery<MapDetails> typedQuery = entityManager.createQuery(query);
        List<MapDetails> sites = typedQuery.getResultList();

        return sites.stream().map(i -> i.getSite().getId()).toList();

    }

    List<Long> getTopography(List<Topography> topographies) {

        CriteriaQuery<MapDetails> query = criteriaBuilder.createQuery(MapDetails.class);
        Root<MapDetails> mapDetails = query.from(MapDetails.class);
        List<Predicate> ps = new ArrayList<>();
        topographies.forEach(n -> ps.add(criteriaBuilder.equal(mapDetails.get("topography"), n)));
        Optional<Predicate> optPredicate = ps.stream().reduce(criteriaBuilder::or);


        if (optPredicate.isEmpty()) {
            query.select(mapDetails);
        } else {
            Predicate predicate = optPredicate.get();
            query.select(mapDetails).where(predicate);
        }

        TypedQuery<MapDetails> typedQuery = entityManager.createQuery(query);
        List<MapDetails> sites = typedQuery.getResultList();

        return sites.stream().map(i -> i.getSite().getId()).toList();

    }

    List<Long> getChallengeDifficulty(Operator op, Integer integer) {

        CriteriaQuery<MapDetails> query = criteriaBuilder.createQuery(MapDetails.class);
        Root<MapDetails> mapDetails = query.from(MapDetails.class);

        Predicate predicate = getPredicate("challengeDifficulty", op, integer, mapDetails);

        query.select(mapDetails).where(predicate);
        TypedQuery<MapDetails> typedQuery = entityManager.createQuery(query);
        List<MapDetails> sites = typedQuery.getResultList();

        return sites.stream().map(i -> i.getSite().getId()).toList();

    }

}
