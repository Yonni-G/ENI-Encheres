package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;
import fr.eni.eniencheres.eniencheres.dal.UtilisateurRepository;
import fr.eni.eniencheres.eniencheres.exceptions.UtilisateurExceptions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
            // on encrypte le mdp
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            utilisateurRepository.add(utilisateur);
        } catch (DuplicateKeyException e) {
            // Vérification du message pour savoir si c'est le pseudo ou l'email
            if (e.getMessage().contains("unique_pseudo")) {
                throw new UtilisateurExceptions.PseudoDejaExistant();
            } else if (e.getMessage().contains("unique_email")) {
                throw new UtilisateurExceptions.EmailDejaExistant();
            }
        }
    }

    @Override
    public Utilisateur getUtilisateur(String pseudo) {

        try {
            return utilisateurRepository.getUtilisateur(pseudo);
        } catch (EmptyResultDataAccessException e) {
            throw new UtilisateurExceptions.UtilisateurNonTrouve();
        }

    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }
}
