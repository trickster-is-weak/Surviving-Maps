package uk.co.brett.surviving.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.model.site.BreakthroughMap;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BreakthroughMapRepo extends JpaRepository<BreakthroughMap, Breakthrough> {


    default List<Long> getIds(GameVariant variant, List<Breakthrough> btr) {
        return new ArrayList<>();
    }

}
