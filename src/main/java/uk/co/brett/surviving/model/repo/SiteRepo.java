package uk.co.brett.surviving.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.model.site.Site;

import java.util.List;

@Repository
public interface SiteRepo extends JpaRepository<Site, Long> {
    @Query(value = "SELECT * FROM SITE LIMIT ?1", nativeQuery = true)
    List<Site> fetchWithLimit(int limit);
}
