package uk.co.brett.surviving.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import uk.co.brett.surviving.enums.MapName;
import uk.co.brett.surviving.enums.NamedLandingArea;
import uk.co.brett.surviving.enums.Topography;
import uk.co.brett.surviving.io.LandingSiteFlat;

import javax.persistence.*;

@Entity
@Table(name = "Map_Details")
@JsonSerialize
@JsonDeserialize
public class MapDetails {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    @JsonIgnore
    Site site;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int altitude;

    private int temperature;

    private int challengeDifficulty;

    @Enumerated(EnumType.STRING)
    private Topography topography;

    @Enumerated(EnumType.STRING)
    private MapName mapName;

    @Enumerated(EnumType.STRING)
    private NamedLandingArea namedLocation;

    public MapDetails() {

    }

    public MapDetails(LandingSiteFlat flat, Site site) {
        this.altitude = flat.altitude();
        this.temperature = flat.temperature();
        this.challengeDifficulty = flat.difficulty();
        this.topography = flat.topography();
        this.mapName = flat.mapName();
        this.namedLocation = flat.namedLocation();
        this.site = site;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getChallengeDifficulty() {
        return challengeDifficulty;
    }

    public void setChallengeDifficulty(int challengeDifficulty) {
        this.challengeDifficulty = challengeDifficulty;
    }

    public Topography getTopography() {
        return topography;
    }

    public void setTopography(Topography topography) {
        this.topography = topography;
    }

    public MapName getMapName() {
        return mapName;
    }

    public void setMapName(MapName mapName) {
        this.mapName = mapName;
    }

    public NamedLandingArea getNamedLocation() {
        return namedLocation;
    }

    public void setNamedLocation(NamedLandingArea namedLocation) {
        this.namedLocation = namedLocation;
    }

    @Override
    public String toString() {
        return "MapDetails{" +
                "id=" + id +
                ", altitude=" + altitude +
                ", temperature=" + temperature +
                ", namedLocation=" + namedLocation +
                ", topography=" + topography +
                ", challengeDifficulty=" + challengeDifficulty +
                ", mapName=" + mapName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapDetails that = (MapDetails) o;
        return altitude == that.altitude && temperature == that.temperature && challengeDifficulty == that.challengeDifficulty && topography == that.topography && mapName == that.mapName && namedLocation == that.namedLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(altitude, temperature, challengeDifficulty, topography, mapName, namedLocation);
    }
}
