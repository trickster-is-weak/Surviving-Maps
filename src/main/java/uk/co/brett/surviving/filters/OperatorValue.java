package uk.co.brett.surviving.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.function.Predicate;

@JsonDeserialize
@JsonSerialize
public class OperatorValue {
    private Operator op;
    private ComplexPrevalence value;

    public OperatorValue(Operator op, ComplexPrevalence value) {
        this.op = op;
        this.value = value;
    }

    public OperatorValue() {
        this.op = Operator.NO_PREFERENCE;
        this.value = ComplexPrevalence.fromInt(0);
    }

    public Operator getOp() {
        return op;
    }

    public void setOp(Operator op) {
        this.op = op;
    }

    public ComplexPrevalence getValue() {
        return value;
    }

    public void setValue(ComplexPrevalence value) {
        this.value = value;
    }

    public int getValueInt() {
        return value.getRating();
    }

    public Predicate<ComplexPrevalence> getPredicate() {
        return switch (op) {
            case AT_LEAST -> i -> i.getRating() >= value.getRating();
            case AT_MOST -> i -> i.getRating() <= value.getRating();
            case EQUAL_TO -> i -> i == value;
            case NO_PREFERENCE -> i -> i.getRating() >= 0;
        };

    }

    public Predicate<Integer> getIntPredicate() {
        return switch (op) {
            case AT_LEAST -> i -> i >= value.getRating();
            case AT_MOST -> i -> i <= value.getRating();
            case EQUAL_TO -> i -> i == value.getRating();
            case NO_PREFERENCE -> i -> i >= 0;
        };

    }

    @Override
    public String toString() {
        return "OperatorValue{" +
                "op=" + op +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OperatorValue that = (OperatorValue) o;

        return new EqualsBuilder().append(op, that.op).append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(op).append(value).toHashCode();
    }

    public List<Integer> getRange() {
        List<Integer> base = List.of(1, 2, 3, 4);
        return switch (op) {
            case NO_PREFERENCE -> base;
            case AT_LEAST -> base.stream().filter(i -> i >= value.getRating()).toList();
            case AT_MOST -> base.stream().filter(i -> i <= value.getRating()).toList();
            case EQUAL_TO -> List.of(value.getRating());
        };

    }
}
