package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    void add(Utilisateur utilisateur);

    List<Utilisateur> findAll();
}
