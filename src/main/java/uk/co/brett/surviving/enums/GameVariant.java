package uk.co.brett.surviving.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum GameVariant {

    STANDARD("Standard"),
    GREEN_PLANET("Green Planet"),
    BELOW_BEYOND("Below and Beyond"),
    BEYOND_GREEN("Beyond + Green"),
    TITO_GREEN_PLANET("Tito: Green Planet"),
    EVANS_GREEN_PLANET("Evans: Green Planet");

    private static final Map<String, GameVariant> ENUM_MAP;

    static {
        Map<String, GameVariant> map = new ConcurrentHashMap<>();
        for (GameVariant instance : GameVariant.values()) {
            map.put(instance.getFormatted(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String formatted;

    GameVariant(String s) {
        this.formatted = s;
    }

    public String getFormatted() {
        return formatted;
    }
}
