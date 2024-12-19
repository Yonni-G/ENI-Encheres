package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UtilisateurController {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/erreurCompte")
    public String erreurCompte(Model model) {
        return "pages/utilisateur/erreurCompte";
    }

    /*
        Les 2 fonctions en GET /profil et /modification impliquent que le compte de l'utilisateur connecté soit existant
        => on le vérifiera dans les 2 fonctions sus-nommées
     */
    @GetMapping("/profil/{pseudo}")
    public String profilGet(@PathVariable String pseudo, @RequestParam(value = "success", required = false) String success, Model model) {

        Optional<Utilisateur> utilisateurConnecteExiste;
        try {
            utilisateurConnecteExiste = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch(UtilisateurExceptions.UtilisateurNonTrouve e) {
            return "redirect:/erreurCompte";
        }

        model.addAttribute("utilisateur", utilisateurService.getUtilisateur(pseudo).get());

        if(success != null) {
            model.addAttribute("successMessage", "Votre profil a bien été mis à jour.");
        }
        return "pages/utilisateur/profil";
    }

    @GetMapping("/modification")
    public String modificationGet(@RequestParam(value = "delete", required = false) String delete, HttpServletRequest request, HttpServletResponse response, Model model) {

        // on recupere le pseudo (username) de la session
        String utilisateurConnectePseudo =  SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Utilisateur> utilisateurConnecteExiste;
        try {
            utilisateurConnecteExiste = utilisateurService.getUtilisateur(utilisateurConnectePseudo);
        } catch(UtilisateurExceptions.UtilisateurNonTrouve e) {
            return "redirect:/erreurCompte";
        }

        /* ===================== SUPPRESSION DE COMPTE ===================================== */
        model.addAttribute("utilisateur", utilisateurConnecteExiste.get());
        if(delete != null) {
            try {
                utilisateurService.delete(utilisateurConnectePseudo);
            } catch (UtilisateurExceptions.UtilisateurNonTrouve e) {
                model.addAttribute("errorMessage", "Impossible de trouver le compte utilisateur de la session en cours !");
                return "pages/utilisateur/modification";
            }
            // Déconnecter l'utilisateur après l'inscription
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            // Rediriger vers la page d'accueil
            return "redirect:/";  // Page de connexion après la déconnexion
        }
        return "pages/utilisateur/modification";
    }

    @PostMapping("/modification")
    public String modificationPost(@Valid Utilisateur utilisateur, BindingResult controlUser, @RequestParam("NouveauMotDePasse") String NouveauMotDePasse, @RequestParam("NouveauMotDePasseConfirmation") String NouveauMotDePasseConfirmation, Model model) {

        if (controlUser.hasErrors()) {
            return "pages/utilisateur/modification";
        }

        // on vérifie que le mdp saisi est correct
        Optional<Utilisateur> utilisateurData = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if(utilisateurData.isPresent()) {
            String mdpData = utilisateurData.get().getMotDePasse();

            if (!passwordEncoder.matches(utilisateur.getMotDePasse(), mdpData)) {
                model.addAttribute("errorMessage", "Votre mot de passe actuel est incorrect !");
                return "pages/utilisateur/modification";
            }
        }else {
            throw new UtilisateurExceptions.UtilisateurNonTrouve();
        }

        // on controle que les 2 nouveaux mots de passe coincident
        if (!NouveauMotDePasse.equals(NouveauMotDePasseConfirmation)) {
            model.addAttribute("errorMessage", "Les 2 mots de passe ne coincident pas !");
            return "pages/utilisateur/modification";
        }

        //  arrivé ici, tout est bon =>
        //  1. on met à jour :
        //      1.1 le mot de passe de l'utilisateur avec un encode du nouveau mdp
        utilisateur.setMotDePasse(passwordEncoder.encode(NouveauMotDePasse));
        //      1.2 l'id de l'utilisateur
        utilisateur.setNoUtilisateur(utilisateurData.get().getNoUtilisateur());

        //  2. on tente de valider l'update des données
        utilisateur.setMotDePasse(passwordEncoder.encode(NouveauMotDePasse));
        try {

            utilisateurService.update(utilisateur);
        } catch (UtilisateurExceptions.EmailDejaExistant e) {
            model.addAttribute("errorMessage", "Cette adresse email est déjà utilisée.");
            return "pages/utilisateur/modification";
        }

        model.addAttribute("successMessage", "OK");
        return "redirect:profil/" + utilisateur.getPseudo() + "?success";

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
