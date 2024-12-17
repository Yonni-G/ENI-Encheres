package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EnchereController {

    @Autowired
    private EnchereService service;
    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping({"/", "/encheres"})
    public String accueil(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Integer noCategorie,
            Model model) {

        List<ArticleVendu> articles;

        if (noCategorie != null && nom != null) {
            articles = service.findArticleByNomAndCategorieId(noCategorie, nom);
        } else if (noCategorie != null) {
            articles = service.findArticleByCategorieId(noCategorie);
        } else if (nom != null) {
            articles = service.findArticleByNom(nom);
        } else {
            articles = service.findAllArticleVendu();
        }
        // Ajout à la vue de la liste des catégories
        model.addAttribute("categories", service.findAllCategories());
        // Ajout à la vue de la liste des articles vendus
        model.addAttribute("articles", articles);
        // Ajout à la vue de la liste des utilisateurs
        // TODO: ne fonctionne pas !!!!!!!!!!!!!!!!!!!!!!!!!
        model.addAttribute("utilisateur", utilisateurService.findAll());
        return "pages/encheres/encheres";
    }

    @GetMapping("/detailVente/{noArticle}")
    public String detailVente(@PathVariable("noArticle") Integer noArticle, Model model) {
        return "pages/encheres/detailVente";
    }
}
