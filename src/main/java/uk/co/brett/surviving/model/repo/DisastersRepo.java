package uk.co.brett.surviving.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.model.site.Disasters;

import java.util.List;

@Repository
public interface DisastersRepo extends JpaRepository<Disasters, Long> {

    @Query(value = "SELECT d.sum as sum from DISASTERS d INNER JOIN Site ON Site.DISASTER_ID=d.ID", nativeQuery = true)
    List<Integer> getSum();

    @Query(value = "SELECT site.ID from DISASTERS d INNER JOIN Site ON Site.DISASTER_ID=d.ID WHERE d.sum < ?1 LIMIT ?2", nativeQuery = true)
    List<Long> filterSumBelow(int low, int limit);

    @Query(value = "select site.id from DISASTERS d INNER JOIN Site ON Site.DISASTER_ID=d.ID WHERE d.sum >= ?1 AND d.sum <= ?2 LIMIT ?3", nativeQuery = true)
    List<Long> filterSumBetween(int low, int high, int limit);

    @Query(value = "select site.id from DISASTERS d INNER JOIN Site ON Site.DISASTER_ID=d.ID WHERE d.sum > ?1 LIMIT ?2", nativeQuery = true)
    List<Long> filterSumAbove(int high, int limit);

    @Query(value = "select id from SITE LIMIT ?1", nativeQuery = true)
    List<Long> findAny(int limit);
}
