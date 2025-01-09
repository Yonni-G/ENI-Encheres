package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;
import fr.eni.eniencheres.eniencheres.bo.Enchere;
import fr.eni.eniencheres.eniencheres.bo.Retrait;

public interface ArticleVenduRepository {
    void ajouter(ArticleVendu articleVendu);
    ArticleVendu getById(int noArticle);


    boolean modifier(ArticleVendu articleVendu);
}
