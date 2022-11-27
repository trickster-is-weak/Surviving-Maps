package uk.co.brett.surviving.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.io.repo.InputHashRepo;
import uk.co.brett.surviving.model.service.CleanupService;
import uk.co.brett.surviving.services.LandingSitesFlat;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Service
public class IngestService {
    private static final Logger LOGGER = LogManager.getLogger(IngestService.class);
    private final ConfigurableApplicationContext applicationContext;

    private final InputHashRepo hashRepo;
    private final IngestChecker ingestChecker;
    private final CleanupService cleanupService;

    public IngestService(ConfigurableApplicationContext applicationContext, IngestChecker checker,
                         InputHashRepo hashRepo, CleanupService cleanupService) {
        this.applicationContext = applicationContext;
        this.ingestChecker = checker;
        this.hashRepo = hashRepo;
        this.cleanupService = cleanupService;

    }

    @PostConstruct
    public void post() {

        boolean integrity = ingestChecker.checkIntegrity();

        if (!integrity) {
            cleanUp();
            fullIngest();
            ingestChecker.populateInputHashTable();
        }
    }

    void cleanUp() {
        cleanupService.cleanUp();
        hashRepo.deleteAll();
    }

    private void fullIngest() {

        LOGGER.info("Started Context");
        AutowireCapableBeanFactory bf = applicationContext.getAutowireCapableBeanFactory();
        try (LandingSitesFlat lfs = bf.createBean(LandingSitesFlat.class)) {
            CompletableFuture<Boolean> cf = lfs.ingestInput();
            LOGGER.info("Called ingest");

            LOGGER.info("Waiting ingest...");
            CompletableFuture.allOf(cf);
            LOGGER.info("Waited ingest");
            LOGGER.info(cf);
            bf.destroyBean(lfs);
        } catch (Exception e) {
            throw new IngestException(e.getMessage());
        }
    }

}
