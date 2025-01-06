package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.ArticleVenduService;
import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class ArticleController {


    private EnchereService enchereService;
    private UtilisateurService utilisateurService;
    private ArticleVenduService articleVenduService;

    ArticleController(EnchereService enchereService, UtilisateurService utilisateurService, ArticleVenduService articleVenduService) {
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
        this.articleVenduService = articleVenduService;

    }

    @GetMapping("/vendre")
    public String vendreGet(Model model) {
        ArticleVendu articleVendu = new ArticleVendu();

        // on ajoute le vendeur (qui est donc l'utilisateur connecté) à l'article
        Optional<Utilisateur> vendeur = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if(vendeur.isPresent()) {
            articleVendu.setVendeur(vendeur.get());
        }
        else articleVendu.setVendeur(new Utilisateur());

        // par defaut, l'adresse du retrait est celle du vendeur
        articleVendu.setLieuRetrait(new Retrait(
            articleVendu.getVendeur().getRue(),
            articleVendu.getVendeur().getCodePostal(),
            articleVendu.getVendeur().getVille())
        );

        model.addAttribute("articleVendu", articleVendu);

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        return "pages/articles/formAjoutArticle";

    }

    @PostMapping("/vendre")
    public String vendrePost(@Valid ArticleVendu articleVendu, BindingResult controleArticleVendu, Model model){

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        System.out.println("Categorie Article ID (noCategorie): " + articleVendu.getCategorieArticle().getNoCategorie());

        if(controleArticleVendu.hasErrors()) {

            return "pages/articles/formAjoutArticle";
        }

        // on ajoute le vendeur (qui est donc l'utilisateur connecté) à l'article
        Optional<Utilisateur> vendeur = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if(vendeur.isPresent()) {
            articleVendu.setVendeur(vendeur.get());
        }
        else {
            model.addAttribute("errorMessage", "Impossible de trouver cet utilisateur");
            return "pages/articles/formAjoutArticle";
        }

        // on ajoute le lieu de retrait à l'article
//        articleVendu.setLieuRetrait(new Retrait(
//                articleVendu.getVendeur().getRue(),
//                articleVendu.getVendeur().getCodePostal(),
//                articleVendu.getVendeur().getVille()
//        ));

        // on insere le nouvel article
        articleVenduService.ajouter(articleVendu);

        return "redirect:/";
    }
}
