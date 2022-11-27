package uk.co.brett.surviving.io;

import com.google.common.hash.Hashing;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.file.FileHashes;
import uk.co.brett.surviving.io.file.InputFile;
import uk.co.brett.surviving.io.model.InputHash;
import uk.co.brett.surviving.io.model.QInputHash;
import uk.co.brett.surviving.io.repo.InputHashRepo;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class IngestChecker {
    private static final Logger LOGGER = LogManager.getLogger(IngestChecker.class);

    private final Map<GameVariant, InputFile> inputFileMap;
    private final JPAQueryFactory queryFactory;

    private final InputHashRepo hashRepo;

    private final Map<GameVariant, InputFile> regen;

    @Value("${data.ignoreFileIntegrity:false}")
    private boolean ignoreIntegrity;

    @Autowired
    public IngestChecker(EntityManager entityManager, FileHashes fileHashes, InputHashRepo hashRepo) {

        this.inputFileMap = fileHashes.getMap();
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.hashRepo = hashRepo;
        regen = regenerateHashes(inputFileMap);
    }

    private Map<GameVariant, InputFile> regenerateHashes(Map<GameVariant, InputFile> map) {
        Map<GameVariant, InputFile> tempMap = new EnumMap<>(GameVariant.class);
        for (GameVariant variant : GameVariant.values()) {
            String location = map.get(variant).getResourceLocation();
            String hash = generateHash(location);
            InputFile inputFile = new InputFile(location, hash);
            tempMap.put(variant, inputFile);
        }

        return tempMap;
    }

    public boolean checkIntegrity() {

        boolean fileIntegrity = checkInputFileIntegrity(regen);
        boolean databaseIntegrity = checkDatabaseIntegrity(regen);

        LOGGER.info("File Integrity Conditions:     {} {}", fileIntegrity, ignoreIntegrity ? "but ignored" : "");
        LOGGER.info("Database Integrity Conditions: {}", databaseIntegrity);

        return (fileIntegrity || ignoreIntegrity) && databaseIntegrity;

    }

    boolean checkDatabaseIntegrity(Map<GameVariant, InputFile> map) {

        List<InputHash> inputHashes = queryFactory.selectFrom(QInputHash.inputHash).fetch();
        Map<GameVariant, InputFile> dbMap = createMap(inputHashes);

        for (GameVariant variant : GameVariant.values()) {

            if (!map.get(variant).equals(dbMap.getOrDefault(variant, new InputFile()))) {
                LOGGER.warn("Database Integrity Check Failed for {}", variant);
//                LOGGER.info(map.get(variant).getHash());
//                LOGGER.info(dbMap.get(variant).getHash());
                LOGGER.warn("This suggests the database is out of date");
                LOGGER.warn("Database will be reinitialised");
                LOGGER.warn("This may take several minutes");
                return false;
            }
            LOGGER.debug("Database Integrity Check Passed for {}", variant);
        }
        LOGGER.info("Database Integrity Checks Passed");
        return true;
    }

    boolean checkInputFileIntegrity(Map<GameVariant, InputFile> regenMap) {

        for (GameVariant variant : GameVariant.values()) {
            if (!inputFileMap.get(variant).equals(regenMap.getOrDefault(variant, new InputFile()))) {
                LOGGER.warn("File Integrity Check Failed for {}", variant);
                LOGGER.warn("Expected hash   : {}", inputFileMap.get(variant).getHash());
                LOGGER.warn("Calculated hash : {}", regenMap.get(variant).getHash());
                LOGGER.warn("This suggests one of the input files has been edited");
                if (!ignoreIntegrity) {
                    LOGGER.warn("If this is intentional, please set the 'data.ignoreFileIntegrity' property in application.settings");
                    LOGGER.warn("This will result in the database reloading on each run");
                }
                return false;
            }

            LOGGER.debug("File Integrity Check Passed for {}", variant);
        }
        LOGGER.info("File Integrity Checks Passed");
        return true;
    }

    Map<GameVariant, InputFile> createMap(List<InputHash> hashList) {
        Map<GameVariant, InputFile> map = new EnumMap<>(GameVariant.class);

        for (InputHash hash : hashList) {
            GameVariant variant = hash.getVariant();
            InputFile file = new InputFile(hash.getFilename(), hash.getHash());
            map.put(variant, file);
        }

        return map;
    }


    public String generateHash(String s) {

        try {
            String originalString = IOUtils.resourceToString(s, StandardCharsets.UTF_8);

            return Hashing.sha256()
                    .hashString(originalString, StandardCharsets.UTF_8)
                    .toString();

        } catch (IOException e) {
            throw new IngestException(e.getMessage());
        }
    }

    public void populateInputHashTable() {

        for (GameVariant variant : GameVariant.values()) {
            InputHash inputHash = new InputHash(variant, regen.get(variant));
            hashRepo.save(inputHash);
        }

        hashRepo.flush();
    }
}
