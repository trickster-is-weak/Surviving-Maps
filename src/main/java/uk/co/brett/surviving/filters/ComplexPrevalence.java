package uk.co.brett.surviving.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonDeserialize
@JsonSerialize
public enum ComplexPrevalence {

    LOW(1, "Low"),
    MED(2, "Medium"),
    HIGH(3, "High"),
    VERY_HIGH(4, "Very High");

    private static final Map<String, ComplexPrevalence> ENUM_MAP;
    private static final Map<Integer, ComplexPrevalence> INT_MAP;

    static {
        Map<String, ComplexPrevalence> map = new ConcurrentHashMap<>();
        Map<Integer, ComplexPrevalence> intMap = new ConcurrentHashMap<>();
        for (ComplexPrevalence instance : ComplexPrevalence.values()) {
            map.put(instance.getFormatted(), instance);
            intMap.put(instance.getRating(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
        INT_MAP = Collections.unmodifiableMap(intMap);
    }

    private final String formatted;

    private final int rating;

    ComplexPrevalence(int rating, String formatted) {
        this.formatted = formatted;
        this.rating = rating;
    }

    @JsonCreator
    public static ComplexPrevalence fromString(String s) {
        return ENUM_MAP.get(s);
    }

    @JsonCreator
    public static ComplexPrevalence fromInt(int i) {
        return INT_MAP.get(i);
    }


    public static OperatorValue atMost(ComplexPrevalence prevalence) {
        return new OperatorValue(Operator.AT_MOST, prevalence);
    }

    public static OperatorValue equalTo(ComplexPrevalence prevalence) {
        return new OperatorValue(Operator.EQUAL_TO, prevalence);
    }

    public static OperatorValue atLeast(ComplexPrevalence prevalence) {
        return new OperatorValue(Operator.AT_LEAST, prevalence);
    }

    public String getFormatted() {
        return formatted;
    }

    public int getRating() {
        return rating;
    }
}
