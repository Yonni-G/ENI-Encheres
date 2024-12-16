package fr.eni.eniencheres.eniencheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @GetMapping({"/", "/encheres"})
    public String accueil(Model model) {



        return "pages/encheres/encheres";
    }

}
