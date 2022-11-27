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
@ConditionalOnProperty(value = "data.live", havingValue = "true", matchIfMissing = true)
public class LiveInputConfig {

    private static final Logger LOGGER = LogManager.getLogger(LiveInputConfig.class);

    @Bean
    public DatabaseSettings liveData() {
        return DatabaseSettings.of("database", "live");
    }


    @Bean
    FileHashes getFiles() throws IOException {

        LOGGER.info("");
        LOGGER.info("    ██╗     ██╗██╗   ██╗███████╗    ██████╗  █████╗ ████████╗ █████╗     ");
        LOGGER.info("    ██║     ██║██║   ██║██╔════╝    ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗    ");
        LOGGER.info("    ██║     ██║██║   ██║█████╗      ██║  ██║███████║   ██║   ███████║    ");
        LOGGER.info("    ██║     ██║╚██╗ ██╔╝██╔══╝      ██║  ██║██╔══██║   ██║   ██╔══██║    ");
        LOGGER.info("    ███████╗██║ ╚████╔╝ ███████╗    ██████╔╝██║  ██║   ██║   ██║  ██║    ");
        LOGGER.info("    ╚══════╝╚═╝  ╚═══╝  ╚══════╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝    ");
        LOGGER.info("");

        String input = "/InputMap.json";
        InputStream fis = LandingSitesFlat.class.getResource(input).openStream();
        ObjectMapper mapper = new ObjectMapper().registerModule(new GuavaModule());
        MapType t = mapper.getTypeFactory().constructMapType(Map.class, GameVariant.class, InputFile.class);

        return new FileHashes(mapper.readValue(fis, t));
    }

}
