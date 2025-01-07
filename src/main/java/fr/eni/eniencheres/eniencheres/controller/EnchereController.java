package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.ArticleVenduService;
import fr.eni.eniencheres.eniencheres.bll.EnchereService;
import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.EnchereDTO;
import fr.eni.eniencheres.eniencheres.dal.EnchereFiltresDTO;
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

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EnchereController {

    @Autowired
    private EnchereService service;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private ArticleVenduService articleVenduService;

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
        // noCategorie peut être 'null' pour le filtre 'Toutes'

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
        if (encheresOuvertes) {
            articles = service.findArticleByEncheresOuvertes();
        } else if (encheresEnCours) {
            Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
            if (utilisateurConnecte.isPresent()) {
                articles = service.findArticleByEncheresEnCours(utilisateurConnecte.get().getNoUtilisateur());
            } else {
                System.out.println("Utilisateur non trouvé...");
            }
//        } TODO: else if (encheresRemportees == true) {
//
        } else if (ventesEnCours) {
            Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
            articles = service.findArticleByMesVentesEnCours(utilisateurConnecte.get().getNoUtilisateur());
        } else if (ventesNonDebutees) {
            articles = service.findArticleByVenteNonDebutee();
        } else if (ventesTerminees) {
            articles = service.findArticleByVenteTerminee();
        }

        // Ajout à la vue de la liste des catégories
        model.addAttribute("categories", service.findAllCategories());
        // Ajout à la vue de la liste des articles vendus
        model.addAttribute("articles", articles);

        // Afficher si la vente est terminée
        LocalDateTime now = LocalDateTime.now();
        boolean isVenteTerminee = false;
        for (ArticleVendu article : articles) {
            if (article.getDateFinEncheres().isBefore(now)) {
                isVenteTerminee = true;
            }
        }

        model.addAttribute("isVenteTerminee", isVenteTerminee);
        model.addAttribute("now", now);  // Passer la date actuelle au modèle




        return "pages/encheres/encheres";
    }

    @GetMapping("/detailVente/{noArticle}")
    public String detailVente(@PathVariable("noArticle") Integer noArticle, @RequestParam(value = "success", required = false) String success, Model model) {

        if(success != null) model.addAttribute("success", "success");

        //model.addAttribute("enchere", new Enchere());
        model.addAttribute("articles", service.getDetailsVente(noArticle));
        model.addAttribute("utilEnchere", service.getWinner(noArticle));

        // Afficher si la vente est remportée
        Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        EnchereDTO enchere = service.getWinner(noArticle);
        ArticleVendu articleVendu = articleVenduService.getById(noArticle);
        LocalDateTime dateActuelle = LocalDateTime.now();
        boolean aRemporteLaVente = false;
        // Vérification : enchère existante et utilisateur connecté
        if (enchere != null && utilisateurConnecte.isPresent()) {
            // Vérifier si l'utilisateur connecté a remporté l'enchère
            if (
                    utilisateurConnecte.get().getNoUtilisateur() == enchere.getNoUtilisateur()
                            && articleVendu.getDateFinEncheres().isBefore(dateActuelle)
            ) {
                aRemporteLaVente = true;
            }
        }

        if (enchere == null) {
            enchere = new EnchereDTO();  // Crée un objet vide si nécessaire
        }
        model.addAttribute("enchere", enchere);
        model.addAttribute("aRemporteLaVente", aRemporteLaVente);

        // Afficher si la vente est terminée
        if(articleVendu.getDateFinEncheres().isBefore(dateActuelle))
            model.addAttribute("isVenteTerminee", true);

        return "pages/encheres/detailVente";
    }

    @PostMapping("/detailVente/{noArticle}")
    public String detailVente(@PathVariable("noArticle") Integer noArticle, @Valid Enchere enchere, BindingResult controlEnchere, Model model) {


        model.addAttribute("noArticle", noArticle);  // Ajout de l'ID au modèle
        EnchereDTO enchereDTO = service.getDetailsVente(noArticle);
        model.addAttribute("articles", enchereDTO);
        model.addAttribute("utilEnchere", service.getUtilEnchere(noArticle));

        if(controlEnchere.hasErrors()) {
            return "pages/encheres/detailVente";
        }

        // Si la validation est réussie, traitement de l'enchère
        // on enrichit l'enchere avec les donnees de l'encherisseur
        Optional<Utilisateur> utilisateurExiste = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        Utilisateur utilisateurConnecte;
        if(utilisateurExiste.isPresent()) {
            utilisateurConnecte = utilisateurExiste.get();
            enchere.setUtilisateur(utilisateurConnecte);
        } else utilisateurConnecte = new Utilisateur();

        // on enrichit l'enchere avec les donnees de l'article
        ArticleVendu articleVendu = articleVenduService.getById(noArticle);
        enchere.setArticleVendu(articleVendu);

        // CAS ERREUR 1 : ENCHERIR SUR SON PROPRE OBJET
        if(utilisateurConnecte.getNoUtilisateur() == articleVendu.getVendeur().getNoUtilisateur()) {
            model.addAttribute("erreurEnchere", "Vous ne pouvez pas enchérir sur votre propre objet !");
            return "pages/encheres/detailVente";
        }

        // CAS ERREUR 2 : ON EST DEJA LE MEILLEUR ENCHERISSEUR
        if(utilisateurConnecte.getNoUtilisateur() == enchereDTO.getNoUtilisateur()) {
            model.addAttribute("erreurEnchere", "Vous êtes déjà le meilleur enchérisseur sur cet objet !");
            return "pages/encheres/detailVente";
        }

        // on recupere le max entre la mise à prix et l'enchere en cours
        int enchereMinimumAttendue = Math.max(articleVendu.getMiseAPrix(), enchereDTO.getMontantEnchere());

        // CAS ERREUR 3 : CREDIT INSUFFISANT
        // est-ce que l'utilisateur a assez de credit pour encherir ?
        if(utilisateurConnecte.getCredit() < enchereMinimumAttendue) {
            model.addAttribute("erreurEnchere", "Vous n'avez pas assez de points pour enchérir (vous disposez de " + utilisateurConnecte.getCredit() + " points.)");
            return "pages/encheres/detailVente";
        }

        // CAS ERREUR 4 : ENCHERE TROP FAIBLE
        if(enchere.getMontantEnchere() <= enchereMinimumAttendue) {
            model.addAttribute("erreurEnchere", "Votre enchère est trop faible (doit être > à " + enchereMinimumAttendue + ")");
            return "pages/encheres/detailVente";
        }

        // on enregistre l'enchere
        utilisateurService.encherir(enchere);

        System.out.println("jouat");
        return "redirect:/detailVente/" + noArticle + "?success";  // Redirige vers la page avec l'ID de l'article après succès
    }

}
