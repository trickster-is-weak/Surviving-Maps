//package uk.co.brett.surviving;
//
//import org.assertj.core.data.Offset;
//import org.assertj.core.error.AssertJMultipleFailuresError;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import uk.co.brett.surviving.enums.*;
//import uk.co.brett.surviving.types.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
//import static uk.co.brett.surviving.enums.Breakthrough.*;
//import static uk.co.brett.surviving.enums.Compass.EAST;
//import static uk.co.brett.surviving.enums.Compass.NORTH;
//import static uk.co.brett.surviving.enums.MapName.CANYON_1;
//import static uk.co.brett.surviving.enums.NamedLandingArea.ALBOR_THOLUS;
//import static uk.co.brett.surviving.enums.NamedLandingArea.UNNAMED;
//import static uk.co.brett.surviving.enums.Rating.LOW;
//import static uk.co.brett.surviving.enums.Topography.ROUGH;
//
//public class LandingSiteTest {
//    private final int latitude = 10;
//    private final int longitude = 10;
//    private final int difficulty = 110;
//    private final int altitude = 10;
//    private final int temperature = -10;
//    private final Compass northSouth = NORTH;
//    private final Compass eastWest = EAST;
//    private final Topography topography = ROUGH;
//    private final Rating metals = LOW;
//    private final Rating rareMetals = LOW;
//    private final Rating concrete = LOW;
//    private final Rating water = LOW;
//    private final Rating dustDevils = LOW;
//    private final Rating dustStorms = LOW;
//    private final Rating meteors = LOW;
//    private final Rating coldWaves = LOW;
//    private final MapName mapName = CANYON_1;
//    private final NamedLandingArea namedLocation = ALBOR_THOLUS;
//    private final Breakthrough breakthrough1 = ADVANCED_DRONE_DRIVE;
//    private final Breakthrough breakthrough2 = ALIEN_IMPRINTS;
//    private final Breakthrough breakthrough3 = ANCIENT_TERRAFORMING_DEVICE;
//    private final Breakthrough breakthrough4 = ARTIFICIAL_MUSCLES;
//    private final Breakthrough breakthrough5 = AUTONOMOUS_HUBS;
//    private final Breakthrough breakthrough6 = CLONING;
//    private final Breakthrough breakthrough7 = CONSTRUCTION_NANITES;
//    private final Breakthrough breakthrough8 = CORE_METALS;
//    private final Breakthrough breakthrough9 = CORE_RARE_METALS;
//    private final Breakthrough breakthrough10 = CORE_WATER;
//    private final Breakthrough breakthrough11 = CRYO_SLEEP;
//    private final Breakthrough breakthrough12 = DESIGNED_FORESTATION;
//
//    private LandingSite site;
//
//    @BeforeEach
//    void setUp() {
//
//        site = ImmutableLandingSite.builder()
//                .latitude(latitude)
//                .northSouth(northSouth)
//                .longitude(longitude)
//                .eastWest(eastWest)
//                .topography(topography)
//                .difficulty(difficulty)
//                .altitude(altitude)
//                .temperature(temperature)
//                .metals(metals)
//                .rareMetals(rareMetals)
//                .concrete(concrete)
//                .water(water)
//                .dustDevils(dustDevils)
//                .dustStorms(dustStorms)
//                .meteors(meteors)
//                .coldWaves(coldWaves)
//                .mapName(mapName)
//                .namedLocation(namedLocation)
//                .breakthrough1(breakthrough1)
//                .breakthrough2(breakthrough2)
//                .breakthrough3(breakthrough3)
//                .breakthrough4(breakthrough4)
//                .breakthrough5(breakthrough5)
//                .breakthrough6(breakthrough6)
//                .breakthrough7(breakthrough7)
//                .breakthrough8(breakthrough8)
//                .breakthrough9(breakthrough9)
//                .breakthrough10(breakthrough10)
//                .breakthrough11(breakthrough11)
//                .breakthrough12(breakthrough12)
//                .build();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    public void testDisasters() {
//        Disasters expected = ImmutableDisasters.builder()
//                .putMap(DisasterType.DUST_DEVILS, dustDevils)
//                .putMap(DisasterType.DUST_STORMS, dustStorms)
//                .putMap(DisasterType.METEORS, meteors)
//                .putMap(DisasterType.COLD_WAVES, coldWaves)
//                .build();
//
//        assertThat(site.disasters()).isEqualTo(expected);
//    }
//
//    @Test
//    public void testResources() {
//        Resources expected = ImmutableResources.builder()
//                .putMap(ResourceType.CONCRETE, concrete)
//                .putMap(ResourceType.WATER, water)
//                .putMap(ResourceType.METAL, metals)
//                .putMap(ResourceType.RARE_METAL, rareMetals)
//                .build();
//
//        assertThat(site.resources()).isEqualTo(expected);
//    }
//
//    @Test
//    public void testLocation() {
//        Location expected = ImmutableLocation.builder()
//                .latitude(latitude)
//                .longitude(longitude)
//                .northSouth(northSouth)
//                .eastWest(eastWest)
//                .build();
//
//        assertThat(site.location()).isEqualTo(expected);
//    }
//
//
//    @Test
//    public void testMapDetails() {
//        MapDetails expected = ImmutableMapDetails.builder()
//                .topography(topography)
//                .temperature(temperature)
//                .challengeDifficulty(difficulty)
//                .altitude(altitude)
//                .mapName(mapName)
//                .namedLocation(namedLocation)
//                .build();
//
//        assertThat(site.mapDetails()).isEqualTo(expected);
//    }
//
//    @Test
//    public void testBreakthroughs() {
//        List<Breakthrough> expected = List.of(
//                breakthrough1,
//                breakthrough2,
//                breakthrough3,
//                breakthrough4,
//                breakthrough5,
//                breakthrough6,
//                breakthrough7,
//                breakthrough8,
//                breakthrough9,
//                breakthrough10,
//                breakthrough11,
//                breakthrough12
//        );
//
//        assertThat(site.breakthroughs())
//                .containsExactlyInAnyOrderElementsOf(expected);
//    }
//
//    @Test
//    public void defaultingBehaviour() {
//
//        LandingSite test = ImmutableLandingSite.builder()
//                .latitude(latitude)
//                .northSouth(northSouth)
//                .longitude(longitude)
//                .eastWest(eastWest)
//                .topography(topography)
//                .difficulty(difficulty)
//                .altitude(altitude)
//                .temperature(temperature)
//                .metals(metals)
////                .rareMetals(rareMetals) missing for test
//                .concrete(concrete)
//                .water(water)
//                .dustDevils(dustDevils)
//                .dustStorms(dustStorms)
//                .meteors(meteors)
//                .coldWaves(coldWaves)
//                .mapName(mapName)
////                .namedLocation(namedLocation) missing for test
//                .breakthrough1(breakthrough1)
//                .breakthrough2(breakthrough2)
//                .breakthrough3(breakthrough3)
//                .breakthrough4(breakthrough4)
//                .breakthrough5(breakthrough5)
//                .breakthrough6(breakthrough6)
//                .breakthrough7(breakthrough7)
//                .breakthrough8(breakthrough8)
//                .breakthrough9(breakthrough9)
//                .breakthrough10(breakthrough10)
//                .breakthrough11(breakthrough11)
//                .breakthrough12(breakthrough12)
//                .build();
//
//        assertThat(test.rareMetals()).isEqualTo(metals);
//        assertThat(test.namedLocation()).isEqualTo(UNNAMED);
//
//    }
//
//
//    @Test
//    public void checks() {
//
//        ImmutableLandingSite.Builder builder = ImmutableLandingSite.builder().from(site);
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.latitude(-2).build())
//                .withMessageContaining("Latitude out of Range");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.longitude(181).build())
//                .withMessageContaining("Longitude out of Range");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.eastWest(NORTH).build())
//                .withMessageContaining("EastWest invalid");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.northSouth(EAST).build())
//                .withMessageContaining("NorthSouth invalid");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.difficulty(10).build())
//                .withMessageContaining("Difficulty out of range");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.altitude(-10000).build())
//                .withMessageContaining("Altitude out of range");
//
//        assertThatExceptionOfType(AssertJMultipleFailuresError.class)
//                .isThrownBy(() -> builder.temperature(10).build())
//                .withMessageContaining("Temperature out of range");
//
//
//    }
//
//
//    @Test
//    public void prettyString() {
//        String text = site.toPrettyString();
////        System.out.println(text);
//        assertThat(text).contains(String.format("\"id\" : %d", site.id()));
//        assertThat(text).contains(String.format("\"latitude\" : %d", latitude));
//        assertThat(text).contains(String.format("\"northSouth\" : \"%s\"", northSouth));
//        assertThat(text).contains(String.format("\"longitude\" : %d", longitude));
//        assertThat(text).contains(String.format("\"eastWest\" : \"%s\"", eastWest));
//        assertThat(text).contains(String.format("\"shortFormatted\" : \"%s\"", site.location().shortFormatted()));
//
//        assertThat(text).contains(String.format("\"topography\" : \"%s\"", topography));
//        assertThat(text).contains(String.format("\"challengeDifficulty\" : %d", difficulty));
//        assertThat(text).contains(String.format("\"altitude\" : %d", altitude));
//        assertThat(text).contains(String.format("\"temperature\" : %d", temperature));
//        assertThat(text).contains(String.format("\"mapName\" : \"%s\"", mapName));
//        assertThat(text).contains(String.format("\"namedLocation\" : \"%s\"", namedLocation));
//
//        assertThat(text).contains(String.format("\"METAL\" : \"%s\"", metals));
//        assertThat(text).contains(String.format("\"RARE_METAL\" : \"%s\"", rareMetals));
//        assertThat(text).contains(String.format("\"WATER\" : \"%s\"", water));
//        assertThat(text).contains(String.format("\"CONCRETE\" : \"%s\"", concrete));
//
//        assertThat(text).contains(String.format("\"DUST_DEVILS\" : \"%s\"", dustDevils));
//        assertThat(text).contains(String.format("\"DUST_STORMS\" : \"%s\"", dustStorms));
//        assertThat(text).contains(String.format("\"METEORS\" : \"%s\"", meteors));
//        assertThat(text).contains(String.format("\"COLD_WAVES\" : \"%s\"", coldWaves));
//
//        assertThat(
//                Arrays.stream(text.split("\n")).toList().stream()
//                        .filter(i -> i.contains("breakthroughs"))
//                        .findFirst().orElseThrow())
//                .contains(
//                        site.breakthroughs().stream().map(Enum::name).toList());
//
//    }
//
//    @Test
//    public void difficultyScore() {
//        int i = site.difficultyScore();
//
//        double av = IntStream.of(
//                site.mapDetails().difficulty(),
//                site.resources().difficulty(),
//                site.disasters().difficulty()
//        ).average().orElseThrow();
//
//        assertThat((double)i).isCloseTo(av, Offset.offset(0.5));
//
//    }
//}