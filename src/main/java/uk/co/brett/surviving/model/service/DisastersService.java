package uk.co.brett.surviving.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import uk.co.brett.surviving.filters.Prevalence;
import uk.co.brett.surviving.model.repo.DisastersRepo;
import uk.co.brett.surviving.model.site.Disasters;

import java.util.List;
import java.util.Optional;

@Service
public class DisastersService extends LocalService {

    private static Range<Integer> range;

    private final DisastersRepo disastersRepo;

    @Autowired
    public DisastersService(DisastersRepo disastersRepo) {
        this.disastersRepo = disastersRepo;
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
            case LOW -> Optional.of(disastersRepo.filterSumBelow(getLow(), limit));
            case MED -> Optional.of(disastersRepo.filterSumBetween(getLow(), getHigh(), limit));
            case HIGH -> Optional.of(disastersRepo.filterSumAbove(getHigh(), limit));
        };
    }

    @Override
    protected List<Integer> getSum() {
        return disastersRepo.getSum();
    }

    public Range<Integer> getRange() {

        if (range == null) {
            DisastersService.setRange(calculateRange());
        }
        return range;
    }

    public static void setRange(Range<Integer> range) {
        DisastersService.range = range;
    }

    public Disasters save(Disasters d) {
        return disastersRepo.save(d);
    }
}
