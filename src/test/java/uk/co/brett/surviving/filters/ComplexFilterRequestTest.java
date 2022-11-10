package uk.co.brett.surviving.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.enums.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.co.brett.surviving.LandingSiteTestUtil.getAssortedEnums;
import static uk.co.brett.surviving.LandingSiteTestUtil.getRandomEnum;

class ComplexFilterRequestTest {

    ComplexFilterRequest cfr;

    @BeforeEach
    void setUp() {

        cfr = TestFilterUtils.createComplex();

    }

    @Test
    void mapDifficultiesOp() {
        Operator exp = TestFilterUtils.getOperator();
        cfr.setMapDifficultiesOp(exp);
        assertThat(cfr.getMapDifficultiesOp()).isEqualTo(exp);
    }

    @Test
    void mapDifficultiesInt() {
        Difficulties exp = getRandomEnum(Difficulties.class);
        cfr.setMapDifficultiesInt(exp);
        assertThat(cfr.getMapDifficultiesInt()).isEqualTo(exp);
    }


    @Test
    void getNamedLandingAreas() {
        List<NamedLandingArea> exp = getAssortedEnums(NamedLandingArea.class, 3);
        cfr.setNamedLandingAreas(exp);
        assertThat(cfr.getNamedLandingAreas()).isEqualTo(exp);
    }

    @Test
    void topographies() {
        List<Topography> exp = getAssortedEnums(Topography.class, 2);
        cfr.setTopographies(exp);
        assertThat(cfr.getTopographies()).isEqualTo(exp);
    }


    @Test
    void variant() {
        GameVariant exp = getRandomEnum(GameVariant.class);
        cfr.setVariant(exp);
        assertThat(cfr.getVariant()).isEqualTo(exp);
    }


    @Test
    void getWater() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setWater(exp);
        assertThat(cfr.getWater()).isEqualTo(exp);
    }


    @Test
    void getConcrete() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setConcrete(exp);
        assertThat(cfr.getConcrete()).isEqualTo(exp);
    }


    @Test
    void getMetals() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setMetals(exp);
        assertThat(cfr.getMetals()).isEqualTo(exp);
    }


    @Test
    void getRareMetals() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setRareMetals(exp);
        assertThat(cfr.getRareMetals()).isEqualTo(exp);
    }


    @Test
    void getMeteors() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setMeteors(exp);
        assertThat(cfr.getMeteors()).isEqualTo(exp);
    }


    @Test
    void getColdWaves() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setColdWaves(exp);
        assertThat(cfr.getColdWaves()).isEqualTo(exp);
    }


    @Test
    void getDustStorms() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setDustStorms(exp);
        assertThat(cfr.getDustStorms()).isEqualTo(exp);
    }


    @Test
    void setDustDevils() {
        OperatorValue exp = TestFilterUtils.getOperatorValue();
        cfr.setDustDevils(exp);
        assertThat(cfr.getDustDevils()).isEqualTo(exp);
    }

    @Test
    void getBreakthroughs() {
        List<Breakthrough> exp = getAssortedEnums(Breakthrough.class, 3);
        cfr.setBreakthroughs(exp);
        assertThat(cfr.getBreakthroughs()).isEqualTo(exp);
    }


    @Test
    void setDisasters() {
        Map<DisasterType, OperatorValue> disMap = new EnumMap<>(DisasterType.class);
        disMap.put(DisasterType.COLD_WAVES, TestFilterUtils.getOperatorValue());
        disMap.put(DisasterType.METEORS, TestFilterUtils.getOperatorValue());
        disMap.put(DisasterType.DUST_DEVILS, TestFilterUtils.getOperatorValue());
        disMap.put(DisasterType.DUST_STORMS, TestFilterUtils.getOperatorValue());
        cfr.setDisasters(disMap);
        assertThat(cfr.getDisasterMap()).isEqualTo(disMap);
    }

    @Test
    void setResources() {
        Map<ResourceType, OperatorValue> resMap = new EnumMap<>(ResourceType.class);
        resMap.put(ResourceType.CONCRETE, TestFilterUtils.getOperatorValue());
        resMap.put(ResourceType.WATER, TestFilterUtils.getOperatorValue());
        resMap.put(ResourceType.METAL, TestFilterUtils.getOperatorValue());
        resMap.put(ResourceType.RARE_METAL, TestFilterUtils.getOperatorValue());
        cfr.setResources(resMap);
        assertThat(cfr.getResourceMap()).isEqualTo(resMap);
    }

    @Test
    void mapNames() {
        List<MapName> maps = getAssortedEnums(MapName.class, 5);
        cfr.setMapNames(maps);
        assertThat(cfr.getMapNames()).isEqualTo(maps);
    }

}