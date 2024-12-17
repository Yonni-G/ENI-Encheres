package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

import java.util.List;

public interface UtilisateurRepository {
    void add(Utilisateur utilisateur);

    List<Utilisateur> findAll();
}
