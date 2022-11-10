package uk.co.brett.surviving.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum NamedLandingArea {

    ALBOR_THOLUS("Albor Tholus"),
    ARABIA_TERRA("Arabia Terra"),
    ARCADIA_PLANITIA("Arcadia Planitia"),
    ARGYRE_PLANITIA("Argyre Planitia"),
    ARSAI_MONS("Arsai Mons"),
    ASCRAEUS_MONS("Ascraeus Mons"),
    COLONY_SITE("Colony Site"),
    ELYSIUM_MONS("Elysium Mons"),
    GALE_CRATER("Gale Crater"),
    HECATUS_THOLUS("Hecatus Tholus"),
    HELLAS_PLANITIA("Hellas Planitia"),
    MARS_PATHFINDER_LANDING_AREA("Mars Pathfinder Landing Area"),
    NORTH_CAP("North Cap"),
    OLYMPUS_MONS("Olympus Mons"),
    OPPORTUNITY_LANDING_AREA("Opportunity Landing Area"),
    PAVONIS_MONS("Pavonis Mons"),
    SOUTH_CAP("South Cap"),
    SPIRIT_LANDING_AREA("Spirit Landing Area"),
    TEMPA_TERRA("Tempa Terra"),
    TERRA_SABAEA("Terra Sabaea"),
    TERRA_SIREUM("Terra Sireum"),
    UTOPIA_PLANITIA("Utopia Planitia"),
    VALLES_MARINERIS("Valles Marineris"),
    VIKING_1_LANDING_AREA("Viking 1 Landing Area"),
    VIKING_2_LANDING_AREA("Viking 2 Landing Area"),
    UNNAMED("Unnamed");

    private static final Map<String, NamedLandingArea> ENUM_MAP;

    static {
        Map<String, NamedLandingArea> map = new ConcurrentHashMap<>();
        for (NamedLandingArea instance : NamedLandingArea.values()) {
            map.put(instance.getFormatted(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String formatted;

    NamedLandingArea(String formatted) {
        this.formatted = formatted;
    }

    @JsonCreator
    public static NamedLandingArea getNamedLandingArea(String formatted) {

        return ENUM_MAP.getOrDefault(formatted.trim(), UNNAMED);
    }

    public String getFormatted() {
        return formatted;
    }


}
