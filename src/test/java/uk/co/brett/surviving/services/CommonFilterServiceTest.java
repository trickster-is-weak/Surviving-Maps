package uk.co.brett.surviving.services;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CommonFilterServiceTest {

    CommonFilterService service = new CommonFilterService();
    List<Long> common;

    @Test
    void intersect() {

        List<List<Long>> ids = new ArrayList<>();
        ids.add(List.of(1L, 2L, 3L, 4L));
        ids.add(List.of(3L, 4L, 5L, 6L));
        ids.add(List.of(3L, 5L, 7L));

        List<Long> out = service.intersect(ids);
        assertThat(out).isEqualTo(List.of(3L));

    }

    @Test
    void largeLimitedIntersect() {
        List<List<Long>> lists = createListOfLists(5, 100);
        List<Long> out = service.intersect(lists, 20);
        assertThat(out).containsAnyElementsOf(common);
    }

    @Test
    void largeIntersect() {
        List<List<Long>> lists = createListOfLists(5, 100);
        List<Long> out = service.intersect(lists);
        assertThat(out).containsExactlyElementsOf(common);
    }

    @Test
    void smallIntersect() {
        List<List<Long>> lists = createListOfLists(50, 20);
        List<Long> out = service.intersect(lists);
        assertThat(out).containsExactlyElementsOf(common);
    }

    List<List<Long>> createListOfLists(int num, int depth) {
        List<List<Long>> listOfLists = new ArrayList<>();
        Set<Long> ints = new HashSet<>();
        int size = (num + 1) * depth;
        Random random = new Random();

        while (ints.size() != size) {
            ints.add((long) random.nextInt(size * 4));
        }

        Iterable<List<Long>> out = Iterables.partition(ints, depth);
        Iterator<List<Long>> iter = out.iterator();
        common = iter.next();

        iter.forEachRemaining(i -> listOfLists.add(new ArrayList<>(i)));

        for (List<Long> list : listOfLists) {
            list.addAll(common);
        }

        return listOfLists;
    }

}