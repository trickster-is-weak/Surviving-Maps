package uk.co.brett.surviving.filters;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;

import java.util.List;

public class SimpleFilterRequest {

    private GameVariant variant;

    private Prevalence resources;

    private Prevalence disasters;

    private List<Breakthrough> breakthroughs;

    public SimpleFilterRequest() {
        variant = GameVariant.STANDARD;
        resources = Prevalence.NA;
        disasters = Prevalence.NA;
        breakthroughs = List.of();
    }

    public GameVariant getVariant() {
        return variant;
    }

    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    public Prevalence getResources() {
        return resources;
    }

    public void setResources(Prevalence resources) {
        this.resources = resources;
    }

    public Prevalence getDisasters() {
        return disasters;
    }

    public void setDisasters(Prevalence disasters) {
        this.disasters = disasters;
    }

    public List<Breakthrough> getBreakthroughs() {
        return breakthroughs;
    }

    public void setBreakthroughs(List<Breakthrough> breakthroughs) {
        this.breakthroughs = breakthroughs;
    }

    @Override
    public String toString() {
        return "SimpleFilterRequest{" +
                "\nvariant=" + variant +
                ", \nresources=" + resources +
                ", \ndisasters=" + disasters +
                ", \nbreakthroughs=" + breakthroughs +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleFilterRequest that = (SimpleFilterRequest) o;

        return new EqualsBuilder().append(variant, that.variant).append(resources, that.resources).append(disasters, that.disasters).append(breakthroughs, that.breakthroughs).isEquals();
    }


    public boolean equalsNoVariant(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SimpleFilterRequest that = (SimpleFilterRequest) o;

        return new EqualsBuilder().append(resources, that.resources).append(disasters, that.disasters).append(breakthroughs, that.breakthroughs).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(variant).append(resources).append(disasters).append(breakthroughs).toHashCode();
    }
}
