package uk.co.brett.surviving.io.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.io.model.InputHash;

@Repository
public interface InputHashRepo extends JpaRepository<InputHash, Long> {
}
