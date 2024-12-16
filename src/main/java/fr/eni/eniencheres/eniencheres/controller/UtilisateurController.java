package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UtilisateurController {

    @GetMapping("/inscription")
    public String UtilisateurInscriptionGet(Model model) {

        model.addAttribute("utilisateur", new Utilisateur());
        return "pages/utilisateur/inscription";
    }

    @GetMapping("/inscription")
    public String UtilisateurInscriptionPost(@Valid Utilisateur utilisateur, BindingResult controUser, Model model) {
        if(controUser.hasErrors()) {

        }
        return "pages/utilisateur/inscription";
    }
}
