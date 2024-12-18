package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    void add(Utilisateur utilisateur);
    Optional<Utilisateur> getUtilisateur(String pseudo);
    List<Utilisateur> findAll();
    void update(Utilisateur utilisateur);
}
