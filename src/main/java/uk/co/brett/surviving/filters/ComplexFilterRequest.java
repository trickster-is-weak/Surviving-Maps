package uk.co.brett.surviving.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.co.brett.surviving.enums.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static uk.co.brett.surviving.enums.DisasterType.*;
import static uk.co.brett.surviving.enums.ResourceType.*;

@JsonDeserialize
@JsonSerialize
public class ComplexFilterRequest {
    private final OperatorValue defaultVal = new OperatorValue();
    private GameVariant variant;
    private List<Breakthrough> breakthroughs;

    private Operator mapDifficultiesOp;
    private Difficulties mapDifficultiesInt;
    private List<NamedLandingArea> namedLandingAreas;
    private List<Topography> topographies;

    private List<MapName> mapNames;
    private OperatorValue water;
    private OperatorValue concrete;
    private OperatorValue metals;
    private OperatorValue rareMetals;
    private OperatorValue meteors;
    private OperatorValue coldWaves;
    private OperatorValue dustStorms;
    private OperatorValue dustDevils;

    public ComplexFilterRequest() {
        variant = GameVariant.STANDARD;
        breakthroughs = List.of();
        topographies = List.of();
        namedLandingAreas = List.of();
        mapNames = List.of();
        water = new OperatorValue();
        concrete = new OperatorValue();
        metals = new OperatorValue();
        rareMetals = new OperatorValue();
        meteors = new OperatorValue();
        coldWaves = new OperatorValue();
        dustStorms = new OperatorValue();
        dustDevils = new OperatorValue();
        mapDifficultiesOp = Operator.NO_PREFERENCE;
        mapDifficultiesInt = Difficulties.D100;
    }

    public Operator getMapDifficultiesOp() {
        return mapDifficultiesOp;
    }

    public void setMapDifficultiesOp(Operator mapDifficultiesOp) {
        this.mapDifficultiesOp = mapDifficultiesOp;
    }

    public Difficulties getMapDifficultiesInt() {
        return mapDifficultiesInt;
    }

    public void setMapDifficultiesInt(Difficulties mapDifficultiesInt) {
        this.mapDifficultiesInt = mapDifficultiesInt;
    }

    public List<NamedLandingArea> getNamedLandingAreas() {
        return namedLandingAreas;
    }

    public void setNamedLandingAreas(List<NamedLandingArea> namedLandingAreas) {
        this.namedLandingAreas = namedLandingAreas;
    }

    public List<Topography> getTopographies() {
        return topographies;
    }

    public void setTopographies(List<Topography> topographies) {
        this.topographies = topographies;
    }

    public GameVariant getVariant() {
        return variant;
    }

    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    public OperatorValue getWater() {
        return water;
    }

    public void setWater(OperatorValue water) {
        this.water = water;
    }

    public OperatorValue getConcrete() {
        return concrete;
    }

    public void setConcrete(OperatorValue concrete) {
        this.concrete = concrete;
    }

    public OperatorValue getMetals() {
        return metals;
    }

    public void setMetals(OperatorValue metals) {
        this.metals = metals;
    }

    public OperatorValue getRareMetals() {
        return rareMetals;
    }

    public void setRareMetals(OperatorValue rareMetals) {
        this.rareMetals = rareMetals;
    }

    public OperatorValue getMeteors() {
        return meteors;
    }

    public void setMeteors(OperatorValue meteors) {
        this.meteors = meteors;
    }

    public OperatorValue getColdWaves() {
        return coldWaves;
    }

    public void setColdWaves(OperatorValue coldWaves) {
        this.coldWaves = coldWaves;
    }

    public OperatorValue getDustStorms() {
        return dustStorms;
    }

    public void setDustStorms(OperatorValue dustStorms) {
        this.dustStorms = dustStorms;
    }

    public OperatorValue getDustDevils() {
        return dustDevils;
    }

    public void setDustDevils(OperatorValue dustDevils) {
        this.dustDevils = dustDevils;
    }

    public List<Breakthrough> getBreakthroughs() {
        return breakthroughs;
    }

    public void setBreakthroughs(List<Breakthrough> breakthroughs) {
        this.breakthroughs = breakthroughs;
    }

    @Override
    public String toString() {
        return "ComplexFilterRequest{" +
                "\ndefaultVal=" + defaultVal +
                ", \nvariant=" + variant +
                ", \nbreakthroughs=" + breakthroughs +
                ", \nnamedLandingAreas=" + namedLandingAreas +
                ", \ntopographies=" + topographies +
                ", \nwater=" + water +
                ", \nconcrete=" + concrete +
                ", \nmetals=" + metals +
                ", \nrareMetals=" + rareMetals +
                ", \nmeteors=" + meteors +
                ", \ncoldWaves=" + coldWaves +
                ", \ndustStorms=" + dustStorms +
                ", dustDevils=" + dustDevils +
                '}';
    }

    public void setDisasters(Map<DisasterType, OperatorValue> disasterMap) {
        meteors = disasterMap.getOrDefault(METEORS, defaultVal);
        coldWaves = disasterMap.getOrDefault(COLD_WAVES, defaultVal);
        dustStorms = disasterMap.getOrDefault(DUST_STORMS, defaultVal);
        dustDevils = disasterMap.getOrDefault(DUST_DEVILS, defaultVal);
    }

    public void setResources(Map<ResourceType, OperatorValue> resourceMap) {
        water = resourceMap.getOrDefault(WATER, defaultVal);
        concrete = resourceMap.getOrDefault(CONCRETE, defaultVal);
        metals = resourceMap.getOrDefault(METAL, defaultVal);
        rareMetals = resourceMap.getOrDefault(RARE_METAL, defaultVal);
    }

    public Map<ResourceType, OperatorValue> getResourceMap() {
        Map<ResourceType, OperatorValue> map = new EnumMap<>(ResourceType.class);
        map.put(WATER, water);
        map.put(CONCRETE, concrete);
        map.put(METAL, metals);
        map.put(RARE_METAL, rareMetals);

        return map;
    }

    public Map<DisasterType, OperatorValue> getDisasterMap() {
        Map<DisasterType, OperatorValue> map = new EnumMap<>(DisasterType.class);
        map.put(METEORS, meteors);
        map.put(COLD_WAVES, coldWaves);
        map.put(DUST_DEVILS, dustDevils);
        map.put(DUST_STORMS, dustStorms);
        return map;
    }

    public List<MapName> getMapNames() {
        return mapNames;
    }

    public void setMapNames(List<MapName> mapNames) {
        this.mapNames = mapNames;
    }
}
