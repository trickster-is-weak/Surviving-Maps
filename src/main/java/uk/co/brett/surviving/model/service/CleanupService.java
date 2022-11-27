package uk.co.brett.surviving.model.service;

import org.springframework.stereotype.Service;
import uk.co.brett.surviving.model.repo.BreakthroughMapRepo;
import uk.co.brett.surviving.model.repo.DisastersRepo;
import uk.co.brett.surviving.model.repo.ResourcesRepo;
import uk.co.brett.surviving.model.repo.SiteRepo;

@Service
public class CleanupService {
    private final BreakthroughMapRepo btrRepo;
    private final SiteRepo siteRepo;
    private final ResourcesRepo resourcesRepo;
    private final DisastersRepo disastersRepo;

    public CleanupService(BreakthroughMapRepo btrRepo, SiteRepo siteRepo, ResourcesRepo resourcesRepo, DisastersRepo disastersRepo) {
        this.btrRepo = btrRepo;
        this.siteRepo = siteRepo;
        this.resourcesRepo = resourcesRepo;
        this.disastersRepo = disastersRepo;
    }

    public void cleanUp() {
        btrRepo.deleteAll();
        siteRepo.deleteAll();
        disastersRepo.deleteAll();
        resourcesRepo.deleteAll();
    }

}
