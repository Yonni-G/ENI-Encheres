package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/inscription")
    public String UtilisateurInscriptionGet(Model model) {

        model.addAttribute("utilisateur", new Utilisateur(
                0,
                "pseudo",
                "nom",
                "prenom",
                "email@email.com",
                "telephone",
                "nom de la rue",
                "codepostal",
                "nom de la ville",
                "motpasse",
                0,
                false
        ));
        return "pages/utilisateur/inscription";
    }

    @PostMapping("/inscription")
    public String UtilisateurInscriptionPost(@Valid Utilisateur utilisateur, BindingResult controlUser, Model model) {
        if(controlUser.hasErrors()) {
            return "pages/utilisateur/inscription";
        }
        try {
            // ici le formulaire est valide => on tente d'ajouter l'utilisateur en BDD
            utilisateurService.add(utilisateur);
        } catch (UtilisateurExceptions.EmailDejaExistant e) {
            model.addAttribute("errorMessage", "L'email est déjà utilisé.");
            return "pages/utilisateur/inscription";  // Page ou message d'erreur si l'email est déjà pris
        } catch (UtilisateurExceptions.PseudoDejaExistant e) {
            model.addAttribute("errorMessage", "Le pseudo est déjà utilisé.");// Page ou message d'erreur si le pseudo est déjà pris
            return "pages/utilisateur/inscription";

        }

//        } catch (Exception e) {
//            model.addAttribute("message", "Une erreur interne est survenue.");
//            return "utilisateur/erreur";  // Page d'erreur générique
//        }
        return "pages/utilisateur/inscription";
    }
}
