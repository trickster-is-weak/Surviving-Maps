package uk.co.brett.surviving.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.immutables.value.Value;
import uk.co.brett.surviving.enums.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "latitude",
        "northSouth",
        "longitude",
        "eastWest",
        "topography",
        "difficulty",
        "altitude",
        "temperature",
        "metals",
        "rareMetals",
        "concrete",
        "water",
        "dustDevils",
        "dustStorms",
        "meteors",
        "coldWaves",
        "mapName",
        "namedLocation",
        "breakthrough1",
        "breakthrough2",
        "breakthrough3",
        "breakthrough4",
        "breakthrough5",
        "breakthrough6",
        "breakthrough7",
        "breakthrough8",
        "breakthrough9",
        "breakthrough10",
        "breakthrough11",
        "breakthrough12",
        "breakthrough13",
        "breakthrough14",
        "breakthrough15",
        "breakthrough16",
        "breakthrough17"
})

public abstract class LandingSiteFlat {

    public static final String RESOURCE_OUT_OF_RANGE = "Resource out of Range";
    public static final String DISASTER_OUT_OF_RANGE = "Disaster out of Range";

    public abstract int latitude();

    public abstract Compass northSouth();

    public abstract int longitude();

    public abstract Compass eastWest();

    public abstract Topography topography();

    public abstract int difficulty();

    public abstract int altitude();

    public abstract MapName mapName();

    @Value.Default
    public NamedLandingArea namedLocation() {
        return NamedLandingArea.UNNAMED;
    }

    public abstract int temperature();

    public abstract int metals();

    @Nullable
    abstract Integer rareMetals();

    @Value.Derived
    public int defaultRareMetals() {
        Integer v = rareMetals();
        return v != null ? v : metals();
    }

    public abstract int concrete();

    public abstract int water();

    public abstract int dustDevils();

    public abstract int dustStorms();

    public abstract int meteors();

    public abstract int coldWaves();

    public abstract Breakthrough breakthrough1();

    public abstract Breakthrough breakthrough2();

    public abstract Breakthrough breakthrough3();

    public abstract Breakthrough breakthrough4();

    public abstract Breakthrough breakthrough5();

    public abstract Breakthrough breakthrough6();

    public abstract Breakthrough breakthrough7();

    public abstract Breakthrough breakthrough8();

    public abstract Breakthrough breakthrough9();

    public abstract Breakthrough breakthrough10();

    public abstract Breakthrough breakthrough11();

    public abstract Breakthrough breakthrough12();

    public abstract Optional<Breakthrough> breakthrough13();

    public abstract Optional<Breakthrough> breakthrough14();

    public abstract Optional<Breakthrough> breakthrough15();

    public abstract Optional<Breakthrough> breakthrough16();

    public abstract Optional<Breakthrough> breakthrough17();

    @Value.Check
    protected void check() {
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {

            softly.assertThat(metals()).as(RESOURCE_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(concrete()).as(RESOURCE_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(water()).as(RESOURCE_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(dustDevils()).as(DISASTER_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(dustStorms()).as(DISASTER_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(meteors()).as(DISASTER_OUT_OF_RANGE).isBetween(1, 4);
            softly.assertThat(coldWaves()).as(DISASTER_OUT_OF_RANGE).isBetween(1, 4);

            softly.assertThat(latitude()).as("Latitude out of Range").isBetween(0, 90);
            softly.assertThat(longitude()).as("Longitude out of Range").isBetween(0, 180);
            softly.assertThat(eastWest()).as("EastWest invalid").isIn(Compass.EAST, Compass.WEST);
            softly.assertThat(northSouth()).as("NorthSouth invalid").isIn(Compass.NORTH, Compass.SOUTH);

            softly.assertThat(difficulty()).as("Difficulty out of range").isBetween(100, 240);
            softly.assertThat(altitude()).as("Altitude out of range").isBetween(-9000, 25000);
            softly.assertThat(temperature()).as("Temperature out of range").isBetween(-100, 0);
        }
    }

    public List<Breakthrough> getBreakthroughs() {
        List<Breakthrough> l = new ArrayList<>(
                List.of(breakthrough1(),
                        breakthrough2(),
                        breakthrough3(),
                        breakthrough4(),
                        breakthrough5(),
                        breakthrough6(),
                        breakthrough7(),
                        breakthrough8(),
                        breakthrough9(),
                        breakthrough10(),
                        breakthrough11(),
                        breakthrough12()
                ));

        List<Optional<Breakthrough>> l2 = List.of(
                breakthrough13(),
                breakthrough14(),
                breakthrough15(),
                breakthrough16(),
                breakthrough17()
        );

        l2.stream().filter(Optional::isPresent).map(Optional::get).forEach(l::add);

        return l;
    }

    public String shortFormatted() {
        return String.format("%02d%s:%03d%s", latitude(), northSouth().getAbbrev(), longitude(), eastWest().getAbbrev());
    }
}
