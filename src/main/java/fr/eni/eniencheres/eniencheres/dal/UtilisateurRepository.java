package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Utilisateur;

public interface UtilisateurRepository {
    void add(Utilisateur utilisateur);

    Utilisateur getUtilisateur(String pseudo);
}
