package uk.co.brett.surviving.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.hash.Hashing;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.GameVariant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

@Service
public class IngestChecker {
    private static final Logger LOGGER = LogManager.getLogger(IngestChecker.class);
    private final InputService inputService;

    @Autowired
    public IngestChecker(InputService inputService) {
        this.inputService = inputService;
    }

    public void createIngestFile() {
        ImmutableIngest.Builder builder = ImmutableIngest.builder().complete(true);
        Map<GameVariant, InputFile> map = new EnumMap<>(GameVariant.class);
        builder.map(map);
        for (GameVariant variant : GameVariant.values()) {
            InputFile file = inputService.getMap().get(variant);
            String hash = generateHash(file.getResourceLocation());

            if (!hash.equals(file.getHash())) {
                LOGGER.warn("Hash Mismatch between parsed and expected on file {}", file.getResourceLocation());
            }

            InputFile inputFile = new InputFile(file.getResourceLocation(), hash);
            map.put(variant, inputFile);
        }

        FileUtils.deleteQuietly(new File(inputService.getIngestCheck()));

        try (FileWriter ingestFile = new FileWriter(inputService.getIngestCheck())) {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(ingestFile, builder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateHash(String s) {

        try {
            String originalString = IOUtils.resourceToString(s, StandardCharsets.UTF_8);

            return Hashing.sha256()
                    .hashString(originalString, StandardCharsets.UTF_8)
                    .toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkExisting() {
        try {
            File previousIngest = new File(inputService.getIngestCheck());
            if (previousIngest.exists()) {
                Ingest ingest = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new GuavaModule()).readerFor(Ingest.class).readValue(previousIngest);
                System.out.println(ingest);
                if (ingest.complete()) {
                    if (ingest.map().equals(inputService.getMap())) return true;
                }
            }

        } catch (IOException e) {
            return false;
        }
        return false;


    }

}
