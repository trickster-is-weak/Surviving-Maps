package uk.co.brett.surviving.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.model.repo.CustomBreakthroughMapRepo;
import uk.co.brett.surviving.model.service.SiteServiceImpl;
import uk.co.brett.surviving.model.site.Site;

import java.util.List;

@Service
public class ComplexFilterService {
    private static final Logger LOGGER = LogManager.getLogger(ComplexFilterService.class);
    private final SiteServiceImpl siteService;
    private final CustomBreakthroughMapRepo customBreakthroughMapRepo;

    @Autowired
    public ComplexFilterService(SiteServiceImpl siteService, CustomBreakthroughMapRepo customBreakthroughMapRepo) {
        this.siteService = siteService;
        this.customBreakthroughMapRepo = customBreakthroughMapRepo;
    }


    public List<Site> getAll() {
        return siteService.fetchSites();
    }

    public List<Site> filter(ComplexFilterRequest request) {
        LOGGER.info(request);
        return customBreakthroughMapRepo.getIds(request);
    }
}
