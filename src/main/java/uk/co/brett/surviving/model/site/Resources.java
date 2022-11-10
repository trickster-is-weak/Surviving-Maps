package uk.co.brett.surviving.model.site;

import uk.co.brett.surviving.enums.ResourceType;
import uk.co.brett.surviving.io.LandingSiteFlat;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Resources")
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int metal;
    private int rareMetal;
    private int water;
    private int concrete;
    private int sum;

    public Resources() {
    }

    public Resources(LandingSiteFlat flat) {
        this.metal = flat.metals();
        this.rareMetal = flat.defaultRareMetals();
        this.water = flat.water();
        this.concrete = flat.concrete();
        sum = metal + rareMetal + water + concrete;
    }

    public Long getId() {
        return id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metals) {
        this.metal = metals;
    }

    public int getRareMetal() {
        return rareMetal;
    }

    public void setRareMetal(int rareMetals) {
        this.rareMetal = rareMetals;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getConcrete() {
        return concrete;
    }

    public void setConcrete(int concrete) {
        this.concrete = concrete;
    }

    @Override
    public String toString() {
        return "Resources{" +
                "id=" + id +
                ", metals=" + metal +
                ", rareMetals=" + rareMetal +
                ", water=" + water +
                ", concrete=" + concrete +
                ", sum=" + sum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resources resources = (Resources) o;
        return metal == resources.metal &&
                rareMetal == resources.rareMetal &&
                water == resources.water &&
                concrete == resources.concrete;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metal, rareMetal, water, concrete);
    }

    public int get(ResourceType type) {

        return switch (type) {
            case METAL -> getMetal();
            case RARE_METAL -> getRareMetal();
            case WATER -> getWater();
            case CONCRETE -> getConcrete();
        };

    }
}
