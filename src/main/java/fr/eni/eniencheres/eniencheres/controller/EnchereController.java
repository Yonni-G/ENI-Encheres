package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @Autowired
    private EnchereService service;

    @GetMapping({"/", "/encheres"})
    public String accueil(Model model) {
        // Ajout à la vue de la liste des catégories
        model.addAttribute("categories", service.findAllCategories());
        // Ajout à la vue de la liste des articles vendus
        model.addAttribute("articles", service.findAllArticleVendu());
        return "pages/encheres/encheres";
    }

}
