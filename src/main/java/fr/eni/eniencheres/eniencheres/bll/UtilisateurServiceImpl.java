package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.UtilisateurRepository;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import fr.eni.eniencheres.eniencheres.security.AuthenticationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(Utilisateur utilisateur) {

        try {
            utilisateurRepository.add(utilisateur);
        } catch (DuplicateKeyException e) {
            // Vérification du message pour savoir si c'est le pseudo ou l'email
            if (e.getMessage().contains("unique_pseudo")) {
                throw new UtilisateurExceptions.PseudoDejaExistant();
            } else if (e.getMessage().contains("unique_email")) {
                throw new UtilisateurExceptions.EmailDejaExistant();
            }
        }

        // arrivé ici la création du compte utilisateur a réussie
        // il faut le connecter automatiquement et le rediriger vers l'accueil (çà c'est le controller qui va le faire)

    }

    @Override
    public Optional<Utilisateur> getUtilisateur(String pseudo) {

        try {
            Utilisateur utilisateur = utilisateurRepository.getUtilisateur(pseudo);
            return Optional.ofNullable(utilisateur);
        } catch (EmptyResultDataAccessException e) {
            throw new UtilisateurExceptions.UtilisateurNonTrouve();
        }

    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    public void update(Utilisateur utilisateur) {
        try {
            utilisateurRepository.update(utilisateur);
        } catch (DuplicateKeyException e) {
            throw new UtilisateurExceptions.EmailDejaExistant();
        }
    }

    @Override
    public void delete(String pseudo) {
        try {
            utilisateurRepository.delete(pseudo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
