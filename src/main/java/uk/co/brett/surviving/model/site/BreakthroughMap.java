package uk.co.brett.surviving.model.site;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.co.brett.surviving.enums.GameVariant.*;

@Entity
@Table(name = "Breakthrough_Map")
@JsonSerialize
@JsonDeserialize
public class BreakthroughMap {

    @Id
    @Enumerated(value = EnumType.STRING)
    private Breakthrough breakthrough;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> standard;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> greenPlanet;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> belowBeyond;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> belowBeyondGreenPlanet;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> titoGreenPlanet;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    private List<Long> evansGreenPlanet;

    protected BreakthroughMap() {
    }

    public BreakthroughMap(Breakthrough bt, Map<GameVariant, List<Long>> varMap) {
        this.breakthrough = bt;
        this.standard = (varMap.getOrDefault(STANDARD, new ArrayList<>()));
        this.greenPlanet = (varMap.getOrDefault(GREEN_PLANET, new ArrayList<>()));
        this.belowBeyond = (varMap.getOrDefault(BELOW_BEYOND, new ArrayList<>()));
        this.belowBeyondGreenPlanet = (varMap.getOrDefault(BEYOND_GREEN, new ArrayList<>()));
        this.titoGreenPlanet = (varMap.getOrDefault(TITO_GREEN_PLANET, new ArrayList<>()));
        this.evansGreenPlanet = (varMap.getOrDefault(EVANS_GREEN_PLANET, new ArrayList<>()));
    }

    public List<Long> getTitoGreenPlanet() {
        return titoGreenPlanet;
    }

    public List<Long> getBelowBeyond() {
        return belowBeyond;
    }

    public List<Long> getBelowBeyondGreenPlanet() {
        return belowBeyondGreenPlanet;
    }

    public List<Long> getGreenPlanet() {
        return greenPlanet;
    }

    public Breakthrough getBreakthrough() {
        return breakthrough;
    }

    public List<Long> getStandard() {
        return standard;
    }

    public List<Long> getEvansGreenPlanet() {
        return evansGreenPlanet;
    }

    public List<Long> getIdList(GameVariant variant) {

        return switch (variant) {
            case STANDARD -> (getStandard());
            case BEYOND_GREEN -> getBelowBeyondGreenPlanet();
            case GREEN_PLANET -> (getGreenPlanet());
            case BELOW_BEYOND -> getBelowBeyond();
            case TITO_GREEN_PLANET -> getTitoGreenPlanet();
            case EVANS_GREEN_PLANET -> getEvansGreenPlanet();
        };
    }

    @Override
    public String toString() {
        return "BreakthroughMap{" + "breakthrough=" + breakthrough + ", standard='" + standard + '\'' + ", greenPlanet='" + greenPlanet + '\'' + ", belowBeyond='" + belowBeyond + '\'' + ", belowBeyondGreenPlanet='" + belowBeyondGreenPlanet + '\'' + ", titoGreenPlanet='" + titoGreenPlanet + '\'' + ", evansGreenPlanet='" + evansGreenPlanet + '\'' + '}';
    }
}
