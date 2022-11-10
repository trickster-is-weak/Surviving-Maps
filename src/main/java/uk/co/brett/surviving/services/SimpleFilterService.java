package uk.co.brett.surviving.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.filters.SimpleFilterRequest;
import uk.co.brett.surviving.model.service.SiteServiceImpl;
import uk.co.brett.surviving.model.site.Site;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SimpleFilterService {

    private static final Logger LOGGER = LogManager.getLogger(SimpleFilterService.class);

    private final ConfigurableApplicationContext applicationContext;


    private final SiteServiceImpl siteService;

    @Autowired
    public SimpleFilterService(ConfigurableApplicationContext applicationContext, SiteServiceImpl siteService) {
        this.applicationContext = applicationContext;
        this.siteService = siteService;
    }

    @PostConstruct
    public void post() {

        LOGGER.info("Started Context");
        AutowireCapableBeanFactory bf = applicationContext.getAutowireCapableBeanFactory();
        try(LandingSitesFlat lfs = bf.createBean(LandingSitesFlat.class)) {
            CompletableFuture<Boolean> cf = lfs.ingestInput();
            LOGGER.info("Called ingest");

            LOGGER.info("Waiting ingest...");
            CompletableFuture.allOf(cf);
            LOGGER.info("Waited ingest");
            LOGGER.info(cf);
            bf.destroyBean(lfs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Site> filter(SimpleFilterRequest request) {
        return filterResults(request);
    }

    public List<Site> getAll() {
        return siteService.fetchSites();
    }

    public List<Site> getSmall() {
        return siteService.fetchSites(10);
    }

    public List<Site> filterResults(SimpleFilterRequest filter) {
        LOGGER.info(filter);

        if (filter.equalsNoVariant(new SimpleFilterRequest())) return siteService.fetchSites();
        return siteService.fetchSites(filter);
    }

    public Site getSingle(Long id) {
        return siteService.fetchSiteById(id);
    }
}
