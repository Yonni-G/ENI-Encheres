package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class ArticleController {


    private EnchereService enchereService;
    private UtilisateurService utilisateurService;
    ArticleController(EnchereService enchereService, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/vendre")
    public String vendreGet(Model model) {
        ArticleVendu articleVendu = new ArticleVendu();
        model.addAttribute("articleVendu", articleVendu);

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        // on ajoute le vendeur (qui est donc l'utilisateur connecté) à l'article
        Optional<Utilisateur> vendeur = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if(vendeur.isPresent()) {
            articleVendu.setVendeur(vendeur.get());
        }
        else articleVendu.setVendeur(new Utilisateur());

        return "pages/articles/formAjoutArticle";

    }

    @PostMapping("/vendre")
    public String vendrePost(@Valid ArticleVendu articleVendu, BindingResult controleArticleVendu, Model model){

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());
        
        if(controleArticleVendu.hasErrors()) {
            return "pages/articles/formAjoutArticle";
        }
        return "pages/articles/formAjoutArticle";
    }
}
