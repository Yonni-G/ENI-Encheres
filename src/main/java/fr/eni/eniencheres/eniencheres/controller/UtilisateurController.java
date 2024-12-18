package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        //this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping("/login")
//    String loginPost() {
//
//    }
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
                "codePostal",
                "nom ville",
                "motDePasse",
                0,
                false

        ));
        return "pages/utilisateur/inscription";
    }

    @PostMapping("/annulation")
    public String annulationPost() {
        return "redirect:/";
    }

    @PostMapping("/inscription")
    public String UtilisateurInscriptionPost(@Valid Utilisateur utilisateur, BindingResult controlUser, @RequestParam("motDePasseConfirmation") String motDePasseConfirmation, Model model) {


        if(controlUser.hasErrors()) {
            return "pages/utilisateur/inscription";
        }

        // on controle que les 2 mots de passe coincident
        if(!utilisateur.getMotDePasse().equals(motDePasseConfirmation)) {
            model.addAttribute("errorMessage", "Les 2 mots de passe ne coincident pas !");
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
        // arrive ici l'utilisateur a cree son compte et est automatiquement connecté
        // => on le redirige vers l'accueil

        // Authentifie l'utilisateur immédiatement après l'inscription
        Authentication authentication = new UsernamePasswordAuthenticationToken(utilisateur.getPseudo(), utilisateur.getMotDePasse());
        Authentication authResult = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Redirige vers la page d'accueil après connexion réussie
        return "redirect:/";


    }
}
