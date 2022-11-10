//package uk.co.brett.surviving;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import uk.co.brett.surviving.enums.Breakthrough;
//import uk.co.brett.surviving.model.repo.BreakthroughMapRepo;
//import uk.co.brett.surviving.model.repo.SiteRepo;
//import uk.co.brett.surviving.model.service.DisastersService;
//import uk.co.brett.surviving.model.service.SiteServiceImpl;
//import uk.co.brett.surviving.model.service.ResourcesService;
//import uk.co.brett.surviving.model.site.BreakthroughMap;
//import uk.co.brett.surviving.model.site.Site;
//import uk.co.brett.surviving.services.LandingSitesFlat;
//
//import java.util.ArrayList;
//import java.util.EnumMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@EnableJpaRepositories
//public class SpringAppTest {
//
//    @Autowired
//    SiteRepo siteRepo;
//    @Autowired
//    SiteServiceImpl landingService;
//
//    @Autowired
//    LandingSitesFlat sites;
//
//    @Autowired
//    BreakthroughMapRepo breakthroughMapRepo;
////
////    @Autowired
////    ResourcesRepo resourcesRepo;
////
////    @Autowired
////    DisastersRepo disastersRepo;
////
////    @Autowired
////    MapDetailsRepo mapDetailsRepo;
//
//    @Autowired
//    DisastersService disastersService;
//
//    @Autowired
//    ResourcesService resourcesService;
//
//
//    @Test
//    public void ILA() {
//
//
////        System.out.println("disastersService.getRange() = " + disastersService.getRange());
////        System.out.println("resourcesService.getRange() = " + resourcesService.getRange());
////        System.out.println("disastersService.getRange() = " + disastersService.getRange());
////        System.out.println("resourcesService.getRange() = " + resourcesService.getRange());
////
////        System.out.println("disastersService.getMin() = " + disastersService.getMin());
////        System.out.println("disastersService.getMin() = " + disastersService.getMed());
////        System.out.println("disastersService.getMin() = " + disastersService.getMax());
//
//        List<Integer> a = resourcesService.getMed();
//        List<Integer> b = disastersService.getMed();
//
//        System.out.println("a.size() = " + a.size());
//        System.out.println("b.size() = " + b.size());
//
//        b.retainAll(a);
//
//
//        System.out.println("overlap: " + b.size());
//
//    }
//
//
//    @Test
//    public void test() {
//        Map<Breakthrough, List<Long>> map = new EnumMap<>(Breakthrough.class);
//        for (LandingSiteFlat flat : sites.getSites()) {
//
//            Site site = new Site(flat);
//            List<Breakthrough> bt = flat.getBreakthroughs();
//            site = siteRepo.save(site);
//
//            for (Breakthrough b : bt) {
//                map.computeIfAbsent(b, k -> new ArrayList<>()).add(site.getId());
//            }
//
//        }
//        map.forEach((key, value) -> breakthroughMapRepo.save(new BreakthroughMap(key, value)));
//
//    }
//}
