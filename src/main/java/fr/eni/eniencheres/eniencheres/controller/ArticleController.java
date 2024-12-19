package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.dal.EnchereFiltresDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private EnchereService service;
    ArticleController(EnchereService service) {
        this.service = service;
    }

    @GetMapping("/vendre")
    public String vendreGet(Model model) {
        ArticleVendu articleVendu = new ArticleVendu();
        model.addAttribute("articleVendu", articleVendu);
        // il faut également injecter les catégories
        System.out.println(""+service.findAllCategories());
        model.addAttribute("categories", service.findAllCategories());
        return "pages/articles/formAjoutArticle";

    }
}
