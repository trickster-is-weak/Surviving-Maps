package uk.co.brett.surviving.io.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.io.file.FileHashes;
import uk.co.brett.surviving.io.file.InputFile;
import uk.co.brett.surviving.services.LandingSitesFlat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "data.live", havingValue = "false")
public class TestInputConfig {

    private static final Logger LOGGER = LogManager.getLogger(TestInputConfig.class);

    @Bean
    public DatabaseSettings testData() {
        return DatabaseSettings.of("database", "test");
    }

    @Bean
    FileHashes getTestFiles() throws IOException {

        LOGGER.info("");
        LOGGER.info("    ████████╗███████╗███████╗████████╗    ██████╗  █████╗ ████████╗ █████╗     ");
        LOGGER.info("    ╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝    ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗    ");
        LOGGER.info("       ██║   █████╗  ███████╗   ██║       ██║  ██║███████║   ██║   ███████║    ");
        LOGGER.info("       ██║   ██╔══╝  ╚════██║   ██║       ██║  ██║██╔══██║   ██║   ██╔══██║    ");
        LOGGER.info("       ██║   ███████╗███████║   ██║       ██████╔╝██║  ██║   ██║   ██║  ██║    ");
        LOGGER.info("       ╚═╝   ╚══════╝╚══════╝   ╚═╝       ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝    ");
        LOGGER.info("");


        String testInput = "/TestInput.json";
        InputStream fis = LandingSitesFlat.class.getResource(testInput).openStream();
        ObjectMapper mapper = new ObjectMapper().registerModule(new GuavaModule());
        MapType t = mapper.getTypeFactory().constructMapType(Map.class, GameVariant.class, InputFile.class);

        return new FileHashes(mapper.readValue(fis, t));
    }


}
