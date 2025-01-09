package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository {
    void add(Utilisateur utilisateur);

    Utilisateur getUtilisateur(String pseudo);

    Optional<Utilisateur> getUtilisateurById(int noUtilisateur);

    List<Utilisateur> findAll();

    void update(Utilisateur utilisateur);

    void delete(String pseudo);

    void encherir(Enchere enchere);
}
