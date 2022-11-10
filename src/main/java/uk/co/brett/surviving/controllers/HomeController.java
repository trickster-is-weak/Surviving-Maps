package uk.co.brett.surviving.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final ModelAndView mav = new ModelAndView();

    @GetMapping("/")
    public ModelAndView home() {
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/faq")
    public ModelAndView faq() {
        mav.setViewName("faq");
        return mav;
    }

    @GetMapping("/support")
    public ModelAndView support() {
        return home();
    }

    @GetMapping("/contribute")
    public ModelAndView contribute() {
        return home();
    }


}
