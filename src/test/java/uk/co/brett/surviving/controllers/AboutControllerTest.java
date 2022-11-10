package uk.co.brett.surviving.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.about.AboutService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AboutControllerTest {

    AboutController controller;
    @Autowired
    AboutService service;

    @BeforeEach
    public void before() {
        controller = new AboutController(service);
    }

    @Test
    void about() {

        ModelAndView mav = controller.about();
        assertThat(mav.getViewName()).isEqualTo("about");
        assertThat(mav.getModel().get("list")).isNotNull().isInstanceOf(List.class);

    }
}