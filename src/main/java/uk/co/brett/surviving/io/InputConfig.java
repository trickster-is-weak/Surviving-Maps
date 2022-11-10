package uk.co.brett.surviving.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.services.LandingSitesFlat;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SpringBootConfiguration
public class InputConfig {

    private static final Logger LOGGER = LogManager.getLogger(InputConfig.class);

    @Bean
    @ConditionalOnProperty(
            value = "data.live",
            havingValue = "false")
    InputService getTestFiles() throws IOException {


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

        return new InputService(mapper.readValue(fis, t), "database/testIngest.json");
    }

    @Bean
    @ConditionalOnProperty(
            value = "data.live",
            havingValue = "true",
            matchIfMissing = true)
    InputService getFiles() throws IOException {

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

        return new InputService(mapper.readValue(fis, t), "database/liveIngest.json");
    }


    @Bean(name = "h2DataSource")
    @ConditionalOnProperty(value = "data.live", havingValue = "true", matchIfMissing = true)
    public DataSource h2DataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:./database/live;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("pass");
        return dataSourceBuilder.build();
    }

    @Bean(name = "h2DataSource")
    @ConditionalOnProperty(value = "data.live", havingValue = "false")
    public DataSource h2DataTestSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:./database/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("pass");
        return dataSourceBuilder.build();
    }

}
