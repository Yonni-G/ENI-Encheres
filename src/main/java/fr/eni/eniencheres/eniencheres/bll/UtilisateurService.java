package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

public interface UtilisateurService {
    void add(Utilisateur utilisateur);

    Utilisateur getUtilisateur(String pseudo);
}
