package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    void add(Utilisateur utilisateur);

    void update(Utilisateur utilisateur);

    void delete(String pseudo);

    Optional<Utilisateur> getUtilisateur(String pseudo);

    Optional<Utilisateur> getUtilisateurById(int noUtilisateur);

    List<Utilisateur> findAll();


    void encherir(Enchere enchere);
}
