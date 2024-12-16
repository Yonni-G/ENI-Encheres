package fr.eni.eniencheres.eniencheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UtilisateurController {

    @GetMapping("/inscription")
    public String UtilisateurInscriptionGet(){
        return "pages/utilisateur/inscription";
    }
}
