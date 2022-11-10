package uk.co.brett.surviving.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.brett.surviving.LandingSiteTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.co.brett.surviving.filters.ComplexPrevalence.*;

class OperatorValueTest {
    OperatorValue ov;
    Operator op;

    ComplexPrevalence prev;

    @BeforeEach
    public void before() {
        op = LandingSiteTestUtil.getRandomEnum(Operator.class);
        prev = LandingSiteTestUtil.getRandomEnum(ComplexPrevalence.class);
        ov = new OperatorValue(op, prev);
    }

    @Test
    void getOp() {
        assertThat(ov.getOp()).isEqualTo(op);
    }

    @Test
    void getValue() {
        assertThat(ov.getValue()).isEqualTo(prev);
    }

    @Test
    void getValueInt() {
        assertThat(ov.getValueInt()).isEqualTo(prev.getRating());
    }

    @Test
    void getPredicate() {

        List<ComplexPrevalence> complexPrevalances = List.of(values());
        op = Operator.NO_PREFERENCE;
        prev = LOW;
        ov = new OperatorValue(op, prev);

        List<ComplexPrevalence> list = complexPrevalances.stream().filter(p -> ov.getPredicate().test(p)).toList();
        assertThat(list).contains(values());

        op = Operator.EQUAL_TO;
        ov = new OperatorValue(op, prev);

        list = complexPrevalances.stream().filter(p -> ov.getPredicate().test(p)).toList();
        assertThat(list).containsExactly(LOW);

        op = Operator.AT_LEAST;
        prev = MED;
        ov = new OperatorValue(op, prev);

        list = complexPrevalances.stream().filter(p -> ov.getPredicate().test(p)).toList();
        assertThat(list).containsExactly(MED, HIGH, VERY_HIGH);

        op = Operator.AT_MOST;
        prev = MED;
        ov = new OperatorValue(op, prev);

        list = complexPrevalances.stream().filter(p -> ov.getPredicate().test(p)).toList();
        assertThat(list).containsExactly(LOW, MED);

    }

    @Test
    void getIntPredicate() {

        List<Integer> integers = List.of(1, 2, 3, 4);
        op = Operator.NO_PREFERENCE;
        prev = LOW;
        ov = new OperatorValue(op, prev);

        List<Integer> list = integers.stream().filter(p -> ov.getIntPredicate().test(p)).toList();
        assertThat(list).contains(1, 2, 3, 4);

        op = Operator.EQUAL_TO;
        ov = new OperatorValue(op, prev);

        list = integers.stream().filter(p -> ov.getIntPredicate().test(p)).toList();
        assertThat(list).containsExactly(1);

        op = Operator.AT_LEAST;
        prev = MED;
        ov = new OperatorValue(op, prev);

        list = integers.stream().filter(p -> ov.getIntPredicate().test(p)).toList();
        assertThat(list).containsExactly(2, 3, 4);

        op = Operator.AT_MOST;
        prev = MED;
        ov = new OperatorValue(op, prev);

        list = integers.stream().filter(p -> ov.getIntPredicate().test(p)).toList();
        assertThat(list).containsExactly(1, 2);

    }

    @Test
    void testEquals() {
        assertThat(ov.equals(ov)).isTrue();
        assertThat(ov.equals("this")).isFalse();
        assertThat(ov.equals(null)).isFalse();
        OperatorValue ov2 = new OperatorValue(op, prev);
        assertThat(ov.equals(ov2)).isTrue();
    }

    @Test
    void testHashCode() {
        OperatorValue ov2 = new OperatorValue(op, prev);
        assertThat(ov).hasSameHashCodeAs(ov2);
        List<ComplexPrevalence> list = new ArrayList<>(LandingSiteTestUtil.getAssortedEnums(ComplexPrevalence.class, 2));
        list.remove(prev);
        ov2.setValue(list.get(0));
        assertThat(ov.hashCode()).isNotEqualTo(ov2.hashCode());
    }
}