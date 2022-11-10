package uk.co.brett.surviving.model.site;

import uk.co.brett.surviving.enums.DisasterType;
import uk.co.brett.surviving.io.LandingSiteFlat;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Disasters")
public class Disasters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dustDevils;

    private int dustStorms;

    private int meteors;

    private int coldWaves;

    private int sum;

    public Disasters() {

    }

    public Disasters(LandingSiteFlat flat) {
        this.dustDevils = flat.dustDevils();
        this.dustStorms = flat.dustStorms();
        this.meteors = flat.meteors();
        this.coldWaves = flat.coldWaves();
        this.sum = dustDevils + dustStorms + meteors + coldWaves;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }


    public int getDustDevils() {
        return dustDevils;
    }

    public void setDustDevils(int dustDevils) {
        this.dustDevils = dustDevils;
    }

    public int getDustStorms() {
        return dustStorms;
    }

    public void setDustStorms(int dustStorms) {
        this.dustStorms = dustStorms;
    }

    public int getMeteors() {
        return meteors;
    }

    public void setMeteors(int meteors) {
        this.meteors = meteors;
    }

    public int getColdWaves() {
        return coldWaves;
    }

    public void setColdWaves(int coldWaves) {
        this.coldWaves = coldWaves;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Disasters{" +
                "id=" + id +
                ", dustDevils=" + dustDevils +
                ", dustStorms=" + dustStorms +
                ", meteors=" + meteors +
                ", coldWaves=" + coldWaves +
                ", sum=" + sum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disasters disasters = (Disasters) o;
        return dustDevils == disasters.dustDevils &&
                dustStorms == disasters.dustStorms &&
                meteors == disasters.meteors &&
                coldWaves == disasters.coldWaves;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dustDevils, dustStorms, meteors, coldWaves);
    }

    public int get(DisasterType type) {
        return switch (type) {
            case DUST_DEVILS -> getDustDevils();
            case DUST_STORMS -> getDustStorms();
            case METEORS -> getMeteors();
            case COLD_WAVES -> getColdWaves();
        };
    }
}
