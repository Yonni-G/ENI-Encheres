package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.Categorie;

import java.util.List;

public interface EnchereRepository {
    public List<Categorie> findAllCategories();
}
