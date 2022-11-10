package uk.co.brett.surviving.model.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.model.site.MapDetails;

@Repository
public interface MapDetailsRepo extends CrudRepository<MapDetails, Long> {
}
