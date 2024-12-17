package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Categorie;

import java.util.List;

public interface EnchereRepository {
    public List<Categorie> findAllCategories();
    public List<ArticleVendu> findAllArticleVendu();
}
