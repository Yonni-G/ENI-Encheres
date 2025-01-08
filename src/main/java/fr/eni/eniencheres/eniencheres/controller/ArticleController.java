package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.ArticleVenduService;
import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.FileStorageService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Retrait;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


@Controller
public class ArticleController {


    private EnchereService enchereService;
    private UtilisateurService utilisateurService;
    private ArticleVenduService articleVenduService;
    @Autowired
    private FileStorageService fileStorageService;

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
    public String vendrePost(@Valid ArticleVendu articleVendu, BindingResult controleArticleVendu, MultipartFile file, Model model){

        // il faut également injecter les catégories
        model.addAttribute("categories", enchereService.findAllCategories());

        if(controleArticleVendu.hasErrors()) {

            return "pages/articles/formAjoutArticle";
        }

        // ici on controle que la date de fin des encheres est postérieure à la date de debut des encheres
        if(articleVendu.getDateFinEncheres().isBefore(articleVendu.getDateDebutEncheres())) {
            model.addAttribute("errorMessage", "La date de fin des enchères doit être plus tard que la date de début des enchères !");
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

        // On ajoute l'image (upload)
        try {
            // Sauvegarde de l'image et récupérer chemin du fichier
            String filePath = fileStorageService.saveFile(file);
            System.out.println("Chemin de l'image sauvegardée : " + filePath);

            // On affecte à l'objet ArticleVendu
            articleVendu.setLien_image(filePath);
            model.addAttribute("message", "Image uploadé avec succès!");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Impossible de sauver le fichier"  + e.getMessage());
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
