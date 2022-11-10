package uk.co.brett.surviving.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.model.site.Resources;

import java.util.List;

@Repository
public interface ResourcesRepo extends JpaRepository<Resources, Long> {

    @Query(value = "SELECT r.sum as sum from Resources r INNER JOIN Site ON Site.RESOURCE_ID=R.ID", nativeQuery = true)
    List<Integer> getSum();

    @Query(value = "SELECT s.ID from Resources r INNER JOIN Site s ON s.RESOURCE_ID=R.ID WHERE r.sum < ?1 LIMIT ?2", nativeQuery = true)
    List<Long> filterSumBelow(int low, int limit);

    @Query(value = "select s.id, east_west, latitude, longitude, named_location, north_south, disaster_id, resource_id from Resources r INNER JOIN Site s ON s.RESOURCE_ID=R.ID WHERE r.sum >= ?1 AND r.sum <= ?2 LIMIT ?3", nativeQuery = true)
    List<Long> filterSumBetween(int low, int high, int limit);

    @Query(value = "select s.id from Resources r INNER JOIN Site s ON s.RESOURCE_ID=R.ID WHERE r.sum > ?1 LIMIT ?2", nativeQuery = true)
    List<Long> filterSumAbove(int high, int limit);

    @Query(value = "select id from SITE LIMIT ?1", nativeQuery = true)
    List<Long> findAny(int limit);

}
