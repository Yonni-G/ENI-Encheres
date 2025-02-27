package fr.eni.eniencheres.eniencheres.controller;

import fr.eni.eniencheres.eniencheres.bll.UtilisateurService;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class UtilisateurController {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final String FORM_USER_PROFIL_UPDATE_URL = "pages/utilisateur/modification";

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
        } catch (UtilisateurExceptions.UtilisateurNonTrouve e) {
            return "redirect:/erreurCompte";
        }

        model.addAttribute("utilisateur", utilisateurService.getUtilisateur(pseudo).get());

        if (utilisateurService.getUtilisateur(pseudo).get().getNoUtilisateur() == utilisateurConnecteExiste.get().getNoUtilisateur()) {
            model.addAttribute("estAutorise", "true");
        }

        if (success != null) {
            model.addAttribute("successMessage", "Votre profil a bien été mis à jour.");
        }
        return "pages/utilisateur/profil";
    }

    @GetMapping("/modification")
    public String modificationGet(@RequestParam(value = "delete", required = false) String delete, HttpServletRequest request, HttpServletResponse response, Model model) {

        // on recupere le pseudo (username) de la session
        String utilisateurConnectePseudo = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Utilisateur> utilisateurConnecteExiste;
        try {
            utilisateurConnecteExiste = utilisateurService.getUtilisateur(utilisateurConnectePseudo);
        } catch (UtilisateurExceptions.UtilisateurNonTrouve e) {
            return "redirect:/erreurCompte";
        }

        /* ===================== SUPPRESSION DE COMPTE ===================================== */
        model.addAttribute("utilisateur", utilisateurConnecteExiste.get());
        if (delete != null) {
            try {
                utilisateurService.delete(utilisateurConnectePseudo);
            } catch (UtilisateurExceptions.UtilisateurNonTrouve e) {
                model.addAttribute("errorMessage", "Impossible de trouver le compte utilisateur de la session en cours !");
                return FORM_USER_PROFIL_UPDATE_URL;
            }
            // Déconnecter l'utilisateur après l'inscription
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            // Rediriger vers la page d'accueil
            return "redirect:/";  // Page de connexion après la déconnexion
        }
        return FORM_USER_PROFIL_UPDATE_URL;
    }

    @PostMapping("/modification")
    public String modificationPost(@Valid Utilisateur utilisateur, BindingResult controlUser,
                                   @RequestParam("NouveauMotDePasse") String NouveauMotDePasse, @RequestParam("NouveauMotDePasseConfirmation") String NouveauMotDePasseConfirmation,
                                   Model model) {
        /*
            ON VÉRIFIE SI TOUTES LES CONTRAINTES DE VALIDATION
            DE NOTRE OBJET UTILISATEUR SONT VÉRIFIÉES OU NON
         */
        if (controlUser.hasErrors()) {
            return FORM_USER_PROFIL_UPDATE_URL;
        }
        /*
            ON VÉRIFIE QUE LE MOT DE PASSE SAISI CORRESPOND BIEN
            À CELUI ENREGISTRÉ EN BASE DE DONNÉES
         */
        Optional<Utilisateur> utilisateurData = utilisateurService.getUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName());
        if (utilisateurData.isPresent()) {
            // Fonction importante qui se charge de comparer le nouveau mot de passe "en clair" avec celui "crypté" en BDD
            if (!passwordEncoder.matches(utilisateur.getMotDePasse(), utilisateurData.get().getMotDePasse())) {
                model.addAttribute("errorMessage", "Votre mot de passe actuel est incorrect !");
                return FORM_USER_PROFIL_UPDATE_URL;
            }
        } else throw new UtilisateurExceptions.UtilisateurNonTrouve();
        /*
            ARRIVÉ ICI, ON SAIT QUE L'UTILISATEUR A SAISI
            CORRECTEMENT SON MOT DE PASSE ACTUEL
         */
        // On vérifie que le nouveau mot de passe et sa vérification coincident
        if (!NouveauMotDePasse.equals(NouveauMotDePasseConfirmation)) {
            model.addAttribute("errorMessage", "Les 2 mots de passe ne coincident pas !");
            return FORM_USER_PROFIL_UPDATE_URL;
        }
        /*
            LES DONNÉES DU FORMULAIRE SONT CORRECTES ET
            PEUVENT DONC ÊTRE ENVOYÉES DANS LES COUCHES SUIVANTES
         */
        // On "set" le nouveau mot de passe encodé dans notre objet utilisateur
        utilisateur.setMotDePasse(passwordEncoder.encode(NouveauMotDePasse));
        // On "set" l'ID utilisateur dans notre objet utilisateur
        utilisateur.setNoUtilisateur(utilisateurData.get().getNoUtilisateur());
        /*
            ON ESSAIE D'EFFECTUER LA MISE À JOUR DU PROFIL DE L'UTILISATEUR
         */
        try {
            utilisateurService.update(utilisateur);
        } catch (UtilisateurExceptions.EmailDejaExistant e) {
            // On gère l'unicité de l'email avec une exception dédiée
            model.addAttribute("errorMessage", "Cette adresse email est déjà utilisée.");
            return FORM_USER_PROFIL_UPDATE_URL;
        }
        /*
            LE PROFIL A ÉTÉ MODIFIÉ AVEC SUCCÈS => ON ORIENTE L'UTILISATEUR
            VERS SON PROFIL EN GET EN SPÉCIFIANT UN FLAG DE SUCCÈS
         */
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
    public String UtilisateurInscriptionPost(@Valid Utilisateur utilisateur, BindingResult controlUser, @RequestParam("motDePasseConfirmation") String motDePasseConfirmation, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {


        if (controlUser.hasErrors()) {
            return "pages/utilisateur/inscription";
        }

        // on controle que les 2 mots de passe coincident
        if (!utilisateur.getMotDePasse().equals(motDePasseConfirmation)) {
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

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        Map<String, String> mapParametres = new HashMap<>();
        mapParametres.put("username", utilisateur.getPseudo());
        mapParametres.put("password", motDePasseConfirmation);
        redirectAttributes.addAllAttributes(mapParametres);

        return "redirect:/connexion";
    }
}
