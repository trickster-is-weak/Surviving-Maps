package uk.co.brett.surviving.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.model.repo.BreakthroughMapRepo;
import uk.co.brett.surviving.model.site.BreakthroughMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BreakthroughService {
    private static final Logger LOGGER = LogManager.getLogger(BreakthroughService.class);


    private final BreakthroughMapRepo repo;

    @Autowired
    public BreakthroughService(BreakthroughMapRepo repo) {
        this.repo = repo;
    }

    public Optional<List<Long>> filter(List<Breakthrough> btr, GameVariant variant) {
        if (btr == null || btr.isEmpty()) {
            return Optional.empty();
        } else {
            List<BreakthroughMap> abc = btr.stream().map(repo::getById).toList();
            List<Long> base = new ArrayList<>(abc.get(0).getIdList(variant));

            abc.stream().map(breakthroughMap -> breakthroughMap.getIdList(variant)).forEach(base::retainAll);
            LOGGER.info("final breakthrough size: {}", base.size());

            return Optional.of(base);
        }
    }
}
