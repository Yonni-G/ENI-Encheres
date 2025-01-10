package fr.eni.eniencheres.eniencheres.bll;

import fr.eni.eniencheres.eniencheres.bo.ArticleVendu;

public interface ArticleVenduService {
    void ajouter(ArticleVendu articleVendu);

    ArticleVendu getById(Integer noArticle);

    boolean modifier(ArticleVendu articleVendu);

    void desactiver(ArticleVendu articleVendu);
}
