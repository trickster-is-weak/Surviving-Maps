package uk.co.brett.surviving.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.enums.*;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.model.site.Site;
import uk.co.brett.surviving.services.ComplexFilterService;

import java.util.List;

@Controller
public class ComplexController {
    private static final Logger LOGGER = LogManager.getLogger(ComplexController.class);
    private static final String FILTER = "filter";
    private static final String SITE_LIST = "sites";
    private static final String LANDING_AREA = "landingAreas";
    private static final String TOPOGRAPHY = "topography";

    private static final String BREAKTHROUGHS = "breakthroughs";
    private static final String MAP_NAME = "mapName";
    private final ComplexFilterService filterService;
    ModelAndView mav = new ModelAndView("complex");
    GameVariant variant = GameVariant.STANDARD;

    @Autowired
    public ComplexController(ComplexFilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/complex")
    public ModelAndView complex() {

        ComplexFilterRequest filter = new ComplexFilterRequest();
        List<Breakthrough> breakthroughs = Breakthrough.filterVariant(filter.getVariant());


        mav.setViewName("complex");
        mav.addObject(FILTER, filter);
        mav.addObject(BREAKTHROUGHS, breakthroughs);
        filterService.getAll();
        mav.addObject(SITE_LIST, filterService.getAll());
        mav.addObject(LANDING_AREA, NamedLandingArea.values());
        mav.addObject(TOPOGRAPHY, Topography.values());
        mav.addObject(MAP_NAME, MapName.values());
        return mav;
    }

    @PostMapping(value = "/complex")
    public ModelAndView complexFilter(@ModelAttribute ComplexFilterRequest filter) {

        List<Site> filtered = filterService.filter(filter);
        variant = filter.getVariant();
        List<Breakthrough> breakthroughs = Breakthrough.filterVariant(filter.getVariant());

        mav.setViewName("fragments/simpleFragments :: table");
        mav.addObject(FILTER, filter);
        mav.addObject(SITE_LIST, filtered);
        mav.addObject(BREAKTHROUGHS, breakthroughs);
        LOGGER.info("Filter completed");
        return mav;
    }

}
