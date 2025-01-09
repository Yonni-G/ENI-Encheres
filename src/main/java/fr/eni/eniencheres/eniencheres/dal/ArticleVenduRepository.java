package fr.eni.eniencheres.eniencheres.dal;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;

public interface ArticleVenduRepository {
    void ajouter(ArticleVendu articleVendu);

    ArticleVendu getById(int noArticle);


    boolean modifier(ArticleVendu articleVendu);
}
