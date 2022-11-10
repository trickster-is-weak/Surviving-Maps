package uk.co.brett.surviving.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.LandingSiteTestUtil;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.filters.SimpleFilterRequest;
import uk.co.brett.surviving.io.LandingSiteFlat;
import uk.co.brett.surviving.model.repo.DisastersRepo;
import uk.co.brett.surviving.model.repo.ResourcesRepo;
import uk.co.brett.surviving.model.service.SiteService;
import uk.co.brett.surviving.model.site.Disasters;
import uk.co.brett.surviving.model.site.Resources;
import uk.co.brett.surviving.model.site.Site;
import uk.co.brett.surviving.services.SimpleFilterService;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static uk.co.brett.surviving.enums.GameVariant.GREEN_PLANET;
import static uk.co.brett.surviving.enums.GameVariant.STANDARD;

//@DataJpaTest
@SpringBootTest
class SimpleControllerTest {


    @Autowired
    SiteService siteService;
    //
    @Autowired
    ResourcesRepo resourcesRepo;
    //
//    @Autowired
    private SimpleController controller;
    //
    @Autowired
    private DisastersRepo disastersRepo;

    private ModelAndView mav;


    @BeforeEach
    void setUp() {
        SimpleFilterService mock = mock(SimpleFilterService.class);
        controller = new SimpleController(mock);
    }

    @Test
    void simple() {
        mav = controller.simple();
        assertThat(mav.getViewName()).isEqualTo("simple");
        assertThat(mav.getModel()).containsKey("filter");
        assertThat(mav.getModel()).containsKey("breakthroughs");
    }

    @Test
    void simpleFilter() {
        SimpleFilterRequest filter = new SimpleFilterRequest();
        mav = controller.simpleFilter(filter);
        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: table");
        assertThat(mav.getModel()).containsKey("filter");
        assertThat(mav.getModel()).containsKey("sites");
        assertThat(mav.getModel()).containsKey("breakthroughs");
    }

    @Test
    @Transactional
    void display() {
        Site site = siteService.fetchSites(1).get(0);
        Long id = site.getId();
        mav = controller.display(id);

        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: display");
        assertThat(mav.getModel()).containsEntry("site", site);
        assertThat(mav.getModel()).containsEntry("variant", STANDARD);

        mav = controller.display(-1L);
        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: defaultDisplay");

    }

    @Test
    void getData() {
        LandingSiteFlat flat = LandingSiteTestUtil.getLandingSite();
        Map<GameVariant, List<Breakthrough>> btrVarMap = LandingSiteTestUtil.breakthroughMap();

        Resources r = resourcesRepo.save(new Resources(flat));
        Disasters d = disastersRepo.save(new Disasters(flat));


        Site site = new Site(flat, btrVarMap);
        site.setDisasters(d);
        site.setResources(r);
        site = siteService.saveSite(site);

        mav = controller.getData();

        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: table");

        assertThat(mav.getModel().get("sites")).isInstanceOf(List.class);
        assertThat(mav.getModel().get("sites"))
                .asList()
                .isNotEmpty()
                .hasAtLeastOneElementOfType(Site.class);

    }

    @Test
    void updateBreakthroughs() {
        GameVariant variant = STANDARD;
        mav = controller.updateBreakthroughs(variant);
        List<Breakthrough> exp = Breakthrough.filterVariant(variant);

        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: breakthroughs");
        assertThat(mav.getModel()).containsEntry("breakthroughs", exp);
    }

    @Test
    void reloadForm() {
        SimpleFilterRequest filter = new SimpleFilterRequest();
        filter.setVariant(GREEN_PLANET);
        List<Breakthrough> exp = Breakthrough.filterVariant(filter.getVariant());

        mav = controller.reloadForm(filter);
        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: filter");
        assertThat(mav.getModel()).containsEntry("filter", filter);
        assertThat(mav.getModel()).containsEntry("breakthroughs", exp);
    }
}