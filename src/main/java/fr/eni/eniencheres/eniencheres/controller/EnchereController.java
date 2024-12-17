package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.dal.EnchereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    @Autowired
    private EnchereRepository repository;

    @GetMapping({"/", "/encheres"})
    public String accueil(Model model) {
        // Ajout à la vue de la liste des catégories
        model.addAttribute("categories", repository.findAllCategories());
        // Ajout à la vue de la liste des articles vendus
        model.addAttribute("articles", repository.findAllArticleVendu());
        return "pages/encheres/encheres";
    }

}
