package uk.co.brett.surviving.model.service;

import org.springframework.data.domain.Range;
import uk.co.brett.surviving.filters.Prevalence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class LocalService {

    Range<Integer> calculateRange() {
        List<Integer> sums = getSum();

        Collections.sort(sums);
        int third = sums.size() / 3;
        int avg = Math.round((float) sums.stream().mapToInt(Integer::intValue).average().orElseThrow());

        int low = 0;
        int high = 0;

        for (int width = 0; width < 5; width++) {

            int innerLow = avg - width;
            int innerHigh = avg + width;

            int size = sums.stream()
                    .filter(i -> i >= innerLow)
                    .filter(i -> i <= innerHigh)
                    .toList().size();

            if (size >= third) {
                low = innerLow;
                high = innerHigh;
                break;
            }

        }
        return Range.closed(low, high);

    }

    abstract Optional<List<Long>> filter(Prevalence prevalence, int limit);

    protected abstract List<Integer> getSum();

}
