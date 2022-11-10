package uk.co.brett.surviving.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.filters.ComplexFilterRequest;
import uk.co.brett.surviving.services.ComplexFilterService;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;

class ComplexControllerTest {

    ComplexController controller;

    @BeforeEach
    void setUp() {
        ComplexFilterService filterService = mock(ComplexFilterService.class);
        controller = new ComplexController(filterService);
    }

    @Test
    void complex() {
        ModelAndView mav = controller.complex();
        assertThat(mav.getViewName()).isEqualTo("complex");
        Map<String, Object> map = mav.getModel();
        assertThat(map).containsKey("filter");
        assertThat(map).containsKey("sites");
        assertThat(map).containsKey("landingAreas");
        assertThat(map).containsKey("topography");
        assertThat(map).containsKey("breakthroughs");
        assertThat(map).containsKey("mapName");

    }

    @Test
    void complexFilter() {
        ModelAndView mav = controller.complexFilter(new ComplexFilterRequest());
        assertThat(mav.getViewName()).isEqualTo("fragments/simpleFragments :: table");
        Map<String, Object> map = mav.getModel();
        assertThat(map).containsKey("filter");
        assertThat(map).containsKey("sites");
        assertThat(map).containsKey("breakthroughs");
    }
}