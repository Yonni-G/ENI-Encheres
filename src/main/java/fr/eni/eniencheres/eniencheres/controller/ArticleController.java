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
import org.springframework.web.bind.annotation.PathVariable;
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
        if (vendeur.isPresent()) {
            articleVendu.setVendeur(vendeur.get());
        } else articleVendu.setVendeur(new Utilisateur());

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
    public String vendrePost(@Valid ArticleVendu articleVendu, BindingResult controleArticleVendu, Model model) {

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        if (controleArticleVendu.hasErrors()) {

            return "pages/articles/formAjoutArticle";
        }

        // ici on controle que la date de fin des encheres est postérieure à la date de debut des encheres
        if (articleVendu.getDateFinEncheres().isBefore(articleVendu.getDateDebutEncheres())) {
            model.addAttribute("errorMessage", "La date de fin des enchères doit être plus tard que la date de début des enchères !");
            return "pages/articles/formAjoutArticle";
        }
        // on ajoute le vendeur (qui est donc l'utilisateur connecté) à l'article
        Optional<Utilisateur> vendeur = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if (vendeur.isPresent()) {
            articleVendu.setVendeur(vendeur.get());
        } else {
            model.addAttribute("errorMessage", "Impossible de trouver cet utilisateur");
            return "pages/articles/formAjoutArticle";
        }

        // on insere le nouvel article
        articleVenduService.ajouter(articleVendu);

        return "redirect:/";
    }

    @GetMapping("/modifierVente/{noArticle}")
    public String modifierVente(@PathVariable("noArticle") int noArticle, @RequestParam(value = "success", required = false) String success, Model model) {

        if (success != null) model.addAttribute("success", "success");

        model.addAttribute("articleVendu", articleVenduService.getById(noArticle));

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        return "pages/articles/formModificationArticle";

    }

    @PostMapping("/modifierVente/{noArticle}")
    public String modifierVente(@PathVariable("noArticle") int noArticle, @Valid ArticleVendu articleVendu, BindingResult controlArticle, Model model) {


        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        if (controlArticle.hasErrors()) {
            return "pages/articles/formModificationArticle";
        }

        // ici on controle que la date de fin des encheres est postérieure à la date de debut des encheres
        if (articleVendu.getDateFinEncheres().isBefore(articleVendu.getDateDebutEncheres())) {
            model.addAttribute("errorMessage", "La date de fin des enchères doit être plus tard que la date de début des enchères !");
            return "pages/articles/formAjoutArticle";
        }

        // on controle que la mise a jour s'est bien passée
        if (!articleVenduService.modifier(articleVendu))
            model.addAttribute("errorMessage", "Impossible de modifier cet article");

        return "redirect:/modifierVente/" + noArticle + "?success";

    }

}
