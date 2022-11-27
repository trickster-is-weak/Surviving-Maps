package uk.co.brett.surviving.model.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.Compass;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.enums.NamedLandingArea;
import uk.co.brett.surviving.io.LandingSiteFlat;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static javax.persistence.EnumType.STRING;
import static uk.co.brett.surviving.enums.Compass.NORTH;
import static uk.co.brett.surviving.enums.GameVariant.STANDARD;

@Entity
@Table(name = "Site")
@JsonSerialize
@JsonDeserialize
public class Site {

    private static final Logger LOGGER = LogManager.getLogger(Site.class);

    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @OneToOne(mappedBy = "site", cascade = CascadeType.ALL)
    MapDetails mapDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "btr_map_lut",
            joinColumns = {@JoinColumn(name = "site_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "list_id", referencedColumnName = "id")})
    @MapKeyEnumerated(STRING)
    Map<GameVariant, Breakthroughs> breakthroughHashMap = new EnumMap<>(GameVariant.class);

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    @JsonIgnore
    private Resources resources;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "disaster_id", nullable = false)
    @JsonIgnore
    private Disasters disasters;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int latitude;

    @Enumerated(EnumType.STRING)
    private Compass northSouth;

    private int longitude;

    @Enumerated(EnumType.STRING)
    private Compass eastWest;

    @Enumerated(EnumType.STRING)
    private NamedLandingArea namedLocation;

    public Site(LandingSiteFlat site, Map<GameVariant, List<Breakthrough>> btrVarMap) {
        this.latitude = site.latitude();

        if (latitude == 0 && site.northSouth().equals(Compass.SOUTH)) {
            this.northSouth = NORTH;
        } else {
            this.northSouth = site.northSouth();
        }

        this.longitude = site.longitude();
        this.eastWest = site.eastWest();
        this.namedLocation = site.namedLocation();
        this.resources = new Resources(site);
        this.disasters = new Disasters(site);
        this.mapDetails = new MapDetails(site, this);

        for (Map.Entry<GameVariant, List<Breakthrough>> entry : btrVarMap.entrySet()) {
            breakthroughHashMap.put(entry.getKey(), new Breakthroughs(btrVarMap.get(entry.getKey()), this, entry.getKey()));
        }

    }

    public Site() {

    }

    public Disasters getDisasters() {
        return disasters;
    }

    public void setDisasters(Disasters disasters) {
        this.disasters = disasters;
    }

    public MapDetails getMapDetails() {
        return mapDetails;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }

    public int getLatitude() {
        return latitude;
    }

    public Compass getNorthSouth() {
        return northSouth;
    }

    public int getLongitude() {
        return longitude;
    }

    public Compass getEastWest() {
        return eastWest;
    }

    public NamedLandingArea getNamedLocation() {
        return namedLocation;
    }

    @Override
    public String toString() {
        return "Site{" +
                "\nid=" + id +
                ", \nlatitude=" + latitude +
                ", \nnorthSouth=" + northSouth +
                ", \nlongitude=" + longitude +
                ", \neastWest=" + eastWest +
                ", \nnamedLocation=" + namedLocation +
                ", \nresources=" + resources +
                ", \ndisasters=" + disasters +
                ", \nmapDetails=" + mapDetails +
                '}';
    }

    public String toPrettyString() {
        Map<String, Object> theMap = new LinkedHashMap<>();
        theMap.put("id", id);
        theMap.put("latitude", latitude);
        theMap.put("northSouth", northSouth);
        theMap.put("longitude", longitude);
        theMap.put("eastWest", eastWest);
        theMap.put("namedLocation", namedLocation);
        theMap.put("mapDetails", mapDetails);
        theMap.put("resources", resources);
        theMap.put("disasters", disasters);
        theMap.put("breakthroughs", breakthroughHashMap.get(STANDARD));

        try {
            return ow.writeValueAsString(theMap);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error creating pretty JSON");
        }
        return theMap.toString();
    }

    public String shortFormatted() {
        return String.format("%02d%s:%03d%s", latitude, northSouth.getAbbrev(), longitude, eastWest.getAbbrev());
    }

    public List<Breakthrough> getBreakthroughs(GameVariant variant) {
        return breakthroughHashMap.getOrDefault(variant, breakthroughHashMap.get(STANDARD)).getList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return latitude == site.latitude && longitude == site.longitude && Objects.equal(id, site.id) && northSouth == site.northSouth && eastWest == site.eastWest;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, latitude, northSouth, longitude, eastWest);
    }
}
