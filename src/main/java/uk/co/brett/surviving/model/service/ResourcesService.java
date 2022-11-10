package uk.co.brett.surviving.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.filters.Prevalence;
import uk.co.brett.surviving.model.repo.ResourcesRepo;
import uk.co.brett.surviving.model.site.Resources;

import java.util.List;
import java.util.Optional;

@Service
public class ResourcesService extends LocalService {
    private static Range<Integer> range;

    private final ResourcesRepo resourcesRepo;

    @Autowired
    public ResourcesService(ResourcesRepo resourcesRepo) {
        this.resourcesRepo = resourcesRepo;
    }


    int getLow() {
        return getRange().getLowerBound().getValue().orElseThrow();
    }

    int getHigh() {
        return getRange().getUpperBound().getValue().orElseThrow();
    }

    @Override
    Optional<List<Long>> filter(Prevalence prevalence, int limit) {
        return switch (prevalence) {
            case NA -> Optional.empty();
            case LOW -> Optional.of(resourcesRepo.filterSumBelow(getLow(), limit));
            case MED -> Optional.of(resourcesRepo.filterSumBetween(getLow(), getHigh(), limit));
            case HIGH -> Optional.of(resourcesRepo.filterSumAbove(getHigh(), limit));
        };
    }

    @Override
    protected List<Integer> getSum() {
        return resourcesRepo.getSum();
    }


    public Resources save(Resources type) {
        return resourcesRepo.save(type);
    }


    public Range<Integer> getRange() {

        if (range == null) ResourcesService.setRange(calculateRange());

        return range;
    }

    public static void setRange(Range<Integer> range) {
        ResourcesService.range = range;
    }


}
