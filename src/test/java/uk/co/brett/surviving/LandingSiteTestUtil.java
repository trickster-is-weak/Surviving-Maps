package uk.co.brett.surviving;

import org.apache.commons.math3.random.RandomDataGenerator;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.io.ImmutableLandingSiteFlat;
import uk.co.brett.surviving.io.LandingSiteFlat;

import java.util.*;

import static uk.co.brett.surviving.enums.GameVariant.STANDARD;
import static uk.co.brett.surviving.enums.GameVariant.values;

public class LandingSiteTestUtil {

    private static final RandomDataGenerator r = new RandomDataGenerator();

    public static LandingSiteFlat getLandingSite() {
        return getLandingSite(STANDARD);
    }


    public static LandingSiteFlat getLandingSite(GameVariant variant) {

        ImmutableLandingSiteFlat.Builder builder = ImmutableLandingSiteFlat.builder()
                .latitude(getInt(0, 90))
                .northSouth(compassPicker(true))
                .longitude(getInt(0, 180))
                .eastWest(compassPicker(false))
                .topography(getRandomEnum(Topography.class))
                .difficulty(getInt(100, 190))
                .altitude(getInt(-5000, 5000))
                .temperature(getInt(-100, -10))
                .metals(getInt(1, 4))
                .rareMetals(getInt(1, 4))
                .concrete(getInt(1, 4))
                .water(getInt(1, 4))
                .dustDevils(getInt(1, 4))
                .dustStorms(getInt(1, 4))
                .meteors(getInt(1, 4))
                .coldWaves(getInt(1, 4))
                .mapName(getRandomEnum(MapName.class))
                .namedLocation(getRandomEnum(NamedLandingArea.class));


        List<Breakthrough> breakthroughs = switch (variant) {
            case STANDARD, GREEN_PLANET, BELOW_BEYOND, BEYOND_GREEN -> getBreakthroughs(12);
            case TITO_GREEN_PLANET, EVANS_GREEN_PLANET -> getBreakthroughs(17);
        };

        return switch (variant) {
            case STANDARD, GREEN_PLANET, BELOW_BEYOND, BEYOND_GREEN -> builder
                    .breakthrough1(breakthroughs.get(0))
                    .breakthrough2(breakthroughs.get(1))
                    .breakthrough3(breakthroughs.get(2))
                    .breakthrough4(breakthroughs.get(3))
                    .breakthrough5(breakthroughs.get(4))
                    .breakthrough6(breakthroughs.get(5))
                    .breakthrough7(breakthroughs.get(6))
                    .breakthrough8(breakthroughs.get(7))
                    .breakthrough9(breakthroughs.get(8))
                    .breakthrough10(breakthroughs.get(9))
                    .breakthrough11(breakthroughs.get(10))
                    .breakthrough12(breakthroughs.get(11))
                    .build();

            case TITO_GREEN_PLANET, EVANS_GREEN_PLANET -> builder
                    .breakthrough1(breakthroughs.get(0))
                    .breakthrough2(breakthroughs.get(1))
                    .breakthrough3(breakthroughs.get(2))
                    .breakthrough4(breakthroughs.get(3))
                    .breakthrough5(breakthroughs.get(4))
                    .breakthrough6(breakthroughs.get(5))
                    .breakthrough7(breakthroughs.get(6))
                    .breakthrough8(breakthroughs.get(7))
                    .breakthrough9(breakthroughs.get(8))
                    .breakthrough10(breakthroughs.get(9))
                    .breakthrough11(breakthroughs.get(10))
                    .breakthrough12(breakthroughs.get(11))
                    .breakthrough13(breakthroughs.get(12))
                    .breakthrough14(breakthroughs.get(13))
                    .breakthrough15(breakthroughs.get(14))
                    .breakthrough16(breakthroughs.get(15))
                    .breakthrough17(breakthroughs.get(16))
                    .build();

        };


    }


    public static List<LandingSiteFlat> getLandingSites(int count) {
        List<LandingSiteFlat> sites = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            sites.add(getLandingSite());
        }
        return sites;
    }

    public static int getInt(int min, int max) {
        return r.nextInt(min, max);
    }

    public static <T extends Enum<?>> T getRandomEnum(Class<T> clazz) {
        int x = r.nextInt(0, clazz.getEnumConstants().length - 1);
        return clazz.getEnumConstants()[x];
    }

    public static Compass compassPicker(boolean northSouth) {
        double rnd = r.nextUniform(0, 1);

        if (northSouth) {
            return rnd > 0.5 ? Compass.NORTH : Compass.SOUTH;
        } else {
            return rnd > 0.5 ? Compass.EAST : Compass.WEST;
        }
    }

    public static List<Breakthrough> getBreakthroughs(int count) {
        List<Breakthrough> list = Arrays.asList(Breakthrough.values());
        Collections.shuffle(list);
        return list.stream().limit(count).toList();
    }

    public static <T extends Enum<?>> List<T> getAssortedEnums(Class<T> clazz, int count) {
        List<T> list = Arrays.asList(clazz.getEnumConstants());
        Collections.shuffle(list);
        return list.stream().limit(count).toList();
    }

    public static Map<GameVariant, List<Breakthrough>> breakthroughMap() {
        Map<GameVariant, List<Breakthrough>> btrVarMap = new HashMap<>();
        for (GameVariant variant : values()) {
            List<Breakthrough> bts = new ArrayList<>(Breakthrough.filterVariant(variant));
            Collections.shuffle(bts);
            List<Breakthrough> bt = bts.stream().limit(12).toList();
            btrVarMap.computeIfAbsent(variant, k -> new ArrayList<>()).addAll(bt);
        }
        return btrVarMap;
    }

}
