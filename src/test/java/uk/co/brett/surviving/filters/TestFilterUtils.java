package uk.co.brett.surviving.filters;

import uk.co.brett.surviving.LandingSiteTestUtil;

import java.util.List;

import static uk.co.brett.surviving.enums.Breakthrough.AUTONOMOUS_HUBS;
import static uk.co.brett.surviving.enums.Breakthrough.EXTRACTOR_AI;
import static uk.co.brett.surviving.enums.GameVariant.STANDARD;
import static uk.co.brett.surviving.enums.MapName.BLANK_1;
import static uk.co.brett.surviving.enums.MapName.CANYON_2;
import static uk.co.brett.surviving.enums.NamedLandingArea.ELYSIUM_MONS;
import static uk.co.brett.surviving.enums.NamedLandingArea.UNNAMED;
import static uk.co.brett.surviving.enums.Topography.RELATIVELY_FLAT;
import static uk.co.brett.surviving.enums.Topography.STEEP;
import static uk.co.brett.surviving.filters.ComplexPrevalence.HIGH;
import static uk.co.brett.surviving.filters.ComplexPrevalence.MED;
import static uk.co.brett.surviving.filters.Difficulties.D120;
import static uk.co.brett.surviving.filters.Operator.AT_LEAST;
import static uk.co.brett.surviving.filters.Operator.EQUAL_TO;

public class TestFilterUtils {
    public static ComplexFilterRequest createComplex() {
        ComplexFilterRequest complex = new ComplexFilterRequest();
        complex.setVariant(STANDARD);
        complex.setBreakthroughs(List.of(EXTRACTOR_AI, AUTONOMOUS_HUBS));
        complex.setMapDifficultiesOp(AT_LEAST);
        complex.setMapDifficultiesInt(D120);
        complex.setNamedLandingAreas(List.of(ELYSIUM_MONS, UNNAMED));
        complex.setTopographies(List.of(STEEP, RELATIVELY_FLAT));
        complex.setMapNames(List.of(BLANK_1, CANYON_2));
        complex.setWater(new OperatorValue(AT_LEAST, MED));
        complex.setConcrete(new OperatorValue(AT_LEAST, MED));
        complex.setMetals(new OperatorValue(EQUAL_TO, HIGH));
        complex.setRareMetals(new OperatorValue(AT_LEAST, MED));
        complex.setMeteors(new OperatorValue(AT_LEAST, MED));
        complex.setColdWaves(new OperatorValue(AT_LEAST, MED));
        complex.setDustStorms(new OperatorValue(AT_LEAST, MED));
        complex.setDustDevils(new OperatorValue(AT_LEAST, MED));

        return complex;
    }

    public static Operator getOperator() {
        return LandingSiteTestUtil.getRandomEnum(Operator.class);
    }

    public static OperatorValue getOperatorValue() {
        return new OperatorValue(getOperator(), LandingSiteTestUtil.getRandomEnum(ComplexPrevalence.class));
    }
}
