package fr.eni.eniencheres.eniencheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/encheres"})
    public String accueil() {
        return "encheres";
    }

}
