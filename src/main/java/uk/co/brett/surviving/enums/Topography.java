package uk.co.brett.surviving.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public enum Topography {

    RELATIVELY_FLAT("Relatively Flat", 1),
    STEEP("Steep", 2),
    ROUGH("Rough", 3),
    MOUNTAINOUS("Mountainous", 4);

    private static final Map<String, Topography> ENUM_MAP;

    static {
        Map<String, Topography> map = new ConcurrentHashMap<>();
        for (Topography instance : Topography.values()) {
            map.put(instance.getFormatted(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String formatted;

    private final int value;

    Topography(String formatted, int rating) {
        this.formatted = formatted;
        this.value = rating;
    }

    @JsonCreator
    public static Topography forValues(String formatted) {
        return ENUM_MAP.get(formatted);
    }

    public static Predicate<Topography> all() {
        return rating -> rating.getValue() > 0;
    }

    public int getValue() {
        return value;
    }

    public String getFormatted() {
        return formatted;
    }


}
