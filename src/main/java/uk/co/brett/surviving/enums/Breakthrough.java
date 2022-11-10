package uk.co.brett.surviving.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.co.brett.surviving.enums.GameVariant.*;

public enum Breakthrough {

    ADVANCED_DRONE_DRIVE("Advanced Drone Drive", STANDARD),
    ALIEN_IMPRINTS("Alien Imprints", STANDARD),
    ANCIENT_TERRAFORMING_DEVICE("Ancient Terraforming Device", GREEN_PLANET),
    ARTIFICIAL_MUSCLES("Artificial Muscles", STANDARD),
    AUTONOMOUS_HUBS("Autonomous Hubs", STANDARD),
    CAPTURE_ASTEROID("Capture Asteroid", BELOW_BEYOND),
    CARGO_BAY_OF_HOLDING("Cargobay of holding", BELOW_BEYOND),
    CLONING("Cloning", STANDARD),
    CONSTRUCTION_NANITES("Construction Nanites", STANDARD),
    CORE_METALS("Core Metals", STANDARD),
    CORE_RARE_METALS("Core Rare Metals", STANDARD),
    CORE_WATER("Core Water", STANDARD),
    CRYO_SLEEP("Cryo-sleep", STANDARD),
    DESIGNED_FORESTATION("Designed Forestation", GREEN_PLANET),
    DOME_STREAMLINING("Dome Streamlining", STANDARD),
    DRY_FARMING("Dry Farming", STANDARD),
    ETERNAL_FUSION("Eternal Fusion", STANDARD),
    EXTRACTOR_AI("Extractor AI", STANDARD),
    FACTORY_AUTOMATION("Factory Automation", STANDARD),
    FOREVER_YOUNG("Forever Young", STANDARD),
    FRICTIONLESS_COMPOSITES("Frictionless Composites", STANDARD),
    GEM_ARCHITECTURE("Gem Architecture", STANDARD),
    GENE_SELECTION("Gene Selection", STANDARD),
    GIANT_CROPS("Giant Crops", STANDARD),
    GLOBAL_SUPPORT("Global Support", BELOW_BEYOND),
    GOOD_VIBRATIONS("Good Vibrations", STANDARD),
    HIVE_MIND("Hive Mind", STANDARD),
    HULL_POLARIZATION("Hull Polarization", STANDARD),
    HYPERSENSITIVE_PHOTOVOLTAICS("Hypersensitive Photovoltaics", STANDARD),
    INSPIRING_ARCHITECTURE("Inspiring Architecture", STANDARD),
    INTERPLANETARY_LEARNING("Interplanetary Learning", STANDARD),
    LAKE_VAPORATORS("Lake Vaporators", GREEN_PLANET),
    LANDSCAPING_NANITES("Landscaping Nanites", GREEN_PLANET),
    MAGNETIC_EXTRACTION("Magnetic Extraction", STANDARD),
    MARS_MARKETING("Mars Marketing", BELOW_BEYOND),
    MARTIAN_DIET("Martian Diet", STANDARD),
    MARTIAN_STEEL("Martian Steel", STANDARD),
    MARTIANBORN_INGENUITY("Martianborn Ingenuity", STANDARD),
    MOLE_PEOPLE("Mole People", BELOW_BEYOND),
    MULTISPIRAL_ARCHITECTURE("Multispiral Architecture", STANDARD),
    NANO_REFINEMENT("Nano Refinement", STANDARD),
    NEOCONCRETE("Neo-Concrete", STANDARD),
    NEURAL_EMPATHY("Neural Empathy", STANDARD),
    NOCTURNAL_ADAPTATION("Nocturnal Adaptation", STANDARD),
    OVERCHARGE_AMPLIFICATIONS("Overcharge Amplifications", STANDARD),
    PLASMA_ROCKET("Plasma Rocket", STANDARD),
    PLUTONIUM_SYNTHESIS("Plutonium Synthesis", STANDARD),
    PREFAB_COMPRESSION("Prefab Compression", STANDARD),
    PRINTED_ELECTRONICS("Printed Electronics", STANDARD),
    PROJECT_PHOENIX("Project Phoenix", STANDARD),
    RAPID_SLEEP("Rapid Sleep", STANDARD),
    RESILIENT_VEGETATION("Resilient Vegetation", GREEN_PLANET),
    SAFE_MODE("Safe Mode", STANDARD),
    SERVICE_BOTS("Service Bots", STANDARD),
    SOYLENT_GREEN("Soylent Green", STANDARD),
    SPACE_REHABILITATION("Space Rehabilitation", STANDARD),
    SUPERCONDUCTING_COMPUTING("Superconducting Computing", STANDARD),
    SUPERFUNGUS("Superfungus", STANDARD),
    SUPERIOR_CABLES("Superior Cables", STANDARD),
    SUPERIOR_PIPES("Superior Pipes", STANDARD),
    SUSTAINED_WORKLOAD("Sustained Workload", STANDARD),
    THE_POSITRONIC_BRAIN("The Positronic Brain", STANDARD),
    VECTOR_PUMP("Vector Pump", STANDARD),
    VEHICLE_WEIGHT_OPTIMISATIONS("Vehicle Weight Optimizations", BELOW_BEYOND),
    VOCATION_ORIENTED_SOCIETY("Vocation-Oriented Society", STANDARD),
    WIRELESS_POWER("Wireless Power", STANDARD),
    ZERO_SPACE_COMPUTING("Zero-Space Computing", STANDARD);

    private static final Map<String, Breakthrough> ENUM_MAP;

    static {
        Map<String, Breakthrough> map = new ConcurrentHashMap<>();
        for (Breakthrough instance : Breakthrough.values()) {
            map.put(instance.getFormatted(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String formatted;

    private final GameVariant variant;

    Breakthrough(String pretty, GameVariant variant) {
        this.formatted = pretty;
        this.variant = variant;
    }

    @JsonCreator
    public static Breakthrough getBreakthrough(String formatted) {

        if (ENUM_MAP.get(formatted.trim()) != null) {
            return ENUM_MAP.get(formatted.trim());
        }

        return Breakthrough.valueOf(formatted);
    }

    public static List<Breakthrough> filterVariant(GameVariant variant) {

        List<GameVariant> search = switch (variant) {
            case STANDARD -> List.of(STANDARD);
            case BELOW_BEYOND -> List.of(STANDARD, BELOW_BEYOND);
            case BEYOND_GREEN -> List.of(STANDARD, BELOW_BEYOND, GREEN_PLANET);
            case GREEN_PLANET,
                    TITO_GREEN_PLANET,
                    EVANS_GREEN_PLANET -> List.of(STANDARD, GREEN_PLANET);
        };

        return Arrays.stream(Breakthrough.values()).filter(b -> search.contains(b.variant)).toList();

    }

    public String getFormatted() {
        return formatted;
    }

    public GameVariant getVariant() {
        return variant;
    }
}
