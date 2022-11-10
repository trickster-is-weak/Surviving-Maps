package uk.co.brett.surviving.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.filters.Prevalence;
import uk.co.brett.surviving.filters.SimpleFilterRequest;
import uk.co.brett.surviving.model.repo.SiteRepo;
import uk.co.brett.surviving.model.site.Site;
import uk.co.brett.surviving.services.CommonFilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {
    private static final Logger LOGGER = LogManager.getLogger(SiteServiceImpl.class);

    private static final int LIMIT = 100;

    private static final int FILTER_LIMIT = 5000;


    private final CommonFilterService commonFilterService;

    private final ResourcesService resourcesService;

    private final DisastersService disastersService;

    private final SiteRepo repo;

    private final BreakthroughService breakthroughService;

    @Autowired
    public SiteServiceImpl(CommonFilterService commonFilterService, ResourcesService resourcesService, DisastersService disastersService, SiteRepo repo, BreakthroughService breakthroughService) {
        this.commonFilterService = commonFilterService;
        this.resourcesService = resourcesService;
        this.disastersService = disastersService;
        this.repo = repo;
        this.breakthroughService = breakthroughService;
    }


    @Override
    public Site saveSite(Site area) {
        return repo.save(area);
    }

    @Override
    public List<Site> fetchSites() {
        return repo.fetchWithLimit(LIMIT);
    }

    @Override
    public List<Site> fetchSites(int limit) {
        return repo.fetchWithLimit(limit);
    }

    public List<Site> fetchSites(SimpleFilterRequest filter) {

        Prevalence res = filter.getResources();
        Prevalence dis = filter.getDisasters();
        List<Breakthrough> btr = filter.getBreakthroughs();

        List<List<Long>> collection = new ArrayList<>();

        Optional<List<Long>> resId = resourcesService.filter(res, FILTER_LIMIT);
        Optional<List<Long>> disId = disastersService.filter(dis, FILTER_LIMIT);
        Optional<List<Long>> btrId = breakthroughService.filter(btr, filter.getVariant());

        if (resId.isEmpty() && disId.isEmpty() && btrId.isEmpty()) return new ArrayList<>();

        resId.ifPresent(collection::add);
        disId.ifPresent(collection::add);
        btrId.ifPresent(collection::add);

        LOGGER.info("collection.size() = {}", collection.size());
        List<Long> base = commonFilterService.intersect(collection, LIMIT);

        LOGGER.info("base size: {}", base.size());
        LOGGER.info("ids: {}", base);

        return repo.findAllById(base);
    }

    @Override
    public Site fetchSiteById(Long id) {
        return repo.getById(id);
    }

    public List<Site> fetchSites(List<Long> list) {
        list = list.stream().limit(LIMIT).toList();
        return repo.findAllById(list);
    }
}
