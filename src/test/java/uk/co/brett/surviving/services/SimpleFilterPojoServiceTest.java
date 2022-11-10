//package uk.co.brett.surviving.services;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import uk.co.brett.surviving.LandingSite;
//import uk.co.brett.surviving.filters.SimpleFilterRequest;
//
//import java.util.Comparator;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//class SimpleFilterPojoServiceTest {
//    @Autowired
//    private SimpleFilterService filterService;
//    @Autowired
//    private LandingSites landingSites;
//
//
//    @Test
//    void filter() {
//
//    }
//
//    @Test
//    void getAll() {
//        int size = landingSites.getSites().size();
//        assertThat(filterService.getAll().size()).isEqualTo(size);
//        assertThat(filterService.getAll()).containsExactlyInAnyOrderElementsOf(landingSites.getSites());
//    }
//
//    @Test
//    void getSmall() {
//
//        assertThat(filterService.getSmall().size()).isEqualTo(10);
//    }
//
//    @Test
//    void filterResults() {
//        List<LandingSite> sites = filterService.filter(new SimpleFilterRequest());
//        List<LandingSite> filtered = landingSites.getSites().stream().sorted(Comparator.comparing(LandingSite::id)).limit(10).toList();
//        assertThat(sites.size()).isEqualTo(10);
//        assertThat(sites).containsExactlyInAnyOrderElementsOf(filtered);
//
//    }
//
//    @Test
//    void getSingle() {
//        LandingSite selected = landingSites.getSites().stream().filter(i -> i.id() == 0).findFirst().orElseThrow();
//        assertThat(filterService.getSingle(0)).usingRecursiveComparison().isEqualTo(selected);
//    }
//}