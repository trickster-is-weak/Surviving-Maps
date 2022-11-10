package uk.co.brett.surviving.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum MapName {

    BLANK_1("BlankBig_01", "Blank 1"),
    BLANK_2("BlankBig_02", "Blank 2"),
    BLANK_3("BlankBig_03", "Blank 3"),
    BLANK_4("BlankBig_04", "Blank 4"),
    CRATER_1("BlankBigCrater_01", "Crater 1"),
    HEART_3("BlankBigHeartCMix_03", "Heart 3"),
    TERRACE_5("BlankTerraceBig_05", "Terrace 5"),
    CANYON_1("BlankBigCanyonCMix_01", "Canyon 1"),
    CANYON_2("BlankBigCanyonCMix_02", "Canyon 2"),
    CANYON_3("BlankBigCanyonCMix_03", "Canyon 3"),
    CANYON_4("BlankBigCanyonCMix_04", "Canyon 4"),
    CANYON_5("BlankBigCanyonCMix_05", "Canyon 5"),
    CANYON_6("BlankBigCanyonCMix_06", "Canyon 6"),
    CANYON_7("BlankBigCanyonCMix_07", "Canyon 7"),
    CANYON_8("BlankBigCanyonCMix_08", "Canyon 8"),
    CANYON_9("BlankBigCanyonCMix_09", "Canyon 9"),
    CLIFFS_1("BlankBigCliffsCMix_01", "Cliffs 1"),
    CLIFFS_2("BlankBigCliffsCMix_02", "Cliffs 2"),
    CRATER_MIX_1("BlankBigCratersCMix_01", "Crater Mix 1"),
    TERRACE_MIX_1("BlankBigTerraceCMix_01", "Terrace Mix 1"),
    TERRACE_MIX_2("BlankBigTerraceCMix_02", "Terrace Mix 2"),
    TERRACE_MIX_3("BlankBigTerraceCMix_03", "Terrace Mix 3"),
    TERRACE_MIX_4("BlankBigTerraceCMix_04", "Terrace Mix 4"),
    TERRACE_MIX_5("BlankBigTerraceCMix_05", "Terrace Mix 5"),
    TERRACE_MIX_6("BlankBigTerraceCMix_06", "Terrace Mix 6"),
    TERRACE_MIX_7("BlankBigTerraceCMix_07", "Terrace Mix 7"),
    TERRACE_MIX_8("BlankBigTerraceCMix_08", "Terrace Mix 8"),
    TERRACE_MIX_9("BlankBigTerraceCMix_09", "Terrace Mix 9"),
    TERRACE_MIX_10("BlankBigTerraceCMix_10", "Terrace Mix 10"),
    TERRACE_MIX_11("BlankBigTerraceCMix_11", "Terrace Mix 11"),
    TERRACE_MIX_12("BlankBigTerraceCMix_12", "Terrace Mix 12"),
    TERRACE_MIX_13("BlankBigTerraceCMix_13", "Terrace Mix 13"),
    TERRACE_MIX_14("BlankBigTerraceCMix_14", "Terrace Mix 14"),
    TERRACE_MIX_15("BlankBigTerraceCMix_15", "Terrace Mix 15"),
    TERRACE_MIX_16("BlankBigTerraceCMix_16", "Terrace Mix 16"),
    TERRACE_MIX_17("BlankBigTerraceCMix_17", "Terrace Mix 17"),
    TERRACE_MIX_18("BlankBigTerraceCMix_18", "Terrace Mix 18"),
    TERRACE_MIX_19("BlankBigTerraceCMix_19", "Terrace Mix 19"),
    TERRACE_MIX_20("BlankBigTerraceCMix_20", "Terrace Mix 20");

    private static final Map<String, MapName> ENUM_MAP;

    static {
        Map<String, MapName> map = new ConcurrentHashMap<>();
        for (MapName instance : MapName.values()) {
            map.put(instance.getRaw(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String formatted;

    private final String raw;


    MapName(String raw, String formatted) {
        this.formatted = formatted;
        this.raw = raw;
    }

    @JsonCreator
    public static MapName forValues(String raw) {
        return ENUM_MAP.get(raw);
    }

    public String getRaw() {
        return raw;
    }

    public String getFormatted() {
        return formatted;
    }

    public String getPng() {
        return raw + ".png";
    }

}
