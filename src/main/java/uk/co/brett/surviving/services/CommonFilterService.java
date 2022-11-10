package uk.co.brett.surviving.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonFilterService {

    public List<Long> intersect(List<List<Long>> ids) {

        List<Long> init = new ArrayList<>(ids.get(0));
        ids.forEach(init::retainAll);

        return init;
    }

    public List<Long> intersect(List<List<Long>> collection, int limit) {
        List<Long> list = intersect(collection);
        return list.stream().limit(limit).toList();
    }
}
