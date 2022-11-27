package uk.co.brett.surviving.services;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.ImmutableLandingSiteFlat;
import uk.co.brett.surviving.io.IngestException;
import uk.co.brett.surviving.io.LandingSiteFlat;
import uk.co.brett.surviving.io.file.CsvReader;
import uk.co.brett.surviving.io.file.FileHashes;
import uk.co.brett.surviving.io.file.InputFile;
import uk.co.brett.surviving.model.repo.BreakthroughMapRepo;
import uk.co.brett.surviving.model.repo.SiteRepo;
import uk.co.brett.surviving.model.service.DisastersService;
import uk.co.brett.surviving.model.service.ResourcesService;
import uk.co.brett.surviving.model.site.BreakthroughMap;
import uk.co.brett.surviving.model.site.Disasters;
import uk.co.brett.surviving.model.site.Resources;
import uk.co.brett.surviving.model.site.Site;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static uk.co.brett.surviving.enums.GameVariant.STANDARD;

//@Component
public class LandingSitesFlat implements InitializingBean, DisposableBean, AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(LandingSitesFlat.class);
    private final SiteRepo siteRepo;
    private final BreakthroughMapRepo breakthroughMapRepo;
    private final ResourcesService resourcesService;
    private final DisastersService disastersService;
    private final FileHashes inputFiles;
    private final ThreadPoolTaskExecutor ioPoolTaskExecutor;
    private Set<Resources> resourcesSet = new HashSet<>();
    private Set<Disasters> disastersSet = new HashSet<>();
    private BiMap<Resources, Long> resourcesMap = HashBiMap.create();
    private BiMap<Disasters, Long> disastersMap = HashBiMap.create();
    private Map<Breakthrough, Map<GameVariant, List<Long>>> superMap = new EnumMap<>(Breakthrough.class);

    public LandingSitesFlat(SiteRepo siteRepo, BreakthroughMapRepo breakthroughMapRepo, ResourcesService resourcesService, DisastersService disastersService, FileHashes inputFiles, ThreadPoolTaskExecutor ioPoolTaskExecutor) {
        this.siteRepo = siteRepo;
        this.breakthroughMapRepo = breakthroughMapRepo;
        this.resourcesService = resourcesService;
        this.disastersService = disastersService;
        this.inputFiles = inputFiles;
        this.ioPoolTaskExecutor = ioPoolTaskExecutor;

    }


    public CompletableFuture<Boolean> ingestInput() {
        Map<GameVariant, Map<String, LandingSiteFlat>> flatMap = getFlatMap();

        LOGGER.info("Sites size: {}", flatMap.get(STANDARD).size());
        streamSites(flatMap);

        LOGGER.info("Streamed");
        populateBreakthroughMap();
        LOGGER.info("Breakthrough");

        resourcesService.getRange();
        disastersService.getRange();

        LOGGER.info("Complete");

        return CompletableFuture.completedFuture(true);
    }

    void streamSites(Map<GameVariant, Map<String, LandingSiteFlat>> flatMap) {
        LOGGER.info("Site Stream");
        Map<String, LandingSiteFlat> sitesST = flatMap.get(STANDARD);

        Comparator<LandingSiteFlat> comparator = Comparator.comparing(LandingSiteFlat::latitude)
                .thenComparing(LandingSiteFlat::longitude);

        sitesST.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .forEach(entry -> {

                            String key = entry.getKey();
                            LandingSiteFlat flat = entry.getValue();

                            Map<GameVariant, List<Breakthrough>> btrVarMap = new EnumMap<>(GameVariant.class);
                            for (GameVariant variant : GameVariant.values()) {
                                btrVarMap.put(variant, flatMap.get(variant).get(key).getBreakthroughs());
                            }

                            Site site = new Site(flat, btrVarMap);
                            site.setResources(getResource(flat));
                            site.setDisasters(getDisaster(flat));
                            site = siteRepo.save(site);

                            Long siteId = site.getId();
                            populateGlobalMap(siteId, btrVarMap);

                            if (siteId % 5000L == 0) LOGGER.info("processed site {}", siteId);


                        }
                );


        LOGGER.info("Site Stream Complete");

    }

    Disasters getDisaster(LandingSiteFlat flat) {
        Disasters d = new Disasters(flat);
        if (disastersSet.contains(d)) {
            Long id = disastersMap.get(d);
            d = disastersMap.inverse().get(id);
        } else {
            disastersSet.add(d);
            d = disastersService.save(d);
            disastersMap.put(d, d.getId());
        }
        return d;
    }

    Resources getResource(LandingSiteFlat flat) {
        Resources r = new Resources(flat);
        if (resourcesSet.contains(r)) {
            Long id = resourcesMap.get(r);
            r = resourcesMap.inverse().get(id);
        } else {
            resourcesSet.add(r);
            r = resourcesService.save(r);
            resourcesMap.put(r, r.getId());
        }

        return r;
    }


    Map<GameVariant, Map<String, LandingSiteFlat>> getFlatMap() {

        Map<GameVariant, Map<String, LandingSiteFlat>> flatMap = new EnumMap<>(GameVariant.class);
        Map<GameVariant, InputFile> inp = inputFiles.getMap();
        Map<GameVariant, CompletableFuture<Map<String, LandingSiteFlat>>> futureMap = new EnumMap<>(GameVariant.class);


        LOGGER.info("pre async");
        for (GameVariant variant : GameVariant.values()) {
            CompletableFuture<Map<String, LandingSiteFlat>> b = CompletableFuture
                    .supplyAsync(() -> readLandingSites(inp.get(variant).getResourceLocation()), ioPoolTaskExecutor);

            futureMap.put(variant, b);
        }

        futureMap.forEach((key, value) -> flatMap.put(key, value.join()));

        LOGGER.info("returning");

        return flatMap;
    }

    Map<String, LandingSiteFlat> readLandingSites(String file) {
        try {
            LOGGER.info("{} started", file);
            InputStream fis = Objects.requireNonNull(LandingSitesFlat.class.getResource(file)).openStream();
            CsvReader reader = new CsvReader();
            Reader fileReader = new InputStreamReader(fis);

            List<LandingSiteFlat> sites = new ArrayList<>(reader.read(fileReader, ImmutableLandingSiteFlat.class));

            Map<String, LandingSiteFlat> map = new HashMap<>();

            sites.forEach(s -> map.put(s.shortFormatted(), s));
            LOGGER.info("{} finished", file);
            return map;
        } catch (IOException e) {
            throw new IngestException("IOE thrown in ingest");
        }
    }

    void populateGlobalMap(Long siteId, Map<GameVariant, List<Breakthrough>> btrVarMap) {

        for (GameVariant v : GameVariant.values()) {
            List<Breakthrough> m1 = btrVarMap.get(v);
            for (Breakthrough b : m1) {
                Map<GameVariant, List<Long>> m2 = superMap.computeIfAbsent(b, k -> new HashMap<>());
                m2.computeIfAbsent(v, k -> new ArrayList<>()).add(siteId);
            }
        }
    }


    void populateBreakthroughMap() {
        for (Breakthrough b : Breakthrough.values()) {
            Map<GameVariant, List<Long>> varMap = superMap.get(b);
            breakthroughMapRepo.save(new BreakthroughMap(b, varMap));
        }
    }

    @Override
    public void destroy() {
        resourcesSet = null;
        disastersSet = null;
        resourcesMap = null;
        disastersMap = null;
        superMap = null;

        LOGGER.info("--- destroy executed ---");
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.info("--- Properties Set executed ---");
    }

    @Override
    public void close() {
        resourcesSet = null;
        disastersSet = null;
        resourcesMap = null;
        disastersMap = null;
        superMap = null;

    }
}
