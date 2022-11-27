package uk.co.brett.surviving.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.model.site.BreakthroughMap;

@Repository
public interface BreakthroughMapRepo extends JpaRepository<BreakthroughMap, Breakthrough> {

}
