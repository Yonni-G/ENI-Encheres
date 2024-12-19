package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.EnchereFiltresDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
            @RequestParam(required = false) boolean encheresOuvertes,
            @RequestParam(required = false) boolean encheresEnCours,
            @RequestParam(required = false) boolean encheresRemportees,
            @RequestParam(required = false) boolean ventesEnCours,
            @RequestParam(required = false) boolean ventesNonDebutees,
            @RequestParam(required = false) boolean ventesTerminees,
            Model model) {

        // Initialisation de valeurs par défaut pour éviter les valeurs 'null'
        if (nom == null) { nom = "";}
        if (noCategorie == null) { noCategorie = null;} // Peut être 'null' pour le filtre 'Toutes'

        List<ArticleVendu> articles;
        // Récupération des articles en fonction des filtres
        if (noCategorie != null && !nom.isBlank()) {
            articles = service.findArticleByNomAndCategorieId(noCategorie, nom);
        } else if (noCategorie != null) {
            articles = service.findArticleByCategorieId(noCategorie);
        } else if (!nom.isBlank()) {
            articles = service.findArticleByNom(nom);
        } else {
            articles = service.findAllArticleVendu();
        }

        // Gérer les filtres Utilisateur connecté
        EnchereFiltresDTO enchereFiltres = new EnchereFiltresDTO();
        model.addAttribute("enchereFiltres", enchereFiltres);
        if (encheresOuvertes == true) {
            articles = service.findArticleByEncheresOuvertes();
        } else if (encheresEnCours == true) {
            Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
            if (utilisateurConnecte.isPresent()) {
                articles = service.findArticleByEncheresEnCours(utilisateurConnecte.get().getNoUtilisateur());
            }
            // TODO: gérer cas utilisateur empty (normalement ça sert à rien)
//        } TODO: else if (encheresRemportees == true) {
//
        } else if (ventesEnCours == true) {
            Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
            articles = service.findArticleByMesVentesEnCours(utilisateurConnecte.get().getNoUtilisateur());
        } else if (ventesNonDebutees == true) {
            articles = service.findArticleByVenteNonDebutee();
        } else if (ventesTerminees == true) {
            articles = service.findArticleByVenteTerminee();
        }

        // Ajout à la vue de la liste des catégories
        model.addAttribute("categories", service.findAllCategories());
        // Ajout à la vue de la liste des articles vendus
        model.addAttribute("articles", articles);

        return "pages/encheres/encheres";
    }

    @GetMapping("/detailVente/{noArticle}")
    public String detailVente(@PathVariable("noArticle") Integer noArticle, Model model) {
        //model.addAttribute("articles", service.findArticleById(noArticle));

        model.addAttribute("articles", service.getDetailsVente(noArticle));

        return "pages/encheres/detailVente";
    }
}
