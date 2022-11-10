package uk.co.brett.surviving.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Compass {

    NORTH("N"), SOUTH("S"), EAST("E"), WEST("W");

    private static final Map<String, Compass> ENUM_MAP;

    static {
        Map<String, Compass> map = new ConcurrentHashMap<>();
        for (Compass instance : Compass.values()) {
            map.put(instance.getAbbrev(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String abbrev;

    Compass(String a) {
        abbrev = a;
    }

    @JsonCreator
    public static Compass getCompass(String abbrev) {
        return ENUM_MAP.get(abbrev);
    }

    public String getAbbrev() {
        return abbrev;
    }
}
