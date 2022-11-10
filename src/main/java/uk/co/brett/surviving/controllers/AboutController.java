package uk.co.brett.surviving.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.brett.surviving.about.AboutService;

@Controller
public class AboutController {


    private final AboutService aboutService;

    @Autowired
    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("about");
        mav.addObject("list", aboutService.getAboutHistory());
        return mav;
    }

}
