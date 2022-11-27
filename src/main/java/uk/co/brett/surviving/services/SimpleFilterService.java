package uk.co.brett.surviving.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.filters.SimpleFilterRequest;
import uk.co.brett.surviving.model.service.SiteServiceImpl;
import uk.co.brett.surviving.model.site.Site;

import java.util.List;

@Service
public class SimpleFilterService {

    private static final Logger LOGGER = LogManager.getLogger(SimpleFilterService.class);
    private final SiteServiceImpl siteService;


    @Autowired
    public SimpleFilterService(SiteServiceImpl siteService) {

        this.siteService = siteService;

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
