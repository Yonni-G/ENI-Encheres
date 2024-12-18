package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/modification")
    String modificationGet(Model model) {
        model.addAttribute("utilisateur", utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "pages/utilisateur/modification";
    }

    @GetMapping("/connexion")
    String connexionGet() {

        return "pages/utilisateur/connexion";
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

        // On encrypte le mdp
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
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
        // Authentification de l'utilisateur après l'inscription
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(utilisateur.getPseudo(), motDePasseConfirmation);
            Authentication authResult = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authResult);



            // Tester si l'authentification a réussi
            if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                System.out.println("Authentification réussie pour : " + authentication.getName());
            } else {
                System.out.println("L'authentification a échoué (même si aucune exception n'a été levée).");
            }
        } catch (AuthenticationException ex) {
            // Si une exception est lancée, cela signifie que l'authentification a échoué
            System.out.println("L'authentification a échoué : " + ex.getMessage());
            System.out.println(utilisateur);
            model.addAttribute("errorMessage", "Identifiants invalides");
            return "redirect:/login";
        }

        // CONNEXION AUTOMATIQUE VERS L ACCUEIL
        return "redirect:/";


    }
}
