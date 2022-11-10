package uk.co.brett.surviving.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.enums.Breakthrough;
import uk.co.brett.surviving.enums.GameVariant;
import uk.co.brett.surviving.filters.SimpleFilterRequest;
import uk.co.brett.surviving.model.site.Site;
import uk.co.brett.surviving.services.SimpleFilterService;

import java.util.List;

import static uk.co.brett.surviving.enums.GameVariant.STANDARD;

@Controller
public class SimpleController {

    private static final Logger LOGGER = LogManager.getLogger(SimpleController.class);

    private static final String FILTER = "filter";

    private static final String SITE_LIST = "sites";

    private static final String SITE = "site";

    private static final String BREAKTHROUGHS = "breakthroughs";

    private final ModelAndView mav = new ModelAndView();

    private final SimpleFilterService filterService;

    private GameVariant variant = STANDARD;

    @Autowired
    public SimpleController(SimpleFilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/simple")
    public ModelAndView simple() {
        mav.setViewName("simple");
        SimpleFilterRequest filter = new SimpleFilterRequest();
        List<Breakthrough> breakthroughs = Breakthrough.filterVariant(filter.getVariant());

        mav.addObject(FILTER, filter);
        mav.addObject(BREAKTHROUGHS, breakthroughs);
        return mav;
    }


    @PostMapping(value = "/simple")
    public ModelAndView simpleFilter(@ModelAttribute SimpleFilterRequest filter) {
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


    @GetMapping(value = "/getDisplay")
    public ModelAndView display(@RequestParam(name = "id") final Long id) {
        LOGGER.info("Selected ID: {}", id);
        if (id == -1) {
            mav.setViewName("fragments/simpleFragments :: defaultDisplay");
            return mav;
        } else {
            mav.setViewName("fragments/simpleFragments :: display");
            LOGGER.info("display variant {}", variant);
            Site site = filterService.getSingle(id);
            site.getBreakthroughs(variant);
            mav.addObject(SITE, filterService.getSingle(id));
            mav.addObject("variant", variant);
            return mav;
        }

    }


    @GetMapping("/getdata")
    public ModelAndView getData() {
        mav.setViewName("fragments/simpleFragments :: table");
        return mav.addObject(SITE_LIST, filterService.getAll());
    }

    @GetMapping("/updateBreakthroughs")
    public ModelAndView updateBreakthroughs(@RequestParam(name = "variant") final GameVariant postedVariant) {
        variant = postedVariant;
        mav.setViewName("fragments/simpleFragments :: breakthroughs");
        mav.addObject(BREAKTHROUGHS, Breakthrough.filterVariant(variant));
        return mav;
    }

    @GetMapping("/reloadForm")
    public ModelAndView reloadForm(@ModelAttribute SimpleFilterRequest filter) {
        List<Breakthrough> breakthroughs = Breakthrough.filterVariant(filter.getVariant());

        mav.setViewName("fragments/simpleFragments :: filter");
        mav.addObject(FILTER, filter);
        mav.addObject(BREAKTHROUGHS, breakthroughs);
        return mav;
    }


}
