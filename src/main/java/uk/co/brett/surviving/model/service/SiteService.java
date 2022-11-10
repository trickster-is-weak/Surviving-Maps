package uk.co.brett.surviving.model.service;

import org.springframework.stereotype.Service;
import uk.co.brett.surviving.model.site.Site;

import java.util.List;

@Service
public interface SiteService {

    Site saveSite(Site area);

    List<Site> fetchSites();

    List<Site> fetchSites(int limit);

    Site fetchSiteById(Long id);

}
