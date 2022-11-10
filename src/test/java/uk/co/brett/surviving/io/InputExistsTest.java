package uk.co.brett.surviving.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import uk.co.brett.surviving.config.TestConfig;
import uk.co.brett.surviving.enums.GameVariant;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@SpringBootTest(classes = TestConfig.class)
class InputExistsTest {

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    private InputService inputService;

    @Autowired
    private IngestChecker ingestChecker;

    @Test
    public void test() throws NoSuchAlgorithmException, IOException {

        Map<GameVariant, InputFile> map = inputService.getMap();
        map.forEach((key, value) -> System.out.println(value.getHash()));

        String s = map.get(GameVariant.STANDARD).getResourceLocation();

//        InputStream stream = LandingSitesFlat.class.getResourceAsStream(s);

        String originalString = IOUtils.resourceToString(s, StandardCharsets.UTF_8);

        String sha256hex = Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();

        System.out.println(sha256hex);
        System.out.println(map.get(GameVariant.STANDARD).getHash());

        Ingest ingest = ImmutableIngest.builder().map(map).complete(true).build();
        System.out.println(ingest);
        FileWriter ingestFile = new FileWriter(inputService.getIngestCheck());
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(ingestFile, ingest);
        ingestFile.close();

    }

    @Test
    public void ingest() {
         
    }
}

