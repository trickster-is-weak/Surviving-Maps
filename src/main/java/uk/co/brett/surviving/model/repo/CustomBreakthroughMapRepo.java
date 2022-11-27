package uk.co.brett.surviving.model.repo;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.filters.Difficulties;
import uk.co.brett.surviving.filters.Operator;
import uk.co.brett.surviving.filters.OperatorValue;
import uk.co.brett.surviving.model.site.*;
import uk.co.brett.surviving.services.CommonFilterService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Service
public class CustomBreakthroughMapRepo {

    private static final Logger LOGGER = LogManager.getLogger(CustomBreakthroughMapRepo.class);
    private final CommonFilterService commonFilterService;

    private final JPAQueryFactory queryFactory;

    private static final QSite qSite = QSite.site;

    private static final QBreakthroughMap breakthroughMap = QBreakthroughMap.breakthroughMap;

    private static final QMapDetails mapDetails = QMapDetails.mapDetails;

    private static final QDisasters qDisasters = QDisasters.disasters;
    private static final QResources qResources = QResources.resources;

    @Autowired
    public CustomBreakthroughMapRepo(CommonFilterService commonFilterService, EntityManager entityManager) {
        this.commonFilterService = commonFilterService;
        queryFactory = new JPAQueryFactory(entityManager);
    }


    public List<Site> getIds(ComplexFilterRequest request) {

        Predicate predicate = getResourceDisasterPredicate(request);
        Predicate btrPredicate = getBreakthroughPredicate(request);
        Predicate mapPredicate = getMapPredicate(request);

        List<Site> siteList = queryFactory.selectFrom(qSite)
                .where(predicate)
                .where(btrPredicate)
                .where(mapPredicate)
                .limit(100)
                .fetch();

        LOGGER.info(siteList.size());

        return siteList;
    }

    Predicate getMapPredicate(ComplexFilterRequest request) {

        Predicate namedLandingArea = getNamedLandingArea(request.getNamedLandingAreas());
        Predicate topography = getTopography(request.getTopographies());
        Predicate mapName = getMapName(request.getMapNames());
        Predicate difficulty = getChallengeDifficulty(request.getMapDifficultiesOp(), request.getMapDifficultiesInt());

        List<Long> results = queryFactory.select(qSite.id).from(qSite)
                .where(namedLandingArea, topography, mapName, difficulty)
                .fetch();

        return qSite.id.in(results);
    }

    Predicate getBreakthroughPredicate(ComplexFilterRequest request) {

        GameVariant variant = request.getVariant();
        List<Breakthrough> breakthroughs = request.getBreakthroughs();

        if (breakthroughs.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }

        List<BreakthroughMap> query = queryFactory.select(breakthroughMap).from(breakthroughMap)
                .where(breakthroughMap.breakthrough.in(breakthroughs)).fetch();
        List<List<Long>> results = query.stream().map(q -> q.getIdList(variant)).toList();

        List<Long> list = commonFilterService.intersect(results);

        return qSite.id.in(list);
    }

    Predicate getBreakthroughShrunkPredicate(ComplexFilterRequest request) {

        GameVariant variant = request.getVariant();
        List<Breakthrough> breakthroughs = request.getBreakthroughs();

        if (breakthroughs.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }

        List<BreakthroughMap> query = queryFactory.select(breakthroughMap).from(breakthroughMap)
                .where(breakthroughMap.breakthrough.in(breakthroughs)).fetch();
        List<List<Long>> results = query.stream().map(q -> q.getIdList(variant)).toList();

        List<Long> list = commonFilterService.intersect(results);

        return qSite.id.in(list);
    }


    Predicate getResourceDisasterPredicate(ComplexFilterRequest request) {

        Predicate resPred = Expressions.asBoolean(true).isTrue();
        Map<ResourceType, OperatorValue> resMap = request.getResourceMap();
        for (Map.Entry<ResourceType, OperatorValue> entry : resMap.entrySet()) {
            if (entry.getValue().getOp().equals(Operator.NO_PREFERENCE)) continue;
            Predicate val = getResourcePredicate(entry);
            resPred = ExpressionUtils.and(resPred, val);
        }
        for (Map.Entry<DisasterType, OperatorValue> entry : request.getDisasterMap().entrySet()) {
            if (entry.getValue().getOp().equals(Operator.NO_PREFERENCE)) continue;
            Predicate val = getDisasterPredicate(entry);
            resPred = ExpressionUtils.and(resPred, val);
        }

        return resPred;
    }

    Predicate getDisasterPredicate(Map.Entry<DisasterType, OperatorValue> entry) {

        return switch (entry.getKey()) {
            case DUST_DEVILS -> qDisasters.dustDevils.in(entry.getValue().getRange());
            case DUST_STORMS -> qDisasters.dustStorms.in(entry.getValue().getRange());
            case METEORS -> qDisasters.meteors.in(entry.getValue().getRange());
            case COLD_WAVES -> qDisasters.coldWaves.in(entry.getValue().getRange());
        };
    }

    Predicate getResourcePredicate(Map.Entry<ResourceType, OperatorValue> entry) {

        return switch (entry.getKey()) {
            case METAL -> qResources.metal.in(entry.getValue().getRange());
            case RARE_METAL -> qResources.rareMetal.in(entry.getValue().getRange());
            case WATER -> qResources.water.in(entry.getValue().getRange());
            case CONCRETE -> qResources.concrete.in(entry.getValue().getRange());
        };
    }

    Predicate getNamedLandingArea(List<NamedLandingArea> namedLandingAreas) {
        if (namedLandingAreas.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        } else {
            return mapDetails.namedLocation.in(namedLandingAreas);
        }
    }

    Predicate getTopography(List<Topography> topographies) {
        if (topographies.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        } else {
            return mapDetails.topography.in(topographies);
        }
    }

    Predicate getMapName(List<MapName> mapNames) {
        if (mapNames.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        } else {
            return mapDetails.mapName.in(mapNames);
        }

    }

    Predicate getChallengeDifficulty(Operator operator, Difficulties value) {
        return switch (operator) {
            case NO_PREFERENCE -> Expressions.asBoolean(true).isTrue();
            case AT_LEAST -> mapDetails.challengeDifficulty.goe(value.getValue());
            case AT_MOST -> mapDetails.challengeDifficulty.loe(value.getValue());
            case EQUAL_TO -> mapDetails.challengeDifficulty.eq(value.getValue());
        };
    }

}
